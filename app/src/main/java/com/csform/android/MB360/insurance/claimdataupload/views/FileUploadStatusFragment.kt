package com.csform.android.MB360.insurance.claimdataupload.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csform.android.MB360.databinding.FragmentAllClaimsBinding
import com.csform.android.MB360.databinding.FragmentFileUploadStatusBinding
import com.csform.android.MB360.insurance.claimdataupload.base.ApiHelperImpl
import com.csform.android.MB360.insurance.claimdataupload.base.UiState
import com.csform.android.MB360.insurance.claimdataupload.base.ViewModelFactory
import com.csform.android.MB360.insurance.claimdataupload.interfaces.ViewPagerNavigation
import com.csform.android.MB360.insurance.claimdataupload.repository.ClaimDocumentUploadViewModel
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AaData
import com.csform.android.MB360.insurance.claimdataupload.retrofit.ClaimIntimationRetrofitClient
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel
import com.csform.android.MB360.utilities.DefaultDispatcherProvider
import com.csform.android.MB360.utilities.EncryptionPreference
import com.csform.android.MB360.utilities.LogMyBenefits
import com.csform.android.MB360.utilities.LogTags
import kotlinx.coroutines.launch

class FileUploadStatusFragment : Fragment() {

    private var _binding: FragmentFileUploadStatusBinding? = null
    private var viewPagerNavigation: ViewPagerNavigation? = null
    var claimDocumentUploadViewModel: ClaimDocumentUploadViewModel? = null
    private val binding get() = _binding!!
    var aaData: AaData? = null
    var CLM_DOCS_UPLOAD_REQ_SR_NO:String =""
    var emp_sr_no:String=""
    var loadSessionViewModel: LoadSessionViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewPagerNavigation = it.getSerializable("viewPagerNavigation") as? ViewPagerNavigation
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFileUploadStatusBinding.inflate(inflater, container, false)

        val view = binding.root
        return view
    }

    companion object {
        fun newInstance(viewPagerNavigation: ViewPagerNavigation): FileUploadStatusFragment {
            val fragment = FileUploadStatusFragment()
            val args = Bundle()
            args.putSerializable("viewPagerNavigation", viewPagerNavigation)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()

        binding.gotToAllClaimsBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        claimDocumentUploadViewModel = ViewModelProvider(
                requireActivity(),
                ViewModelFactory(
                        ApiHelperImpl(ClaimIntimationRetrofitClient.apiService),
                        DefaultDispatcherProvider()
                )
        )[ClaimDocumentUploadViewModel::class.java]


        getDataFromDocument()

        loadSessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)
        val emp_sr_no: String = loadSessionViewModel!!.loadSessionData.value!!.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo()


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




    }

    private fun getDataFromDocument() {
        try {
            claimDocumentUploadViewModel!!.viewModelScope.launch {
                claimDocumentUploadViewModel!!.uiStateInsertDocuments.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility=View.GONE
                            binding.gotToAllClaimsBtn.visibility=View.VISIBLE
                            if(it.data.res!!.Status!!){
                                binding.uploadAnimation.visibility = View.VISIBLE
                                binding.feedbackClaimsText.visibility=View.VISIBLE


                                binding.stausText.visibility=View.VISIBLE

                                aaData = claimDocumentUploadViewModel?.EditData?.value
                                if(aaData!=null){
                                    binding.stausText.text="Claim Data Updated"
                                }else
                                {
                                    binding.stausText.text="Claim Data Uploaded"
                                }
                                val encryptionPreference = EncryptionPreference(requireActivity().getApplicationContext())
                                var CLAIM_NO = encryptionPreference.getEncryptedDataString("CLAIM_INTIMATION_SR_NO")
                                //binding.feedbackClaimsText.text = "Claim Data uploaded Successfully $CLAIM_NO is your Claim sr no"
                                binding.feedbackClaimsText.text = "Your claim data has been successfully uploaded. \nYour claim serial number is #$CLAIM_NO."
                                binding.imgError.visibility=View.GONE
                                binding.feedbackClaimsTextError.visibility=View.GONE
                                viewPagerNavigation!!.disableProgressBar()

                              /*  claimDocumentUploadViewModel!!.tpaConformation(emp_sr_no, loadSessionViewModel!!.loadSessionData.value!!.getGroupInfoData().getGroupchildsrno(),
                                        loadSessionViewModel!!.loadSessionData.value!!.groupPolicies.get(0).groupGMCPoliciesData[0].oeGrpBasInfSrNo,CLM_DOCS_UPLOAD_REQ_SR_NO)

*/
                            }else
                            {
                                binding.feedbackClaimsText.visibility=View.GONE
                                binding.uploadAnimation.visibility = View.GONE
                                binding.imgError.visibility=View.VISIBLE
                                binding.stausText.visibility=View.GONE
                                binding.feedbackClaimsTextError.visibility=View.VISIBLE
                                binding.feedbackClaimsTextError.text =it.data.res!!.Message!!.toString()/*encryptionPreference.getEncryptedDataString("Status_MSG")*/
                            }
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility=View.GONE
                            LogMyBenefits.d(LogTags.CDU, it.message)

                        }

                        is UiState.Loading -> {
                            LogMyBenefits.d(LogTags.CDU, "LOADING")

                            if(claimDocumentUploadViewModel?.EditData?.value!=null)
                            {
                                binding.progressBar.visibility=View.GONE
                                binding.gotToAllClaimsBtn.visibility=View.VISIBLE
                                binding.uploadAnimation.visibility = View.VISIBLE
                                binding.feedbackClaimsText.visibility=View.VISIBLE


                                binding.stausText.visibility=View.VISIBLE

                                aaData = claimDocumentUploadViewModel?.EditData?.value
                                if(aaData!=null){
                                    binding.stausText.text="Claim Data Updated"
                                }
                                val encryptionPreference = EncryptionPreference(requireActivity().getApplicationContext())
                                var CLAIM_NO = encryptionPreference.getEncryptedDataString("CLAIM_INTIMATION_SR_NO")
                                //binding.feedbackClaimsText.text = "Claim Data uploaded Successfully $CLAIM_NO is your Claim sr no"
                                binding.feedbackClaimsText.text = "Your claim data has been successfully uploaded. \nYour claim serial number is #$CLAIM_NO."
                                binding.imgError.visibility=View.GONE
                                binding.feedbackClaimsTextError.visibility=View.GONE
                                viewPagerNavigation!!.disableProgressBar()

                               /* claimDocumentUploadViewModel!!.tpaConformation(emp_sr_no, loadSessionViewModel!!.loadSessionData.value!!.getGroupInfoData().getGroupchildsrno(),
                                        loadSessionViewModel!!.loadSessionData.value!!.groupPolicies.get(0).groupGMCPoliciesData[0].oeGrpBasInfSrNo,CLM_DOCS_UPLOAD_REQ_SR_NO)
*/
                            }
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
}