package com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeUpdateInfo {
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("ResponseData")
    @Expose
    private Object responseData;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "EmployeeUpdateInfo{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", responseData=" + responseData +
                '}';
    }
}
