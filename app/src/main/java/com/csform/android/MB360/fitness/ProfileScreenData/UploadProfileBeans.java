package com.csform.android.MB360.fitness.ProfileScreenData;

import com.google.gson.annotations.SerializedName;

public class UploadProfileBeans {
    @SerializedName("MEMBER_ID")
    private String strMemberID;

    @SerializedName("MEMBER_EXTERNAL_ID")
    private String strMemberExternalId;

    @SerializedName("MEMBER_ONBOARD")
    private String strMemberOnboard;

    @SerializedName("COMPANY_ID")
    private String strCompanyId;

    @SerializedName("COMPANY_EXT_ID")
    private String strCompantExtId;

    @SerializedName("COMPANY_ONBOARD")
    private String strCompanyOnboard;

    @SerializedName("WAKE_UP_TIME")
    private String strWakeUpTime;

    @SerializedName("BED_TIME")
    private String strBedTime;

    @SerializedName("WEIGHT")
    private String strWeight;

    @SerializedName("HEIGHT")
    private String strHeight;

    @SerializedName("EMPLOYEE_SR_NO")
    private String strEmpSrno;

    public String getStrMemberID() {
        return strMemberID;
    }

    public void setStrMemberID(String strMemberID) {
        this.strMemberID = strMemberID;
    }

    public String getStrMemberExternalId() {
        return strMemberExternalId;
    }

    public void setStrMemberExternalId(String strMemberExternalId) {
        this.strMemberExternalId = strMemberExternalId;
    }

    public String getStrMemberOnboard() {
        return strMemberOnboard;
    }

    public void setStrMemberOnboard(String strMemberOnboard) {
        this.strMemberOnboard = strMemberOnboard;
    }

    public String getStrCompanyId() {
        return strCompanyId;
    }

    public void setStrCompanyId(String strCompanyId) {
        this.strCompanyId = strCompanyId;
    }

    public String getStrCompantExtId() {
        return strCompantExtId;
    }

    public void setStrCompantExtId(String strCompantExtId) {
        this.strCompantExtId = strCompantExtId;
    }

    public String getStrCompanyOnboard() {
        return strCompanyOnboard;
    }

    public void setStrCompanyOnboard(String strCompanyOnboard) {
        this.strCompanyOnboard = strCompanyOnboard;
    }



    public String getStrWakeUpTime() {
        return strWakeUpTime;
    }

    public void setStrWakeUpTime(String strWakeUpTime) {
        this.strWakeUpTime = strWakeUpTime;
    }

    public String getStrBedTime() {
        return strBedTime;
    }

    public void setStrBedTime(String strBedTime) {
        this.strBedTime = strBedTime;
    }

    public String getStrWeight() {
        return strWeight;
    }

    public void setStrWeight(String strWeight) {
        this.strWeight = strWeight;
    }

    public String getStrHeight() {
        return strHeight;
    }

    public void setStrHeight(String strHeight) {
        this.strHeight = strHeight;
    }

    public String getStrEmpSrno() {
        return strEmpSrno;
    }

    public void setStrEmpSrno(String strEmpSrno) {
        this.strEmpSrno = strEmpSrno;
    }
}
