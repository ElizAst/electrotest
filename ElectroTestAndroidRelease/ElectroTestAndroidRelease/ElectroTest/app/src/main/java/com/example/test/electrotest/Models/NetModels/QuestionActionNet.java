package com.example.test.electrotest.Models.NetModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuestionActionNet {

    @SerializedName("ResultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("WorkID")
    @Expose
    private Integer userID;
    @SerializedName("QuestionContent")
    @Expose
    private String questionContent;
    @SerializedName("AnswerActionNets")
    @Expose
    private List<AnswerActionNet> answerActionNets = null;

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

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public List<AnswerActionNet> getAnswerActionNets() {
        return answerActionNets;
    }

    public void setAnswerActionNets(List<AnswerActionNet> answerActionNets) {
        this.answerActionNets = answerActionNets;
    }

}
