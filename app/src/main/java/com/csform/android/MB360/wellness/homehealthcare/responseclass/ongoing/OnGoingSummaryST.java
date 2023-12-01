package com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnGoingSummaryST implements Parcelable {

    @SerializedName("HHC_ST_APPT_INFO_SR_NO")
    @Expose
    private String hhcStApptInfoSrNo;
    @SerializedName("HHC_ST_REMARKS")
    @Expose
    private String hhcStRemarks;
    @SerializedName("HHC_APPT_ST_REF_NO")
    @Expose
    private String hhcApptStRefNo;
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
    @SerializedName("ST_CATEGORY")
    @Expose
    private String stCategory;
    @SerializedName("APPT_STATUS")
    @Expose
    private String apptStatus;
    @SerializedName("DATE_PREFERENCE")
    @Expose
    private String datePreference;
    @SerializedName("TIME_PREFERENCE")
    @Expose
    private String timePreference;
    @SerializedName("PKG_PRICE")
    @Expose
    private String pkgPrice;
    @SerializedName("SCHEDULED_ON")
    @Expose
    private String scheduledOn;


    protected OnGoingSummaryST(Parcel in) {
        hhcStApptInfoSrNo = in.readString();
        hhcStRemarks = in.readString();
        hhcApptStRefNo = in.readString();
        appntPerson = in.readString();
        appntPersonAge = in.readString();
        appntPersonSrNo = in.readString();
        apptLocation = in.readString();
        selectedPkgSrNo = in.readString();
        stCategory = in.readString();
        apptStatus = in.readString();
        datePreference = in.readString();
        timePreference = in.readString();
        pkgPrice = in.readString();
        scheduledOn = in.readString();
    }

    public static final Creator<OnGoingSummaryST> CREATOR = new Creator<OnGoingSummaryST>() {
        @Override
        public OnGoingSummaryST createFromParcel(Parcel in) {
            return new OnGoingSummaryST(in);
        }

        @Override
        public OnGoingSummaryST[] newArray(int size) {
            return new OnGoingSummaryST[size];
        }
    };

    public String getHhcStApptInfoSrNo() {
        return hhcStApptInfoSrNo;
    }

    public void setHhcStApptInfoSrNo(String hhcStApptInfoSrNo) {
        this.hhcStApptInfoSrNo = hhcStApptInfoSrNo;
    }

    public String getHhcStRemarks() {
        return hhcStRemarks;
    }

    public void setHhcStRemarks(String hhcStRemarks) {
        this.hhcStRemarks = hhcStRemarks;
    }

    public String getHhcApptStRefNo() {
        return hhcApptStRefNo;
    }

    public void setHhcApptStRefNo(String hhcApptStRefNo) {
        this.hhcApptStRefNo = hhcApptStRefNo;
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

    public String getStCategory() {
        return stCategory;
    }

    public void setStCategory(String stCategory) {
        this.stCategory = stCategory;
    }

    public String getApptStatus() {
        return apptStatus;
    }

    public void setApptStatus(String apptStatus) {
        this.apptStatus = apptStatus;
    }

    public String getDatePreference() {
        return datePreference;
    }

    public void setDatePreference(String datePreference) {
        this.datePreference = datePreference;
    }

    public String getTimePreference() {
        return timePreference;
    }

    public void setTimePreference(String timePreference) {
        this.timePreference = timePreference;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(hhcStApptInfoSrNo);
        parcel.writeString(hhcStRemarks);
        parcel.writeString(hhcApptStRefNo);
        parcel.writeString(appntPerson);
        parcel.writeString(appntPersonAge);
        parcel.writeString(appntPersonSrNo);
        parcel.writeString(apptLocation);
        parcel.writeString(selectedPkgSrNo);
        parcel.writeString(stCategory);
        parcel.writeString(apptStatus);
        parcel.writeString(datePreference);
        parcel.writeString(timePreference);
        parcel.writeString(pkgPrice);
        parcel.writeString(scheduledOn);
    }
}
