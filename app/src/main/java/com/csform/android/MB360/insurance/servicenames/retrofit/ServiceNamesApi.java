package com.csform.android.MB360.insurance.servicenames.retrofit;

import com.csform.android.MB360.insurance.servicenames.responseclass.ServiceNamesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceNamesApi {
    @GET("Login/ShowHideButtonsforInsurance")
    Call<ServiceNamesResponse> getServiceName(
            @Query("strGroupChildSrno") String groupChildSrno
    );
}
