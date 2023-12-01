package com.csform.android.MB360.insurance.claimdataupload.interfaces

import com.csform.android.MB360.insurance.claimdataupload.responseclass.AllClaimsResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.CDUAllSelecetedValue.CDUAllSelectedResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ClaimDepenedentResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.intimatedClaimResonse.IntimatedClaimsResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.LoadBeneficiaryResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.LoadRequiredDocumentDetails.LoadRequiredDocResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ResultInfo
import com.csform.android.MB360.insurance.claimdataupload.responseclass.cduUpdateDocsData.CDUUpdatedDocResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.inserClaimDocResponse.InsertClaimDocResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.loadClaimNoFromIntimateClaim.LoadClaimNoFromIntimateResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface ClaimDocUploadApiHelper {

    fun LoadIntimationNo(employeesrno: String): Flow<IntimatedClaimsResponse>

    fun LoadDepenedetDetails(claimIntSrNo: String): Flow<ClaimDepenedentResponse>

    fun AllClaimDetails(EndIndex: String,
                        StartIndex: String,
                        Search: String,
                        employeesrno: String,
                        groupchildsrno: String,
                        oegrpbasinfsrno: String): Flow<AllClaimsResponse>

    fun LoadBeneficiaryDetails(employeesrno: String,
                               groupchildsrno: String,
                               oegrpbasinfsrno: String): Flow<LoadBeneficiaryResponse>

    fun LoadCDUAllSelecetedData(DOC_REQ_BY: String,
                                IS_CLM_PRE_HOSP: String,
                                IS_CLM_MAIN_HOSP: String,
                                IS_CLM_POST_HOSP: String,
                                CLAIM_INTIMATED: String,
                                CLAIM_INTIMATED_DEST: String,
                                CLM_INT_SR_NO: String,
                                CLAIM_INTIMATION_NO: String,
                                PERSON_SR_NO: String): Flow<CDUAllSelectedResponse>

    fun LoadClaimNoFromClaimsDetails(
            employeesrno: String,
            groupchildsrno: String,
            /* oegrpbasinfsrno: String*/
    ): Flow<LoadClaimNoFromIntimateResponse>

    fun InserClaimDocuments(
            requestBody: RequestBody,
    ): Flow<InsertClaimDocResponse>

    fun LoadRequiredClaimsDocDetails(groupchildsrno: String,
                                     oegrpbasinfsrno: String): Flow<LoadRequiredDocResponse>


    fun CDUUpdatedDocData(CLM_DOCS_UPLOAD_REQ_SR_NO: String,
                          DOC_REQ_BY: String,
                          IS_CLM_PRE_HOSP: String,
                          IS_CLM_MAIN_HOSP: String,
                          IS_CLM_POST_HOSP: String,
                          CLAIM_INTIMATED: String,
                          CLAIM_INTIMATED_DEST: String,
                          CLM_INT_SR_NO: String,
                          CLAIM_INTIMATION_NO: String,
                          PERSON_SR_NO: String): Flow<CDUUpdatedDocResponse>


    fun LoadTPAConformationDetails(employeesrno: String,
                                   groupchildsrno: String,
                                   oegrpbasinfsrno: String,
                                   CLM_DOCS_UPLOAD_REQ_SR_NO: String): Flow<ResultInfo>


}
