package com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeFieldDisplaySubResponse {
    @SerializedName("$id")
    @Expose
    private String $id;
    @SerializedName("PI_OE_GRP_FIELD_SR_NO")
    @Expose
    private Integer piOeGrpFieldSrNo;
    @SerializedName("PI_FIELD_ORDER_NO")
    @Expose
    private Integer piFieldOrderNo;
    @SerializedName("PI_FIELD_NAME")
    @Expose
    private String piFieldName;
    @SerializedName("PI_IS_MANDATORY")
    @Expose
    private Integer piIsMandatory;
    @SerializedName("PI_CAN_DISPLAY")
    @Expose
    private Integer piCanDisplay;
    @SerializedName("PI_CAN_EDIT")
    @Expose
    private Integer piCanEdit;

    private  boolean editState=false;


    public boolean isEditState() {
        return editState;
    }

    public void setEditState(boolean editState) {
        this.editState = editState;
    }


    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public Integer getPiOeGrpFieldSrNo() {
        return piOeGrpFieldSrNo;
    }

    public void setPiOeGrpFieldSrNo(Integer piOeGrpFieldSrNo) {
        this.piOeGrpFieldSrNo = piOeGrpFieldSrNo;
    }

    public Integer getPiFieldOrderNo() {
        return piFieldOrderNo;
    }

    public void setPiFieldOrderNo(Integer piFieldOrderNo) {
        this.piFieldOrderNo = piFieldOrderNo;
    }

    public String getPiFieldName() {
        return piFieldName;
    }

    public void setPiFieldName(String piFieldName) {
        this.piFieldName = piFieldName;
    }

    public Integer getPiIsMandatory() {
        return piIsMandatory;
    }

    public void setPiIsMandatory(Integer piIsMandatory) {
        this.piIsMandatory = piIsMandatory;
    }

    public Integer getPiCanDisplay() {
        return piCanDisplay;
    }

    public void setPiCanDisplay(Integer piCanDisplay) {
        this.piCanDisplay = piCanDisplay;
    }

    public Integer getPiCanEdit() {
        return piCanEdit;
    }

    public void setPiCanEdit(Integer piCanEdit) {
        this.piCanEdit = piCanEdit;
    }

    @Override
    public String toString() {
        return "EmployeeFieldDisplayResponse{" +
                "$id='" + $id + '\'' +
                ", piOeGrpFieldSrNo=" + piOeGrpFieldSrNo +
                ", piFieldOrderNo=" + piFieldOrderNo +
                ", piFieldName='" + piFieldName + '\'' +
                ", piIsMandatory=" + piIsMandatory +
                ", piCanDisplay=" + piCanDisplay +
                ", piCanEdit=" + piCanEdit +
                '}';
    }

}
