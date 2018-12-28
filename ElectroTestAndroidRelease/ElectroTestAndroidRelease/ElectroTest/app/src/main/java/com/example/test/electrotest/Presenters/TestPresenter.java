package com.example.test.electrotest.Presenters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.test.electrotest.Activities.StartTestActivity;
import com.example.test.electrotest.Activities.UserActivity;
import com.example.test.electrotest.ActivityBuilder.ActivityBuilder;
import com.example.test.electrotest.App;
import com.example.test.electrotest.Models.ModelsInteract.FinishTest;
import com.example.test.electrotest.Models.ModelsInteract.UserAnswer;
import com.example.test.electrotest.Models.NetModels.AnswerActionNet;
import com.example.test.electrotest.Models.NetModels.QuestionActionNet;
import com.example.test.electrotest.Models.NetModels.QuestionsListNet;
import com.example.test.electrotest.Models.NetModels.UniversalActionNet;
import com.example.test.electrotest.R;
import com.example.test.electrotest.ServerInteract.TestInteract;
import com.example.test.electrotest.ServerInteract.UserContextOperation;
import com.example.test.electrotest.Activities.TestActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/// Обработка презентора тестов
public class TestPresenter extends ICreatablePresenter<TestActivity> {

    private QuestionsListNet questionsListNet;
    private TestInteract testInteract = new TestInteract();
    private List<UserAnswer> userAnswers = new ArrayList<>();
    private CountDownTimer timer;
    private int currentQuestion = 0;
    private int ticket;

    public final Object lock = new Object();
    public boolean isTestGoing = false;

    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    public QuestionsListNet getQuestionsListNet() {
        return questionsListNet;
    }

    public void addUserAnswer(UserAnswer userAnswer) {
        userAnswers.add(userAnswer);
    }

    public int getCurrentQuestionInt() {
        return currentQuestion;
    }

    public QuestionActionNet questionActionNetById(int id) {
        for (QuestionActionNet actionNet : questionsListNet.getQuestionActionNets()) {
            if (actionNet.getUserID() == id)
                return actionNet;
        }
        return null;
    }

    public boolean isRight(QuestionActionNet questionActionNet, UserAnswer answerId) {
        for (AnswerActionNet answerActionNet : questionActionNet.getAnswerActionNets()) {
            if (answerActionNet.getUserID() == answerId.getAnswerID()) {
                return answerActionNet.isRight();
            }
        }
        return false;
    }


    public int getCountQuestions() {
        return questionsListNet.getQuestionActionNets().size();
    }

    public UserAnswer isAnswered(int id) {
        for (UserAnswer userAnswer : userAnswers)
            if (userAnswer.getQuestionID() == id)
                return userAnswer;
        return null;
    }

    public void clear() {
        this.userAnswers.clear();
        this.timer.cancel();
        this.currentQuestion = 0;
        this.timer = null;
    }

    private void startTimer(final long time) {
        if (timer != null)
            return;
        final TextView timeTV = activity.findViewById(R.id.time);
        final Activity activityTest = this.activity;
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Date date = new Date(millisUntilFinished);
                DateFormat formatter = new SimpleDateFormat("mm:ss");
                timeTV.setText(formatter.format(date));
            }

            @Override
            public void onFinish() {
                Call<UniversalActionNet> call = App.getApi().FinishTest(new FinishTest(UserContextOperation.getTestResultID(),
                        true));
                call.enqueue(new Callback<UniversalActionNet>() {
                    @Override
                    public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activityTest);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(activityTest, UserActivity.class);
                                BuilderPresenter.getTestPresenter().clear();
                                activityTest.startActivity(intent);
                            }
                        });
                        builder.setMessage("Вы не успели за отведенное время")
                                .setTitle("Не успели!");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    @Override
                    public void onFailure(Call<UniversalActionNet> call, Throwable t) {
                    }
                });
            }
        }.start();
    }


    public void setFirstUnansweredQuestion() {
        int index = 0;
        for (QuestionActionNet actionNet : questionsListNet.getQuestionActionNets()) {
            if (isAnswered(actionNet.getUserID()) == null) {
                currentQuestion = index;
                break;
            }
            index++;
        }
    }

    public void setQuestionsListNet(QuestionsListNet questionsListNet) {
        this.questionsListNet = questionsListNet;
    }

    public QuestionActionNet getCurrentQuestion() {
        return questionsListNet.getQuestionActionNets().get(currentQuestion);
    }

    public QuestionActionNet nextQuestion() {
        currentQuestion++;
        return getCurrentQuestion();
    }

    public QuestionActionNet prevQuestion() {
        if (currentQuestion - 1 >= 0)
            currentQuestion--;
        return getCurrentQuestion();
    }

    public boolean IsLastQuestion() {
        return this.questionsListNet.getQuestionActionNets().size() == currentQuestion + 1;
    }

    public void answer(int answer) {
        int question = this.questionsListNet.getQuestionActionNets().get(currentQuestion).getUserID();
        UserAnswer userAnswer =
                new UserAnswer(UserContextOperation.getUserID(), question, answer, UserContextOperation.getTestResultID());
        userAnswers.add(userAnswer);
        testInteract.answer(userAnswer, activity);
    }

    public void finishTest(boolean isPrev, boolean isTimeOut) {
        testInteract.finishTest(activity, isPrev, isTimeOut);
    }

    @Override
    public void onCreate(TestActivity v) {
        this.activity = v;
        testInteract.questionsListNet(UserContextOperation.getTestResultID(), this, activity,
                ticket);
        startTimer(1200121);
    }

    public void onCreate(TestActivity testActivity, int ticket) {
        this.ticket = ticket;
        onCreate(testActivity);
    }

    public void isSTartTest(boolean isStart, StartTestActivity startTestActivity) {
        testInteract.isTestStart(isStart, startTestActivity);
    }

}
