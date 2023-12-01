package com.csform.android.MB360.fitness.ProfileScreenData;

import com.csform.android.MB360.wellness.homehealthcare.cancellation.responseclass.ApiStatus;
import com.google.gson.annotations.SerializedName;

public class SemanticResponse {
    @SerializedName("message")
    private ApiStatus apiStatus;

    public ApiStatus getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(ApiStatus apiStatus) {
        this.apiStatus = apiStatus;
    }
}
