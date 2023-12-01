package com.csform.android.MB360.fitness.AktivoData;

import com.csform.android.MB360.wellness.homehealthcare.cancellation.responseclass.ApiStatus;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AktivoMBProfile {

    @SerializedName("UserPhysicalInfo")
    private List<UserPhysicalInfo> lstUserPhysicalInfo;

    @SerializedName("UserMemberIdInfo")
    private List<UserMemberIdInfo> lstUserMemberIdInfo;

    @SerializedName("UserCompIdInfo")
    private List<UserCompIdInfo> lstUserCompIdInfo;

    @SerializedName("message")
    private ApiStatus apiStatus;

    public ApiStatus getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(ApiStatus apiStatus) {
        this.apiStatus = apiStatus;
    }

    public List<UserPhysicalInfo> getLstUserPhysicalInfo() {
        return lstUserPhysicalInfo;
    }

    public void setLstUserPhysicalInfo(List<UserPhysicalInfo> lstUserPhysicalInfo) {
        this.lstUserPhysicalInfo = lstUserPhysicalInfo;
    }

    public List<UserMemberIdInfo> getLstUserMemberIdInfo() {
        return lstUserMemberIdInfo;
    }

    public void setLstUserMemberIdInfo(List<UserMemberIdInfo> lstUserMemberIdInfo) {
        this.lstUserMemberIdInfo = lstUserMemberIdInfo;
    }

    public List<UserCompIdInfo> getLstUserCompIdInfo() {
        return lstUserCompIdInfo;
    }

    public void setLstUserCompIdInfo(List<UserCompIdInfo> lstUserCompIdInfo) {
        this.lstUserCompIdInfo = lstUserCompIdInfo;
    }

    public static class UserPhysicalInfo {
        @SerializedName("WAKE_UP_TIME")
        private String strWakeupTime;

        @SerializedName("BED_TIME")
        private String strBedTime;

        @SerializedName("WEIGHT")
        private String strWeight;

        @SerializedName("HEIGHT")
        private String strHeight;

        @SerializedName("EMPLOYEE_SR_NO")
        private String strEmpSrNo;

        public String getStrWakeupTime() {
            return strWakeupTime;
        }

        public void setStrWakeupTime(String strWakeupTime) {
            this.strWakeupTime = strWakeupTime;
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

        public String getStrEmpSrNo() {
            return strEmpSrNo;
        }

        public void setStrEmpSrNo(String strEmpSrNo) {
            this.strEmpSrNo = strEmpSrNo;
        }
    }


    public static class UserMemberIdInfo {
        @SerializedName("MEMBER_ID")
        private String strMemberId;

        @SerializedName("MEMBER_EXTERNAL_ID")
        private String strMemberExtId;

        @SerializedName("MEMBER_ONBOARD")
        private String strMemberOnBoard;

        public String getStrMemberId() {
            return strMemberId;
        }

        public void setStrMemberId(String strMemberId) {
            this.strMemberId = strMemberId;
        }

        public String getStrMemberExtId() {
            return strMemberExtId;
        }

        public void setStrMemberExtId(String strMemberExtId) {
            this.strMemberExtId = strMemberExtId;
        }

        public String getStrMemberOnBoard() {
            return strMemberOnBoard;
        }

        public void setStrMemberOnBoard(String strMemberOnBoard) {
            this.strMemberOnBoard = strMemberOnBoard;
        }
    }

    public static class UserCompIdInfo {

        @SerializedName("COMPANY_ID")
        private String strCompanyID;

        @SerializedName("COMPANY_EXT_ID")
        private String strCompanyExtID;

        @SerializedName("COMPANY_ONBOARD")
        private String strCompanyonboard;

        public String getStrCompanyID() {
            return strCompanyID;
        }

        public void setStrCompanyID(String strCompanyID) {
            this.strCompanyID = strCompanyID;
        }

        public String getStrCompanyExtID() {
            return strCompanyExtID;
        }

        public void setStrCompanyExtID(String strCompanyExtID) {
            this.strCompanyExtID = strCompanyExtID;
        }

        public String getStrCompanyonboard() {
            return strCompanyonboard;
        }

        public void setStrCompanyonboard(String strCompanyonboard) {
            this.strCompanyonboard = strCompanyonboard;
        }
    }

}
