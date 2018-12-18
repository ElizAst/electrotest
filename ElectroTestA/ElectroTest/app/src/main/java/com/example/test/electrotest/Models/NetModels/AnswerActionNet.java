package com.example.test.electrotest.Models.NetModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerActionNet {

    @SerializedName("ResultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("WorkID")
    @Expose
    private Integer userID;
    @SerializedName("AnswerContent")
    @Expose
    private String answerContent;

    @SerializedName("IsRight")
    @Expose
    private boolean isRight;

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

}
