package com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnGoingSummaryLT implements Parcelable {
    @SerializedName("HHC_LT_APPT_INFO_SR_NO")
    @Expose
    private String hhcLtApptInfoSrNo;
    @SerializedName("HHC_LT_REMARKS")
    @Expose
    private String hhcLtRemarks;
    @SerializedName("HHC_APPT_LT_REF_NO")
    @Expose
    private String hhcApptLtRefNo;
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
    @SerializedName("LT_HOURS")
    @Expose
    private String ltHours;
    @SerializedName("LT_DURATIONS")
    @Expose
    private String ltDurations;
    @SerializedName("LT_NACOUNT")
    @Expose
    private String ltNacount;
    @SerializedName("LT_CATEGORY")
    @Expose
    private String ltCategory;
    @SerializedName("APPT_STATUS")
    @Expose
    private String apptStatus;
    @SerializedName("NO_OF_DAYS")
    @Expose
    private String noOfDays;
    @SerializedName("NO_OF_MONTHS")
    @Expose
    private String noOfMonths;
    @SerializedName("NO_OF_WEEKS")
    @Expose
    private String noOfWeeks;
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

    protected OnGoingSummaryLT(Parcel in) {
        hhcLtApptInfoSrNo = in.readString();
        hhcLtRemarks = in.readString();
        hhcApptLtRefNo = in.readString();
        appntPerson = in.readString();
        appntPersonAge = in.readString();
        appntPersonSrNo = in.readString();
        apptLocation = in.readString();
        apptLocationIsMetro = in.readString();
        selectedPkgSrNo = in.readString();
        ltHours = in.readString();
        ltDurations = in.readString();
        ltNacount = in.readString();
        ltCategory = in.readString();
        apptStatus = in.readString();
        noOfDays = in.readString();
        noOfMonths = in.readString();
        noOfWeeks = in.readString();
        dateCondition = in.readString();
        datePreference = in.readString();
        fromDate = in.readString();
        toDate = in.readString();
        pkgPrice = in.readString();
        totalPrice = in.readString();
        scheduledOn = in.readString();
    }

    public static final Creator<OnGoingSummaryLT> CREATOR = new Creator<OnGoingSummaryLT>() {
        @Override
        public OnGoingSummaryLT createFromParcel(Parcel in) {
            return new OnGoingSummaryLT(in);
        }

        @Override
        public OnGoingSummaryLT[] newArray(int size) {
            return new OnGoingSummaryLT[size];
        }
    };

    public String getHhcLtApptInfoSrNo() {
        return hhcLtApptInfoSrNo;
    }

    public void setHhcLtApptInfoSrNo(String hhcLtApptInfoSrNo) {
        this.hhcLtApptInfoSrNo = hhcLtApptInfoSrNo;
    }

    public String getHhcLtRemarks() {
        return hhcLtRemarks;
    }

    public void setHhcLtRemarks(String hhcLtRemarks) {
        this.hhcLtRemarks = hhcLtRemarks;
    }

    public String getHhcApptLtRefNo() {
        return hhcApptLtRefNo;
    }

    public void setHhcApptLtRefNo(String hhcApptLtRefNo) {
        this.hhcApptLtRefNo = hhcApptLtRefNo;
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

    public String getLtHours() {
        return ltHours;
    }

    public void setLtHours(String ltHours) {
        this.ltHours = ltHours;
    }

    public String getLtDurations() {
        return ltDurations;
    }

    public void setLtDurations(String ltDurations) {
        this.ltDurations = ltDurations;
    }

    public String getLtNacount() {
        return ltNacount;
    }

    public void setLtNacount(String ltNacount) {
        this.ltNacount = ltNacount;
    }

    public String getLtCategory() {
        return ltCategory;
    }

    public void setLtCategory(String ltCategory) {
        this.ltCategory = ltCategory;
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

    public String getNoOfMonths() {
        return noOfMonths;
    }

    public void setNoOfMonths(String noOfMonths) {
        this.noOfMonths = noOfMonths;
    }

    public String getNoOfWeeks() {
        return noOfWeeks;
    }

    public void setNoOfWeeks(String noOfWeeks) {
        this.noOfWeeks = noOfWeeks;
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

    @Override
    public String toString() {
        return "OnGoingSummaryLT{" +
                "hhcLtApptInfoSrNo='" + hhcLtApptInfoSrNo + '\'' +
                ", hhcLtRemarks='" + hhcLtRemarks + '\'' +
                ", hhcApptLtRefNo='" + hhcApptLtRefNo + '\'' +
                ", appntPerson='" + appntPerson + '\'' +
                ", appntPersonAge='" + appntPersonAge + '\'' +
                ", appntPersonSrNo='" + appntPersonSrNo + '\'' +
                ", apptLocation='" + apptLocation + '\'' +
                ", apptLocationIsMetro='" + apptLocationIsMetro + '\'' +
                ", selectedPkgSrNo='" + selectedPkgSrNo + '\'' +
                ", ltHours='" + ltHours + '\'' +
                ", ltDurations='" + ltDurations + '\'' +
                ", ltNacount='" + ltNacount + '\'' +
                ", ltCategory='" + ltCategory + '\'' +
                ", apptStatus='" + apptStatus + '\'' +
                ", noOfDays='" + noOfDays + '\'' +
                ", noOfMonths='" + noOfMonths + '\'' +
                ", noOfWeeks='" + noOfWeeks + '\'' +
                ", dateCondition='" + dateCondition + '\'' +
                ", datePreference='" + datePreference + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", pkgPrice='" + pkgPrice + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", scheduledOn='" + scheduledOn + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(hhcLtApptInfoSrNo);
        parcel.writeString(hhcLtRemarks);
        parcel.writeString(hhcApptLtRefNo);
        parcel.writeString(appntPerson);
        parcel.writeString(appntPersonAge);
        parcel.writeString(appntPersonSrNo);
        parcel.writeString(apptLocation);
        parcel.writeString(apptLocationIsMetro);
        parcel.writeString(selectedPkgSrNo);
        parcel.writeString(ltHours);
        parcel.writeString(ltDurations);
        parcel.writeString(ltNacount);
        parcel.writeString(ltCategory);
        parcel.writeString(apptStatus);
        parcel.writeString(noOfDays);
        parcel.writeString(noOfMonths);
        parcel.writeString(noOfWeeks);
        parcel.writeString(dateCondition);
        parcel.writeString(datePreference);
        parcel.writeString(fromDate);
        parcel.writeString(toDate);
        parcel.writeString(pkgPrice);
        parcel.writeString(totalPrice);
        parcel.writeString(scheduledOn);
    }
}
