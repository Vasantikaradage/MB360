package com.csform.android.MB360.insurance.claims.repository.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentLoadIntimatedClaimsBinding;
import com.csform.android.MB360.insurance.claims.repository.ClaimsFragment;
import com.csform.android.MB360.insurance.claims.repository.ClaimsViewModel;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGPAPolicyEmployeeDatum;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGTLPolicyEmployeeDatum;
import com.csform.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.csform.android.MB360.onboarding.authentication.LoginActivity;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.utilities.error.ErrorActivity;

public class LoadIntimatedClaimsFragment extends Fragment {

    FragmentLoadIntimatedClaimsBinding binding;
    View view;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    ClaimsViewModel claimsViewModel;
    ClaimAdapter adapter;
    String PRODUCT_CODE = "GMC";

    public LoadIntimatedClaimsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoadIntimatedClaimsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        claimsViewModel = new ViewModelProvider(this).get(ClaimsViewModel.class);

        getClaims();

        claimsViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        claimsViewModel.getReloginState().observe(getActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });

    }

    private void getClaims() {
        //to get the  claims data we need some parameters from load session values


        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {
            PRODUCT_CODE = groupPolicyData.getProductCode();

            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
                String employeeSrNo = "";
                String groupChildSrvNo = "";
                String oeGrpBasInfSrNo = "";

                switch (PRODUCT_CODE) {
                    case "GMC":
                        GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum;
                        groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);

                        //queries for claims
                        employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();
                        groupChildSrvNo = groupGMCPolicyEmployeeDatum.getGroupchildsrno();
                        oeGrpBasInfSrNo = groupGMCPolicyEmployeeDatum.getOeGrpBasInfSrNo();

                        if (claimsViewModel.getClaimsData().getValue() == null) {
                            claimsViewModel.getClaims(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);
                        }
                        break;
                    case "GPA":
                        GroupGPAPolicyEmployeeDatum groupGPAPolicyEmployeeDatum;
                        groupGPAPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0);

                        //queries for claims
                        employeeSrNo = groupGPAPolicyEmployeeDatum.getEmployeeSrNo();
                        groupChildSrvNo = groupGPAPolicyEmployeeDatum.getGroupchildsrno();
                        oeGrpBasInfSrNo = groupGPAPolicyEmployeeDatum.getOeGrpBasInfSrNo();

                        if (claimsViewModel.getClaimsData().getValue() == null) {
                            claimsViewModel.getClaims(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);
                        }
                        break;
                    case "GTL":
try {
    GroupGTLPolicyEmployeeDatum groupGTLPolicyEmployeeDatum;
    groupGTLPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0);

    //queries for claims
    employeeSrNo = groupGTLPolicyEmployeeDatum.getEmployeeSrNo();
    groupChildSrvNo = groupGTLPolicyEmployeeDatum.getGroupchildsrno();
    oeGrpBasInfSrNo = groupGTLPolicyEmployeeDatum.getOeGrpBasInfSrNo();

    if (claimsViewModel.getClaimsData().getValue() == null) {
        claimsViewModel.getClaims(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);
    }
}catch (Exception e)
{
    e.printStackTrace();
}
                        break;
                    default:
                        //error
                        Toast.makeText(getContext(), "Something Wrong Happened", Toast.LENGTH_SHORT).show();

                }


            });


        });


        claimsViewModel.getClaimsData().observe(getViewLifecycleOwner(), claimsResponse -> {
            if (claimsResponse != null) {
                if (claimsResponse.getClaimslist() != null) {

                    if (claimsResponse.getClaimslist().isEmpty()) {
                        binding.errorLayout.setVisibility(View.VISIBLE);
                        binding.messageTextView.setText(getString(R.string.no_intimations_found));
                        binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.noclaim));
                    } else {
                        adapter = new ClaimAdapter(requireContext(), claimsResponse.getClaimslist());
                        binding.claimsRecyclerView.setAdapter(adapter);
                        adapter.notifyItemRangeChanged(0, claimsResponse.getClaimslist().size());
                        binding.errorLayout.setVisibility(View.GONE);
                    }
                    try

                    {
                    if (!claimsResponse.getResult().getStatus()) {
                        binding.messageTextView.setVisibility(View.VISIBLE);
                        binding.messageTextView.setText("Error: " + claimsResponse.getResult().getMessage());
                        binding.errorLayout.setVisibility(View.VISIBLE);
                    }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else {
                   /* binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText(claimsResponse.getResult().getMessage().toLowerCase().startsWith("unable") ? "Unable to reach server.\nPlease try again later." : getString(R.string.no_intimations_found));
                    binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_api_error_state));
                    binding.errorLayout.setVisibility(View.VISIBLE);*/

                    //No Data found
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText(claimsResponse.getResult().getMessage().toLowerCase().startsWith("unable") ?
                            "Unable to reach server.\nPlease try again later." :
                            getString(R.string.no_intimations_found));
                    binding.imgError.setImageDrawable(claimsResponse.getResult().getMessage().toLowerCase().startsWith("unable") ? ContextCompat.getDrawable(requireContext(), R.drawable.ic_api_error_state) : ContextCompat.getDrawable(requireContext(), R.drawable.noclaim));
                    binding.errorLayout.setVisibility(View.VISIBLE);


                }
            } else {
                binding.messageTextView.setVisibility(View.VISIBLE);
                binding.messageTextView.setText(getString(R.string.something_went_wrong));
                binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.noclaim));
                binding.errorLayout.setVisibility(View.VISIBLE);
            }

        });
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }
}