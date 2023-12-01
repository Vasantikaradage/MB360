package com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmpCanCover {

    @SerializedName("$id")
    @Expose
    private String $id;
    @SerializedName("CAN_FEML_COVR_PARENTS")
    @Expose
    private Integer canFemlCovrParents;
    @SerializedName("CAN_ML_COVR_PARENTS")
    @Expose
    private Integer canMlCovrParents;
    @SerializedName("CAN_FEML_COVR_INLAWS")
    @Expose
    private Integer canFemlCovrInlaws;
    @SerializedName("CAN_ML_COVR_INLAWS")
    @Expose
    private Integer canMlCovrInlaws;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public Integer getCanFemlCovrParents() {
        return canFemlCovrParents;
    }

    public void setCanFemlCovrParents(Integer canFemlCovrParents) {
        this.canFemlCovrParents = canFemlCovrParents;
    }

    public Integer getCanMlCovrParents() {
        return canMlCovrParents;
    }

    public void setCanMlCovrParents(Integer canMlCovrParents) {
        this.canMlCovrParents = canMlCovrParents;
    }

    public Integer getCanFemlCovrInlaws() {
        return canFemlCovrInlaws;
    }

    public void setCanFemlCovrInlaws(Integer canFemlCovrInlaws) {
        this.canFemlCovrInlaws = canFemlCovrInlaws;
    }

    public Integer getCanMlCovrInlaws() {
        return canMlCovrInlaws;
    }

    public void setCanMlCovrInlaws(Integer canMlCovrInlaws) {
        this.canMlCovrInlaws = canMlCovrInlaws;
    }

    @Override
    public String toString() {
        return "EmpCanCover{" +
                "$id='" + $id + '\'' +
                ", canFemlCovrParents=" + canFemlCovrParents +
                ", canMlCovrParents=" + canMlCovrParents +
                ", canFemlCovrInlaws=" + canFemlCovrInlaws +
                ", canMlCovrInlaws=" + canMlCovrInlaws +
                '}';
    }
}
