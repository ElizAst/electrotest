package com.example.test.electrotest.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.electrotest.Models.ModelsInteract.UserAnswer;
import com.example.test.electrotest.Models.NetModels.AnswerActionNet;
import com.example.test.electrotest.Models.NetModels.QuestionActionNet;
import com.example.test.electrotest.Presenters.BuilderPresenter;
import com.example.test.electrotest.Presenters.TestPresenter;
import com.example.test.electrotest.R;

public class TestActivity extends AppCompatActivity {

    TestPresenter presenter;

    /// Кнопка назад
    @Override
    public void onBackPressed() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        presenter.finishTest(true, false);
                        TestActivity.super.onBackPressed();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Хотите продолжить тест?").setPositiveButton("Да", dialogClickListener)
                .setNegativeButton("Нет", dialogClickListener).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        int ticket = 1;
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            ticket = bd.getInt("Ticket");
        }

        presenter = BuilderPresenter.getTestPresenter();
        presenter.onCreate(this, ticket);
    }

    /// Сделать текущим первый неотвеченный вопрос
    public void setFirstUnansweredQuestion() {
        presenter.setFirstUnansweredQuestion();
        initCurrentQuestion();
    }

    /// Ининциализация формы
    public void initCurrentQuestion() {
        QuestionActionNet questionActionNet = presenter.getCurrentQuestion();
        ((TextView) this.findViewById(R.id.infoCenter))
                .setText(new StringBuilder()
                        .append(presenter.getCurrentQuestionInt() + 1)
                        .append("/")
                        .append(presenter.getCountQuestions()).toString());

        TextView textView = findViewById(R.id.question);
        UserAnswer isAnswered = presenter.isAnswered(questionActionNet.getUserID());
        textView.setText(questionActionNet.getQuestionContent());
        LinearLayout answers = (LinearLayout) findViewById(R.id.answers);
        if (answers.getChildCount() > 0)
            answers.removeAllViews();
        for (AnswerActionNet answerActionNet : questionActionNet.getAnswerActionNets()) {
            TextView answer = new TextView(this);
            answer.setPadding(0, 10, 0, 10);
            answer.setId(answerActionNet.getUserID());
            answer.setText(answerActionNet.getAnswerContent());
            answer.setPadding(0, 15, 0, 15);
            if (isAnswered == null)
                answer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView answer = (TextView) v;
                        presenter.answer(answer.getId());
                    }
                });
            else {
                if (isAnswered.getAnswerID() == answerActionNet.getUserID()) {
                    answer.setTextColor(Color.parseColor("#ffffff"));
                    answer.setBackgroundColor(Color.parseColor("#696969"));
                }
            }
            answers.addView(answer);
        }

        Button nextBtn = findViewById(R.id.nextBtn);
        if (presenter.IsLastQuestion()) {
            nextBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishTest();
                }
            });
            nextBtn.setBackgroundResource(R.drawable.finish);
        } else {
            nextBtn.setBackgroundResource(R.drawable.next);
            nextBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextQuestion(v);
                }
            });
        }

        if (presenter.getCurrentQuestionInt() == 0) {
            findViewById(R.id.prevBtn).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.prevBtn).setVisibility(View.VISIBLE);
        }

    }

    /// Завершить тест
    public void finishTest() {
        presenter.finishTest(false, false);
    }

    /// След вопрос
    public void nextQuestion(View view) {
        presenter.nextQuestion();
        initCurrentQuestion();
    }

    public void prevQuestion(View view) {
        presenter.prevQuestion();
        initCurrentQuestion();
    }
}
