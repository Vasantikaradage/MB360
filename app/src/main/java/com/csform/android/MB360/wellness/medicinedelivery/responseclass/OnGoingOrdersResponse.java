package com.csform.android.MB360.wellness.medicinedelivery.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OnGoingOrdersResponse {
    @SerializedName("OrderDetails")
    @Expose
    private List<OrderDetail> orderDetails = new ArrayList<>();
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
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

    @Override
    public String toString() {
        return "OnGoingOrdersResponse{" +
                "orderDetails=" + orderDetails +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
