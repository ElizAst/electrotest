package com.example.test.electrotest.ActivityBuilder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.electrotest.Activities.TestActivity;
import com.example.test.electrotest.Models.ModelsInteract.UserAnswer;
import com.example.test.electrotest.Models.NetModels.QuestionActionNet;
import com.example.test.electrotest.Presenters.BuilderPresenter;
import com.example.test.electrotest.Presenters.TestPresenter;
import com.example.test.electrotest.R;

import java.util.List;

public class ActivityBuilder {


    /// Создать строку для таблицы выбора билета
    public static <T> View CreateRowForLinearViewWithInformation(List<T> list, final Activity activity, final int id) {
        LinearLayout linearLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lp);
        linearLayout.setId(id);
        for (T elem : list) {
            TextView elemLayout = new TextView(activity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            elemLayout.setLayoutParams(layoutParams);
            elemLayout.setBackgroundResource(R.drawable.buttonstyle);
            elemLayout.setGravity(Gravity.CENTER);
            elemLayout.setTextSize(12);
            elemLayout.setPadding(0,20,0,20);
            elemLayout.setText(elem.toString());
            linearLayout.addView(elemLayout);
        }
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TestActivity.class);
                intent.putExtra("Ticket", id);
                activity.startActivity(intent);
            }
        });
        return linearLayout;
    }

    private static View getTextView(String text, Activity activity) {
        TextView textView = new TextView(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        textView.setText(text);
        textView.setLayoutParams(layoutParams);
        textView.setPadding(20, 20, 20, 20);
        return textView;
    }

    public static View getTextViewWrap(String text, Activity activity) {
        TextView textView = new TextView(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        textView.setPadding(20, 20, 20, 20);
        return textView;
    }

    public static View CreateRowsForFinish(Activity activity) {
        TestPresenter testPresenter = BuilderPresenter.getTestPresenter();
        LinearLayout linearLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        for (UserAnswer userAnswer : testPresenter.getUserAnswers()) {
            QuestionActionNet questionActionNet = testPresenter.questionActionNetById(userAnswer.getQuestionID());
            TextView textView = (TextView) getTextView(questionActionNet.getQuestionContent(), activity);
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setBackgroundColor(testPresenter.isRight(questionActionNet, userAnswer) ?
                    Color.parseColor("#00ff00") : Color.parseColor("#ff0000"));
            linearLayout.addView(textView);
        }
        testPresenter.getUserAnswers().clear();
        return linearLayout;
    }

}
