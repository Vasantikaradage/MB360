package com.csform.android.MB360.insurance.policyfeatures.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.csform.android.MB360.database.converters.PolicyFeatureConverter.PolicyFeatureResponseConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PolicyFeaturesResponse {

    @SerializedName("POL_FEAT_DISPLAY_TYPE")
    @Expose
    private String polFeatDisplayType;
    @SerializedName("POL_FEAT_TYPE")
    @Expose
    private String polFeatType;
    @SerializedName("POL_INFORMATION")
    @Expose
    private String polInformation;
    @SerializedName("POL_TERMS_CONDITIONS")
    @Expose
    private String polTermsConditions;
    @SerializedName("POL_INFO_DISPLAY_TO")
    @Expose
    private String polInfoDisplayTo;


    public String getPolFeatDisplayType() {
        return polFeatDisplayType;
    }

    public void setPolFeatDisplayType(String polFeatDisplayType) {
        this.polFeatDisplayType = polFeatDisplayType;
    }

    public String getPolFeatType() {
        return polFeatType;
    }

    public void setPolFeatType(String polFeatType) {
        this.polFeatType = polFeatType;
    }

    public String getPolInformation() {
        return polInformation;
    }

    public void setPolInformation(String polInformation) {
        this.polInformation = polInformation;
    }

    public String getPolTermsConditions() {
        return polTermsConditions;
    }

    public void setPolTermsConditions(String polTermsConditions) {
        this.polTermsConditions = polTermsConditions;
    }

    public String getPolInfoDisplayTo() {
        return polInfoDisplayTo;
    }

    public void setPolInfoDisplayTo(String polInfoDisplayTo) {
        this.polInfoDisplayTo = polInfoDisplayTo;
    }

    @Override
    public String toString() {
        return "PolicyFeaturesResponse{" +
                "polFeatDisplayType='" + polFeatDisplayType + '\'' +
                ", polFeatType='" + polFeatType + '\'' +
                ", polInformation='" + polInformation + '\'' +
                ", polTermsConditions='" + polTermsConditions + '\'' +
                ", polInfoDisplayTo='" + polInfoDisplayTo + '\'' +
                '}';
    }
}
