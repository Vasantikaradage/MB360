package com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relation {

    @SerializedName("RELATION_ID")
    @Expose
    private String relationId;
    @SerializedName("RELATION")
    @Expose
    private String relation;


    public String getRelationID() {
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
        return "Relation{" +
                "relationId='" + relationId + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}
