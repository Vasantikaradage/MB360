package com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnGoingSummaryDM {
    @SerializedName("HHC_DM_APPT_INFO_SR_NO")
    @Expose
    private String hhcDmApptInfoSrNo;
    @SerializedName("HHC_DM_REMARKS")
    @Expose
    private String hhcDmRemarks;
    @SerializedName("HHC_APPT_DM_REF_NO")
    @Expose
    private String hhcApptDmRefNo;
    @SerializedName("APPNT_PERSON")
    @Expose
    private String appntPerson;
    @SerializedName("APPNT_PERSON_AGE")
    @Expose
    private String appntPersonAge;
    @SerializedName("APPNT_PERSON_SR_NO")
    @Expose
    private String appntPersonSrNo;
    @SerializedName("APPT_LOCATION")
    @Expose
    private String apptLocation;
    @SerializedName("SELECTED_PKG_SR_NO")
    @Expose
    private String selectedPkgSrNo;
    @SerializedName("DM_CATEGORY")
    @Expose
    private String dmCategory;
    @SerializedName("APPT_STATUS")
    @Expose
    private String apptStatus;
    @SerializedName("PKG_PRICE")
    @Expose
    private String pkgPrice;
    @SerializedName("SCHEDULED_ON")
    @Expose
    private String scheduledOn;

    public String getHhcDmApptInfoSrNo() {
        return hhcDmApptInfoSrNo;
    }

    public void setHhcDmApptInfoSrNo(String hhcDmApptInfoSrNo) {
        this.hhcDmApptInfoSrNo = hhcDmApptInfoSrNo;
    }

    public String getHhcDmRemarks() {
        return hhcDmRemarks;
    }

    public void setHhcDmRemarks(String hhcDmRemarks) {
        this.hhcDmRemarks = hhcDmRemarks;
    }

    public String getHhcApptDmRefNo() {
        return hhcApptDmRefNo;
    }

    public void setHhcApptDmRefNo(String hhcApptDmRefNo) {
        this.hhcApptDmRefNo = hhcApptDmRefNo;
    }

    public String getAppntPerson() {
        return appntPerson;
    }

    public void setAppntPerson(String appntPerson) {
        this.appntPerson = appntPerson;
    }

    public String getAppntPersonAge() {
        return appntPersonAge;
    }

    public void setAppntPersonAge(String appntPersonAge) {
        this.appntPersonAge = appntPersonAge;
    }

    public String getAppntPersonSrNo() {
        return appntPersonSrNo;
    }

    public void setAppntPersonSrNo(String appntPersonSrNo) {
        this.appntPersonSrNo = appntPersonSrNo;
    }

    public String getApptLocation() {
        return apptLocation;
    }

    public void setApptLocation(String apptLocation) {
        this.apptLocation = apptLocation;
    }

    public String getSelectedPkgSrNo() {
        return selectedPkgSrNo;
    }

    public void setSelectedPkgSrNo(String selectedPkgSrNo) {
        this.selectedPkgSrNo = selectedPkgSrNo;
    }

    public String getDmCategory() {
        return dmCategory;
    }

    public void setDmCategory(String dmCategory) {
        this.dmCategory = dmCategory;
    }

    public String getApptStatus() {
        return apptStatus;
    }

    public void setApptStatus(String apptStatus) {
        this.apptStatus = apptStatus;
    }

    public String getPkgPrice() {
        return pkgPrice;
    }

    public void setPkgPrice(String pkgPrice) {
        this.pkgPrice = pkgPrice;
    }

    public String getScheduledOn() {
        return scheduledOn;
    }

    public void setScheduledOn(String scheduledOn) {
        this.scheduledOn = scheduledOn;
    }

    @Override
    public String toString() {
        return "OnGoingSummaryDM{" +
                "hhcDmApptInfoSrNo='" + hhcDmApptInfoSrNo + '\'' +
                ", hhcDmRemarks='" + hhcDmRemarks + '\'' +
                ", hhcApptDmRefNo='" + hhcApptDmRefNo + '\'' +
                ", appntPerson='" + appntPerson + '\'' +
                ", appntPersonAge='" + appntPersonAge + '\'' +
                ", appntPersonSrNo='" + appntPersonSrNo + '\'' +
                ", apptLocation='" + apptLocation + '\'' +
                ", selectedPkgSrNo='" + selectedPkgSrNo + '\'' +
                ", dmCategory='" + dmCategory + '\'' +
                ", apptStatus='" + apptStatus + '\'' +
                ", pkgPrice='" + pkgPrice + '\'' +
                ", scheduledOn='" + scheduledOn + '\'' +
                '}';
    }
}
