package com.example.test.electrotest.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.example.test.electrotest.ActivityBuilder.ActivityBuilder;
import com.example.test.electrotest.Presenters.BuilderPresenter;
import com.example.test.electrotest.R;

public class FinishActivity extends AppCompatActivity {


    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        ScrollView scrollView = findViewById(R.id.results);
        scrollView.addView(ActivityBuilder.CreateRowsForFinish(this));
    }

    public void toMenu(View view) {
        BuilderPresenter.getTestPresenter().clear();
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("FinishTest", true);
        this.startActivity(intent);
    }
}
