package com.csform.android.MB360.insurance.claimdataupload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

import com.csform.android.MB360.R
import com.csform.android.MB360.databinding.FragmentAllClaimsBinding
import com.csform.android.MB360.insurance.claimdataupload.adapters.AllClaimsAdapter
import com.csform.android.MB360.insurance.claimdataupload.base.ApiHelperImpl
import com.csform.android.MB360.insurance.claimdataupload.base.UiState
import com.csform.android.MB360.insurance.claimdataupload.base.ViewModelFactory
import com.csform.android.MB360.insurance.claimdataupload.interfaces.CduEditListerner
import com.csform.android.MB360.insurance.claimdataupload.interfaces.FileUploadListerner
import com.csform.android.MB360.insurance.claimdataupload.repository.ClaimDocumentUploadViewModel
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AaData
import com.csform.android.MB360.insurance.claimdataupload.retrofit.ClaimIntimationRetrofitClient
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel
import com.csform.android.MB360.utilities.DefaultDispatcherProvider
import com.csform.android.MB360.utilities.LogMyBenefits
import com.csform.android.MB360.utilities.LogTags
import kotlinx.coroutines.launch


class AllClaimsFragment : Fragment(), FileUploadListerner,CduEditListerner {

    private var _binding: FragmentAllClaimsBinding? = null
    private val binding get() = _binding!!
    var navController: NavController? = null

    private lateinit var allClaimsViewModel: ClaimDocumentUploadViewModel
    var loadSessionViewModel: LoadSessionViewModel? = null
    lateinit var adapter: AllClaimsAdapter
    private var itemsdata: MutableList<AaData> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentAllClaimsBinding.inflate(inflater, container, false)


        val view = binding.root
        //  navController = findNavController(requireActivity(), R.id.nav_host_fragment)
        binding.nextLayout.setOnClickListener {
            // Navigate to the destination fragment using the NavController
            findNavController().navigate(R.id.claim_data_upload_fragment_app)
        }




        setupObserver()

        allClaimsViewModel!!.viewModelScope.launch {
            allClaimsViewModel!!.uiStateForAllClaims.collect {
                when (it) {
                    is UiState.Success -> {
                        LogMyBenefits.d(LogTags.CDU_ALL_CLAIM_FRAGMENT, "SUCCESS")
                        binding.progressBar.visibility = View.GONE
                        binding.errorLayoutCdu.visibility = View.GONE
                        if (!it.data.aaData.isNullOrEmpty()) {
                            itemsdata = it.data.aaData
                            LogMyBenefits.d(LogTags.CDU_ALL_CLAIM_FRAGMENT, "${itemsdata}")
                            adapter = AllClaimsAdapter(itemsdata, this@AllClaimsFragment, this@AllClaimsFragment,this@AllClaimsFragment)
                            binding.rvAllClaims.adapter = adapter
                        } else {
                            binding.errorLayoutCdu.visibility = View.VISIBLE

                        }

                    }

                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorLayoutCdu.visibility = View.VISIBLE
                        LogMyBenefits.d(LogTags.CDU_ALL_CLAIM_FRAGMENT, it.message)
                    }

                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorLayoutCdu.visibility = View.GONE

                        LogMyBenefits.d(LogTags.CDU_ALL_CLAIM_FRAGMENT, "LOADING")
                    }

                    else -> {
                        LogMyBenefits.d(LogTags.CDU_ALL_CLAIM_FRAGMENT, "FAILED")
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun setupObserver() {
        try {

            allClaimsViewModel = ViewModelProvider(
                    requireActivity(),
                    ViewModelFactory(
                            ApiHelperImpl(ClaimIntimationRetrofitClient.apiService),
                            DefaultDispatcherProvider()
                    )
            )[ClaimDocumentUploadViewModel::class.java]
            loadSessionViewModel = ViewModelProvider(requireActivity()).get(LoadSessionViewModel::class.java)
            val emp_sr_no: String = loadSessionViewModel!!.loadSessionData.value!!.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo()

            allClaimsViewModel.allClaims("50", "0", "", emp_sr_no, loadSessionViewModel!!.loadSessionData.value!!.getGroupInfoData().getGroupchildsrno(),
                    loadSessionViewModel!!.loadSessionData.value!!.groupPolicies.get(0).groupGMCPoliciesData[0].oeGrpBasInfSrNo)
        } catch (e: Exception) {
            e.stackTrace
        }


    }

    companion object {

    }


    override fun onResume() {
        super.onResume()

        setupObserver()
        allClaimsViewModel!!.EditData(null)

    }

    override fun fileOnClickListerner(param: Int) {
        val action = AllClaimsFragmentDirections.actionAllclaimsToClaimDataUploadedFragment(itemsdata.get(param))
        findNavController().navigate(action)

    }

    override fun onEditListerner(pos: Int) {
        val action = AllClaimsFragmentDirections.actionAllclaimsToClaimDataUploadFragmentApp(itemsdata.get(pos))
        findNavController().navigate(action)
    }
}