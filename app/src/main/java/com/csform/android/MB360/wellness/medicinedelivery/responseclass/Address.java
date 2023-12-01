package com.csform.android.MB360.wellness.medicinedelivery.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("FlatHouse")
    @Expose
    private String flatHouse;
    @SerializedName("Area")
    @Expose
    private String area;
    @SerializedName("Landmark")
    @Expose
    private String landmark;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("EmailId")
    @Expose
    private String emailId;
    @SerializedName("Mobile")
    @Expose
    private String mobile;

    public String getFlatHouse() {
        return flatHouse;
    }

    public void setFlatHouse(String flatHouse) {
        this.flatHouse = flatHouse;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "Address{" +
                "flatHouse='" + flatHouse + '\'' +
                ", area='" + area + '\'' +
                ", landmark='" + landmark + '\'' +
                ", pincode='" + pincode + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", emailId='" + emailId + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
