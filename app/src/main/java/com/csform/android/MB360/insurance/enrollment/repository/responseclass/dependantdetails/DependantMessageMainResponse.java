package com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails;

public class DependantMessageMainResponse {
    boolean loading = true;
    boolean error = true;
    private DependantMessageResponse addDependantResponse;

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

    public DependantMessageResponse getAddDependantResponse() {
        return addDependantResponse;
    }

    public void setAddDependantResponse(DependantMessageResponse addDependantResponse) {
        this.addDependantResponse = addDependantResponse;
    }

    @Override
    public String toString() {
        return "AddDependantMainResponse{" +
                "loading=" + loading +
                ", error=" + error +
                ", addDependantResponse=" + addDependantResponse +
                '}';
    }
}
