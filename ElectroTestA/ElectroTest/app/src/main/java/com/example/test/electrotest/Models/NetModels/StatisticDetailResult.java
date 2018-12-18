package com.example.test.electrotest.Models.NetModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticDetailResult {

    @SerializedName("ResultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("WorkID")
    @Expose
    private Integer workID;
    @SerializedName("QuestionActionNets")
    @Expose
    private List<QuestionActionNet> questionActionNets = null;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getWorkID() {
        return workID;
    }

    public void setWorkID(Integer workID) {
        this.workID = workID;
    }

    public List<QuestionActionNet> getQuestionActionNets() {
        return questionActionNets;
    }

    public void setQuestionActionNets(List<QuestionActionNet> questionActionNets) {
        this.questionActionNets = questionActionNets;
    }

}