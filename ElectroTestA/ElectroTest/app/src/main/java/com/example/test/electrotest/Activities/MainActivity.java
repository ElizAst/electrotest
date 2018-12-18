package com.example.test.electrotest.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test.electrotest.App;
import com.example.test.electrotest.R;
import com.example.test.electrotest.ServerInteract.AccountInteract;

public class MainActivity extends AppCompatActivity {

    AccountInteract accountInteract;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accountInteract = new AccountInteract((TextView)findViewById(R.id.textViewInfo));
        App.TryToLoginStart(this);
    }

    /// Логинация
    public void login(View view) {
        String login = ((EditText) findViewById(R.id.textBoxLogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.textBoxPassword)).getText().toString();
        accountInteract.login(login, password, this);
    }

    /// Регистрация
    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
