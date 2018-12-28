package com.example.test.electrotest.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test.electrotest.R;
import com.example.test.electrotest.ServerInteract.AccountInteract;

public class RegisterActivity extends AppCompatActivity {

    AccountInteract accountInteract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        accountInteract = new AccountInteract((TextView) findViewById(R.id.textViewInfo));
    }

    /// Регистрация
    public void register(View view) {
        String login = ((EditText) findViewById(R.id.textBoxLogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.textBoxPassword)).getText().toString();
        String confirmedPassword = ((EditText) findViewById(R.id.textBoxConfirmedPassword)).getText().toString();
        accountInteract.register(login, password, confirmedPassword, this);
    }
}
