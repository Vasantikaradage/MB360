package com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.healthcheckupongoing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OngoingAppointmentResponse {
    @SerializedName("AppointmentList")
    @Expose
    private List<Appointment> appointmentList;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
