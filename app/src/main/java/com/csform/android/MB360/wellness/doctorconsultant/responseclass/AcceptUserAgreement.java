package com.csform.android.MB360.wellness.doctorconsultant.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcceptUserAgreement {

    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AcceptUserAgreement{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }
}
