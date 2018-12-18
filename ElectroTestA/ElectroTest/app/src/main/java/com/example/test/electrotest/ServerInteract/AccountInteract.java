package com.example.test.electrotest.ServerInteract;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.electrotest.Activities.ProfileActivity;
import com.example.test.electrotest.App;
import com.example.test.electrotest.Models.ModelsInteract.IntegerOutput;
import com.example.test.electrotest.Models.ModelsInteract.RegisterModel;
import com.example.test.electrotest.Models.ModelsInteract.UserLogin;
import com.example.test.electrotest.Models.NetModels.ProfileUserInfo;
import com.example.test.electrotest.Models.NetModels.UniversalActionNet;
import com.example.test.electrotest.R;
import com.example.test.electrotest.ResultCode;
import com.example.test.electrotest.Activities.StartTestActivity;
import com.example.test.electrotest.Activities.UserActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/// Взаимодействие с данными пользователя
public class AccountInteract {

    private APIService apiService = App.getApi();
    private TextView textView;

    public AccountInteract() {
    }

    public AccountInteract(TextView textView) {
        this.textView = textView;
    }

    public void login(final String login, final String password, final AppCompatActivity intent) {
        try {
            UserLogin userLogin = new UserLogin(login, password);
            Call<UniversalActionNet> actionNetCall = apiService.Login(userLogin);
            actionNetCall.enqueue(new Callback<UniversalActionNet>() {
                @Override
                public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                    UniversalActionNet actionNet = response.body();
                    switch (ResultCode.values()[actionNet.getResultCode().intValue()]) {
                        case Success:
                            App.SetLoginPasswordShPref(login, password, intent);
                            UserContextOperation.setUserID(actionNet.getWorkID());
                            if (!actionNet.getIsTestWork())
                                intent.startActivity(new Intent(intent, StartTestActivity.class));
                            else
                                intent.startActivity(new Intent(intent, UserActivity.class));
                            break;
                        case Error:
                            textView.setText("Indefinite error:\n" + actionNet.getMessage());
                            break;
                        case ErrorLoginPassword:
                            textView.setText("Incorrect login/password");
                            break;
                    }
                }

                @Override
                public void onFailure(Call<UniversalActionNet> call, Throwable t) {
                    textView.setText(t.getMessage());
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }

    public void register(String login, String password, String confirmedPassword, final AppCompatActivity intent) {
        if (!password.equals(confirmedPassword)) {
            textView.setText("Password and Confirmed Password are not equals");
            return;
        }

        final RegisterModel registerModel = new RegisterModel(login, password, confirmedPassword);

        Call<UniversalActionNet> netCall = apiService.Register(registerModel);
        netCall.enqueue(new Callback<UniversalActionNet>() {
            @Override
            public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                UniversalActionNet actionNet = response.body();
                switch (ResultCode.values()[actionNet.getResultCode().intValue()]) {
                    case Success:
                        new AccountInteract(textView).login(registerModel.getLogin(), registerModel.getPassword(), intent);
                        break;
                    case Error:
                        textView.setText("Indefinite error:\n" + actionNet.getMessage());
                        break;
                    case ErrorLoginPassword:
                        textView.setText("User with this Login exists");
                        break;
                }
            }

            @Override
            public void onFailure(Call<UniversalActionNet> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    public void loadStatistic() {

    }

    public void loadProfileInfo(final ProfileActivity profileActivity) {
        Call<ProfileUserInfo> call = apiService.ProfileInfo(new IntegerOutput(UserContextOperation.getUserID()));
        call.enqueue(new Callback<ProfileUserInfo>() {
            @Override
            public void onResponse(Call<ProfileUserInfo> call, Response<ProfileUserInfo> response) {
                ProfileUserInfo body = response.body();
                ((EditText) profileActivity.findViewById(R.id.name)).setText(body.getNickname());
                ((ImageView)profileActivity.findViewById(R.id.image)).setImageBitmap(App.GetActualBitmap(profileActivity));
            }

            @Override
            public void onFailure(Call<ProfileUserInfo> call, Throwable t) {
            }
        });
    }
}
