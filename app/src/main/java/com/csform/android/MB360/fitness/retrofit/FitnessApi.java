package com.csform.android.MB360.fitness.retrofit;

import com.csform.android.MB360.fitness.AktivoData.AktivoMBProfile;
import com.csform.android.MB360.fitness.AktivoData.AktivoResponse;
import com.csform.android.MB360.fitness.ProfileScreenData.AktivoBeanProfile;
import com.csform.android.MB360.fitness.ProfileScreenData.OnBoardAktivoBeans;
import com.csform.android.MB360.fitness.ProfileScreenData.OnBoardRequest;
import com.csform.android.MB360.fitness.ProfileScreenData.SemanticResponse;
import com.csform.android.MB360.fitness.ProfileScreenData.UploadProfileBeans;
import com.csform.android.MB360.fitness.request.FitnessEmpGroupInfoRequest;
import com.csform.android.MB360.fitness.response.AktivoChallengeResponse;
import com.csform.android.MB360.fitness.response.FitnessEmpGroupInfoResponse;
import com.csform.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FitnessApi {

    @POST("UserProfile/UploadFitnessEmployeegroupInfo")
    Call<FitnessEmpGroupInfoResponse> getFotnessEmpGroupInfo(@Body FitnessEmpGroupInfoRequest fitnessEmpGroupInfoRequest);

    //Aktivo Api
    @FormUrlEncoded
    @POST("oauth/token")
    Single<AktivoResponse> getAccessToken(
            @Field("Type") String strType,
            @Field("client_id") String strClientId,
            @Field("client_secret") String strClientSecret,
            @Field("grant_type") String strGrantType);

    @FormUrlEncoded
    @POST("oauth/token")
    Single<AktivoResponse> getAccessToken(
            @Field("Type") String strType,
            @Field("client_id") String strClientId,
            @Field("client_secret") String strClientSecret,
            @Field("grant_type") String strGrantType,
            @Field("scope") String scoped);

    @GET("api/users")
    Single<AktivoBeanProfile> getMemberID(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("email") String email);

    @GET("api/users/{memberId}")
    Single<AktivoBeanProfile> getMember_ID(
            @Path("memberId") String memberId,
            @Query("include") String leaders,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate);

    /*UserProfile/GetActivoEmployeeInfo?strEmpSrno=36471*/
    @GET("UserProfile/GetActivoEmployeeInfo")
    Single<AktivoMBProfile> checkOnBoardEmp(@Query("strEmpSrno") String strEmpSrno);


    @GET("api/users/{memberId}")
    Call<AktivoBeanProfile> getAktivoChallenge(
            @Header("Authorization") String token,
            @Path("memberId") String memberId,
            @Query("include") String leaders,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate);

    @POST("UserProfile/UploadEmployeePhysicalInfo")
    Single<SemanticResponse> uploadInfo(@Body UploadProfileBeans uploadResponse);

    @POST("api/system/onboard")
    Single<OnBoardAktivoBeans> setOnBoardNewEmp(@Body OnBoardRequest jsonObject);

   /* *//*Login/ShowHideButtonsforInsurance?strGroupChildSrno=1275*//*
    @GET("Login/ShowHideButtonsforInsurance")
    Single<DashboardResponse> checkDashboardLinks(@Query("strGroupChildSrno") String strGrpChldSrNo);*/


}
