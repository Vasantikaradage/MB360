package com.csform.android.MB360.wellness.medicinedelivery.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddressResponse {

    @SerializedName("Address")
    @Expose
    private List<Address> address = new ArrayList<>();
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
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
        return "AddressResponse{" +
                "address=" + address +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
