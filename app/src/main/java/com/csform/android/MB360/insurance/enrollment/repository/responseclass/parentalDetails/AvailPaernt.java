package com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailPaernt {

    @SerializedName("RELATION_ID")
    @Expose
    private String relationId;
    @SerializedName("RELATION")
    @Expose
    private String relation;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "AvailPaernt{" +
                "relationId='" + relationId + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}
