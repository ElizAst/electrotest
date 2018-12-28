package com.example.test.electrotest.Models.NetModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartActionNet {

    @SerializedName("ResultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("WorkID")
    @Expose
    private Integer userID;
    @SerializedName("IsStartPrev")
    @Expose
    private Boolean isStartPrev;
    @SerializedName("PrevID")
    @Expose
    private Integer prevID;

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

    public Boolean getIsStartPrev() {
        return isStartPrev;
    }

    public void setIsStartPrev(Boolean isStartPrev) {
        this.isStartPrev = isStartPrev;
    }

    public Integer getPrevID() {
        return prevID;
    }

    public void setPrevID(Integer prevID) {
        this.prevID = prevID;
    }

}