package com.csform.android.MB360.insurance.enrollment.repository.responseclass.coverage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoverageResponseData {
    @SerializedName("SI_LIST")
    @Expose
    private List<Si> siList;
    @SerializedName("IS_RELGRP_DIVIDED")
    @Expose
    private Integer isRelgrpDivided;

    private String policy_selected;

    public String getPolicy_selected() {
        return policy_selected;
    }

    public void setPolicy_selected(String policy_selected) {
        this.policy_selected = policy_selected;
    }

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
        return "CoverageResponseData{" +
                "siList=" + siList +
                ", isRelgrpDivided=" + isRelgrpDivided +
                ", policy_selected='" + policy_selected + '\'' +
                '}';
    }
}
