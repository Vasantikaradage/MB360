package com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup;

public class TopUpSiMainResponse {
    private  Boolean errorState;
    private  Boolean loadingState;
    private TopUSiResponseData topUSiResponseData;

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

    public TopUSiResponseData getTopUpResponseData() {
        return topUSiResponseData;
    }

    public void setTopUpResponseData(TopUSiResponseData topUSiResponseData) {
        this.topUSiResponseData = topUSiResponseData;
    }

    @Override
    public String toString() {
        return "TopUpSumInsuredResponse{" +
                "errorState=" + errorState +
                ", loadingState=" + loadingState +
                ", topUpResponseData=" + topUSiResponseData +
                '}';
    }
}
