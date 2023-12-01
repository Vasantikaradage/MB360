package com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParentalRelationResponse {
    boolean loading = true;
    boolean error = true;


    @SerializedName("$id")
    @Expose
    private String $id;
    @SerializedName("AvailPaernts")
    @Expose
    private List<AvailPaernt> availPaernts;
    @SerializedName("EmpGender")
    @Expose
    private List<String> empGender;
    @SerializedName("EmpCanCover")
    @Expose
    private List<EmpCanCover> empCanCover;
    @SerializedName("IsCrossCombinationAllowed")
    @Expose
    private List<String> isCrossCombinationAllowed;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public List<AvailPaernt> getAvailPaernts() {
        return availPaernts;
    }

    public void setAvailPaernts(List<AvailPaernt> availPaernts) {
        this.availPaernts = availPaernts;
    }

    public List<String> getEmpGender() {
        return empGender;
    }

    public void setEmpGender(List<String> empGender) {
        this.empGender = empGender;
    }

    public List<EmpCanCover> getEmpCanCover() {
        return empCanCover;
    }

    public void setEmpCanCover(List<EmpCanCover> empCanCover) {
        this.empCanCover = empCanCover;
    }

    public List<String> getIsCrossCombinationAllowed() {
        return isCrossCombinationAllowed;
    }

    public void setIsCrossCombinationAllowed(List<String> isCrossCombinationAllowed) {
        this.isCrossCombinationAllowed = isCrossCombinationAllowed;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ParentalRelationResponse{" +
                "loading=" + loading +
                ", error=" + error +
                ", $id='" + $id + '\'' +
                ", availPaernts=" + availPaernts +
                ", empGender=" + empGender +
                ", empCanCover=" + empCanCover +
                ", isCrossCombinationAllowed=" + isCrossCombinationAllowed +
                '}';
    }
}
