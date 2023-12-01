package com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.fetchpayment;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchPaymentResponse {
    @SerializedName("AppointmentList")
    @Expose
    private List<Appointment> appointmentList;
    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("PaymentDetails")
    @Expose
    private PaymentDetails paymentDetails;

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

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    @Override
    public String toString() {
        return "FetchPaymentResponse{" +
                "appointmentList=" + appointmentList +
                ", message=" + message +
                ", paymentDetails=" + paymentDetails +
                '}';
    }
}
