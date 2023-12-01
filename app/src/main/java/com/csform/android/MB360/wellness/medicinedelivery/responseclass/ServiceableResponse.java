package com.csform.android.MB360.wellness.medicinedelivery.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceableResponse {

    @SerializedName("IsServiceable")
    @Expose
    private Boolean isServiceable;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;

    public Boolean getIsServiceable() {
        return isServiceable;
    }

    public void setIsServiceable(Boolean isServiceable) {
        this.isServiceable = isServiceable;
    }

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

}
