package com.example.test.electrotest.Models.NetModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticsListNet {

    @SerializedName("ResultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("WorkID")
    @Expose
    private Integer workID;
    @SerializedName("staticticResults")
    @Expose
    private List<StaticticResult> staticticResults = null;

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

    public List<StaticticResult> getStaticticResults() {
        return staticticResults;
    }

    public void setStaticticResults(List<StaticticResult> staticticResults) {
        this.staticticResults = staticticResults;
    }

}
