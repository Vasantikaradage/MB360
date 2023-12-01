package com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateDependantRequest {
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Relation")
    @Expose
    private String relation;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("RelationID")
    @Expose
    private String relationID;
    @SerializedName("Age")
    @Expose
    private String age;
    @SerializedName("DateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("PersonSrNo")
    @Expose
    private String personSrNo;
    @SerializedName("EmployeeSrNo")
    @Expose
    private String employeeSrNo;
    @SerializedName("IsSelected")
    @Expose
    private String isSelected;
    @SerializedName("IsApplicable")
    @Expose
    private String isApplicable;
    @SerializedName("CanDelete")
    @Expose
    private String canDelete;
    @SerializedName("ReasonForNotAbleToDelete")
    @Expose
    private String reasonForNotAbleToDelete;
    @SerializedName("CanUpdate")
    @Expose
    private String canUpdate;
    @SerializedName("IsTwins")
    @Expose
    private String isTwins;
    @SerializedName("ReasonForNotAbleToUpdate")
    @Expose
    private String reasonForNotAbleToUpdate;
    @SerializedName("IsDisabled")
    @Expose
    private String isDisabled;
    @SerializedName("PairNo")
    @Expose
    private String pairNo;
    @SerializedName("ExtraChildPremium")
    @Expose
    private String extraChildPremium;
    @SerializedName("IsParentOpted")
    @Expose
    private String isParentOpted;
    @SerializedName("Premium")
    @Expose
    private String premium;
    @SerializedName("SumInsured")
    @Expose
    private String sumInsured;
    @SerializedName("AadharNo")
    @Expose
    private String aadharNo;
    @SerializedName("GroupChildSrNo")
    @Expose
    private String groupChildSrNo;
    @SerializedName("ParentalPremiuimSeparate")
    @Expose
    private String parentalPremiuimSeparate;
    @SerializedName("BaseSI")
    @Expose
    private String baseSI;
    @SerializedName("OeGrpBasInfSrNo")
    @Expose
    private String oeGrpBasInfSrNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationID() {
        return relationID;
    }

    public void setRelationID(String relationID) {
        this.relationID = relationID;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPersonSrNo() {
        return personSrNo;
    }

    public void setPersonSrNo(String personSrNo) {
        this.personSrNo = personSrNo;
    }

    public String getEmployeeSrNo() {
        return employeeSrNo;
    }

    public void setEmployeeSrNo(String employeeSrNo) {
        this.employeeSrNo = employeeSrNo;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getIsApplicable() {
        return isApplicable;
    }

    public void setIsApplicable(String isApplicable) {
        this.isApplicable = isApplicable;
    }

    public String getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(String canDelete) {
        this.canDelete = canDelete;
    }

    public String getReasonForNotAbleToDelete() {
        return reasonForNotAbleToDelete;
    }

    public void setReasonForNotAbleToDelete(String reasonForNotAbleToDelete) {
        this.reasonForNotAbleToDelete = reasonForNotAbleToDelete;
    }

    public String getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(String canUpdate) {
        this.canUpdate = canUpdate;
    }

    public String getIsTwins() {
        return isTwins;
    }

    public void setIsTwins(String isTwins) {
        this.isTwins = isTwins;
    }

    public String getReasonForNotAbleToUpdate() {
        return reasonForNotAbleToUpdate;
    }

    public void setReasonForNotAbleToUpdate(String reasonForNotAbleToUpdate) {
        this.reasonForNotAbleToUpdate = reasonForNotAbleToUpdate;
    }

    public String getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(String isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getPairNo() {
        return pairNo;
    }

    public void setPairNo(String pairNo) {
        this.pairNo = pairNo;
    }

    public String getExtraChildPremium() {
        return extraChildPremium;
    }

    public void setExtraChildPremium(String extraChildPremium) {
        this.extraChildPremium = extraChildPremium;
    }

    public String getIsParentOpted() {
        return isParentOpted;
    }

    public void setIsParentOpted(String isParentOpted) {
        this.isParentOpted = isParentOpted;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(String sumInsured) {
        this.sumInsured = sumInsured;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getGroupChildSrNo() {
        return groupChildSrNo;
    }

    public void setGroupChildSrNo(String groupChildSrNo) {
        this.groupChildSrNo = groupChildSrNo;
    }

    public String getParentalPremiuimSeparate() {
        return parentalPremiuimSeparate;
    }

    public void setParentalPremiuimSeparate(String parentalPremiuimSeparate) {
        this.parentalPremiuimSeparate = parentalPremiuimSeparate;
    }

    public String getBaseSI() {
        return baseSI;
    }

    public void setBaseSI(String baseSI) {
        this.baseSI = baseSI;
    }

    public String getOeGrpBasInfSrNo() {
        return oeGrpBasInfSrNo;
    }

    public void setOeGrpBasInfSrNo(String oeGrpBasInfSrNo) {
        this.oeGrpBasInfSrNo = oeGrpBasInfSrNo;
    }

}
