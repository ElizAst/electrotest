

package com.example.test.electrotest.Models.NetModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UniversalActionNet {

    @SerializedName("ResultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("WorkID")
    @Expose
    private Integer workID;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("IsTestWork")
    @Expose
    private Boolean isTestWork;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsTestWork() {
        return isTestWork;
    }

    public void setIsTestWork(Boolean isTestWork) {
        this.isTestWork = isTestWork;
    }

}