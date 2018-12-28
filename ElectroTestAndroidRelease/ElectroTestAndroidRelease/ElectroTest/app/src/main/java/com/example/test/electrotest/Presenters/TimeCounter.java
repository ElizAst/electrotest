package com.example.test.electrotest.Presenters;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCounter extends CountDownTimer {
    // this is my seconds up counter
    int countUpTimer;

    TextView textView;

    public TimeCounter(long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        countUpTimer = 0;
        this.textView = textView;
    }

    @Override
    public void onTick(long l) {
        countUpTimer = countUpTimer + 1000;
        Date date = new Date(countUpTimer);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        textView.setText(formatter.format(date));
    }

    @Override
    public void onFinish() {
        //reset counter to 0 if you want
        countUpTimer = 0;
    }
}
