package com.csform.android.MB360.insurance.claimdataupload.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csform.android.MB360.R
import com.csform.android.MB360.databinding.FragmentAddBeneficiaryCDUBinding
import com.csform.android.MB360.insurance.claimdataupload.adapters.ClaimBenefeciaryAdapter
import com.csform.android.MB360.insurance.claimdataupload.base.ApiHelperImpl
import com.csform.android.MB360.insurance.claimdataupload.base.UiState
import com.csform.android.MB360.insurance.claimdataupload.base.ViewModelFactory
import com.csform.android.MB360.insurance.claimdataupload.interfaces.BeneficiarySelectListener
import com.csform.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation
import com.csform.android.MB360.insurance.claimdataupload.repository.ClaimDocumentUploadViewModel
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AaData
import com.csform.android.MB360.insurance.claimdataupload.responseclass.CDUAllSelecetedValue.CDUAllSelecetedValueRequest
import com.csform.android.MB360.insurance.claimdataupload.responseclass.Detail
import com.csform.android.MB360.insurance.claimdataupload.responseclass.cduUpdateDocsData.CDUUpdatedDocRequest
import com.csform.android.MB360.insurance.claimdataupload.retrofit.ClaimIntimationRetrofitClient
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel
import com.csform.android.MB360.utilities.DefaultDispatcherProvider
import com.csform.android.MB360.utilities.EncryptionPreference
import com.csform.android.MB360.utilities.LogMyBenefits
import com.csform.android.MB360.utilities.LogTags
import kotlinx.coroutines.launch


class AddBeneficiaryFragmentCDU : Fragment(), BeneficiarySelectListener {


    private var viewPagerNavigation: ViewPagerNavigation? = null
    private val binding get() = _binding!!
    private var _binding: FragmentAddBeneficiaryCDUBinding? = null
    private var itemsdata: MutableList<Detail> = mutableListOf()
    lateinit var adapter: ClaimBenefeciaryAdapter
    lateinit var itemDataNew: Detail
    private var tpaSelected = false
    var claimDocumentUploadViewModel: ClaimDocumentUploadViewModel? = null
    var loadSessionViewModel: LoadSessionViewModel? = null

    var tpaData: String = ""
    var isIntimatedClaimdata: String = ""
    var isClaimIntimatedValu: String = ""
    var whereClaimIntimatedValue: String = ""
    var typeOfClaimPre: String = ""
    var typeOfClaimMain: String = ""
    var typeOfClaimPost: String = ""

    var aaData: AaData? = null


    companion object {
        fun newInstance(viewPagerNavigation: ViewPagerNavigation, onTpaSelect: Boolean): AddBeneficiaryFragmentCDU {
            val fragment = AddBeneficiaryFragmentCDU()
            val args = Bundle()

            args.putSerializable("viewPagerNavigation", viewPagerNavigation)
            fragment.arguments = args
            fragment.tpaSelected = onTpaSelect


            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            viewPagerNavigation = it.getSerializable("viewPagerNavigation") as? ViewPagerNavigation
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBeneficiaryCDUBinding.inflate(inflater, container, false)

        binding.gotToHome.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val view = binding.root
        val layoutParams = binding.dependantCycle.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
        binding.dependantCycle.layoutParams = layoutParams
        // setUpObserver()

        //yes with tap condition
        // tpaBeneficiaryData()

        //no condition
        // LoadIntimatedClaimApi()

        //yes with without tpa condition
        // withoutTpaIntimationData()

        return view
    }

    private fun setUpObserver() {
        loadSessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)
        val emp_sr_no: String = loadSessionViewModel!!.loadSessionData.value!!.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo()

        claimDocumentUploadViewModel = ViewModelProvider(
                requireActivity(),
                ViewModelFactory(
                        ApiHelperImpl(ClaimIntimationRetrofitClient.apiService),
                        DefaultDispatcherProvider()
                )
        )[ClaimDocumentUploadViewModel::class.java]
        aaData = claimDocumentUploadViewModel?.EditData?.value
        if (aaData != null) {
            aaData = claimDocumentUploadViewModel!!.EditData.value!!
        }

        var tpavalues = claimDocumentUploadViewModel!!.SelectedList.value
        tpaData = tpavalues!!.get(0).isTpaSelectedYes
        isIntimatedClaimdata = tpavalues!!.get(0).isIntimatedSelecetdYes
        if (tpaData.equals("TPAYes") && isIntimatedClaimdata.equals("isClaimYes")) {
            //yes with tap condition
            LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "yes")
            binding.claimIntimationLayout.visibility = View.VISIBLE
            claimDocumentUploadViewModel!!.allBeneficiaryDetails(emp_sr_no, loadSessionViewModel!!.loadSessionData.value!!.getGroupInfoData().getGroupchildsrno(),
                    loadSessionViewModel!!.loadSessionData.value!!.groupPolicies.get(0).groupGMCPoliciesData[0].oeGrpBasInfSrNo)
            //yes with tap condition
            tpaBeneficiaryData()

        } else if (isIntimatedClaimdata.equals("isClaimNo")) {
            //no condition
            LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "No Condition hide tpa")
            binding.claimIntimationLayout.visibility = View.GONE
            // claimDocumentUploadViewModel!!.loadClaimNoFromClaimsDetails("115038", "1589")
            claimDocumentUploadViewModel!!.loadClaimNoFromClaimsDetails(emp_sr_no, loadSessionViewModel!!.loadSessionData.value!!.getGroupInfoData().getGroupchildsrno())
            //no condition
            LoadIntimatedClaimApi()

        } else {
            //yes with without tpa condition
            claimDocumentUploadViewModel!!.loadIntimationNumbers(emp_sr_no)
            //  yes with without tpa condition
            withoutTpaIntimationData()

        }
    }

    private fun withoutTpaIntimationData() {
        try {
            LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "No")
            itemsdata.clear()
            binding.claimIntimationLayout.visibility = View.GONE
            claimDocumentUploadViewModel!!.viewModelScope.launch {
                claimDocumentUploadViewModel!!.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorLayout.visibility = View.GONE

                            if (!it.data.IntimatedDetailInfo.isNullOrEmpty()) {
                                LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "${it.data.IntimatedDetailInfo.get(0).IntimationDetails!!.Detail.get(0).PERSONNAME}")
                                var items = it.data.IntimatedDetailInfo
                                LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "SUCCESS$items")

                                itemsdata = ArrayList<Detail>()

                                for (i in 0 until it.data.IntimatedDetailInfo.size) {
                                    itemDataNew = Detail()
                                    itemDataNew.claimNo = it.data.IntimatedDetailInfo[i].CLAIMINTIMATIONNO!!
                                    itemDataNew.claimIntSrNo = it.data.IntimatedDetailInfo[i].CLMINTSRNO!!
                                    LogMyBenefits.d("clmIntSrNo", "" + it.data.IntimatedDetailInfo[i].CLMINTSRNO!!)
                                    itemDataNew.DATEOFBIRTH = it.data.IntimatedDetailInfo[0].IntimationDetails!!.Detail[0].DATEOFBIRTH
                                    itemDataNew.CELLPHONENUMBER = it.data.IntimatedDetailInfo[0].IntimationDetails!!.Detail[0].CELLPHONENUMBER
                                    itemDataNew.AGE = it.data.IntimatedDetailInfo[0].IntimationDetails!!.Detail[0].AGE
                                    itemDataNew.GENDER = it.data.IntimatedDetailInfo[0].IntimationDetails!!.Detail[0].GENDER
                                    itemDataNew.PERSONNAME = it.data.IntimatedDetailInfo[0].IntimationDetails!!.Detail[0].PERSONNAME
                                    itemDataNew.RELATIONID = it.data.IntimatedDetailInfo[0].IntimationDetails!!.Detail[0].RELATIONID
                                    itemDataNew.RELATIONNAME = it.data.IntimatedDetailInfo[0].IntimationDetails!!.Detail[0].RELATIONNAME

                                    itemDataNew.isSelected = aaData?.CLMINTSRNO == it.data.IntimatedDetailInfo[i].CLMINTSRNO!!

                                    itemDataNew.PERSONSRNO = it.data.IntimatedDetailInfo[0].IntimationDetails!!.Detail[0].PERSONSRNO
                                    itemsdata.add(itemDataNew)
                                }
                                adapter = ClaimBenefeciaryAdapter(itemsdata, requireContext(), this@AddBeneficiaryFragmentCDU, tpaData)
                                binding.dependantCycle.adapter = adapter
                            } else {
                                binding.progressBar.visibility = View.GONE
                                binding.errorLayout.visibility = View.VISIBLE
                            }
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorLayout.visibility = View.VISIBLE
                            viewPagerNavigation?.disableBottomNavigation()

                            LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, it.message)
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorLayout.visibility = View.GONE
                            LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "LOADING")
                        }

                        else -> {
                            LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "FAILED")
                        }
                    }
                }

            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun tpaBeneficiaryData() {
        itemsdata.clear()

        try {
            claimDocumentUploadViewModel!!.viewModelScope.launch {
                claimDocumentUploadViewModel!!.uiStateBeneficiaryData.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorLayout.visibility = View.GONE
                            LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "SUCCESS")
                            LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "${it.data.Detail.get(0).PERSONNAME}")
                            if (!it.data.Detail.isNullOrEmpty()) {

                                itemsdata = it.data.Detail as MutableList<Detail>

                                var items = it.data.Detail
                                LogMyBenefits.d(LogTags.CDU_BENEFICIARY_DETAILS, "SUCCESS$items")
                                itemsdata = ArrayList<Detail>()

                                for (i in 0 until it.data.Detail.size) {
                                    itemDataNew = Detail()
                                    itemDataNew.claimNo = ""
                                    itemDataNew.claimIntSrNo = "0"
                                    LogMyBenefits.d("clmIntSrNo", "" + itemDataNew.claimNo)
                                    itemDataNew.DATEOFBIRTH = it.data.Detail[i].DATEOFBIRTH
                                    itemDataNew.CELLPHONENUMBER = it.data.Detail[i].CELLPHONENUMBER
                                    itemDataNew.AGE = it.data.Detail[i].AGE
                                    itemDataNew.GENDER = it.data.Detail[i].GENDER
                                    itemDataNew.PERSONNAME = it.data.Detail[i].PERSONNAME
                                    itemDataNew.RELATIONID = it.data.Detail[i].RELATIONID
                                    itemDataNew.RELATIONNAME = it.data.Detail[i].RELATIONNAME
                                    itemDataNew.isSelected = aaData?.PERSONSRNO == it.data.Detail[i].PERSONSRNO

                                    // itemDataNew.isSelected = false
                                    itemDataNew.PERSONSRNO = it.data.Detail[i].PERSONSRNO
                                    itemsdata.add(itemDataNew)
                                }
                            } else {
                                binding.progressBar.visibility = View.GONE
                                binding.errorLayout.visibility = View.VISIBLE
                            }
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorLayout.visibility = View.VISIBLE
                            LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, it.message)
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorLayout.visibility = View.GONE
                            LogMyBenefits.d(LogTags.CDU_BENEFICIARY_DETAILS, "LOADING")
                        }

                        else -> {
                            LogMyBenefits.d(LogTags.CDU_BENEFICIARY_DETAILS, "FAILED")
                        }
                    }
                    adapter = ClaimBenefeciaryAdapter(itemsdata, requireContext(), this@AddBeneficiaryFragmentCDU, tpaData)
                    binding.dependantCycle.adapter = adapter
                }

            }

        } catch (e: Exception) {
            e.stackTrace
        }
    }

    override fun onBeneficiarySelect(position: Int, person: Detail, b: Boolean) {
        try {
            binding.dependantCycle.post {
                for (item in itemsdata) {
                    if (item.isSelected) {
                        item.isSelected = false
                    }
                }


                val layoutParams = binding.dependantCycle.layoutParams as ViewGroup.MarginLayoutParams
                val marginBottomInPixels = resources.getDimensionPixelSize(R.dimen.margin_bottom)
                layoutParams.setMargins(0, 0, 0, marginBottomInPixels)
                binding.dependantCycle.layoutParams = layoutParams

                person.isSelected = true
                itemsdata[position].isSelected = true

                adapter.notifyItemRangeChanged(0, itemsdata.size)
            }
        } catch (e: Exception) {
            e.stackTrace
        }

        claimDocumentUploadViewModel = ViewModelProvider(requireActivity()).get(ClaimDocumentUploadViewModel::class.java)

        var dataList = ArrayList<CDUAllSelecetedValueRequest>()
        var dataListEdit = CDUUpdatedDocRequest()

        try {
            //IntimateClaim/addcliamdetailsDB api call
            claimDocumentUploadViewModel = ViewModelProvider(requireActivity()).get(ClaimDocumentUploadViewModel::class.java)
            loadSessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)
            val emp_sr_no: String = loadSessionViewModel!!.loadSessionData.value!!.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo()

            var tpavalues = claimDocumentUploadViewModel!!.SelectedList.value
            tpaData = tpavalues?.get(0)?.isTpaSelectedYes.toString()
            isIntimatedClaimdata = tpavalues?.get(0)?.isIntimatedSelecetdYes.toString()
            typeOfClaimPre = tpavalues?.get(0)?.typeOfClaimPre.toString()
            typeOfClaimMain = tpavalues?.get(0)?.typeOfClaimMain.toString()
            typeOfClaimPost = tpavalues?.get(0)?.typeOfClaimPost.toString()

            if (typeOfClaimPre.equals("1")) {
                typeOfClaimPre = "1"

            } else {
                typeOfClaimPre = "0"
            }


            if (typeOfClaimMain.equals("1")) {
                typeOfClaimMain = "1"

            } else {
                typeOfClaimMain = "0"
            }
            if (typeOfClaimPost.equals("1")) {
                typeOfClaimPost = "1"

            } else {
                typeOfClaimPost = "0"
            }


            if (isIntimatedClaimdata.equals("isClaimYes")) {
                isClaimIntimatedValu = "1"
            } else {
                isClaimIntimatedValu = "0"
            }
            var cduAllSelecetedValueRequest = CDUAllSelecetedValueRequest()
            if (tpaData.equals("TPAYes")) {
                whereClaimIntimatedValue = "2"
                cduAllSelecetedValueRequest.CLM_INT_SR_NO = "0"
                cduAllSelecetedValueRequest.CLAIM_INTIMATION_NO = binding.claimIntimationNumberEditText.text.toString()
            } else {
                whereClaimIntimatedValue = "1"
                cduAllSelecetedValueRequest.CLM_INT_SR_NO = person.claimIntSrNo
                cduAllSelecetedValueRequest.CLAIM_INTIMATION_NO = person.claimNo
            }
            cduAllSelecetedValueRequest.DOC_REQ_BY = emp_sr_no
            cduAllSelecetedValueRequest.IS_CLM_PRE_HOSP = typeOfClaimPre
            cduAllSelecetedValueRequest.IS_CLM_MAIN_HOSP = typeOfClaimMain
            cduAllSelecetedValueRequest.IS_CLM_POST_HOSP = typeOfClaimPost
            cduAllSelecetedValueRequest.CLAIM_INTIMATED = isClaimIntimatedValu
            cduAllSelecetedValueRequest.CLAIM_INTIMATED_DEST = whereClaimIntimatedValue
            cduAllSelecetedValueRequest.PERSON_SR_NO = person.PERSONSRNO!!
            dataList.add(cduAllSelecetedValueRequest)
            claimDocumentUploadViewModel!!.updateCDUSelectedData(dataList)


            val encryptionPreference = EncryptionPreference(requireActivity().getApplicationContext())
            encryptionPreference.setEncryptedDataString("CLAIM_INTIMATION_SR_NO", cduAllSelecetedValueRequest.CLM_INT_SR_NO)

            LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "${cduAllSelecetedValueRequest.DOC_REQ_BY}, \n" +
                    "                    ${cduAllSelecetedValueRequest.IS_CLM_PRE_HOSP}, \n" +
                    "                    ${cduAllSelecetedValueRequest.IS_CLM_MAIN_HOSP}, \n" +
                    "                    ${cduAllSelecetedValueRequest.IS_CLM_POST_HOSP},\n" +
                    "                    ${cduAllSelecetedValueRequest.CLAIM_INTIMATED}, \n" +
                    "                    ${cduAllSelecetedValueRequest.CLAIM_INTIMATED_DEST}, \n" +
                    "                    ${cduAllSelecetedValueRequest.CLM_INT_SR_NO}, \n" +
                    "                    ${cduAllSelecetedValueRequest.CLAIM_INTIMATION_NO}, \n" +
                    "                    ${cduAllSelecetedValueRequest.PERSON_SR_NO})")

        } catch (e: Exception) {
            e.stackTrace
        }

        validate()

    }


    private fun validate() {
        val atLeastOneSelected = itemsdata.any { it.isSelected }
        //  if (atLeastOneSelected) {


        if (tpaData == "TPAYes" && binding.claimIntimationNumberEditText.text.length > 0 && atLeastOneSelected) {
            val layoutParams = binding.dependantCycle.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, 0, 0, 80)
            binding.dependantCycle.layoutParams = layoutParams
            viewPagerNavigation?.enableBottomNavigation()
            viewPagerNavigation?.enableNextLayout()


        } else if (atLeastOneSelected && tpaData == "TPANo") {
            val layoutParams = binding.dependantCycle.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, 0, 0, 80)
            binding.dependantCycle.layoutParams = layoutParams
            viewPagerNavigation?.enableBottomNavigation()
            viewPagerNavigation?.enableNextLayout()

        } else if (atLeastOneSelected && isIntimatedClaimdata.equals("isClaimNo")) {
            val layoutParams = binding.dependantCycle.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, 0, 0, 80)
            binding.dependantCycle.layoutParams = layoutParams
            viewPagerNavigation?.enableBottomNavigation()
            viewPagerNavigation?.enableNextLayout()
        } else {
            viewPagerNavigation?.disableBottomNavigation()
            viewPagerNavigation?.disableNextLayout()
        }
    }

   /* private fun getTpaPref(): Boolean {
        return try {
            *//*  val pref = EncryptionPreference(requireContext())
              pref.getEncryptedDataBooldefFalse("isTPA")*//*
            val pref = EncryptionPreference(requireContext())

            *//*
                        val preferenceChangeListener: OnSharedPreferenceChangeListener = pref.sharedPreferenceListener.onSharedPreferenceChanged { sharedPreferences, key ->
                            // Handle the change for the specific preference key
                            if ("your_preference_key" == key) {
                                val updatedValue = sharedPreferences.getString(key, defaultValue)
                                // Perform your desired action here
                            }
                        }*//*
            pref.getEncryptedDataBooldefFalse("isTPA")

        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }

    }*/

    override fun onResume() {
        super.onResume()
        validate()
        setUpObserver()

        binding.claimIntimationNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changing.
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has changed.
                val updatedText = s.toString()
                validate()
            }
        })

        viewPagerNavigation?.disableBottomNavigation()

        val layoutParams = binding.dependantCycle.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, 0, 0, 0)
        binding.dependantCycle.layoutParams = layoutParams
        //getTpaPref()


    }

    private fun LoadIntimatedClaimApi() {
        itemsdata.clear()

        claimDocumentUploadViewModel!!.viewModelScope.launch {
            claimDocumentUploadViewModel!!.uiStateLoadIntimationNo.collect {
                when (it) {
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorLayout.visibility = View.GONE
                        LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "SUCCESS")
                        if (!it.data.ClaimInformation.isNullOrEmpty()) {
                            var items = it.data.ClaimInformation

                            itemsdata = ArrayList<Detail>()
                            for (user in items) {
                                itemDataNew = Detail()
                                itemDataNew.DATEOFBIRTH = user.DATEOFBIRTH
                                itemDataNew.CELLPHONENUMBER = user.CELLPHONENUMBER
                                itemDataNew.AGE = user.AGE
                                itemDataNew.GENDER = user.GENDER
                                itemDataNew.PERSONNAME = user.BENEFICIARY
                                itemDataNew.RELATIONID = ""
                                itemDataNew.RELATIONNAME = user.RELATIONWITHEMPLOYEE
                                itemDataNew.isSelected = aaData?.PERSONSRNO == user.PERSONSRNO
                                itemDataNew.PERSONSRNO = user.PERSONSRNO
                                var input_string = user.CLAIMNO!!
                                var claimNo = input_string.replace(" ", "")

                                itemDataNew.claimNo = claimNo
                                itemDataNew.claimIntSrNo = user.CLAIMSRNO!!
                                itemsdata.add(itemDataNew)
                            }
                            adapter = ClaimBenefeciaryAdapter(itemsdata, requireContext(), this@AddBeneficiaryFragmentCDU, tpaData)
                            binding.dependantCycle.adapter = adapter
                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.errorLayout.visibility = View.VISIBLE
                            viewPagerNavigation!!.disableBottomNavigation()


                        }

                    }

                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorLayout.visibility = View.VISIBLE
                        viewPagerNavigation!!.disableBottomNavigation()
                        LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, it.message)
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorLayout.visibility = View.GONE
                        viewPagerNavigation!!.disableBottomNavigation()
                        LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "LOADING")
                    }

                    else -> {
                        LogMyBenefits.d(LogTags.CDU_ADD_BENEFICIARY_FRAGMENTS, "FAILED")
                    }
                }
            }

        }
    }
}