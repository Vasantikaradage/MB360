package com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopUSiResponseData {
    @SerializedName("SI_LIST")
    @Expose
    private List<Si> siList;
    @SerializedName("IS_RELGRP_DIVIDED")
    @Expose
    private Integer isRelgrpDivided;

    public List<Si> getSiList() {
        return siList;
    }

    public void setSiList(List<Si> siList) {
        this.siList = siList;
    }

    public Integer getIsRelgrpDivided() {
        return isRelgrpDivided;
    }

    public void setIsRelgrpDivided(Integer isRelgrpDivided) {
        this.isRelgrpDivided = isRelgrpDivided;
    }

    @Override
    public String toString() {
        return "TopUpResponseData{" +
                "siList=" + siList +
                ", isRelgrpDivided=" + isRelgrpDivided +
                '}';
    }
}
