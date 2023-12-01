package com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Si {
    @SerializedName("EMPLOYEE_CONTRI")
    @Expose
    private String employeeContri;
    @SerializedName("EMPLOYER_CONTRI")
    @Expose
    private String employerContri;
    @SerializedName("SUM_INSURED")
    @Expose
    private String sumInsured;
    @SerializedName("SUM_INSURED_WO_COMMA")
    @Expose
    private String sumInsuredWoComma;
    @SerializedName("SUM_INSURED_TO_SHOW")
    @Expose
    private Object sumInsuredToShow;
    @SerializedName("PREMIUM_VALUE")
    @Expose
    private String premiumValue;
    @SerializedName("PREMIUM_VALUE_TO_SHOW")
    @Expose
    private Object premiumValueToShow;
    @SerializedName("RELATIONSHIP_GRP_NAME")
    @Expose
    private Object relationshipGrpName;
    @SerializedName("SI_TYPE")
    @Expose
    private String siType;
    @SerializedName("PI_TYPE")
    @Expose
    private String piType;
    @SerializedName("Selected")
    @Expose
    private Boolean selected;

    public String getEmployeeContri() {
        return employeeContri;
    }

    public void setEmployeeContri(String employeeContri) {
        this.employeeContri = employeeContri;
    }

    public String getEmployerContri() {
        return employerContri;
    }

    public void setEmployerContri(String employerContri) {
        this.employerContri = employerContri;
    }

    public String getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(String sumInsured) {
        this.sumInsured = sumInsured;
    }

    public String getSumInsuredWoComma() {
        return sumInsuredWoComma;
    }

    public void setSumInsuredWoComma(String sumInsuredWoComma) {
        this.sumInsuredWoComma = sumInsuredWoComma;
    }

    public Object getSumInsuredToShow() {
        return sumInsuredToShow;
    }

    public void setSumInsuredToShow(Object sumInsuredToShow) {
        this.sumInsuredToShow = sumInsuredToShow;
    }

    public String getPremiumValue() {
        return premiumValue;
    }

    public void setPremiumValue(String premiumValue) {
        this.premiumValue = premiumValue;
    }

    public Object getPremiumValueToShow() {
        return premiumValueToShow;
    }

    public void setPremiumValueToShow(Object premiumValueToShow) {
        this.premiumValueToShow = premiumValueToShow;
    }

    public Object getRelationshipGrpName() {
        return relationshipGrpName;
    }

    public void setRelationshipGrpName(Object relationshipGrpName) {
        this.relationshipGrpName = relationshipGrpName;
    }

    public String getSiType() {
        return siType;
    }

    public void setSiType(String siType) {
        this.siType = siType;
    }

    public String getPiType() {
        return piType;
    }

    public void setPiType(String piType) {
        this.piType = piType;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Si{" +
                "employeeContri='" + employeeContri + '\'' +
                ", employerContri='" + employerContri + '\'' +
                ", sumInsured='" + sumInsured + '\'' +
                ", sumInsuredWoComma='" + sumInsuredWoComma + '\'' +
                ", sumInsuredToShow=" + sumInsuredToShow +
                ", premiumValue='" + premiumValue + '\'' +
                ", premiumValueToShow=" + premiumValueToShow +
                ", relationshipGrpName=" + relationshipGrpName +
                ", siType='" + siType + '\'' +
                ", piType='" + piType + '\'' +
                ", selected=" + selected +
                '}';
    }
}

