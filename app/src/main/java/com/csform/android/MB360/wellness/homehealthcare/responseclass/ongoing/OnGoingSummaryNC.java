package com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnGoingSummaryNC {
    @SerializedName("HHC_NC_APPT_INFO_SR_NO")
    @Expose
    private String hhcNcApptInfoSrNo;
    @SerializedName("HHC_NC_REMARKS")
    @Expose
    private String hhcNcRemarks;
    @SerializedName("HHC_APPT_NC_REF_NO")
    @Expose
    private String hhcApptNcRefNo;
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
    @SerializedName("NC_CATEGORY")
    @Expose
    private String ncCategory;
    @SerializedName("APPT_STATUS")
    @Expose
    private String apptStatus;
    @SerializedName("PKG_PRICE")
    @Expose
    private String pkgPrice;
    @SerializedName("SCHEDULED_ON")
    @Expose
    private String scheduledOn;

    public String getHhcNcApptInfoSrNo() {
        return hhcNcApptInfoSrNo;
    }

    public void setHhcNcApptInfoSrNo(String hhcEcApptInfoSrNo) {
        this.hhcNcApptInfoSrNo = hhcEcApptInfoSrNo;
    }

    public String getHhcNcRemarks() {
        return hhcNcRemarks;
    }

    public void setHhcNcRemarks(String hhcEcRemarks) {
        this.hhcNcRemarks = hhcEcRemarks;
    }

    public String getHhcApptNcRefNo() {
        return hhcApptNcRefNo;
    }

    public void setHhcApptNcRefNo(String hhcApptEcRefNo) {
        this.hhcApptNcRefNo = hhcApptEcRefNo;
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

    public String getNcCategory() {
        return ncCategory;
    }

    public void setNcCategory(String ecCategory) {
        this.ncCategory = ecCategory;
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
        return "OnGoingSummaryNC{" +
                "hhcNcApptInfoSrNo='" + hhcNcApptInfoSrNo + '\'' +
                ", hhcNcRemarks='" + hhcNcRemarks + '\'' +
                ", hhcApptNcRefNo='" + hhcApptNcRefNo + '\'' +
                ", appntPerson='" + appntPerson + '\'' +
                ", appntPersonAge='" + appntPersonAge + '\'' +
                ", appntPersonSrNo='" + appntPersonSrNo + '\'' +
                ", apptLocation='" + apptLocation + '\'' +
                ", selectedPkgSrNo='" + selectedPkgSrNo + '\'' +
                ", ncCategory='" + ncCategory + '\'' +
                ", apptStatus='" + apptStatus + '\'' +
                ", pkgPrice='" + pkgPrice + '\'' +
                ", scheduledOn='" + scheduledOn + '\'' +
                '}';
    }
}
