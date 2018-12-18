package com.example.test.electrotest.Models.NetModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaticticResult {

    @SerializedName("ResultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("WorkID")
    @Expose
    private Integer workID;
    @SerializedName("Result")
    @Expose
    private int mark;

    @SerializedName("Ticket")
    @Expose
    private int ticket;

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    @SerializedName("DateTime")
    @Expose
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

}