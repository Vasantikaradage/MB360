package com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopUpPlanResponse {
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;

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

    @Override
    public String toString() {
        return "TopUpPlanResponse{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
