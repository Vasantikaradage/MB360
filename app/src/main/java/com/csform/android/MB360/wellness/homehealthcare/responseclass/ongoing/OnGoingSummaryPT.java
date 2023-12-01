package com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnGoingSummaryPT implements Parcelable {
    @SerializedName("HHC_PT_APPT_INFO_SR_NO")
    @Expose
    private String hhcPtApptInfoSrNo;
    @SerializedName("HHC_PT_REMARKS")
    @Expose
    private String hhcPtRemarks;
    @SerializedName("HHC_APPT_PT_REF_NO")
    @Expose
    private String hhcApptPtRefNo;
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
    @SerializedName("PT_CATEGORY")
    @Expose
    private String ptCategory;
    @SerializedName("APPT_STATUS")
    @Expose
    private String apptStatus;
    @SerializedName("DATE_PREFERENCE")
    @Expose
    private String datePreference;
    @SerializedName("TIME_PREFERENCE")
    @Expose
    private String timePreference;
    @SerializedName("FROM_DATE")
    @Expose
    private String fromDate;
    @SerializedName("TO_DATE")
    @Expose
    private String toDate;
    @SerializedName("PKG_PRICE")
    @Expose
    private String pkgPrice;
    @SerializedName("SCHEDULED_ON")
    @Expose
    private String scheduledOn;

    protected OnGoingSummaryPT(Parcel in) {
        hhcPtApptInfoSrNo = in.readString();
        hhcPtRemarks = in.readString();
        hhcApptPtRefNo = in.readString();
        appntPerson = in.readString();
        appntPersonAge = in.readString();
        appntPersonSrNo = in.readString();
        apptLocation = in.readString();
        selectedPkgSrNo = in.readString();
        ptCategory = in.readString();
        apptStatus = in.readString();
        datePreference = in.readString();
        timePreference = in.readString();
        fromDate = in.readString();
        toDate = in.readString();
        pkgPrice = in.readString();
        scheduledOn = in.readString();
    }

    public static final Creator<OnGoingSummaryPT> CREATOR = new Creator<OnGoingSummaryPT>() {
        @Override
        public OnGoingSummaryPT createFromParcel(Parcel in) {
            return new OnGoingSummaryPT(in);
        }

        @Override
        public OnGoingSummaryPT[] newArray(int size) {
            return new OnGoingSummaryPT[size];
        }
    };

    public String getHhcPtApptInfoSrNo() {
        return hhcPtApptInfoSrNo;
    }

    public void setHhcPtApptInfoSrNo(String hhcPtApptInfoSrNo) {
        this.hhcPtApptInfoSrNo = hhcPtApptInfoSrNo;
    }

    public String getHhcPtRemarks() {
        return hhcPtRemarks;
    }

    public void setHhcPtRemarks(String hhcPtRemarks) {
        this.hhcPtRemarks = hhcPtRemarks;
    }

    public String getHhcApptPtRefNo() {
        return hhcApptPtRefNo;
    }

    public void setHhcApptPtRefNo(String hhcApptPtRefNo) {
        this.hhcApptPtRefNo = hhcApptPtRefNo;
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

    public String getPtCategory() {
        return ptCategory;
    }

    public void setPtCategory(String ptCategory) {
        this.ptCategory = ptCategory;
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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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
        return "OnGoingSummaryPT{" +
                "hhcPtApptInfoSrNo='" + hhcPtApptInfoSrNo + '\'' +
                ", hhcPtRemarks='" + hhcPtRemarks + '\'' +
                ", hhcApptPtRefNo='" + hhcApptPtRefNo + '\'' +
                ", appntPerson='" + appntPerson + '\'' +
                ", appntPersonAge='" + appntPersonAge + '\'' +
                ", appntPersonSrNo='" + appntPersonSrNo + '\'' +
                ", apptLocation='" + apptLocation + '\'' +
                ", selectedPkgSrNo='" + selectedPkgSrNo + '\'' +
                ", ptCategory='" + ptCategory + '\'' +
                ", apptStatus='" + apptStatus + '\'' +
                ", datePreference='" + datePreference + '\'' +
                ", timePreference='" + timePreference + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
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
        parcel.writeString(hhcPtApptInfoSrNo);
        parcel.writeString(hhcPtRemarks);
        parcel.writeString(hhcApptPtRefNo);
        parcel.writeString(appntPerson);
        parcel.writeString(appntPersonAge);
        parcel.writeString(appntPersonSrNo);
        parcel.writeString(apptLocation);
        parcel.writeString(selectedPkgSrNo);
        parcel.writeString(ptCategory);
        parcel.writeString(apptStatus);
        parcel.writeString(datePreference);
        parcel.writeString(timePreference);
        parcel.writeString(fromDate);
        parcel.writeString(toDate);
        parcel.writeString(pkgPrice);
        parcel.writeString(scheduledOn);
    }
}
