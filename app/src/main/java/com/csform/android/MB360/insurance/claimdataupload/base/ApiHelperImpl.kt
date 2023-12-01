package com.csform.android.MB360.insurance.claimdataupload.base

import com.csform.android.MB360.insurance.claimdataupload.interfaces.ClaimDocUploadApiHelper
import com.csform.android.MB360.insurance.claimdataupload.responseclass.CDUAllSelecetedValue.CDUAllSelectedResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.LoadRequiredDocumentDetails.LoadRequiredDocResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ResultInfo
import com.csform.android.MB360.insurance.claimdataupload.responseclass.cduUpdateDocsData.CDUUpdatedDocResponse

import com.csform.android.MB360.insurance.claimdataupload.retrofit.ClaimDocumentUploadApi
import kotlinx.coroutines.flow.Flow


import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody


class ApiHelperImpl(private val apiService: ClaimDocumentUploadApi) : ClaimDocUploadApiHelper {
    override fun LoadIntimationNo(employeeSrNo: String) = flow {
        emit(apiService.loadIntimationNumbers(employeeSrNo))
    }

    override fun LoadDepenedetDetails(claimIntSrNo: String) = flow {
        emit(apiService.loadDependentData(claimIntSrNo))
    }

    override fun AllClaimDetails(EndIndex: String, StartIndex: String, Search: String, employeesrno: String, groupchildsrno: String, oegrpbasinfsrno: String) = flow {
        emit(apiService.loadAllClaimsData(
                EndIndex,
                StartIndex,
                Search,
                employeesrno,
                groupchildsrno,
                oegrpbasinfsrno))
    }

    override fun LoadBeneficiaryDetails(employeesrno: String, groupchildsrno: String, oegrpbasinfsrno: String) = flow {
        emit(apiService.loadBeneficiary(
                employeesrno,
                groupchildsrno,
                oegrpbasinfsrno))
    }

    override fun LoadCDUAllSelecetedData(DOC_REQ_BY: String,
                                         IS_CLM_PRE_HOSP: String,
                                         IS_CLM_MAIN_HOSP: String,
                                         IS_CLM_POST_HOSP: String,
                                         CLAIM_INTIMATED: String,
                                         CLAIM_INTIMATED_DEST: String,
                                         CLM_INT_SR_NO: String,
                                         CLAIM_INTIMATION_NO: String,
                                         PERSON_SR_NO: String) = flow {
        emit(apiService.addClaimDetails(
                DOC_REQ_BY,
                IS_CLM_PRE_HOSP,
                IS_CLM_MAIN_HOSP,
                IS_CLM_POST_HOSP,
                CLAIM_INTIMATED,
                CLAIM_INTIMATED_DEST,
                CLM_INT_SR_NO,
                CLAIM_INTIMATION_NO,
                PERSON_SR_NO))


    }

    override fun LoadClaimNoFromClaimsDetails(employeesrno: String, groupchildsrno: String/*, oegrpbasinfsrno: String*/) = flow {
        emit(apiService.loadClaimNoFromIntimateClaim(
                employeesrno,
                groupchildsrno/*,
                oegrpbasinfsrno*/))
    }

    override fun InserClaimDocuments(multipartBody: RequestBody) = flow {
        emit(apiService.submitAllDocument(multipartBody))
    }

    override fun LoadRequiredClaimsDocDetails(groupchildsrno: String, oegrpbasinfsrno: String) = flow {
        emit(apiService.loadRequiredClaimsDocDetails(
                groupchildsrno,
                oegrpbasinfsrno))
    }

    override fun CDUUpdatedDocData(CLM_DOCS_UPLOAD_REQ_SR_NO: String,
                                   DOC_REQ_BY: String,
                                   IS_CLM_PRE_HOSP: String,
                                   IS_CLM_MAIN_HOSP: String,
                                   IS_CLM_POST_HOSP: String,
                                   CLAIM_INTIMATED: String,
                                   CLAIM_INTIMATED_DEST: String,
                                   CLM_INT_SR_NO: String,
                                   CLAIM_INTIMATION_NO: String,
                                   PERSON_SR_NO: String) = flow {
        emit(apiService.cduUpdatedDocData(
                CLM_DOCS_UPLOAD_REQ_SR_NO,
                DOC_REQ_BY,
                IS_CLM_PRE_HOSP,
                IS_CLM_MAIN_HOSP,
                IS_CLM_POST_HOSP,
                CLAIM_INTIMATED,
                CLAIM_INTIMATED_DEST,
                CLM_INT_SR_NO,
                CLAIM_INTIMATION_NO,
                PERSON_SR_NO))
    }

    override fun LoadTPAConformationDetails(employeesrno: String, groupchildsrno: String, oegrpbasinfsrno: String, CLM_DOCS_UPLOAD_REQ_SR_NO: String)= flow<ResultInfo> {
        emit(apiService.CheckTPAupload(
                employeesrno,
                groupchildsrno,
                oegrpbasinfsrno,
                CLM_DOCS_UPLOAD_REQ_SR_NO))
    }


}