package com.csform.android.MB360.wellness.healthcheckup.repository.responseclass;

import com.csform.android.MB360.wellness.homehealthcare.responseclass.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageResponseDependent {
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
        return "MessageResponseDependent{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
