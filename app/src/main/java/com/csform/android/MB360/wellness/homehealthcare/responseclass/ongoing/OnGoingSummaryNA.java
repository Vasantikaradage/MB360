package com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnGoingSummaryNA implements Parcelable {
    @SerializedName("HHC_APPT_INFO_SR_NO")
    @Expose
    private String hhcApptInfoSrNo;
    @SerializedName("HHC_NA_REMARKS")
    @Expose
    private String hhcNaRemarks;
    @SerializedName("HHC_APPT_NA_REF_NO")
    @Expose
    private String hhcApptNaRefNo;
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
    @SerializedName("APPT_LOCATION_IS_METRO")
    @Expose
    private String apptLocationIsMetro;
    @SerializedName("SELECTED_PKG_SR_NO")
    @Expose
    private String selectedPkgSrNo;
    @SerializedName("NA_HOURS")
    @Expose
    private String naHours;
    @SerializedName("NA_DURATIONS")
    @Expose
    private String naDurations;
    @SerializedName("NA_NACOUNT")
    @Expose
    private String naNacount;
    @SerializedName("APPT_STATUS")
    @Expose
    private String apptStatus;
    @SerializedName("NO_OF_DAYS")
    @Expose
    private String noOfDays;
    @SerializedName("DATE_CONDITION")
    @Expose
    private String dateCondition;
    @SerializedName("DATE_PREFERENCE")
    @Expose
    private String datePreference;
    @SerializedName("FROM_DATE")
    @Expose
    private String fromDate;
    @SerializedName("TO_DATE")
    @Expose
    private String toDate;
    @SerializedName("PKG_PRICE")
    @Expose
    private String pkgPrice;
    @SerializedName("TOTAL_PRICE")
    @Expose
    private String totalPrice;
    @SerializedName("SCHEDULED_ON")
    @Expose
    private String scheduledOn;
    @SerializedName("NO_OF_MONTHS")
    @Expose
    private String noOfMonths;

    public OnGoingSummaryNA() {
    }

    public OnGoingSummaryNA(Parcel in) {
        hhcApptInfoSrNo = in.readString();
        hhcNaRemarks = in.readString();
        hhcApptNaRefNo = in.readString();
        appntPerson = in.readString();
        appntPersonAge = in.readString();
        appntPersonSrNo = in.readString();
        apptLocation = in.readString();
        apptLocationIsMetro = in.readString();
        selectedPkgSrNo = in.readString();
        naHours = in.readString();
        naDurations = in.readString();
        naNacount = in.readString();
        apptStatus = in.readString();
        noOfDays = in.readString();
        dateCondition = in.readString();
        datePreference = in.readString();
        fromDate = in.readString();
        toDate = in.readString();
        pkgPrice = in.readString();
        totalPrice = in.readString();
        scheduledOn = in.readString();
        noOfMonths = in.readString();
    }

    public static final Creator<OnGoingSummaryNA> CREATOR = new Creator<OnGoingSummaryNA>() {
        @Override
        public OnGoingSummaryNA createFromParcel(Parcel in) {
            return new OnGoingSummaryNA(in);
        }

        @Override
        public OnGoingSummaryNA[] newArray(int size) {
            return new OnGoingSummaryNA[size];
        }
    };

    public String getHhcApptInfoSrNo() {
        return hhcApptInfoSrNo;
    }

    public void setHhcApptInfoSrNo(String hhcApptInfoSrNo) {
        this.hhcApptInfoSrNo = hhcApptInfoSrNo;
    }

    public String getHhcNaRemarks() {
        return hhcNaRemarks;
    }

    public void setHhcNaRemarks(String hhcNaRemarks) {
        this.hhcNaRemarks = hhcNaRemarks;
    }

    public String getHhcApptNaRefNo() {
        return hhcApptNaRefNo;
    }

    public void setHhcApptNaRefNo(String hhcApptNaRefNo) {
        this.hhcApptNaRefNo = hhcApptNaRefNo;
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

    public String getApptLocationIsMetro() {
        return apptLocationIsMetro;
    }

    public void setApptLocationIsMetro(String apptLocationIsMetro) {
        this.apptLocationIsMetro = apptLocationIsMetro;
    }

    public String getSelectedPkgSrNo() {
        return selectedPkgSrNo;
    }

    public void setSelectedPkgSrNo(String selectedPkgSrNo) {
        this.selectedPkgSrNo = selectedPkgSrNo;
    }

    public String getNaHours() {
        return naHours;
    }

    public void setNaHours(String naHours) {
        this.naHours = naHours;
    }

    public String getNaDurations() {
        return naDurations;
    }

    public void setNaDurations(String naDurations) {
        this.naDurations = naDurations;
    }

    public String getNaNacount() {
        return naNacount;
    }

    public void setNaNacount(String naNacount) {
        this.naNacount = naNacount;
    }

    public String getApptStatus() {
        return apptStatus;
    }

    public void setApptStatus(String apptStatus) {
        this.apptStatus = apptStatus;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getDateCondition() {
        return dateCondition;
    }

    public void setDateCondition(String dateCondition) {
        this.dateCondition = dateCondition;
    }

    public String getDatePreference() {
        return datePreference;
    }

    public void setDatePreference(String datePreference) {
        this.datePreference = datePreference;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getScheduledOn() {
        return scheduledOn;
    }

    public void setScheduledOn(String scheduledOn) {
        this.scheduledOn = scheduledOn;
    }

    public String getNoOfMonths() {
        return noOfMonths;
    }

    public void setNoOfMonths(String noOfMonths) {
        this.noOfMonths = noOfMonths;
    }

    @Override
    public String toString() {
        return "OnGoingSummaryNA{" +
                "hhcApptInfoSrNo='" + hhcApptInfoSrNo + '\'' +
                ", hhcNaRemarks='" + hhcNaRemarks + '\'' +
                ", hhcApptNaRefNo='" + hhcApptNaRefNo + '\'' +
                ", appntPerson='" + appntPerson + '\'' +
                ", appntPersonAge='" + appntPersonAge + '\'' +
                ", appntPersonSrNo='" + appntPersonSrNo + '\'' +
                ", apptLocation='" + apptLocation + '\'' +
                ", apptLocationIsMetro='" + apptLocationIsMetro + '\'' +
                ", selectedPkgSrNo='" + selectedPkgSrNo + '\'' +
                ", naHours='" + naHours + '\'' +
                ", naDurations='" + naDurations + '\'' +
                ", naNacount='" + naNacount + '\'' +
                ", apptStatus='" + apptStatus + '\'' +
                ", noOfDays='" + noOfDays + '\'' +
                ", dateCondition='" + dateCondition + '\'' +
                ", datePreference='" + datePreference + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", pkgPrice='" + pkgPrice + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", scheduledOn='" + scheduledOn + '\'' +
                ", noOfMonths='" + noOfMonths + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(hhcApptInfoSrNo);
        dest.writeString(hhcNaRemarks);
        dest.writeString(hhcApptNaRefNo);
        dest.writeString(appntPerson);
        dest.writeString(appntPersonAge);
        dest.writeString(appntPersonSrNo);
        dest.writeString(apptLocation);
        dest.writeString(apptLocationIsMetro);
        dest.writeString(selectedPkgSrNo);
        dest.writeString(naHours);
        dest.writeString(naDurations);
        dest.writeString(naNacount);
        dest.writeString(apptStatus);
        dest.writeString(noOfDays);
        dest.writeString(dateCondition);
        dest.writeString(datePreference);
        dest.writeString(fromDate);
        dest.writeString(toDate);
        dest.writeString(pkgPrice);
        dest.writeString(totalPrice);
        dest.writeString(scheduledOn);
        dest.writeString(noOfMonths);
    }
}
