package com.csform.android.MB360.wellness.medicinedelivery.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OrderDetail {
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("EstimatedDate")
    @Expose
    private String estimatedDate;
    @SerializedName("PlacedOrderDate ")
    @Expose
    private String placedOrderDate;
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
    @SerializedName("PeOrderId")
    @Expose
    private String peOrderId;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("OrderInfoSrNo")
    @Expose
    private String orderInfoSrNo;
    @SerializedName("Images")
    @Expose
    private List<String> images = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(String estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public String getPlacedOrderDate() {
        return placedOrderDate;
    }

    public void setPlacedOrderDate(String placedOrderDate) {
        this.placedOrderDate = placedOrderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPeOrderId() {
        return peOrderId;
    }

    public void setPeOrderId(String peOrderId) {
        this.peOrderId = peOrderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderInfoSrNo() {
        return orderInfoSrNo;
    }

    public void setOrderInfoSrNo(String orderInfoSrNo) {
        this.orderInfoSrNo = orderInfoSrNo;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "name='" + name + '\'' +
                ", estimatedDate='" + estimatedDate + '\'' +
                ", placedOrderDate='" + placedOrderDate + '\'' +
                ", customerId='" + customerId + '\'' +
                ", peOrderId='" + peOrderId + '\'' +
                ", address='" + address + '\'' +
                ", orderInfoSrNo='" + orderInfoSrNo + '\'' +
                ", images=" + images +
                '}';
    }
}
