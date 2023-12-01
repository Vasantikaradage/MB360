package com.csform.android.MB360.wellness.medicinedelivery.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartOrdersResponse {
    @SerializedName("CartOrders")
    @Expose
    private List<CartOrder> cartOrders = new ArrayList<>();
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;

    public List<CartOrder> getCartOrders() {
        return cartOrders;
    }

    public void setCartOrders(List<CartOrder> cartOrders) {
        this.cartOrders = cartOrders;
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
        return "CartOrdersResponse{" +
                "cartOrders=" + cartOrders +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
