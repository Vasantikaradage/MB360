package com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails;

import java.util.ArrayList;
import java.util.List;

public class DependantMainResponse {

    boolean loading = true;
    boolean error = true;
    List<Dependant> dependants = new ArrayList<>();

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

    public List<Dependant> getDependants() {
        return dependants;
    }

    public void setDependants(List<Dependant> dependants) {
        this.dependants = dependants;
    }
}
