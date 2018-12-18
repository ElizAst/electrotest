package com.example.test.electrotest.ServerInteract;

import com.example.test.electrotest.Models.ModelsInteract.FinishTest;
import com.example.test.electrotest.Models.ModelsInteract.IntegerOutput;
import com.example.test.electrotest.Models.ModelsInteract.IsStartTest;
import com.example.test.electrotest.Models.ModelsInteract.PrestartTest;
import com.example.test.electrotest.Models.ModelsInteract.RegisterModel;
import com.example.test.electrotest.Models.ModelsInteract.UserAnswer;
import com.example.test.electrotest.Models.ModelsInteract.UserLogin;
import com.example.test.electrotest.Models.ModelsInteract.UserSave;
import com.example.test.electrotest.Models.NetModels.ProfileUserInfo;
import com.example.test.electrotest.Models.NetModels.QuestionsListNet;
import com.example.test.electrotest.Models.NetModels.StartActionNet;
import com.example.test.electrotest.Models.NetModels.StatisticsListNet;
import com.example.test.electrotest.Models.NetModels.TicketActionNet;
import com.example.test.electrotest.Models.NetModels.UniversalActionNet;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/// Подклчючение через RetroFit
public interface APIService {


    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("login")
    Call<UniversalActionNet> Login(@Body UserLogin userLogin);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("register")
    Call<UniversalActionNet> Register(@Body RegisterModel registerModel);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("startTest")
    Call<StartActionNet> StartTest(@Body IntegerOutput user);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("getQuestions")
    Call<QuestionsListNet> GetQuestions(@Body PrestartTest id);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("answer")
    Call<UniversalActionNet> Answer(@Body UserAnswer userAnswer);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("finishTest")
    Call<UniversalActionNet> FinishTest(@Body FinishTest integerOutput);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("isStartTest")
    Call<UniversalActionNet> IsStartTest(@Body IsStartTest isStartTest);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("profileUserInfo")
    Call<ProfileUserInfo> ProfileInfo(@Body IntegerOutput integerOutput);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("getStatistics")
    Call<StatisticsListNet> Statistics(@Body IntegerOutput integerOutput);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("getTickets")
    Call<TicketActionNet> GetTickets();


    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("SaveUser")
    Call<UniversalActionNet> SaveUser(@Body UserSave userLogin);

}
