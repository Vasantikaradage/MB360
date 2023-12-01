package com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails;

import java.util.ArrayList;
import java.util.List;

public class ParentDetailMainResponse {

    boolean loading = true;
    boolean error = false;

    List<Parent> relations = new ArrayList<>();


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

    public List<Parent> getRelations() {
        return relations;
    }

    public void setRelations(List<Parent> relations) {
        this.relations = relations;
    }

    @Override
    public String toString() {
        return "ParentDetailMAinResponse{" +
                "loading=" + loading +
                ", error=" + error +
                ", relations=" + relations +
                '}';
    }
}
