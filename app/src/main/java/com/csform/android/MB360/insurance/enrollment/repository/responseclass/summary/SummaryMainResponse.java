package com.csform.android.MB360.insurance.enrollment.repository.responseclass.summary;

import com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails.EmployeeFieldDisplaySubResponse;

import java.util.List;

public class SummaryMainResponse {

    private Boolean errorState;
    private Boolean loadingState;
    private SummaryResponse summaryResponse;

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

    public SummaryResponse getSummaryResponse() {
        return summaryResponse;
    }

    public void setSummaryResponse(SummaryResponse summaryResponse) {
        this.summaryResponse = summaryResponse;
    }

    @Override
    public String toString() {
        return "SummaryMainResponse{" +
                "errorState=" + errorState +
                ", loadingState=" + loadingState +
                ", summaryResponse=" + summaryResponse +
                '}';
    }
}
