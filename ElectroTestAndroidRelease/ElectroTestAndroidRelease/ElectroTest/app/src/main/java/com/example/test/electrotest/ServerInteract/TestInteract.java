package com.example.test.electrotest.ServerInteract;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.electrotest.Activities.FinishActivity;
import com.example.test.electrotest.Activities.StartTestActivity;
import com.example.test.electrotest.Activities.StatisticActivity;
import com.example.test.electrotest.Activities.TestActivity;
import com.example.test.electrotest.Activities.TicketsActivity;
import com.example.test.electrotest.ActivityBuilder.ActivityBuilder;
import com.example.test.electrotest.App;
import com.example.test.electrotest.Models.ModelsInteract.FinishTest;
import com.example.test.electrotest.Models.ModelsInteract.IntegerOutput;
import com.example.test.electrotest.Models.ModelsInteract.IsStartTest;
import com.example.test.electrotest.Models.ModelsInteract.PrestartTest;
import com.example.test.electrotest.Models.ModelsInteract.UserAnswer;
import com.example.test.electrotest.Models.NetModels.AnswerActionNet;
import com.example.test.electrotest.Models.NetModels.QuestionActionNet;
import com.example.test.electrotest.Models.NetModels.QuestionsListNet;
import com.example.test.electrotest.Models.NetModels.StartActionNet;
import com.example.test.electrotest.Models.NetModels.StaticticResult;
import com.example.test.electrotest.Models.NetModels.StatisticsListNet;
import com.example.test.electrotest.Models.NetModels.TicketActionNet;
import com.example.test.electrotest.Models.NetModels.UniversalActionNet;
import com.example.test.electrotest.Presenters.BuilderPresenter;
import com.example.test.electrotest.Presenters.TestPresenter;
import com.example.test.electrotest.R;
import com.example.test.electrotest.ResultCode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/// Прослойка для взаимодействия с сервером
public class TestInteract {

    private APIService apiService = App.getApi();

    public void startTest(int user, final StartTestActivity startTestActivity) {
        Call<StartActionNet> actionNetCall = apiService.StartTest(new IntegerOutput(user));
        actionNetCall.enqueue(new Callback<StartActionNet>() {
            @Override
            public void onResponse(Call<StartActionNet> call, Response<StartActionNet> response) {
                StartActionNet actionNet = response.body();
                if (ResultCode.values()[actionNet.getResultCode()] == ResultCode.Success) {
                    UserContextOperation.setTestResultID(actionNet.getPrevID());
                    startTestActivity.StartTestResult(actionNet);
                }
            }

            @Override
            public void onFailure(Call<StartActionNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getTickets(final TicketsActivity ticketsActivity) {
        Call<TicketActionNet> netCall = apiService.GetTickets();
        netCall.enqueue(new Callback<TicketActionNet>() {
            @Override
            public void onResponse(Call<TicketActionNet> call, Response<TicketActionNet> response) {
                TicketActionNet ticketActionNet = response.body();
                LinearLayout linearLayout = ticketsActivity.findViewById(R.id.ticketTable);
                for (int ticket : ticketActionNet.getTickets()) {
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(new StringBuilder().append("Билет № ").append(ticket).toString());
                    linearLayout.addView(ActivityBuilder.CreateRowForLinearViewWithInformation(strings, ticketsActivity, ticket));
                }
            }

            @Override
            public void onFailure(Call<TicketActionNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void questionsListNet(int user, final TestPresenter questionsListNet, final TestActivity testActivity, int ticket) {
        Call<QuestionsListNet> netCall = apiService.GetQuestions(new PrestartTest(user, ticket));
        netCall.enqueue(new Callback<QuestionsListNet>() {
            @Override
            public void onResponse(Call<QuestionsListNet> call, Response<QuestionsListNet> response) {
                questionsListNet.setQuestionsListNet(response.body());
                for (QuestionActionNet actionNet : questionsListNet.getQuestionsListNet().getQuestionActionNets()) {
                    if (ResultCode.values()[actionNet.getResultCode().intValue()] == ResultCode.Success) {
                        for (AnswerActionNet answerActionNet : actionNet.getAnswerActionNets()) {
                            if (ResultCode.values()[answerActionNet.getResultCode().intValue()] == ResultCode.Success) {
                                questionsListNet.addUserAnswer(new UserAnswer(UserContextOperation.getUserID(),
                                        actionNet.getUserID(), answerActionNet.getUserID(), response.body().getUserID()));
                                break;
                            }
                        }
                    }
                }
                testActivity.initCurrentQuestion();
            }

            @Override
            public void onFailure(Call<QuestionsListNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void finishTest(final TestActivity testActivity, final boolean isPrev, boolean timeout) {
        Call<UniversalActionNet> netCall = apiService.FinishTest(new FinishTest(UserContextOperation.getTestResultID(), timeout));
        netCall.enqueue(new Callback<UniversalActionNet>() {
            @Override
            public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                UniversalActionNet actionNet = response.body();
                switch (ResultCode.values()[actionNet.getResultCode().intValue()]) {
                    case Success:
                        if (!isPrev) {
                            Intent intent = new Intent(testActivity, FinishActivity.class);
                            testActivity.startActivity(intent);
                        }
                        break;
                    case Error:
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                testActivity.setFirstUnansweredQuestion();
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(testActivity);
                        builder.setMessage("You answer not all question").setPositiveButton("Ok", dialogClickListener).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<UniversalActionNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void isTestStart(boolean isStart, final StartTestActivity startTestActivity) {
        IsStartTest isStartTest = new IsStartTest(isStart, UserContextOperation.getTestResultID(), UserContextOperation.getUserID());
        Call<UniversalActionNet> netCall = apiService.IsStartTest(isStartTest);
        netCall.enqueue(new Callback<UniversalActionNet>() {
            @Override
            public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                UserContextOperation.setTestResultID(response.body().getWorkID());
                Intent intent;
                if (ResultCode.values()[response.body().getResultCode()] == ResultCode.Success)
                    intent = new Intent(startTestActivity, TestActivity.class);
                else
                    intent = new Intent(startTestActivity, TicketsActivity.class);
                startTestActivity.startActivity(intent);
            }

            @Override
            public void onFailure(Call<UniversalActionNet> call, Throwable t) {
            }
        });
    }

    public void statistics(final StatisticActivity statisticActivity) {
        Call<StatisticsListNet> netCall = apiService.Statistics(new IntegerOutput(UserContextOperation.getUserID()));
        netCall.enqueue(new Callback<StatisticsListNet>() {
            @Override
            public void onResponse(Call<StatisticsListNet> call, Response<StatisticsListNet> response) {
                StatisticsListNet listNet = response.body();
                LinearLayout layout = statisticActivity.findViewById(R.id.statistics);
                for (StaticticResult result : listNet.getStaticticResults()) {
                    LinearLayout linearLayout = new LinearLayout(statisticActivity);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    linearLayout.setLayoutParams(lp);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    TextView date = (TextView) ActivityBuilder.getTextViewWrap(result.getDate(), statisticActivity, 100);
                    TextView ticket = (TextView) ActivityBuilder.getTextViewWrap(String.valueOf(result.getTicket()), statisticActivity, 100);
                    TextView resultTV = (TextView) ActivityBuilder.getTextViewWrap(String.valueOf(result.getMark()) + "%", statisticActivity, 0);
                    linearLayout.addView(date);
                    linearLayout.addView(ticket);
                    linearLayout.addView(resultTV);
                    layout.addView(linearLayout);
                }
            }

            @Override
            public void onFailure(Call<StatisticsListNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void answer(final UserAnswer userAnswer, final TestActivity testActivity) {
        Call<UniversalActionNet> netCall = apiService.Answer(userAnswer);
        netCall.enqueue(new Callback<UniversalActionNet>() {
            @Override
            public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                UniversalActionNet actionNet = response.body();
                LinearLayout answer = testActivity.findViewById(userAnswer.getAnswerID());
                ((TextView) ActivityBuilder.getView(TextView.class, answer)).setTextColor(Color.parseColor("#ffffff"));
                answer.setBackgroundColor(Color.parseColor("#696969"));
                ((CheckBox) ActivityBuilder.getView(CheckBox.class, answer)).setChecked(true);
                LinearLayout answers = testActivity.findViewById(R.id.answers);
                for (int i = 0; i < answers.getChildCount(); i++)
                    answers.getChildAt(i).setOnClickListener(null);
            }

            @Override
            public void onFailure(Call<UniversalActionNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
