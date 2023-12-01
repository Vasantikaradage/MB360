package com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup;

public class TopUpPlanMainResponse {
    private  Boolean errorState;
    private  Boolean loadingState;
    private TopUpPlanResponse topUpPlanResponse;

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

    public TopUpPlanResponse getTopUpPlanResponse() {
        return topUpPlanResponse;
    }

    public void setTopUpPlanResponse(TopUpPlanResponse topUpPlanResponse) {
        this.topUpPlanResponse = topUpPlanResponse;
    }

    @Override
    public String toString() {
        return "TopUpPlanMainResponse{" +
                "errorState=" + errorState +
                ", loadingState=" + loadingState +
                ", topUpPlanResponse=" + topUpPlanResponse +
                '}';
    }
}
