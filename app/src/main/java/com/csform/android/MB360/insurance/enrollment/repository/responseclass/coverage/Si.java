package com.csform.android.MB360.insurance.enrollment.repository.responseclass.coverage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Si {
    @SerializedName("EMPLOYEE_CONTRI")
    @Expose
    private Object employeeContri;
    @SerializedName("EMPLOYER_CONTRI")
    @Expose
    private Object employerContri;
    @SerializedName("SUM_INSURED")
    @Expose
    private String sumInsured;
    @SerializedName("SUM_INSURED_WO_COMMA")
    @Expose
    private String sumInsuredWoComma;
    @SerializedName("SUM_INSURED_TO_SHOW")
    @Expose
    private String sumInsuredToShow;
    @SerializedName("PREMIUM_VALUE")
    @Expose
    private Object premiumValue;
    @SerializedName("PREMIUM_VALUE_TO_SHOW")
    @Expose
    private Object premiumValueToShow;
    @SerializedName("RELATIONSHIP_GRP_NAME")
    @Expose
    private Object relationshipGrpName;
    @SerializedName("SI_TYPE")
    @Expose
    private Object siType;
    @SerializedName("PI_TYPE")
    @Expose
    private Object piType;
    @SerializedName("Selected")
    @Expose
    private Boolean selected;

    private  String select_policy;

    public String getSelect_policy() {
        return select_policy;
    }

    public String setSelect_policy(String select_policy) {
        this.select_policy = select_policy;

        return select_policy;
    }

    public Object getEmployeeContri() {
        return employeeContri;
    }

    public void setEmployeeContri(Object employeeContri) {
        this.employeeContri = employeeContri;
    }

    public Object getEmployerContri() {
        return employerContri;
    }

    public void setEmployerContri(Object employerContri) {
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

    public String getSumInsuredToShow() {
        return sumInsuredToShow;
    }

    public void setSumInsuredToShow(String sumInsuredToShow) {
        this.sumInsuredToShow = sumInsuredToShow;
    }

    public Object getPremiumValue() {
        return premiumValue;
    }

    public void setPremiumValue(Object premiumValue) {
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

    public Object getSiType() {
        return siType;
    }

    public void setSiType(Object siType) {
        this.siType = siType;
    }

    public Object getPiType() {
        return piType;
    }

    public void setPiType(Object piType) {
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
                "employeeContri=" + employeeContri +
                ", employerContri=" + employerContri +
                ", sumInsured='" + sumInsured + '\'' +
                ", sumInsuredWoComma='" + sumInsuredWoComma + '\'' +
                ", sumInsuredToShow='" + sumInsuredToShow + '\'' +
                ", premiumValue=" + premiumValue +
                ", premiumValueToShow=" + premiumValueToShow +
                ", relationshipGrpName=" + relationshipGrpName +
                ", siType=" + siType +
                ", piType=" + piType +
                ", selected=" + selected +
                ", select_policy='" + select_policy + '\'' +
                '}';
    }
}
