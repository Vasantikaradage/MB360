package com.csform.android.MB360.wellness.medicinedelivery.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartOrder {


    @SerializedName("FamilySrNo")
    @Expose
    private String familySrNo;
    @SerializedName("PersonName")
    @Expose
    private String personName;
    @SerializedName("TempOrderID")
    @Expose
    private Object tempOrderID;
    @SerializedName("PersonSrNO")
    @Expose
    private String personSrNO;
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
    @SerializedName("Images")
    @Expose
    private List<String> images = new ArrayList<>();
    @SerializedName("Remark")
    @Expose
    private String remark;
    @SerializedName("AddCartOn")
    @Expose
    private String addCartOn;
    @SerializedName("CartId")
    @Expose
    private Integer cartId;

    public String getFamilySrNo() {
        return familySrNo;
    }

    public void setFamilySrNo(String familySrNo) {
        this.familySrNo = familySrNo;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Object getTempOrderID() {
        return tempOrderID;
    }

    public void setTempOrderID(Object tempOrderID) {
        this.tempOrderID = tempOrderID;
    }

    public String getPersonSrNO() {
        return personSrNO;
    }

    public void setPersonSrNO(String personSrNO) {
        this.personSrNO = personSrNO;
    }

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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddCartOn() {
        return addCartOn;
    }

    public void setAddCartOn(String addCartOn) {
        this.addCartOn = addCartOn;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    @Override
    public String toString() {
        return "CartOrder{" + "familySrNo='" + familySrNo + '\'' + ", personName='" + personName + '\'' + ", tempOrderID=" + tempOrderID + ", personSrNO='" + personSrNO + '\'' + ", flatHouse='" + flatHouse + '\'' + ", area='" + area + '\'' + ", landmark='" + landmark + '\'' + ", pincode='" + pincode + '\'' + ", city='" + city + '\'' + ", state='" + state + '\'' + ", emailId='" + emailId + '\'' + ", mobile='" + mobile + '\'' + ", images=" + images + ", remark='" + remark + '\'' + ", addCartOn='" + addCartOn + '\'' + ", cartId=" + cartId + '}';
    }
}
