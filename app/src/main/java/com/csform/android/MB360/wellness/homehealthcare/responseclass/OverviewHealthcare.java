
package com.csform.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverviewHealthcare {

    @SerializedName("Overview")
    @Expose
    private String overview;

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public String toString() {
        return "OverviewHealthcare{" +
                "overview='" + overview + '\'' +
                '}';
    }
}
