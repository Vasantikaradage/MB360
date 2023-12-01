package com.csform.android.MB360.fitness.ProfileScreenData;

import com.csform.android.MB360.fitness.AktivoData.AktivoChallenges;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AktivoBeanProfile implements Serializable {

    @SerializedName("data")
    private Data aktivoData;

    @SerializedName("_embedded")
    public ChallengesData challengesData;

    public ChallengesData getChallengesData() {
        return challengesData;
    }

    public void setChallengesData(ChallengesData challengesData) {
        this.challengesData = challengesData;
    }

    public Data getAktivoData() {
        return aktivoData;
    }

    public void setAktivoData(Data aktivoData) {
        this.aktivoData = aktivoData;
    }

    public static class ChallengesData{

        @SerializedName("leaderboards")
        public List<AktivoChallenges> lstChallenges;

        public List<AktivoChallenges> getLstChallenges() {
            return lstChallenges;
        }

        public void setLstChallenges(List<AktivoChallenges> lstChallenges) {
            this.lstChallenges = lstChallenges;
        }
    }

    public static class Data {

        @SerializedName("_id")
        private String aktivo_id;

        @SerializedName("firstname")
        private String strFirstName;

        @SerializedName("lastname")
        private String strLastName;

        @SerializedName("email")
        private String strEmail;

        @SerializedName("sex")
        private String strGender;

        @SerializedName("timezone")
        private String strTimeZone;

        @SerializedName("wakeup")
        private String strLastTime;

        @SerializedName("bedtime")
        private String strBedTime;

        @SerializedName("platform_connected")
        private Boolean strPlatformConn;

        @SerializedName("platform_connected_forever")
        private Boolean strPlatformConnForever;

        @SerializedName("first_login")
        private String strFirstLogin;

        @SerializedName("platform_connected_type")
        private String strPlatformConnType;

        @SerializedName("platform_connected_datetime")
        private String strPlatformConnDate;

        public String getAktivo_id() {
            return aktivo_id;
        }

        public void setAktivo_id(String aktivo_id) {
            this.aktivo_id = aktivo_id;
        }

        public String getStrFirstName() {
            return strFirstName;
        }

        public void setStrFirstName(String strFirstName) {
            this.strFirstName = strFirstName;
        }

        public String getStrLastName() {
            return strLastName;
        }

        public void setStrLastName(String strLastName) {
            this.strLastName = strLastName;
        }

        public String getStrEmail() {
            return strEmail;
        }

        public void setStrEmail(String strEmail) {
            this.strEmail = strEmail;
        }

        public String getStrGender() {
            return strGender;
        }

        public void setStrGender(String strGender) {
            this.strGender = strGender;
        }

        public String getStrTimeZone() {
            return strTimeZone;
        }

        public void setStrTimeZone(String strTimeZone) {
            this.strTimeZone = strTimeZone;
        }

        public String getStrLastTime() {
            return strLastTime;
        }

        public void setStrLastTime(String strLastTime) {
            this.strLastTime = strLastTime;
        }

        public String getStrBedTime() {
            return strBedTime;
        }

        public void setStrBedTime(String strBedTime) {
            this.strBedTime = strBedTime;
        }

        public Boolean getStrPlatformConn() {
            return strPlatformConn;
        }

        public void setStrPlatformConn(Boolean strPlatformConn) {
            this.strPlatformConn = strPlatformConn;
        }

        public Boolean getStrPlatformConnForever() {
            return strPlatformConnForever;
        }

        public void setStrPlatformConnForever(Boolean strPlatformConnForever) {
            this.strPlatformConnForever = strPlatformConnForever;
        }

        public String getStrFirstLogin() {
            return strFirstLogin;
        }

        public void setStrFirstLogin(String strFirstLogin) {
            this.strFirstLogin = strFirstLogin;
        }
    }

}
