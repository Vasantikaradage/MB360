package com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails;

import java.util.ArrayList;
import java.util.List;

public class EmployeeFiledMainResponse {
    private  Boolean errorState;
    private  Boolean loadingState;
    private List<EmployeeFieldDisplaySubResponse> employeeFieldDisplaySubResponses;

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

    public List<EmployeeFieldDisplaySubResponse> getEmployeeFieldDisplaySubResponses() {
        return employeeFieldDisplaySubResponses;
    }

    public void setEmployeeFieldDisplaySubResponses(List<EmployeeFieldDisplaySubResponse> employeeFieldDisplaySubResponses) {
        this.employeeFieldDisplaySubResponses = employeeFieldDisplaySubResponses;
    }

    @Override
    public String toString() {
        return "EmployeeFiledMainResponse{" +
                "errorState=" + errorState +
                ", loadingState=" + loadingState +
                ", employeeFieldDisplaySubResponses=" + employeeFieldDisplaySubResponses +
                '}';
    }
}
