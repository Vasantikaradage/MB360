package com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnGoingSummaryDS  implements Parcelable {
    @SerializedName("HHC_DS_APPT_INFO_SR_NO")
    @Expose
    private String hhcDsApptInfoSrNo;
    @SerializedName("HHC_DS_REMARKS")
    @Expose
    private String hhcDsRemarks;
    @SerializedName("HHC_APPT_DS_REF_NO")
    @Expose
    private String hhcApptDsRefNo;
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
    @SerializedName("DS_CATEGORY")
    @Expose
    private String dsCategory;
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

    protected OnGoingSummaryDS(Parcel in) {
        hhcDsApptInfoSrNo = in.readString();
        hhcDsRemarks = in.readString();
        hhcApptDsRefNo = in.readString();
        appntPerson = in.readString();
        appntPersonAge = in.readString();
        appntPersonSrNo = in.readString();
        apptLocation = in.readString();
        selectedPkgSrNo = in.readString();
        dsCategory = in.readString();
        apptStatus = in.readString();
        datePreference = in.readString();
        timePreference = in.readString();
        pkgPrice = in.readString();
        scheduledOn = in.readString();
    }

    public static final Creator<OnGoingSummaryDS> CREATOR = new Creator<OnGoingSummaryDS>() {
        @Override
        public OnGoingSummaryDS createFromParcel(Parcel in) {
            return new OnGoingSummaryDS(in);
        }

        @Override
        public OnGoingSummaryDS[] newArray(int size) {
            return new OnGoingSummaryDS[size];
        }
    };

    public String getHhcDsApptInfoSrNo() {
        return hhcDsApptInfoSrNo;
    }

    public void setHhcDsApptInfoSrNo(String hhcDsApptInfoSrNo) {
        this.hhcDsApptInfoSrNo = hhcDsApptInfoSrNo;
    }

    public String getHhcDsRemarks() {
        return hhcDsRemarks;
    }

    public void setHhcDsRemarks(String hhcDsRemarks) {
        this.hhcDsRemarks = hhcDsRemarks;
    }

    public String getHhcApptDsRefNo() {
        return hhcApptDsRefNo;
    }

    public void setHhcApptDsRefNo(String hhcApptDsRefNo) {
        this.hhcApptDsRefNo = hhcApptDsRefNo;
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

    public String getDsCategory() {
        return dsCategory;
    }

    public void setDsCategory(String dsCategory) {
        this.dsCategory = dsCategory;
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
    public String toString() {
        return "OnGoingSummaryDS{" +
                "hhcDsApptInfoSrNo='" + hhcDsApptInfoSrNo + '\'' +
                ", hhcDsRemarks='" + hhcDsRemarks + '\'' +
                ", hhcApptDsRefNo='" + hhcApptDsRefNo + '\'' +
                ", appntPerson='" + appntPerson + '\'' +
                ", appntPersonAge='" + appntPersonAge + '\'' +
                ", appntPersonSrNo='" + appntPersonSrNo + '\'' +
                ", apptLocation='" + apptLocation + '\'' +
                ", selectedPkgSrNo='" + selectedPkgSrNo + '\'' +
                ", dsCategory='" + dsCategory + '\'' +
                ", apptStatus='" + apptStatus + '\'' +
                ", datePreference='" + datePreference + '\'' +
                ", timePreference='" + timePreference + '\'' +
                ", pkgPrice='" + pkgPrice + '\'' +
                ", scheduledOn='" + scheduledOn + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(hhcDsApptInfoSrNo);
        parcel.writeString(hhcDsRemarks);
        parcel.writeString(hhcApptDsRefNo);
        parcel.writeString(appntPerson);
        parcel.writeString(appntPersonAge);
        parcel.writeString(appntPersonSrNo);
        parcel.writeString(apptLocation);
        parcel.writeString(selectedPkgSrNo);
        parcel.writeString(dsCategory);
        parcel.writeString(apptStatus);
        parcel.writeString(datePreference);
        parcel.writeString(timePreference);
        parcel.writeString(pkgPrice);
        parcel.writeString(scheduledOn);
    }
}
