package com.example.test.electrotest.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.test.electrotest.Models.NetModels.StartActionNet;
import com.example.test.electrotest.Presenters.BuilderPresenter;
import com.example.test.electrotest.R;
import com.example.test.electrotest.ServerInteract.TestInteract;
import com.example.test.electrotest.ServerInteract.UserContextOperation;

public class StartTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
    }

    /// Начать тестирование после вопроса пользователю
    public void StartTestResult(final StartActionNet startActionNet) {
        if (startActionNet.getIsStartPrev()) {
            final StartTestActivity startTestActivity = this;
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_NEGATIVE:
                            BuilderPresenter.getTestPresenter().isSTartTest(true, startTestActivity);
                            break;
                        case DialogInterface.BUTTON_POSITIVE:
                            BuilderPresenter.getTestPresenter().getUserAnswers().clear();
                            BuilderPresenter.getTestPresenter().isSTartTest(false, startTestActivity);
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Хотите продолжить незавершенный тест?").setPositiveButton("Да", dialogClickListener)
                    .setNegativeButton("Нет", dialogClickListener).show();
        } else {
            UserContextOperation.setTestResultID(startActionNet.getPrevID());
            Intent intent = new Intent(this, TicketsActivity.class);
            startActivity(intent);
        }
    }

    /// Начать тестирование
    public void StartTest(View view) {
        TestInteract testInteract = new TestInteract();
        testInteract.startTest(UserContextOperation.getUserID(), this);
    }
}
