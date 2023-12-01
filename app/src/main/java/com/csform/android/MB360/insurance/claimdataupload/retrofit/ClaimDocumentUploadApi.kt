package com.csform.android.MB360.insurance.claimdataupload.retrofit


import com.csform.android.MB360.insurance.claimdataupload.responseclass.AllClaimsResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.CDUAllSelecetedValue.CDUAllSelectedResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ClaimDepenedentResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.LoadBeneficiaryResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.LoadRequiredDocumentDetails.LoadRequiredDocResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ResultInfo
import com.csform.android.MB360.insurance.claimdataupload.responseclass.cduUpdateDocsData.CDUUpdatedDocResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.inserClaimDocResponse.InsertClaimDocResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.intimatedClaimResonse.IntimatedClaimsResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.loadClaimNoFromIntimateClaim.LoadClaimNoFromIntimateResponse
import com.csform.android.MB360.insurance.queries.responseclass.Message
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ClaimDocumentUploadApi {
    @GET("IntimateClaim/LoadIntimationNo")
    suspend fun loadIntimationNumbers(@Query("employeesrno") employeeSrNo: String): IntimatedClaimsResponse

    @GET("IntimateClaim/getIntimationDetailsFor")
    suspend fun loadDependentData(@Query("ClaimIntSrNo", encoded = true) employeeSrNo: String): ClaimDepenedentResponse


    @GET("IntimateClaim/LoadALLClaimsDetails")
    suspend fun loadAllClaimsData(@Query("EndIndex") EndIndex: String,
                                  @Query("StartIndex") StartIndex: String,
                                  @Query("Search") Search: String,
                                  @Query("employeesrno", encoded = true) employeeSrNo: String,
                                  @Query("groupchildsrno", encoded = true) groupchildsrno: String,
                                  @Query("oegrpbasinfsrno", encoded = true) oegrpbasinfsrno: String): AllClaimsResponse


    @GET("IntimateClaim/LoadBeneficiary")
    suspend fun loadBeneficiary(@Query("employeesrno", encoded = true) employeeSrNo: String,
                                @Query("groupchildsrno", encoded = true) groupchildsrno: String,
                                @Query("oegrpbasinfsrno", encoded = true) oegrpbasinfsrno: String): LoadBeneficiaryResponse


    @POST("IntimateClaim/addcliamdetailsDB")
    suspend fun addClaimDetails(@Query("DOC_REQ_BY") docReqBy: String,
                                @Query("IS_CLM_PRE_HOSP") IsClmPreHosp: String,
                                @Query("IS_CLM_MAIN_HOSP") isClaimMainHosp: String,
                                @Query("IS_CLM_POST_HOSP") isClaimPostHosp: String,
                                @Query("CLAIM_INTIMATED") claimIntimated: String,
                                @Query("CLAIM_INTIMATED_DEST") claimIntimatedDest: String,
                                @Query("CLM_INT_SR_NO") clmIntSrNo: String,
                                @Query("CLAIM_INTIMATION_NO") claimIntimatinNo: String,
                                @Query("PERSON_SR_NO") personSrNo: String): CDUAllSelectedResponse


    @POST("IntimateClaim/Insertcliamdocreqdetails")
    suspend fun submitAllDocument(
            @Body requestBody: RequestBody?,
    ): InsertClaimDocResponse

    @GET("IntimateClaim/LoadEmpClaimsValues")
    suspend fun loadClaimNoFromIntimateClaim(@Query("employeesrno", encoded = true) employeeSrNo: String,
                                             @Query("groupchildsrno", encoded = true) groupchildsrno: String): LoadClaimNoFromIntimateResponse


    @GET("IntimateClaim/LoadRequiredClaimsDocDetails")
    suspend fun loadRequiredClaimsDocDetails(@Query("groupchildsrno", encoded = true) groupchildsrno: String,
                                             @Query("oegrpbasinfsrno", encoded = true) oegrpbasinfsrno: String): LoadRequiredDocResponse


    @POST("IntimateClaim/updateCliamDetails")
    suspend fun cduUpdatedDocData(@Query("CLM_DOCS_UPLOAD_REQ_SR_NO") clmDocUploadReqSrNo: String,
                                @Query("DOC_REQ_BY") docReqBy: String,
                                @Query("IS_CLM_PRE_HOSP") IsClmPreHosp: String,
                                @Query("IS_CLM_MAIN_HOSP") isClaimMainHosp: String,
                                @Query("IS_CLM_POST_HOSP") isClaimPostHosp: String,
                                @Query("CLAIM_INTIMATED") claimIntimated: String,
                                @Query("CLAIM_INTIMATED_DEST") claimIntimatedDest: String,
                                @Query("CLM_INT_SR_NO") clmIntSrNo: String,
                                @Query("CLAIM_INTIMATION_NO") claimIntimatinNo: String,
                                @Query("PERSON_SR_NO") personSrNo: String): CDUUpdatedDocResponse



    @GET("IntimateClaim/CheckTPAupload")
    suspend fun CheckTPAupload (@Query("employeesrno", encoded = true) employeeSrNo: String,
                                @Query("groupchildsrno", encoded = true) groupchildsrno: String,
                                @Query("oegrpbasinfsrno", encoded = true) oegrpbasinfsrno: String,
                                @Query("CLM_DOCS_UPLOAD_REQ_SR_NO") clmDocUploadReqSrNo: String): ResultInfo
      // GroupChildSrno, oegrpbaseinfsrno, EmpSrNo, CLM_DOCS_UPLOAD_REQ_SR_NO)


}