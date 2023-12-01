package com.csform.android.MB360.insurance.claimdataupload.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.csform.android.MB360.databinding.FragmentClaimsDetailCDUBinding
import com.csform.android.MB360.insurance.claimdataupload.adapters.ClaimBenefeciaryAdapter
import com.csform.android.MB360.insurance.claimdataupload.base.ApiHelperImpl
import com.csform.android.MB360.insurance.claimdataupload.base.ViewModelFactory
import com.csform.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation
import com.csform.android.MB360.insurance.claimdataupload.repository.ClaimDocumentUploadViewModel
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AaData
import com.csform.android.MB360.insurance.claimdataupload.responseclass.CDUSelectedValueModel
import com.csform.android.MB360.insurance.claimdataupload.retrofit.ClaimIntimationRetrofitClient
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel
import com.csform.android.MB360.utilities.DefaultDispatcherProvider
import com.csform.android.MB360.utilities.EncryptionPreference
import com.csform.android.MB360.utilities.LogMyBenefits
import com.csform.android.MB360.utilities.LogTags


@Suppress("DEPRECATION")
class ClaimsDetailFragmentCDU() : Fragment() {

    private var viewPagerNavigation: ViewPagerNavigation? = null
    private var _binding: FragmentClaimsDetailCDUBinding? = null
    var loadSessionViewModel: LoadSessionViewModel? = null

    private lateinit var viewModel: ClaimDocumentUploadViewModel
    lateinit var adapter: ClaimBenefeciaryAdapter

    private var isClaimIntimated: Boolean = false
    private var whereClaimIsIntimated: Boolean = false
    private var typeOfClaim: Boolean = false

    private val binding get() = _binding!!

    var dataList = ArrayList<CDUSelectedValueModel>()
    var cDUSelectedValueModel = CDUSelectedValueModel()
    private var itemsdata: MutableList<AaData> = mutableListOf()




    companion object {
        fun newInstance(viewPagerNavigation: ViewPagerNavigation): ClaimsDetailFragmentCDU {
            val fragment = ClaimsDetailFragmentCDU()
            val args = Bundle()
            args.putSerializable("viewPagerNavigation", viewPagerNavigation)
            fragment.arguments = args
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
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentClaimsDetailCDUBinding.inflate(inflater, container, false)
        val view = binding.root

        setupCheckBoxListeners()
        validate()
        viewModel = ViewModelProvider(requireActivity()).get(ClaimDocumentUploadViewModel::class.java)


        viewModel.EditData
        LogMyBenefits.d(LogTags.CDU_CLAIM_DETAILS_FRAGMENTS, "EDIT DATA" + viewModel.EditData.value)

        //edit condition is claim intimated

        if( viewModel.EditData.value!=null) {
            if (viewModel.EditData.value!!.CLAIMINTIMATED.equals("CLAIM INTIMATED")) {
                binding.whereLayout.visibility = View.VISIBLE
                cDUSelectedValueModel.isIntimatedSelecetdYes = "isClaimYes"
                binding.claimIntimatedYes.isChecked = true
                isClaimIntimated=true
            } else {
                binding.whereLayout.visibility = View.GONE
                cDUSelectedValueModel.isIntimatedSelecetdYes = "isClaimNo"
                binding.claimIntimatedNo.isChecked = true
                isClaimIntimated=true
            }

            //edit condition  for destination
            if (viewModel.EditData.value!!.CLAIMINTIMATEDDEST.equals("BENEFITS YOU")) {
                cDUSelectedValueModel.isTpaSelectedYes = "TPANo"
                binding.claimIntimatedYes2.isChecked = true
                whereClaimIsIntimated=true
            } else {
                cDUSelectedValueModel.isTpaSelectedYes = "TPAYes"
                binding.claimIntimatedNo2.isChecked = true
                whereClaimIsIntimated=true
            }

            //edit condition  for checkbox

            val inputString = viewModel.EditData.value!!.TYPEOFCLAIM
            val parts = inputString!!.split("+").map { it.trim() }

            var selectedValuesFromCheckBox = arrayOf("0", "0", "0")
            for (part in parts) {
                if (part == "PRE-HOSPITALISATION") {
                    binding.checkboxPreHospital.isChecked = true
                    typeOfClaim=true
                    selectedValuesFromCheckBox[0] = if (binding.checkboxPreHospital.isChecked) "1" else "0"
                    cDUSelectedValueModel.typeOfClaimPre = selectedValuesFromCheckBox[0]
                } else if (part == "MAIN-HOSPITALISATION") {
                    binding.checkboxMainHospital.isChecked = true
                    typeOfClaim=true
                    selectedValuesFromCheckBox[1] = if (binding.checkboxMainHospital.isChecked) "1" else "0"
                    cDUSelectedValueModel.typeOfClaimMain = selectedValuesFromCheckBox[1]
                } else if (part == "POST-HOSPITALISATION") {
                    binding.checkboxPostHospital.isChecked = true
                    typeOfClaim=true
                    selectedValuesFromCheckBox[2] = if (binding.checkboxPostHospital.isChecked) "1" else "0"
                    cDUSelectedValueModel.typeOfClaimMain = selectedValuesFromCheckBox[2]

                }

            }
            validate()
        }



        binding.claimIntimatedYes.setOnCheckedChangeListener { compoundButton, b ->
            isClaimIntimated = true
            validate()
            if (b) {
                binding.whereLayout.visibility = View.VISIBLE
                cDUSelectedValueModel.isIntimatedSelecetdYes = "isClaimYes"
            } else {
                binding.whereLayout.visibility = View.GONE
                cDUSelectedValueModel.isIntimatedSelecetdYes = "isClaimNo"
            }
        }


        binding.claimIntimatedNo.setOnCheckedChangeListener { compoundButton, b ->
            isClaimIntimated = true
            validate()
            if (b) {
                binding.whereLayout.visibility = View.GONE
                cDUSelectedValueModel.isIntimatedSelecetdYes = "isClaimNo"
            } else {
                binding.whereLayout.visibility = View.VISIBLE
                cDUSelectedValueModel.isIntimatedSelecetdYes = "isClaimYes"
            }
        }

        binding.claimIntimatedYes2.setOnCheckedChangeListener { compoundButton, b ->
            whereClaimIsIntimated = true

            validate()
            if (b) {

                cDUSelectedValueModel.isTpaSelectedYes = "TPANo"

            } else {
                cDUSelectedValueModel.isTpaSelectedYes = "TPAYes"
            }

        }
        binding.claimIntimatedNo2.setOnCheckedChangeListener { compoundButton, b ->

            whereClaimIsIntimated = true
            validate()
            if (b) {
                cDUSelectedValueModel.isTpaSelectedYes = "TPAYes"
            } else {
                cDUSelectedValueModel.isTpaSelectedYes = "TPANo"
            }


        }
        return view
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(
                requireActivity(),
                ViewModelFactory(
                        ApiHelperImpl(ClaimIntimationRetrofitClient.apiService),
                        DefaultDispatcherProvider()
                )
        )[ClaimDocumentUploadViewModel::class.java]
    }

    private fun saveTPApref(isTpa: Boolean) {
        val pref = EncryptionPreference(requireContext())

        pref.setEncryptedBoolString("isTPA", isTpa)
    }

    private fun setupCheckBoxListeners() {

        val checkBoxes = arrayOf(
                binding.checkboxPreHospital,
                binding.checkboxMainHospital,
                binding.checkboxPostHospital
        )
        var selectedValuesFromCheckBox = arrayOf("0", "0", "0")

        var checkedCount = 0

        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                // Check if at least one checkbox is checked
                val atLeastOneChecked = checkBoxes.any {
                    it.isChecked

                }

                if (checkBox.isChecked) {
                    checkedCount++
                } else {
                    checkedCount--
                }

                if (checkedCount >= 1) {
                    typeOfClaim = atLeastOneChecked
                } else {
                    typeOfClaim = false
                }


                var selectedValue = checkBox.text.toString()
                if (selectedValue == binding.checkboxPreHospital.text.toString()) {
                    selectedValuesFromCheckBox[0] = if (isChecked) "1" else "0"
                    cDUSelectedValueModel.typeOfClaimPre = selectedValuesFromCheckBox[0]
                } else if (selectedValue == binding.checkboxMainHospital.text.toString()) {
                    selectedValuesFromCheckBox[1] = if (isChecked) "1" else "0"
                    cDUSelectedValueModel.typeOfClaimMain = selectedValuesFromCheckBox[1]
                } else if (selectedValue == binding.checkboxPostHospital.text.toString()) {
                    selectedValuesFromCheckBox[2] = if (isChecked) "1" else "0"
                    cDUSelectedValueModel.typeOfClaimPost = selectedValuesFromCheckBox[2]
                }
                validate()
            }
        }
    }

    private fun validate() {

        whereClaimIsIntimated = binding.whereLayout.visibility != View.GONE
        if (isClaimIntimated && whereClaimIsIntimated && typeOfClaim) {
            if (binding.whereLayout.visibility == View.VISIBLE) {
                dataList.add(cDUSelectedValueModel)
                viewModel.updatedRadioButtonSelecetdData(dataList)
                viewPagerNavigation?.enableNextLayout()
                setupViewModel()
            }
        } else if (binding.whereLayout.visibility == View.GONE) {

            if (isClaimIntimated && typeOfClaim) {
                dataList.add(cDUSelectedValueModel)
                viewModel.updatedRadioButtonSelecetdData(dataList)
                viewPagerNavigation?.enableNextLayout()
            } else {
                viewPagerNavigation?.disableNextLayout()
            }
        } else {
            viewPagerNavigation?.disableNextLayout()
        }
    }
}