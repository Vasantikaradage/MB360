package com.csform.android.MB360.insurance.enrollment.repository.responseclass.coverage;

import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUSiResponseData;

public class CoverageMainResponse {

    private  Boolean errorState;
    private  Boolean loadingState;
    private CoverageResponseData  coverageResponseData;

    private  String PolicyType;

    public String getPolicyType() {
        return PolicyType;
    }

    public void setPolicyType(String policyType) {
        PolicyType = policyType;
    }

    public Boolean getErrorState() {
        return errorState;
    }

    public void setErrorState(Boolean errorState) {
        this.errorState = errorState;
    }

    public Boolean getLoadingState() {
        return loadingState;
    }

    public void setLoadingState(Boolean loadingState) {
        this.loadingState = loadingState;
    }

    public CoverageResponseData getCoverageResponseData() {
        return coverageResponseData;
    }

    public void setCoverageResponseData(CoverageResponseData coverageResponseData) {
        this.coverageResponseData = coverageResponseData;
    }

    @Override
    public String toString() {
        return "CoverageMainResponse{" +
                "errorState=" + errorState +
                ", loadingState=" + loadingState +
                ", coverageResponseData=" + coverageResponseData +
                ", PolicyType='" + PolicyType + '\'' +
                '}';
    }
}
