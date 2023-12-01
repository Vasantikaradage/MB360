package com.csform.android.MB360.insurance.claimdataupload.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csform.android.MB360.insurance.claimdataupload.base.UiState
import com.csform.android.MB360.insurance.claimdataupload.interfaces.ClaimDocUploadApiHelper
import com.csform.android.MB360.insurance.claimdataupload.responseclass.intimatedClaimResonse.IntimatedClaimsResponse
import com.csform.android.MB360.utilities.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AaData
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AllClaimsResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.CDUAllSelecetedValue.CDUAllSelecetedValueRequest
import com.csform.android.MB360.insurance.claimdataupload.responseclass.CDUAllSelecetedValue.CDUAllSelectedResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.CDUSelectedValueModel
import com.csform.android.MB360.insurance.claimdataupload.responseclass.cduUpdateDocsData.CDUUpdatedDocResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.LoadBeneficiaryResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.LoadRequiredDocumentDetails.LoadRequiredDocResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ResultInfo
import com.csform.android.MB360.insurance.claimdataupload.responseclass.cduUpdateDocsData.CDUUpdatedDocRequest
import com.csform.android.MB360.insurance.claimdataupload.responseclass.inserClaimDocResponse.InsertClaimDocResponse
import com.csform.android.MB360.insurance.claimdataupload.responseclass.loadClaimNoFromIntimateClaim.LoadClaimNoFromIntimateResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ClaimDocumentUploadViewModel(
        val apiHelper: ClaimDocUploadApiHelper,
        private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    //Load Intimation no
    private val _uiState = MutableStateFlow<UiState<IntimatedClaimsResponse>>(UiState.Loading)
    val uiState: StateFlow<UiState<IntimatedClaimsResponse>> = _uiState

    // All Claims
    private val _uiStateForAllClaims = MutableStateFlow<UiState<AllClaimsResponse>>(UiState.Loading)
    val uiStateForAllClaims: StateFlow<UiState<AllClaimsResponse>> = _uiStateForAllClaims

    // LoadIntimationNo from Intimated claims
    private val _uiStateLoadIntimationNo = MutableStateFlow<UiState<LoadClaimNoFromIntimateResponse>>(UiState.Loading)
    val uiStateLoadIntimationNo: StateFlow<UiState<LoadClaimNoFromIntimateResponse>> = _uiStateLoadIntimationNo

    //Load BeneficiaryData
    private val _uiStateBeneficiaryData = MutableStateFlow<UiState<LoadBeneficiaryResponse>>(UiState.Loading)
    val uiStateBeneficiaryData: StateFlow<UiState<LoadBeneficiaryResponse>> = _uiStateBeneficiaryData


    // CDU Selecetd Data ViewMOdel
    private val _uiStateSelecetdData = MutableStateFlow<UiState<CDUAllSelectedResponse>>(UiState.Loading)
    val uiStateSelecetdData: StateFlow<UiState<CDUAllSelectedResponse>> = _uiStateSelecetdData


    // CDU Selecetd Data ViewMOdel
    private val _uiStateInsertDocuments = MutableStateFlow<UiState<InsertClaimDocResponse>>(UiState.Loading)
    val uiStateInsertDocuments: StateFlow<UiState<InsertClaimDocResponse>> = _uiStateInsertDocuments


    // LoadRequiredClaimsDocDetails
    private val _uiStateLoadRequiredClaimsDoc = MutableStateFlow<UiState<LoadRequiredDocResponse>>(UiState.Loading)
    val uiStateLoadRequiredClaimsDoc: StateFlow<UiState<LoadRequiredDocResponse>> = _uiStateLoadRequiredClaimsDoc


    private val _updatedCDUSelectedData = MutableLiveData<List<CDUAllSelecetedValueRequest>>()
    val updatedCDUSelectedData: LiveData<List<CDUAllSelecetedValueRequest>> = _updatedCDUSelectedData


    private val _selectedList = MutableLiveData<List<CDUSelectedValueModel>>()
    val SelectedList: LiveData<List<CDUSelectedValueModel>> = _selectedList


    private val _editData = MutableLiveData<AaData?>()
    var EditData: MutableLiveData<AaData?> = _editData


    // cdu updated api
    private val _uiStateCDUUpdatedData = MutableStateFlow<UiState<CDUUpdatedDocResponse>>(UiState.Loading)
    val uiStateCDUUpdatedData: StateFlow<UiState<CDUUpdatedDocResponse>> = _uiStateCDUUpdatedData



    // tpa conformation

    private val _tpaConformationData = MutableStateFlow<UiState<ResultInfo>>(UiState.Loading)
    val uiTPAConformationData :StateFlow<UiState<ResultInfo>> = _tpaConformationData

    //Load Intimation no
    fun loadIntimationNumbers(emp_sr_no: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = UiState.Loading
            apiHelper.LoadIntimationNo(/*"88189"*/emp_sr_no)
                    .flowOn(dispatcherProvider.io)
                    .catch { e -> _uiState.value = UiState.Error(e.toString()) }
                    .collect {
                        _uiState.value = UiState.Success(it)
                    }
        }


    }


    // for All Claims
    fun allClaims(endindex: String,
                  startIndex: String,
                  search: String,
                  empSrNo: String,
                  groupChildSrNo: String,
                  oeGrpSrNo: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiStateForAllClaims.value = UiState.Loading
            apiHelper.AllClaimDetails(endindex,
                    startIndex,
                    search,
                    /*"88189"*/empSrNo,
                    /*"1512"*/groupChildSrNo,
                    /*"1523"*/oeGrpSrNo)
                    .flowOn(dispatcherProvider.io)
                    .catch { e -> _uiState.value = UiState.Error(e.toString()) }
                    .collect {
                        _uiStateForAllClaims.value = UiState.Success(it)
                    }
        }


    }


    // LoadIntimationNo from Intimated claims
    fun loadClaimNoFromClaimsDetails(empSrNo: String,
                                     groupChildSrNo: String,
                                   /*  oeGrpSrNo: String*/) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiStateLoadIntimationNo.value = UiState.Loading
            apiHelper.LoadClaimNoFromClaimsDetails(empSrNo,groupChildSrNo)
                    .flowOn(dispatcherProvider.io)
                    .catch { e -> _uiStateLoadIntimationNo.value = UiState.Error(e.toString()) }
                    .collect {
                        _uiStateLoadIntimationNo.value = UiState.Success(it)
                    }
        }


    }


    //Load BeneficiaryData
    fun allBeneficiaryDetails(empSrNo: String,
                              groupChildSrNo: String,
                              oeGrpSrNo: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiStateBeneficiaryData.value = UiState.Loading
            apiHelper.LoadBeneficiaryDetails(
                    /*"115038"*/empSrNo,
                    /*"1589"*/groupChildSrNo,
                    /*"1770"*/oeGrpSrNo)
                    .flowOn(dispatcherProvider.io)
                    .catch { e -> _uiStateBeneficiaryData.value = UiState.Error(e.toString()) }
                    .collect {
                        _uiStateBeneficiaryData.value = UiState.Success(it)
                    }
        }


    }


    //CDU Selecetd data
    fun CDUAllSelecetedData(cduAllSelecetedValueRequest: CDUAllSelecetedValueRequest) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiStateSelecetdData.value = UiState.Loading

            //var cduAllSelecetedValueRequest= CDUAllSelecetedValueRequest("88189","1","0","0","1","2","0","CLMI_TG1_TEST09_MOTHER_GHI_P_A_1_TG1_190523_1","251327")
            apiHelper.LoadCDUAllSelecetedData(
                    cduAllSelecetedValueRequest.DOC_REQ_BY,
                    cduAllSelecetedValueRequest.IS_CLM_PRE_HOSP,
                    cduAllSelecetedValueRequest.IS_CLM_MAIN_HOSP,
                    cduAllSelecetedValueRequest.IS_CLM_POST_HOSP,
                    cduAllSelecetedValueRequest.CLAIM_INTIMATED,
                    cduAllSelecetedValueRequest.CLAIM_INTIMATED_DEST,
                    cduAllSelecetedValueRequest.CLM_INT_SR_NO,
                    cduAllSelecetedValueRequest.CLAIM_INTIMATION_NO,
                    cduAllSelecetedValueRequest.PERSON_SR_NO)
                    .flowOn(dispatcherProvider.io)
                    .catch { e -> _uiStateSelecetdData.value = UiState.Error(e.toString()) }
                    .collect {
                        _uiStateSelecetdData.value = UiState.Success(it)
                    }
        }


    }


    //CDU Insert documents
    fun CDUInsertDocumentsData(requestBody: RequestBody) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiStateInsertDocuments.value = UiState.Loading
            apiHelper.InserClaimDocuments(requestBody)
                    .flowOn(dispatcherProvider.io)
                    .catch { e -> _uiStateInsertDocuments.value = UiState.Error(e.toString()) }
                    .collect {
                        _uiStateInsertDocuments.value = UiState.Success(it)
                    }
        }


    }

    //LoadRequiredClaimsDocDetails
    fun LoadRequiredClaimsDocDetails(groupChildSrNo: String,
                                     oeGrpSrNo: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiStateLoadRequiredClaimsDoc.value = UiState.Loading
            apiHelper.LoadRequiredClaimsDocDetails(/*"1515"*/groupChildSrNo, /*"1534"*/oeGrpSrNo)
                    .flowOn(dispatcherProvider.io)
                    .catch { e -> _uiStateLoadRequiredClaimsDoc.value = UiState.Error(e.toString()) }
                    .collect {
                        _uiStateLoadRequiredClaimsDoc.value = UiState.Success(it)
                    }
        }
    }






    fun updateCDUSelectedData(updatedCDUSelectedDataList: List<CDUAllSelecetedValueRequest>) {
        _updatedCDUSelectedData.value = updatedCDUSelectedDataList
    }
    fun updatedRadioButtonSelecetdData(selectedList: List<CDUSelectedValueModel>) {
        _selectedList.value = selectedList
    }

    fun EditData(selectedList: AaData?) {
        _editData.value = selectedList
    }




    //cdu update api call
    fun CDUUpadetdDocumentsData(cDUUpdatedDocRequest: CDUUpdatedDocRequest) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiStateSelecetdData.value = UiState.Loading

            //var cduAllSelecetedValueRequest= CDUAllSelecetedValueRequest("88189","1","0","0","1","2","0","CLMI_TG1_TEST09_MOTHER_GHI_P_A_1_TG1_190523_1","251327")
            apiHelper.CDUUpdatedDocData(
                    cDUUpdatedDocRequest.CLM_DOCS_UPLOAD_REQ_SR_NO,
                    cDUUpdatedDocRequest.DOC_REQ_BY,
                    cDUUpdatedDocRequest.IS_CLM_PRE_HOSP,
                    cDUUpdatedDocRequest.IS_CLM_MAIN_HOSP,
                    cDUUpdatedDocRequest.IS_CLM_POST_HOSP,
                    cDUUpdatedDocRequest.CLAIM_INTIMATED,
                    cDUUpdatedDocRequest.CLAIM_INTIMATED_DEST,
                    cDUUpdatedDocRequest.CLM_INT_SR_NO,
                    cDUUpdatedDocRequest.CLAIM_INTIMATION_NO,
                    cDUUpdatedDocRequest.PERSON_SR_NO)
                    .flowOn(dispatcherProvider.io)
                    .catch { e -> _uiStateSelecetdData.value = UiState.Error(e.toString()) }
                    .collect {
                        _uiStateCDUUpdatedData.value = UiState.Success(it)
                    }
        }


    }


    //Load TPA conformtion data
    fun tpaConformation(empSrNo: String,
                              groupChildSrNo: String,
                              oeGrpSrNo: String,
                        CLM_DOCS_UPLOAD_REQ_SR_NO:String) {
        viewModelScope.launch(dispatcherProvider.main) {
            _tpaConformationData.value = UiState.Loading
            apiHelper.LoadTPAConformationDetails(
                    /*"115038"*/empSrNo,
                    /*"1589"*/groupChildSrNo,
                    /*"1770"*/oeGrpSrNo,
                    CLM_DOCS_UPLOAD_REQ_SR_NO)
                    .flowOn(dispatcherProvider.io)
                    .catch { e -> _tpaConformationData.value = UiState.Error(e.toString()) }
                    .collect {
                        _tpaConformationData.value = UiState.Success(it)
                    }
        }


    }

}