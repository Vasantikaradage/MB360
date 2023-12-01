package com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails;

import java.util.ArrayList;
import java.util.List;

public class RelationMainResponse {

    boolean loading = true;
    boolean error = false;

    List<Relation> relations = new ArrayList<>();


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

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }
}
