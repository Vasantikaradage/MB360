package com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Dependant {

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
    private Object employeeSrNo;
    @SerializedName("IsSelected")
    @Expose
    private Boolean isSelected;
    @SerializedName("IsApplicable")
    @Expose
    private Boolean isApplicable;
    @SerializedName("CanDelete")
    @Expose
    private Boolean canDelete;
    @SerializedName("ReasonForNotAbleToDelete")
    @Expose
    private Object reasonForNotAbleToDelete;
    @SerializedName("CanUpdate")
    @Expose
    private Boolean canUpdate;
    @SerializedName("IsTwins")
    @Expose
    private String isTwins;
    @SerializedName("ReasonForNotAbleToUpdate")
    @Expose
    private Object reasonForNotAbleToUpdate;
    @SerializedName("IsDisabled")
    @Expose
    private Boolean isDisabled;
    @SerializedName("PairNo")
    @Expose
    private Object pairNo;
    @SerializedName("ExtraChildPremium")
    @Expose
    private Object extraChildPremium;
    @SerializedName("IsParentOpted")
    @Expose
    private String isParentOpted;
    @SerializedName("Premium")
    @Expose
    private Object premium;
    @SerializedName("SumInsured")
    @Expose
    private String sumInsured;
    @SerializedName("AadharNo")
    @Expose
    private String aadharNo;
    @SerializedName("GroupChildSrNo")
    @Expose
    private Object groupChildSrNo;
    @SerializedName("ParentalPremiuimSeparate")
    @Expose
    private Integer parentalPremiuimSeparate;
    @SerializedName("BaseSI")
    @Expose
    private Integer baseSI;
    @SerializedName("OeGrpBasInfSrNo")
    @Expose
    private Object oeGrpBasInfSrNo;

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

    public Object getEmployeeSrNo() {
        return employeeSrNo;
    }

    public void setEmployeeSrNo(Object employeeSrNo) {
        this.employeeSrNo = employeeSrNo;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Boolean getIsApplicable() {
        return isApplicable;
    }

    public void setIsApplicable(Boolean isApplicable) {
        this.isApplicable = isApplicable;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Object getReasonForNotAbleToDelete() {
        return reasonForNotAbleToDelete;
    }

    public void setReasonForNotAbleToDelete(Object reasonForNotAbleToDelete) {
        this.reasonForNotAbleToDelete = reasonForNotAbleToDelete;
    }

    public Boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public String getIsTwins() {
        return isTwins;
    }

    public void setIsTwins(String isTwins) {
        this.isTwins = isTwins;
    }

    public Object getReasonForNotAbleToUpdate() {
        return reasonForNotAbleToUpdate;
    }

    public void setReasonForNotAbleToUpdate(Object reasonForNotAbleToUpdate) {
        this.reasonForNotAbleToUpdate = reasonForNotAbleToUpdate;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Object getPairNo() {
        return pairNo;
    }

    public void setPairNo(Object pairNo) {
        this.pairNo = pairNo;
    }

    public Object getExtraChildPremium() {
        return extraChildPremium;
    }

    public void setExtraChildPremium(Object extraChildPremium) {
        this.extraChildPremium = extraChildPremium;
    }

    public String getIsParentOpted() {
        return isParentOpted;
    }

    public void setIsParentOpted(String isParentOpted) {
        this.isParentOpted = isParentOpted;
    }

    public Object getPremium() {
        return premium;
    }

    public void setPremium(Object premium) {
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

    public Object getGroupChildSrNo() {
        return groupChildSrNo;
    }

    public void setGroupChildSrNo(Object groupChildSrNo) {
        this.groupChildSrNo = groupChildSrNo;
    }

    public Integer getParentalPremiuimSeparate() {
        return parentalPremiuimSeparate;
    }

    public void setParentalPremiuimSeparate(Integer parentalPremiuimSeparate) {
        this.parentalPremiuimSeparate = parentalPremiuimSeparate;
    }

    public Integer getBaseSI() {
        return baseSI;
    }

    public void setBaseSI(Integer baseSI) {
        this.baseSI = baseSI;
    }

    public Object getOeGrpBasInfSrNo() {
        return oeGrpBasInfSrNo;
    }

    public void setOeGrpBasInfSrNo(Object oeGrpBasInfSrNo) {
        this.oeGrpBasInfSrNo = oeGrpBasInfSrNo;
    }
    @Override
    public String toString() {
        return "Dependant{" +
                "name='" + name + '\'' +
                ", relation='" + relation + '\'' +
                ", gender='" + gender + '\'' +
                ", relationID='" + relationID + '\'' +
                ", age='" + age + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", personSrNo='" + personSrNo + '\'' +
                ", employeeSrNo=" + employeeSrNo +
                ", isSelected=" + isSelected +
                ", isApplicable=" + isApplicable +
                ", canDelete=" + canDelete +
                ", reasonForNotAbleToDelete=" + reasonForNotAbleToDelete +
                ", canUpdate=" + canUpdate +
                ", isTwins='" + isTwins + '\'' +
                ", reasonForNotAbleToUpdate=" + reasonForNotAbleToUpdate +
                ", isDisabled=" + isDisabled +
                ", pairNo=" + pairNo +
                ", extraChildPremium=" + extraChildPremium +
                ", isParentOpted='" + isParentOpted + '\'' +
                ", premium=" + premium +
                ", sumInsured='" + sumInsured + '\'' +
                ", aadharNo='" + aadharNo + '\'' +
                ", groupChildSrNo=" + groupChildSrNo +
                ", parentalPremiuimSeparate=" + parentalPremiuimSeparate +
                ", baseSI=" + baseSI +
                ", oeGrpBasInfSrNo=" + oeGrpBasInfSrNo +
                '}';
    }
}
