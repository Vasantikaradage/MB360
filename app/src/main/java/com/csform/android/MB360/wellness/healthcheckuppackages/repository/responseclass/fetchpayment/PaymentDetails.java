package com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.fetchpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetails {
    @SerializedName("GoToPayment")
    @Expose
    private Boolean goToPayment;
    @SerializedName("OrderMasterSrNo")
    @Expose
    private String orderMasterSrNo;
    @SerializedName("OrderReferenceNumber")
    @Expose
    private String orderReferenceNumber;
    @SerializedName("AppointmentRecieptNumber")
    @Expose
    private String appointmentRecieptNumber;
    @SerializedName("RazorPayOrderId")
    @Expose
    private String razorPayOrderId;

    public Boolean getGoToPayment() {
        return goToPayment;
    }

    public void setGoToPayment(Boolean goToPayment) {
        this.goToPayment = goToPayment;
    }

    public String getOrderMasterSrNo() {
        return orderMasterSrNo;
    }

    public void setOrderMasterSrNo(String orderMasterSrNo) {
        this.orderMasterSrNo = orderMasterSrNo;
    }

    public String getOrderReferenceNumber() {
        return orderReferenceNumber;
    }

    public void setOrderReferenceNumber(String orderReferenceNumber) {
        this.orderReferenceNumber = orderReferenceNumber;
    }

    public String getAppointmentRecieptNumber() {
        return appointmentRecieptNumber;
    }

    public void setAppointmentRecieptNumber(String appointmentRecieptNumber) {
        this.appointmentRecieptNumber = appointmentRecieptNumber;
    }

    public String getRazorPayOrderId() {
        return razorPayOrderId;
    }

    public void setRazorPayOrderId(String razorPayOrderId) {
        this.razorPayOrderId = razorPayOrderId;
    }

    @Override
    public String toString() {
        return "PaymentDetails{" +
                "goToPayment=" + goToPayment +
                ", orderMasterSrNo='" + orderMasterSrNo + '\'' +
                ", orderReferenceNumber='" + orderReferenceNumber + '\'' +
                ", appointmentRecieptNumber='" + appointmentRecieptNumber + '\'' +
                ", razorPayOrderId='" + razorPayOrderId + '\'' +
                '}';
    }
}
