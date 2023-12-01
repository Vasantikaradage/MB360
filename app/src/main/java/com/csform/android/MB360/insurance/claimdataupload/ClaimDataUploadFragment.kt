package com.csform.android.MB360.insurance.claimdataupload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.csform.android.MB360.BuildConfig
import com.csform.android.MB360.databinding.FragmentClaimDataUploadBinding
import com.csform.android.MB360.insurance.HomeFragmentDirections
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel
import com.csform.android.MB360.utilities.EncryptionPreference
import com.csform.android.MB360.utilities.LogMyBenefits

class ClaimDataUploadFragment : Fragment() {

    private var _binding: FragmentClaimDataUploadBinding? = null
    private val binding get() = _binding!!
    var loadSessionViewModel: LoadSessionViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClaimDataUploadBinding.inflate(inflater, container, false)
        loadSessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)
        val loadSessionResponse = loadSessionViewModel!!.loadSessionData.value
        val view = binding.root
        val encryptionPreference = EncryptionPreference(requireContext())

        val emp_sr_no: String = loadSessionResponse!!.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo()
        binding.webviewClaim.loadUrl(BuildConfig.DOWNLOAD_BASE_URL + "HandleAppSession.ASPX?APPLOGIN=" + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN) + "?QVAL=claimsdocumentupload")
        return view
    }


}