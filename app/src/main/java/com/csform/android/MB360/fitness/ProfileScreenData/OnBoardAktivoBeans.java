package com.csform.android.MB360.fitness.ProfileScreenData;

import com.google.gson.annotations.SerializedName;

public class OnBoardAktivoBeans {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("member")
        private Member strMemberData;
        @SerializedName("company")
        private Member strCompanyData;
        @SerializedName("isNew")
        private boolean isNew;

        public Member getStrMemberData() {
            return strMemberData;
        }

        public void setStrMemberData(Member strMemberData) {
            this.strMemberData = strMemberData;
        }

        public Member getStrCompanyData() {
            return strCompanyData;
        }

        public void setStrCompanyData(Member strCompanyData) {
            this.strCompanyData = strCompanyData;
        }

        public boolean isNew() {
            return isNew;
        }

        public void setNew(boolean aNew) {
            isNew = aNew;
        }
    }

    public static class Member {
        @SerializedName("_id")
        private String memberID;

        @SerializedName("externalId")
        private String externalId;

        @SerializedName("isNew")
        private boolean isNew;

        public String getMemberID() {
            return memberID;
        }

        public void setMemberID(String memberID) {
            this.memberID = memberID;
        }

        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public boolean isNew() {
            return isNew;
        }

        public void setNew(boolean aNew) {
            isNew = aNew;
        }
    }


}


