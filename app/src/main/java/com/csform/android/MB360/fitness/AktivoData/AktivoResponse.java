package com.csform.android.MB360.fitness.AktivoData;

import com.google.gson.annotations.SerializedName;

public class AktivoResponse {

    @SerializedName("access_token")
    private String strAccessToken;

    @SerializedName("token_type")
    private String strTokenType;

    @SerializedName("expires_in")
    private int strExpiry;

    public String getStrAccessToken() {
        return strAccessToken;
    }

    public void setStrAccessToken(String strAccessToken) {
        this.strAccessToken = strAccessToken;
    }

    public String getStrTokenType() {
        return strTokenType;
    }

    public void setStrTokenType(String strTokenType) {
        this.strTokenType = strTokenType;
    }

    public int getStrExpiry() {
        return strExpiry;
    }

    public void setStrExpiry(int strExpiry) {
        this.strExpiry = strExpiry;
    }
}
