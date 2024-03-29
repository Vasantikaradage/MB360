package com.csform.android.MB360.insurance.myclaims.retrofit;

import com.csform.android.MB360.insurance.myclaims.responseclass.DocumentElement;

import com.csform.android.MB360.insurance.myclaims.responseclassV1.ClaimsResponse;
import com.csform.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimsDetails;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyClaimsApi {

    @POST("LoadEmployeeClaimsValues")
    Call<DocumentElement> getClaims(@Body RequestBody dataRequest);


    @POST("LoadDetailedClaimsValues")
    Call<ClaimsDetails> getClaimDetail(@Body RequestBody dataRequest);

    @GET("EnrollmentDetails/LoadEmployeeClaimsValues")
    Call<ClaimsResponse> getEmployeeClaims(@Query("groupchildsrno") String groupChildSrNo,
                                           @Query("employeesrno") String employeeSrNo);

    @GET("EnrollmentDetails/LoadDetailedClaimsValues")
    Call<ClaimsDetails> getEmployeeClaimsDetails(@Query("groupchildsrno") String groupChildSrNo,
                                                 @Query("oegrpbasinfsrno") String oegrpbasinfsrno,
                                                 @Query("claimsrno") String claimsrno);


}
