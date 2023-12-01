package com.csform.android.MB360.fitness.ProfileScreenData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OnBoardRequest implements Serializable {

    @SerializedName("member")
    private Member member = new Member();

    @SerializedName("company")
    private Company company = new Company();

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public static class Member {

        @SerializedName("externalId")
        public String strEmpSrNo;

        @SerializedName("height_cm")
        public String strHeightCM;

        @SerializedName("weight_kg")
        public String strWeightKG;

        @SerializedName("date_of_birth")
        public String strDateOfBirth;

        @SerializedName("sex")
        public String strGender;

        @SerializedName("bedtime")
        public String strBedTime;

        @SerializedName("wakeup")
        public String strWakeUp;

        @SerializedName("firstname")
        public String strFirstName;

        @SerializedName("lastname")
        public String strLastName;

        public String getStrEmpSrNo() {
            return strEmpSrNo;
        }

        public void setStrEmpSrNo(String strEmpSrNo) {
            this.strEmpSrNo = strEmpSrNo;
        }

        public String getStrHeightCM() {
            return strHeightCM;
        }

        public void setStrHeightCM(String strHeightCM) {
            this.strHeightCM = strHeightCM;
        }

        public String getStrWeightKG() {
            return strWeightKG;
        }

        public void setStrWeightKG(String strWeightKG) {
            this.strWeightKG = strWeightKG;
        }

        public String getStrDateOfBirth() {
            return strDateOfBirth;
        }

        public void setStrDateOfBirth(String strDateOfBirth) {
            this.strDateOfBirth = strDateOfBirth;
        }

        public String getStrGender() {
            return strGender;
        }

        public void setStrGender(String strGender) {
            this.strGender = strGender;
        }

        public String getStrBedTime() {
            return strBedTime;
        }

        public void setStrBedTime(String strBedTime) {
            this.strBedTime = strBedTime;
        }

        public String getStrWakeUp() {
            return strWakeUp;
        }

        public void setStrWakeUp(String strWakeUp) {
            this.strWakeUp = strWakeUp;
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
    }

    public static class Company{
        @SerializedName("externalId")
        private String _id;

        @SerializedName("title")
        private String title;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
