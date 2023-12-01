
package com.csform.android.MB360.wellness.healthcheckuppackages.repository.requestclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleRequest {

    @SerializedName("DiagcenterNo")
    @Expose
    private String diagcenterNo;
    @SerializedName("EmployeeIdentificationNo")
    @Expose
    private String employeeIdentificationNo;

    @SerializedName("FamilySrNo")
    @Expose
    private String familySrNo;

    @SerializedName("GroupCode")
    @Expose
    private String groupCode;
    @SerializedName("GroupName")
    @Expose
    private String groupName;
    @SerializedName("GroupSrNo")
    @Expose
    private Integer groupSrNo;
    @SerializedName("ISRescheduled")
    @Expose
    private String iSRescheduled;
    @SerializedName("IsPersonOrFamily")
    @Expose
    private String isPersonOrFamily;
    @SerializedName("OldApptInfoSrNo")
    @Expose
    private String oldApptInfoSrNo;
    @SerializedName("PackageSrNo")
    @Expose
    private String packageSrNo;
    @SerializedName("PaidOrNot")
    @Expose
    private String paidOrNot;

    @SerializedName("PersonSrNo")
    @Expose
    private String personSrNo;
    @SerializedName("PrefDate1")
    @Expose
    private String prefDate1;
    @SerializedName("PrefDate2")
    @Expose
    private String prefDate2;
    @SerializedName("PrefDate3")
    @Expose
    private String prefDate3;
    @SerializedName("PrefTime1")
    @Expose
    private String prefTime1;
    @SerializedName("PrefTime2")
    @Expose
    private String prefTime2;
    @SerializedName("PrefTime3")
    @Expose
    private String prefTime3;

    @SerializedName("RejtApptSrNo")
    @Expose
    private String rejtApptSrNo;

    public String getDiagcenterNo() {
        return diagcenterNo;
    }

    public void setDiagcenterNo(String diagcenterNo) {
        this.diagcenterNo = diagcenterNo;
    }

    public String getEmployeeIdentificationNo() {
        return employeeIdentificationNo;
    }

    public void setEmployeeIdentificationNo(String employeeIdentificationNo) {
        this.employeeIdentificationNo = employeeIdentificationNo;
    }

    public String getFamilySrNo() {
        return familySrNo;
    }

    public void setFamilySrNo(String familySrNo) {
        this.familySrNo = familySrNo;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupSrNo() {
        return groupSrNo;
    }

    public void setGroupSrNo(Integer groupSrNo) {
        this.groupSrNo = groupSrNo;
    }

    public String getiSRescheduled() {
        return iSRescheduled;
    }

    public void setiSRescheduled(String iSRescheduled) {
        this.iSRescheduled = iSRescheduled;
    }

    public String getIsPersonOrFamily() {
        return isPersonOrFamily;
    }

    public void setIsPersonOrFamily(String isPersonOrFamily) {
        this.isPersonOrFamily = isPersonOrFamily;
    }

    public String getOldApptInfoSrNo() {
        return oldApptInfoSrNo;
    }

    public void setOldApptInfoSrNo(String oldApptInfoSrNo) {
        this.oldApptInfoSrNo = oldApptInfoSrNo;
    }

    public String getPackageSrNo() {
        return packageSrNo;
    }

    public void setPackageSrNo(String packageSrNo) {
        this.packageSrNo = packageSrNo;
    }

    public String getPaidOrNot() {
        return paidOrNot;
    }

    public void setPaidOrNot(String paidOrNot) {
        this.paidOrNot = paidOrNot;
    }

    public String getPersonSrNo() {
        return personSrNo;
    }

    public void setPersonSrNo(String personSrNo) {
        this.personSrNo = personSrNo;
    }

    public String getPrefDate1() {
        return prefDate1;
    }

    public void setPrefDate1(String prefDate1) {
        this.prefDate1 = prefDate1;
    }

    public String getPrefDate2() {
        return prefDate2;
    }

    public void setPrefDate2(String prefDate2) {
        this.prefDate2 = prefDate2;
    }

    public String getPrefDate3() {
        return prefDate3;
    }

    public void setPrefDate3(String prefDate3) {
        this.prefDate3 = prefDate3;
    }

    public String getPrefTime1() {
        return prefTime1;
    }

    public void setPrefTime1(String prefTime1) {
        this.prefTime1 = prefTime1;
    }

    public String getPrefTime2() {
        return prefTime2;
    }

    public void setPrefTime2(String prefTime2) {
        this.prefTime2 = prefTime2;
    }

    public String getPrefTime3() {
        return prefTime3;
    }

    public void setPrefTime3(String prefTime3) {
        this.prefTime3 = prefTime3;
    }

    public String getRejtApptSrNo() {
        return rejtApptSrNo;
    }

    public void setRejtApptSrNo(String rejtApptSrNo) {
        this.rejtApptSrNo = rejtApptSrNo;
    }

    @Override
    public String toString() {
        return "ScheduleRequest{" +
                "diagcenterNo='" + diagcenterNo + '\'' +
                ", employeeIdentificationNo='" + employeeIdentificationNo + '\'' +
                ", familySrNo='" + familySrNo + '\'' +
                ", groupCode='" + groupCode + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupSrNo=" + groupSrNo +
                ", iSRescheduled='" + iSRescheduled + '\'' +
                ", isPersonOrFamily='" + isPersonOrFamily + '\'' +
                ", oldApptInfoSrNo='" + oldApptInfoSrNo + '\'' +
                ", packageSrNo='" + packageSrNo + '\'' +
                ", paidOrNot='" + paidOrNot + '\'' +
                ", personSrNo='" + personSrNo + '\'' +
                ", prefDate1='" + prefDate1 + '\'' +
                ", prefDate2='" + prefDate2 + '\'' +
                ", prefDate3='" + prefDate3 + '\'' +
                ", prefTime1='" + prefTime1 + '\'' +
                ", prefTime2='" + prefTime2 + '\'' +
                ", prefTime3='" + prefTime3 + '\'' +
                ", rejtApptSrNo='" + rejtApptSrNo + '\'' +
                '}';
    }
}