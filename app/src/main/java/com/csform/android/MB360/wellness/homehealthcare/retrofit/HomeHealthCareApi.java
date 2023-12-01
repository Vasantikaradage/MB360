package com.csform.android.MB360.wellness.homehealthcare.retrofit;

import com.csform.android.MB360.wellness.healthcheckup.repository.responseclass.HealthCheckupOverviewResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.CityResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.HealthcareOverviewResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.MembersResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.Message;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.MessageResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageDMResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageDSResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageECResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageLTResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageNCResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackagePTResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageSTResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HomeHealthCareApi {

    @GET("HomeHealthCare/GetAllMembers")
    Call<MembersResponse> getMembers(@Query("EmpID") String empIdNo,
                                     @Query("GroupCode") String groupCode,
                                     @Query("WellSrNo") String wellSrNo);

    @POST("HomeHealthCare/SavePersonInfo")
    Call<MessageResponse> SavePersonAddress(@Query("LINE1") String strLine1,
                                            @Query("LINE2") String strLine2, @Query("LANDMARK") String strLandmark,
                                            @Query("CITY") String strCity, @Query("STATE") String strState,
                                            @Query("PINCODE") String strPinCode, @Query("MobileNumber") String strMobileNumber,
                                            @Query(value = "EmailId", encoded = true) String strEmail, @Query("WellSerSrno") String strWellSrNo,
                                            @Query("PersonSrNo") String strPersonSrNo);


    @GET("HomeHealthCare/GetCities")
    Call<CityResponse> getCities();

    @GET("HomeHealthCare/GetPkgPrice")
    Call<PackageResponse> getPackages();


    // booking calls
    @POST("HomeHealthCare/ScheduleAppointment")
    Call<MessageResponse> scheduleAppointment(
            @Query("PersonSrNo") String strPersonSrno,
            @Query("FamilySrNo") String strFamilySrno,
            @Query("ISRescheduled") int intIsRescheduled,
            @Query("RejtApptSrNo") String intApptSrNo,
            @Query("PkgPriceSrNo") int intPkgPriceSrNo,
            @Query("Date_Condition") int intDateCond,
            @Query(value = "Date_pref", encoded = true) String strdatePref,
            @Query(value = "From_date", encoded = true) String strFromDate,
            @Query(value = "To_date", encoded = true) String strToDate,
            @Query("RescSrNo") int intStrRescSrNo,
            @Query("Remarks") String remark);

    // LT PackageLT
    @GET("HomeHealthCare/GetPkgPriceLT")
    Call<PackageLTResponse> getPackagesLT();

    // LT booking calls
    @POST("HomeHealthCare/ScheduleAppointmentLT")
    Call<MessageResponse> scheduleAppointmentLT(
            @Query("PersonSrNo") String strPersonSrno,
            @Query("FamilySrNo") String strFamilySrno,
            @Query("ISRescheduled") int intIsRescheduled,
            @Query("RejtApptSrNo") String intApptSrNo,
            @Query("PkgPriceSrNo") int intPkgPriceSrNo,
            @Query("Date_Condition") int intDateCond,
            @Query(value = "Date_pref", encoded = true) String strdatePref,
            @Query(value = "From_date", encoded = true) String strFromDate,
            @Query(value = "To_date", encoded = true) String strToDate,
            @Query("RescSrNo") int intStrRescSrNo,
            @Query("Remarks") String remark);

    // ST PackageST
    @GET("HomeHealthCare/GetPkgPriceST")
    Call<PackageSTResponse> getPackagesST();

    // ST booking calls
    // http://wellness.mybenefits360.com/mbapiv2/api/v1/HomeHealthCare/ScheduleAppointmentST?
    // PersonSrNo=60641&
    // FamilySrNo=17128&
    // ISRescheduled=0&
    // RejtApptSrNo=-1&
    // PkgPriceSrNo=1&
    // Date_pref=08-Feb-2023&
    // Time_pref=10:00:AM&
    // RescSrNo=0&
    // Remarks=Test
    @POST("HomeHealthCare/ScheduleAppointmentST")
    Call<MessageResponse> scheduleAppointmentST(
            @Query("PersonSrNo") String strPersonSrno,
            @Query("FamilySrNo") String strFamilySrno,
            @Query("ISRescheduled") int intIsRescheduled,
            @Query("RejtApptSrNo") String intApptSrNo,
            @Query("PkgPriceSrNo") int intPkgPriceSrNo,
            @Query(value = "Date_pref", encoded = true) String strdatePref,
            @Query(value = "Time_pref", encoded = true) String strTimePref,
            @Query("RescSrNo") int intStrRescSrNo,
            @Query(value = "Remarks", encoded = true) String remark);

    // PT PackageST
    @GET("HomeHealthCare/GetPkgPricePT")
    Call<PackagePTResponse> getPackagesPT();

    // PT booking calls

    //PersonSrNo=26185&
    // FamilySrNo=5625&
    // ISRescheduled=0&
    // RejtApptSrNo=-1&
    // PkgPriceSrNo=21&
    // Date_pref=08-Apr-2021&
    // Time_pref=10:00:AM&
    // RescSrNo=0&
    // Remarks=&
    // From_date=01-01-1990&
    // To_date=01-01-1990&
    // Date_Condition=1


    @POST("HomeHealthCare/ScheduleAppointmentPT")
    Call<MessageResponse> scheduleAppointmentPT(
            @Query("PersonSrNo") String strPersonSrno,
            @Query("FamilySrNo") String strFamilySrno,
            @Query("ISRescheduled") int intIsRescheduled,
            @Query("RejtApptSrNo") String intApptSrNo,
            @Query("PkgPriceSrNo") int intPkgPriceSrNo,
            @Query("Date_Condition") int intDateCond,
            @Query(value = "Date_pref", encoded = true) String strdatePref,
            @Query(value = "Time_pref", encoded = true) String time_pref,
            @Query(value = "From_date", encoded = true) String strFromDate,
            @Query(value = "To_date", encoded = true) String strToDate,
            @Query("RescSrNo") int intStrRescSrNo,
            @Query("Remarks") String remark);

    // NC PackageNC
    @GET("HomeHealthCare/GetPkgPriceNC")
    Call<PackageNCResponse> getPackagesNC();

    // NC booking calls
    @POST("HomeHealthCare/ScheduleAppointmentNC")
    Call<MessageResponse> scheduleAppointmentNC(
            @Query("PersonSrNo") String strPersonSrno,
            @Query("FamilySrNo") String strFamilySrno,
            @Query("ISRescheduled") int intIsRescheduled,
            @Query("RejtApptSrNo") String intApptSrNo,
            @Query("PkgPriceSrNo") int intPkgPriceSrNo,
            @Query("Date_Condition") int intDateCond,
            @Query(value = "Date_pref", encoded = true) String strdatePref,
            @Query(value = "From_date", encoded = true) String strFromDate,
            @Query(value = "To_date", encoded = true) String strToDate,
            @Query("RescSrNo") int intStrRescSrNo,
            @Query("Remarks") String remark);

    // DS PackageDS
    @GET("HomeHealthCare/GetPkgPriceDS")
    Call<PackageDSResponse> getPackagesDS();

    // DS booking calls
    //http://wellness.mybenefits360.com/mbapiv2/api/v1/HomeHealthCare/ScheduleAppointmentDS?
    // PersonSrNo=60641&
    // FamilySrNo=17128&
    // ISRescheduled=0&
    // RejtApptSrNo=-1&
    // PkgPriceSrNo=21&
    // Date_pref=08-Apr-2023&
    // Time_pref=10:00 AM&
    // RescSrNo=0&
    // Remarks=TestRemark
    @POST("HomeHealthCare/ScheduleAppointmentDS")
    Call<MessageResponse> scheduleAppointmentDS(
            @Query("PersonSrNo") String strPersonSrno,
            @Query("FamilySrNo") String strFamilySrno,
            @Query("ISRescheduled") int intIsRescheduled,
            @Query("RejtApptSrNo") String intApptSrNo,
            @Query("PkgPriceSrNo") int intPkgPriceSrNo,
            @Query(value = "Date_pref", encoded = true) String strdatePref,
            @Query("Time_pref") String timePref,
            @Query("RescSrNo") int intStrRescSrNo,
            @Query("Remarks") String remark);


    // EC PackageEC
    @GET("HomeHealthCare/GetPkgPriceEC")
    Call<PackageECResponse> getPackagesEC();

    // DS booking calls
    @POST("HomeHealthCare/ScheduleAppointmentEC")
    Call<MessageResponse> scheduleAppointmentEC(
            @Query("PersonSrNo") String strPersonSrno,
            @Query("FamilySrNo") String strFamilySrno,
            @Query("PkgPriceSrNo") int intPkgPriceSrNo,
            @Query("RescSrNo") int intStrRescSrNo,
            @Query("Remarks") String remark);

    // DM PackageDM
    @GET("HomeHealthCare/GetPkgPriceDM")
    Call<PackageDMResponse> getPackagesDM();

    // DM booking calls
    @POST("HomeHealthCare/ScheduleAppointmentDM")
    Call<MessageResponse> scheduleAppointmentDM(
            @Query("PersonSrNo") String strPersonSrno,
            @Query("FamilySrNo") String strFamilySrno,
            @Query("PkgPriceSrNo") int intPkgPriceSrNo,
            @Query("RescSrNo") int intStrRescSrNo,
            @Query("Remarks") String remark);

    // Overview
    @GET("HomeHealthCare/GetOverviewDetails")
    Call<HealthcareOverviewResponse> getHealthcareOverview(@Query("WellSrNo") String wellSrNo);

}
