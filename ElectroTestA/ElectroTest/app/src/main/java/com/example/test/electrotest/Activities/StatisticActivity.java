package com.example.test.electrotest.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.test.electrotest.R;
import com.example.test.electrotest.ServerInteract.TestInteract;

public class StatisticActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        new TestInteract().statistics(this);
    }
}
