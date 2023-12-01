package com.csform.android.MB360.insurance.claimdataupload

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.csform.android.MB360.R
import com.csform.android.MB360.databinding.FragmentClaimDataUploadAppBinding
import com.csform.android.MB360.insurance.claimdataupload.adapters.ClaimViewPagerAdapter
import com.csform.android.MB360.insurance.claimdataupload.base.ApiHelperImpl
import com.csform.android.MB360.insurance.claimdataupload.base.UiState
import com.csform.android.MB360.insurance.claimdataupload.base.ViewModelFactory
import com.csform.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation
import com.csform.android.MB360.insurance.claimdataupload.repository.ClaimDocumentUploadViewModel
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AaData
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ClaimFileUpload
import com.csform.android.MB360.insurance.claimdataupload.responseclass.cduUpdateDocsData.CDUUpdatedDocRequest
import com.csform.android.MB360.insurance.claimdataupload.retrofit.ClaimIntimationRetrofitClient
import com.csform.android.MB360.insurance.claimdataupload.views.AddBeneficiaryFragmentCDU
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel
import com.csform.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel
import com.csform.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData
import com.csform.android.MB360.utilities.DefaultDispatcherProvider
import com.csform.android.MB360.utilities.EncryptionPreference
import com.csform.android.MB360.utilities.LogMyBenefits
import com.csform.android.MB360.utilities.LogTags
import com.csform.android.MB360.utilities.progressview.ProgressView
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class ClaimDataUploadFragmentApp() : Parcelable, Fragment(), ViewPagerNavigation {
    private var _binding: FragmentClaimDataUploadAppBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private var pagerAdapter: ClaimViewPagerAdapter? = null
    private var descriptionData = arrayOf("Claims Details", "Beneficiary Details", "File Upload")
    var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null
    var claimDocumentUploadViewModel: ClaimDocumentUploadViewModel? = null
    var loadSessionViewModel: LoadSessionViewModel? = null

    var selectedPolicyViewModel: SelectedPolicyViewModel? = null
    var personSrNo: String = ""
    var successFileUpload: Boolean = false
    var documentList: ArrayList<ClaimFileUpload> = ArrayList<ClaimFileUpload>()
    lateinit var CLM_DOCS_UPLOAD_REQ_SR_NO: String

    constructor(parcel: Parcel) : this() {
        descriptionData = parcel.createStringArray()!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentClaimDataUploadAppBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.stateProgressBar.setStateDescriptionData(descriptionData)
        binding.stateProgressBar.setStateDescriptionTypeface("font/noto_sans_medium.ttf")

        viewPager = binding.claimDataUploadViewPager
        viewPager.isUserInputEnabled = false
        pagerAdapter = ClaimViewPagerAdapter(requireActivity(), this)
        viewPager.adapter = pagerAdapter

        claimDocumentUploadViewModel = ViewModelProvider(
                requireActivity(),
                ViewModelFactory(
                        ApiHelperImpl(ClaimIntimationRetrofitClient.apiService),
                        DefaultDispatcherProvider()
                )
        )[ClaimDocumentUploadViewModel::class.java]
        claimDocumentUploadViewModel = ViewModelProvider(requireActivity()).get(ClaimDocumentUploadViewModel::class.java)


        val cduUpdateDataInfo = requireArguments().getParcelable<AaData>("cduUpdateData")
        if (cduUpdateDataInfo != null) {
            LogMyBenefits.d("CDU", "EDIT DATA" + requireArguments().getParcelable<AaData>("cduUpdateData"))
            claimDocumentUploadViewModel!!.EditData(cduUpdateDataInfo)


        }

        binding.backLayout.setOnClickListener {
           // previousPage()
        }

        binding.nextLayout.setOnClickListener {
            nextPage()
        }
    }

    override fun nextPage() {
        disableNextLayout()

        val currentItem = viewPager.currentItem
        LogMyBenefits.d("CDU", (currentItem + 1).toString())
        viewPager.setCurrentItem(currentItem + 1, true)
        if (viewPager.currentItem == 1) {
            binding.bottomLayout.visibility = View.GONE
        }
        claimDocumentUploadViewModel!!.viewModelScope.launch {
            claimDocumentUploadViewModel!!.uiStateInsertDocuments.collect {
                when (it) {
                    is UiState.Success -> {
                        LogMyBenefits.d(LogTags.CDU, "SUCCESS")
                        LogMyBenefits.d(LogTags.CDU, "@@@@")
                        successFileUpload = it.data.res!!.Status!!
                        // Toast.makeText(, it.data.res!!.Message, Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        LogMyBenefits.d(LogTags.CDU, it.message)
                        // Toast.makeText(requireActivity, it.message, Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Loading -> {
                        LogMyBenefits.d(LogTags.CDU, "LOADING")
                    }

                    else -> {
                        LogMyBenefits.d(LogTags.CDU, "FAILED")
                    }
                }
            }

        }






        when (viewPager.currentItem) {
            0 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(true)
                binding.stateProgressBar.animationDuration = 550
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.ONE)
               // binding.backLayout.visibility = View.GONE
                binding.nextLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Beneficiary details"
            }

            1 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(true)
                binding.stateProgressBar.animationDuration = 550
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.TWO)
              //  binding.backLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Upload Documents"
                binding.nextLayout.visibility = View.VISIBLE

            }

            2 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(true)
                binding.stateProgressBar.animationDuration = 550
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.THREE)
              //  binding.backLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Submit All"
                claimDocumentUploadViewModel!!.updatedCDUSelectedData.value
                claimDocumentUploadViewModel = ViewModelProvider(
                        requireActivity(),
                        ViewModelFactory(
                                ApiHelperImpl(ClaimIntimationRetrofitClient.apiService),
                                DefaultDispatcherProvider()
                        )
                )[ClaimDocumentUploadViewModel::class.java]

                if (claimDocumentUploadViewModel?.EditData?.value!=null){
                    var   cDUUpdatedDocRequest=CDUUpdatedDocRequest()
                    cDUUpdatedDocRequest.DOC_REQ_BY=claimDocumentUploadViewModel!!.updatedCDUSelectedData.value!!.get(0).DOC_REQ_BY
                    cDUUpdatedDocRequest.CLAIM_INTIMATED=claimDocumentUploadViewModel!!.updatedCDUSelectedData.value!!.get(0).CLAIM_INTIMATED
                    cDUUpdatedDocRequest.CLAIM_INTIMATED_DEST=claimDocumentUploadViewModel!!.updatedCDUSelectedData.value!!.get(0).CLAIM_INTIMATED_DEST
                    cDUUpdatedDocRequest.CLAIM_INTIMATION_NO=claimDocumentUploadViewModel!!.updatedCDUSelectedData.value!!.get(0).CLAIM_INTIMATION_NO
                    cDUUpdatedDocRequest.IS_CLM_PRE_HOSP=claimDocumentUploadViewModel!!.updatedCDUSelectedData.value!!.get(0).IS_CLM_PRE_HOSP
                    cDUUpdatedDocRequest.IS_CLM_MAIN_HOSP=claimDocumentUploadViewModel!!.updatedCDUSelectedData.value!!.get(0).IS_CLM_MAIN_HOSP
                    cDUUpdatedDocRequest.IS_CLM_POST_HOSP=claimDocumentUploadViewModel!!.updatedCDUSelectedData.value!!.get(0).IS_CLM_POST_HOSP
                    cDUUpdatedDocRequest.CLM_INT_SR_NO=claimDocumentUploadViewModel!!.updatedCDUSelectedData.value!!.get(0).CLM_INT_SR_NO
                    cDUUpdatedDocRequest.PERSON_SR_NO=claimDocumentUploadViewModel!!.updatedCDUSelectedData.value!!.get(0).PERSON_SR_NO
                    cDUUpdatedDocRequest.CLM_DOCS_UPLOAD_REQ_SR_NO=claimDocumentUploadViewModel!!.EditData.value!!.CLMDOCSUPLOADREQSRNO.toString()

                    claimDocumentUploadViewModel!!.CDUUpadetdDocumentsData(cDUUpdatedDocRequest)


                }else
                {
                    claimDocumentUploadViewModel!!.CDUAllSelecetedData(claimDocumentUploadViewModel!!.updatedCDUSelectedData.value!!.get(0))

                }

                clickListernerOnUpoadDocuments()
            }

            3 -> {

                binding.stateProgressBar.setAllStatesCompleted(true)
              //  binding.backLayout.visibility = View.VISIBLE
                binding.nextLayoutText.visibility = View.VISIBLE
                binding.bottomNavLayout.visibility = View.GONE
                selectedPolicyViewModel = ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel::class.java)
                val groupPolicyData: GroupPolicyData = selectedPolicyViewModel!!.getSelectedPolicy().getValue()!!
                loadSessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)

                val emp_sr_no: String = loadSessionViewModel!!.loadSessionData.value!!.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo()

                val encryptionPreference = EncryptionPreference(requireActivity().getApplicationContext())
                var CLAIM_NO = encryptionPreference.getEncryptedDataString("CLAIM_INTIMATION_SR_NO")
                LogMyBenefits.d(LogTags.CDU_FILE_UPLOAD_FRAGMENTS, "$CLAIM_NO")

                for (item in documentList) {

                    if(!item.fromEdit) {
                        val requestBody = MultipartBody.Builder()
                                .setType(MultipartBody.FORM)

                        requestBody.addFormDataPart("DOC_REQ_BY", emp_sr_no)
                        if (claimDocumentUploadViewModel?.EditData?.value != null) {
                            requestBody.addFormDataPart("CLM_DOCS_UPLOAD_REQ_SR_NO", claimDocumentUploadViewModel?.EditData?.value?.CLMDOCSUPLOADREQSRNO!!)

                        } else {
                            requestBody.addFormDataPart("CLM_DOCS_UPLOAD_REQ_SR_NO", CLM_DOCS_UPLOAD_REQ_SR_NO)
                        }
                        requestBody.addFormDataPart("CLM_REQ_DOCS_SR_NO", item.CLM_REQ_DOCS_SR_NO.toString())
                        var remark = ""
                        if (!item.remark.isNullOrEmpty()) {
                            remark = item.remark
                        } else {
                            remark = "NA"
                        }
                        requestBody.addFormDataPart("CLM_DOC_UPD_REMARK", remark)
                        requestBody.addFormDataPart("TPA_CODE", groupPolicyData.tpaCode)
                        requestBody.addFormDataPart("CLAIM_NO", CLAIM_NO)

                        requestBody.addFormDataPart("file", item.fileName, RequestBody.create("image/*".toMediaTypeOrNull(), File(item.filePathInfo!!)))
                        val requestBodyData: RequestBody = requestBody.build()

                        if (!item.fileName.isNullOrEmpty()) {
                            claimDocumentUploadViewModel!!.CDUInsertDocumentsData(requestBodyData)
                        }
                    }
                }
            }
        }
    }

    override fun previousPage() {
        val currentItem = viewPager.currentItem
        LogMyBenefits.d("CDU", (currentItem - 1).toString())
        viewPager.setCurrentItem(currentItem - 1, true)
        if (viewPager.currentItem == 1) {
            binding.bottomLayout.visibility = View.GONE
        }
        when (viewPager.currentItem) {
            0 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(false)
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.ONE)
               // binding.backLayout.visibility = View.GONE
                binding.nextLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Beneficiary details"
            }

            1 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(false)
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.TWO)
              //  binding.backLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Upload Documents"
                binding.nextLayout.visibility = View.VISIBLE
                AddBeneficiaryFragmentCDU

            }

            2 -> {
                binding.stateProgressBar.enableAnimationToCurrentState(false)
                binding.stateProgressBar.setCurrentStateNumber(ProgressView.StateNumber.THREE)
               // binding.backLayout.visibility = View.VISIBLE
                binding.nextLayoutText.text = "Submit All"
            }

        }
    }

    private fun clickListernerOnUpoadDocuments() {
        try {
            claimDocumentUploadViewModel!!.viewModelScope.launch {
                claimDocumentUploadViewModel!!.uiStateCDUUpdatedData.collect {
                    when (it) {
                        is UiState.Success -> {
                            LogMyBenefits.d(LogTags.CDU, "upadetd message for SUCCESS" + it.data.Message!!)
                        }

                        is UiState.Error -> {
                            LogMyBenefits.d(LogTags.CDU, it.message)
                            // Toast.makeText(requireActivity, it.message, Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Loading -> {
                            LogMyBenefits.d(LogTags.CDU, "LOADING")
                        }

                        else -> {
                            LogMyBenefits.d(LogTags.CDU, "FAILED")
                        }
                    }
                }

            }
        } catch (e: Exception) {
            e.stackTrace
        }



        /////////////
        try {
            claimDocumentUploadViewModel!!.viewModelScope.launch {
                claimDocumentUploadViewModel!!.uiStateSelecetdData.collect {
                    when (it) {
                        is UiState.Success -> {
                            LogMyBenefits.d(LogTags.CDU, "SUCCESS @@" + it.data.statusValue!!.Message)
                            CLM_DOCS_UPLOAD_REQ_SR_NO = it.data.CLMDOCSUPLOADREQSRNO.toString()
                            // Toast.makeText(requireActivity, it.data.statusValue!!.Message, Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Error -> {
                            LogMyBenefits.d(LogTags.CDU, it.message)
                            // Toast.makeText(requireActivity, it.message, Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Loading -> {
                            LogMyBenefits.d(LogTags.CDU, "LOADING")
                        }

                        else -> {
                            LogMyBenefits.d(LogTags.CDU, "FAILED")
                        }
                    }
                }

            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    override fun disableNextLayout() {
        /* val disableColor = ColorStateList.valueOf(-0x322c23)
         binding.nextLayoutText.setTextColor(disableColor)
         binding.nextLayout.setCardBackgroundColor(disableColor)
         binding.nextLayout.isClickable = false*/
        try {
            val cardBackgroundColor = ContextCompat.getColor(requireContext(), R.color.stroke_color)
            val textColor = ContextCompat.getColor(requireContext(), R.color.background)
            binding.nextLayout.setCardBackgroundColor(cardBackgroundColor)
            binding.nextLayoutText.setTextColor(textColor)
            binding.nextLayoutImage.imageTintList = ColorStateList.valueOf(textColor)
            binding.nextLayout.isEnabled = false
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun enableNextLayout() {
        /*    val disableColor = ColorStateList.valueOf(-0xffff01)
            binding.nextLayoutText.setTextColor(disableColor)
            binding.nextLayout.setCardBackgroundColor(disableColor)
            binding.nextLayout.isClickable = true*/
        binding.bottomLayout.visibility = View.VISIBLE
        try {
            val cardBackgroundColor = ContextCompat.getColor(requireContext(), R.color.gradient_start)
            val textColor = parentFragment?.let { ContextCompat.getColor(it.requireContext(), R.color.white) }
            binding.nextLayout.setCardBackgroundColor(cardBackgroundColor)
            if (textColor != null) {
                binding.nextLayoutText.setTextColor(textColor)
            }
            binding.nextLayoutImage.imageTintList = textColor?.let { ColorStateList.valueOf(it) }
            binding.nextLayout.isEnabled = true
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun enableBottomNavigation() {
        binding.bottomLayout.visibility = View.VISIBLE
    }

    override fun disableBottomNavigation() {
        binding.bottomLayout.visibility = View.GONE
    }

    override fun disableProgressBar() {
        binding.stateProgressBar.visibility = View.GONE
    }

    override fun uploadedDocs(requestBody1: ArrayList<ClaimFileUpload>) {
        documentList = requestBody1;
    }

    override fun onTpaSelect(boolean: Boolean) {
        LogMyBenefits.d("@@", "ontapSelect")

        // pagerAdapter?.ontpaSelect(boolean)
        // pagerAdapter?.notifyDataSetChanged()

    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringArray(descriptionData)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun onResume() {
        super.onResume()
        claimDocumentUploadViewModel = ViewModelProvider(
                requireActivity(),
                ViewModelFactory(
                        ApiHelperImpl(ClaimIntimationRetrofitClient.apiService),
                        DefaultDispatcherProvider()
                )
        )[ClaimDocumentUploadViewModel::class.java]
        claimDocumentUploadViewModel = ViewModelProvider(requireActivity()).get(ClaimDocumentUploadViewModel::class.java)

    }

    companion object CREATOR : Parcelable.Creator<ClaimDataUploadFragmentApp> {
        override fun createFromParcel(parcel: Parcel): ClaimDataUploadFragmentApp {
            return ClaimDataUploadFragmentApp(parcel)
        }

        override fun newArray(size: Int): Array<ClaimDataUploadFragmentApp?> {
            return arrayOfNulls(size)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }


}