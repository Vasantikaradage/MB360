package com.csform.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MaxMinAgeResponse {

    @SerializedName("RELATION_ID")
    @Expose
    private Integer relationId;
    @SerializedName("MAX_AGE")
    @Expose
    private Integer maxAge;
    @SerializedName("MIN_AGE")
    @Expose
    private Integer minAge;
    @SerializedName("RELATION_NAME")
    @Expose
    private String relationName;

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    @Override
    public String toString() {
        return "MaxMinAgeResponse{" +
                "relationId=" + relationId +
                ", maxAge=" + maxAge +
                ", minAge=" + minAge +
                ", relationName='" + relationName + '\'' +
                '}';
    }
}
