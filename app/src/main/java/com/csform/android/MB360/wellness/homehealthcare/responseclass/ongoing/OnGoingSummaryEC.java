package com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnGoingSummaryEC {
    @SerializedName("HHC_EC_APPT_INFO_SR_NO")
    @Expose
    private String hhcEcApptInfoSrNo;
    @SerializedName("HHC_EC_REMARKS")
    @Expose
    private String hhcEcRemarks;
    @SerializedName("HHC_APPT_EC_REF_NO")
    @Expose
    private String hhcApptEcRefNo;
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
    @SerializedName("EC_CATEGORY")
    @Expose
    private String ecCategory;
    @SerializedName("APPT_STATUS")
    @Expose
    private String apptStatus;
    @SerializedName("PKG_PRICE")
    @Expose
    private String pkgPrice;
    @SerializedName("SCHEDULED_ON")
    @Expose
    private String scheduledOn;

    public String getHhcEcApptInfoSrNo() {
        return hhcEcApptInfoSrNo;
    }

    public void setHhcEcApptInfoSrNo(String hhcEcApptInfoSrNo) {
        this.hhcEcApptInfoSrNo = hhcEcApptInfoSrNo;
    }

    public String getHhcEcRemarks() {
        return hhcEcRemarks;
    }

    public void setHhcEcRemarks(String hhcEcRemarks) {
        this.hhcEcRemarks = hhcEcRemarks;
    }

    public String getHhcApptEcRefNo() {
        return hhcApptEcRefNo;
    }

    public void setHhcApptEcRefNo(String hhcApptEcRefNo) {
        this.hhcApptEcRefNo = hhcApptEcRefNo;
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

    public String getEcCategory() {
        return ecCategory;
    }

    public void setEcCategory(String ecCategory) {
        this.ecCategory = ecCategory;
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
        return "OnGoingSummaryEC{" +
                "hhcEcApptInfoSrNo='" + hhcEcApptInfoSrNo + '\'' +
                ", hhcEcRemarks='" + hhcEcRemarks + '\'' +
                ", hhcApptEcRefNo='" + hhcApptEcRefNo + '\'' +
                ", appntPerson='" + appntPerson + '\'' +
                ", appntPersonAge='" + appntPersonAge + '\'' +
                ", appntPersonSrNo='" + appntPersonSrNo + '\'' +
                ", apptLocation='" + apptLocation + '\'' +
                ", selectedPkgSrNo='" + selectedPkgSrNo + '\'' +
                ", ecCategory='" + ecCategory + '\'' +
                ", apptStatus='" + apptStatus + '\'' +
                ", pkgPrice='" + pkgPrice + '\'' +
                ", scheduledOn='" + scheduledOn + '\'' +
                '}';
    }
}
