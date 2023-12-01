package com.csform.android.MB360.insurance.enrollment.repository.retrofit;

import com.csform.android.MB360.insurance.enrollment.repository.responseclass.EmployeeResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentMessage;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.InstructionResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MaxMinAgeResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MyCoveragesResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.Parent;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.coverage.CoverageResponseData;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.DependantMessageResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.Dependant;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.Relation;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails.EmployeeFieldDisplaySubResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails.EmployeeUpdateInfo;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.ParentalRelationResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.summary.SummaryResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUpPlanResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUSiResponseData;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface EnrollmentApi {
    //here we write the end points for enrollment api.


    //EnrollmentWindow period
    @GET("window period")
    Call<WindowPeriodEnrollmentResponse> getWindowPeriod();


    //instruction api
    @GET("instruction url")
    Call<InstructionResponse> getEnrollmentInstructions();


    //Enrollment Coverages Data Call
    @GET("coverages url")
    Call<MyCoveragesResponse> getEnrollmentCoverages();

    //Enrollment employee details call
    @GET("getEmployeeDetails")
    Call<EmployeeResponse> getEnrollmentEmployeeDetails();

    //Enrollment dependant details call
    @GET("EmployeeEnrollement/GetRelationsToAddNew")
    Call<List<Relation>> getRelationShipGroup(@Query("EmpSrNo") String empSrno,
                                              @Query("GroupChildSrNo") String groupChildSrNo,
                                              @Query("OeGrpBasInfSrNo") String oeGrpBasInfoSrNo,
                                              @Query("IsWindowPeriodActive") String windowPeriod);

    //Enrollment get Dependant call
    //https://portal.mybenefits360.com/mb360apiV2/api/EmployeeEnrollement/
    // GetDependentsTableForFinalSummary?EmpSrNo=113024&GroupChildSrNo=1202&OeGrpBasInfSrNo=1613&
    // IsRelGrpDivided=0
    @GET("EmployeeEnrollement/GetDependentsTableForFinalSummary")
    Call<List<Dependant>> getDependants(@Query("EmpSrNo") String emp_sr_no,
                                        @Query("GroupChildSrNo") String GroupChildSrNo,
                                        @Query("OeGrpBasInfSrNo") String oeGrpBasInfoSrNo,
                                        @Query("IsRelGrpDivided") String isRelGrpDivided);

    //Enrollment Add Dependant call and Parent
    @POST("EnrollmentDetails/AddDependant")
    Call<EnrollmentMessage> addDependant(@QueryMap(encoded = true) Map<String, String> dependantsOption);

    //Enrollment Delete Dependant call
    @POST("EnrollmentDetails/DeleteDependant")
    Call<EnrollmentMessage> deleteDependant(@QueryMap Map<String, String> dependantsOption);

    //Enrollment Edit Dependant call
    @POST("EnrollmentDetails/UpdateDependant")
    Call<EnrollmentMessage> updateDependant(@QueryMap(encoded = true) Map<String, String> dependantOption);

    //Enrollment get Parental call
    @GET("EnrollmentDetails/GetParentalDependants")
    Call<List<Parent>> getParentalDependant(@Query("IsWindowPeriodActive") String windowPeriodActive,
                                            @Query("GroupChildSrNo") String groupChildSrNo,
                                            @Query("OeGrpBasInfSrNo") String oeGrpBasInfoSrNo,
                                            @Query("EmpSrNo") String employeeSrNo,
                                            @Query("parentalPremium") String parentalPremium);

    //Enrollment update Parental call
    @POST("EnrollmentDetails/UpdateDependant")
    Call<EnrollmentMessage> updateParent(@QueryMap(encoded = true) Map<String, String> parentOption);


    //Enrollment delete Parental call
    @POST("EnrollmentDetails/DeleteParentalDependant")
    Call<EnrollmentMessage> deleteParent(@QueryMap(encoded = true) Map<String, String> parentOption);


    //EnrollmentTopupSumInsured
    @GET("EmployeeEnrollement/loadTopupSumInsuredValue")
    Call<TopUSiResponseData> getTopUpSumInsured(@Query("EmpSrNo") String pEmpSrNo,
                                                @Query("GroupChildSrNo") String groupChildSrNo,
                                                @Query("OeGrpBasInfSrNo") String oeGrpBasInfSrNo,
                                                @Query("Prdttype") String Prdttype);

    @GET("EmployeeEnrollement/ChooseGMCTopUpPlan")
    Call<TopUpPlanResponse> getTopUpGMCPlanData(@Query("EmpSrNo") String pEmpSrNo,
                                                @Query("GroupChildSrNo") String groupChildSrNo,
                                                @Query("OeGrpBasInfSrNo") String oeGrpBasInfSrNo,
                                                @Query("Amount") String amount,
                                                @Query("Premium") String premium);

    @GET("EmployeeEnrollement/ChooseGPATopUpPlan")
    Call<TopUpPlanResponse> getTopUpGPAPlanData(@Query("EmpSrNo") String pEmpSrNo,
                                                @Query("GroupChildSrNo") String groupChildSrNo,
                                                @Query("OeGrpBasInfSrNo") String oeGrpBasInfSrNo,
                                                @Query("Amount") String amount,
                                                @Query("Premium") String premium);

    @GET("EmployeeEnrollement/ChooseGTLTopUpPlan")
    Call<TopUpPlanResponse> getTopUpGTLPlanData(@Query("EmpSrNo") String pEmpSrNo,
                                                @Query("GroupChildSrNo") String groupChildSrNo,
                                                @Query("OeGrpBasInfSrNo") String oeGrpBasInfSrNo,
                                                @Query("Amount") String amount,
                                                @Query("Premium") String premium);


    //coverage data for ghi
    @GET("EmployeeEnrollement/loadCBGMCSumInsuredValue")
    Call<CoverageResponseData> getCoverageGHIData(@Query("GroupChildSrNo") String GroupChildSrNo,
                                                  @Query("OeGrpBasInfSrNo") String OeGrpBasInfSrNo,
                                                  @Query("EmpSrNo") String EmpSrNo);

    //coverage data for ghi
    @GET("EmployeeEnrollement/loadCBGPASumInsuredValue")
    Call<CoverageResponseData> getCoverageGPAData(@Query("GroupChildSrNo") String GroupChildSrNo,
                                                  @Query("OeGrpBasInfSrNo") String OeGrpBasInfSrNo,
                                                  @Query("EmpIdentificationNo") String EmpSrNo);

    //coverage data for ghi
    @GET("EmployeeEnrollement/loadCBGTLSumInsuredValue")
    Call<CoverageResponseData> getCoverageGTLData(@Query("GroupChildSrNo") String GroupChildSrNo,
                                                  @Query("OeGrpBasInfSrNo") String OeGrpBasInfSrNo,
                                                  @Query("EmpIdentificationNo") String EmpSrNo);

    //get employee filed display api
    @GET("EmployeeEnrollement/EmpFieldDisplayInfo")
    Call<List<EmployeeFieldDisplaySubResponse>> getEmployeeFiledDisplay(@Query("OeGrpBasInfSrNo") String OeGrpBasInfSrNo);


    @POST("EmployeeEnrollement/UpdateEmployeeInfo")
    Call<EmployeeUpdateInfo> updateEmployeeDetails(@Body Map<String, String> employeeUpdateObj);


    @GET("EmployeeEnrollement/GetShortSummary")
    Call<SummaryResponse> summaryDetails(@Query("PI_EMPLOYEE_SR_NO_GMC") String gmcEMpSrNo,
                                         @Query("PI_EMPLOYEE_SR_NO_GPA") String gpaEmpSrNo,
                                         @Query("PI_EMPLOYEE_SR_NO_GTL") String gtlEmpSrNo,
                                         @Query("PI_GROUPCHILDSRNO") String groupChildSrno,
                                         @Query("PI_OE_GRP_BAS_INF_SR_NO") String PI_OE_GRP_BAS_INF_SR_NO);


    //add depenedent new
    @POST("EmployeeEnrollement/AddenrollmentDependents")
    Call<DependantMessageResponse> addDependantData(@Body Map<String, String> addDepenedetData);


    //display depedent data new
    @GET("EmployeeEnrollement/GetEmployeeDetialsForEnrollment")
    //  @GET("EmployeeEnrollement/GetDependentsTableForFinalSummary")
    Call<List<Dependant>> getEmployeeDetialsForEnrollment(@Query("EmpSrNo") String empSrNo,
                                                          @Query("GroupChildSrNo") String groupChildSrNo,
                                                          @Query("OeGrpBasInfSrNo") String oeGrpSrNo,
                                                          @Query("IsWindowPeriodActive") String isWindowPeriodActive);

    //Enrollment Edit Dependant call
    @POST("EmployeeEnrollement/UpdateenrollmentDependents")
    Call<DependantMessageResponse> updateDependantData(@Body Map<String, String> dependantOption);


    //Enrollment Edit Dependant call
    @POST("EmployeeEnrollement/DeleteenrollmentDependents")
    Call<DependantMessageResponse> deleteDependantData(@QueryMap(encoded = true) Map<String, String> dependantsOption);


    //Enrollment parental relationShipGroup details call
    @GET("EmployeeEnrollement/GetParentsRelations")
    Call<ParentalRelationResponse> getParentalRelationShipGroup(@Query("EmpSrNo") String empSrno,
                                                                @Query("GroupChildSrNo") String groupChildSrNo,
                                                                @Query("OeGrpBasInfSrNo") String oeGrpBasInfoSrNo,
                                                                @Query("IsWindowPeriodActive") String windowPeriod);


    // parental data new api call
    @GET("EmployeeEnrollement/GetParentDetailsOptedForParentalInsurance")
    Call<List<Parent>> getParentalDependantData(@Query("IsWindowPeriodActive") String windowPeriodActive,
                                                @Query("GroupChildSrNo") String groupChildSrNo,
                                                @Query("OeGrpBasInfSrNo") String oeGrpBasInfoSrNo,
                                                @Query("EmpSrNo") String employeeSrNo,
                                                @Query("OeGrpBasInfSrNoPar") String parentalPremium,
                                                @Query("TypePolSrNo") String typaPolicySrNo,
                                                @Query("EmployeeType") String employeeType);

    //Enrollment Summary confirm api  call
    @POST("EmployeeEnrollement/ConfirmEnrollmentNew")
    Call<DependantMessageResponse> summaryConfirm(@Body Map<String, String> summaryOption);

    @GET("EmployeeEnrollement/GetMaxAndMinAge")
    Call<List<MaxMinAgeResponse>>getMAXMinAge(@Query("OeGrpBasInfSrNo") String oeGrpBasInfSrNo);



}
