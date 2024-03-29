package com.csform.android.MB360.insurance.claims.repository.retrofit;


import com.csform.android.MB360.insurance.claims.repository.requestclass.NewIntimateRequest;
import com.csform.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;
import com.csform.android.MB360.insurance.claims.repository.responseclass.LoadPersonsIntimationResponse;
import com.csform.android.MB360.insurance.claims.repository.responseclass.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClaimsApi {
    @GET("IntimateClaim/LoadIntimatedClaims")
    Call<ClaimsResponse> getClaims(@Query("EmployeeSrNo") String employeeSrNo,
                                   @Query("GroupChildSrNo") String grpChildSrNo,
                                   @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo);

 /*   @GET("IntimateClaim/LoadPersonsForIntimation")
    Call<LoadPersonsIntimationResponse> getPersons(@Query("EmployeeSrNo") String employeeSrNo,
                                                   @Query("GroupChildSrNo") String grpChildSrNo,
                                                   @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo);
*/

    @GET("IntimateClaim/LoadPersonsForIntimation")
    Call<LoadPersonsIntimationResponse> getPersons(@Query("employeeSrNo") String employeeSrNo,
                                                   @Query("groupChildSrNo") String grpChildSrNo,
                                                   @Query("oegrpBasInfSrNo") String oeGrpBasInfSrNo);

    @POST("IntimateClaim/NewIntimateClaim")
    Call<Result> startIntimation(@Body NewIntimateRequest newIntimateRequest);

}
