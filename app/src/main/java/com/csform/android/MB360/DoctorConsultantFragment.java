package com.csform.android.MB360;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.csform.android.MB360.utilities.LogMyBenefits;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.MB360.databinding.FragmentDoctorConsultantBinding;
import com.csform.android.MB360.databinding.LoadingbarBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;

import com.csform.android.MB360.utilities.LoadingWellnessDialogue;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashBoardWellnessRepository;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.dashboardwellness.repository.responseclass.EmployeeCheckResponse;
import com.csform.android.MB360.wellness.doctorconsultant.repository.DCViewModel;
import com.csform.android.MB360.wellness.doctorconsultant.responseclass.DoctorPackages;
import com.csform.android.MB360.wellness.doctorconsultant.responseclass.UserAgreement;
import com.csform.android.MB360.wellness.doctorconsultant.responseclass.UserAgreementRequest;
import com.csform.android.MB360.wellness.doctorconsultant.ui.DoctorPackagesAdapter;
import com.csform.android.MB360.wellness.doctorconsultant.ui.OnPackageBuyListener;
import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DoctorConsultantFragment extends Fragment implements OnPackageBuyListener {

    FragmentDoctorConsultantBinding binding;
    View view;
    LoadSessionViewModel loadSessionViewModel;
    DCViewModel dcViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;
    DoctorPackagesAdapter adapter;
    String extFamilySrNo = "";
    UserAgreementRequest userAgreementRequest = new UserAgreementRequest();
    String extPersonSrNo = "";
    String extEmployeeSrNo = "";
    DashboardWellnessViewModel dashboardWellnessViewModel;
    Boolean BOTTOM_SHEET_SHOW = false;
    NavController navController;
    Boolean WEBVIEW_OPEN = false;
    LoadingWellnessDialogue loadingWellnessDialogue;


    public DoctorConsultantFragment() {
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

        binding = FragmentDoctorConsultantBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadingWellnessDialogue = new LoadingWellnessDialogue(requireContext(), requireActivity());

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dcViewModel = new ViewModelProvider(requireActivity()).get(DCViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);


        getDCpackage();

        dcViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                loadingWellnessDialogue.showLoading("");
            } else {
                binding.progressBar.setVisibility(View.GONE);
                loadingWellnessDialogue.hideLoading();
            }
        });


        dcViewModel.buyPackageData().observe(getViewLifecycleOwner(), dcPackageResponse -> {
            if (dcPackageResponse != null) {
                if (dcPackageResponse.getStatus()) {
                    if (!WEBVIEW_OPEN) {
                        NavDirections action = DoctorConsultantFragmentDirections.actionDoctorConsultantFragmentToDoctorConsultantPackagesWebView();
                        navController.navigate(action);
                        WEBVIEW_OPEN = true;
                    }

                }
            } else {

            }
        });


        return view;
    }

    public void getDCpackage() {
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        EmployeeCheckResponse employeeCheckResponse = dashboardWellnessViewModel.getEmployeeWellnessDetailsData().getValue();
        extFamilySrNo = employeeCheckResponse.getExtFamilySrNo();
        userAgreementRequest.setGroupChildSrNo(employeeCheckResponse.getExtGroupSrNo());
        extFamilySrNo = employeeCheckResponse.getExtFamilySrNo();
        extEmployeeSrNo = employeeCheckResponse.getExtFamilySrNo();

        userAgreementRequest.setExtEmpSrNo(extEmployeeSrNo);
        userAgreementRequest.setVendorSrNo("1");
        userAgreementRequest.setGroupCode(loadSessionResponse.getGroupInfoData().getGroupcode());


        if (extFamilySrNo != null) {
            dcViewModel.getPackages(extFamilySrNo, "1")
                    .observe(getViewLifecycleOwner(), dcPacks -> {


                        adapter = new DoctorPackagesAdapter(dcPacks.getData(), requireContext(), this);
                        binding.dcRecyclerView.setAdapter(adapter);
                    });
        } else {
            //something went wrong
        }


    }

    private void callPackageAgreement(Context context) {


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.buy_now_package_bottom_view);

        TextView buyNowUserAgeConfirmBtn = bottomSheetDialog.findViewById(R.id.buyNowUserAgeConfirmBtn);
        CheckBox checkBoxBtn = bottomSheetDialog.findViewById(R.id.checkBoxBtn);
        ImageView closeImg = bottomSheetDialog.findViewById(R.id.closeImg);
        AppCompatTextView buyNowTermsCondition = bottomSheetDialog.findViewById(R.id.buyNowTermsCondition);

        closeImg.setOnClickListener(v -> bottomSheetDialog.dismiss());

        buyNowTermsCondition.setOnClickListener(v -> {
            //bottomSheetDialog.dismiss();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mybenefits360.com/wellness/termsOfUse.aspx"));
            v.getContext().startActivity(browserIntent);
        });

        buyNowUserAgeConfirmBtn.setOnClickListener(v -> {
            //bottomSheetDialog.dismiss();
            if (checkBoxBtn.isChecked()) {

                dcViewModel.acceptUserAgreement(userAgreementRequest).observe(getViewLifecycleOwner(), acceptUserAgreement -> {
                    if (acceptUserAgreement != null && acceptUserAgreement.getStatus()) {
                        bottomSheetDialog.dismiss();
                    }
                });

            } else {
                Toast.makeText(v.getContext(), "Please accept Terms of Use", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                BOTTOM_SHEET_SHOW = false;
            }
        });

        bottomSheetDialog.show();
    }

    @Override
    public void onPackageBuyListener(DoctorPackages packages) {

        dcViewModel.isUserAgreed(extFamilySrNo, "1").observe(getViewLifecycleOwner(), userAgreement -> {
            if (userAgreement != null) {
                LogMyBenefits.d(LogTags.DOCTOR_CONSULTANT, "onPackageBuyListener: " + userAgreement.toString());
                if (userAgreement.getIsDCAgree()) {
                    WEBVIEW_OPEN = false;
                    extEmployeeSrNo = "" + packages.getExtEmployeeSrNo();
                    String ext_emp_sr_no = "" + packages.getExtEmployeeSrNo();
                    String ext_per_sr_no = "" + packages.getExtPerSrNo();
                    if (ext_emp_sr_no.isEmpty()) {
                        //we dont get the ext emp sr in package
                        //we will use ext family serial number from employee wellness details
                        ext_emp_sr_no = extFamilySrNo;
                    }

                    if (ext_per_sr_no.isEmpty()) {
                        //we dont get the ext emp sr in package
                        //we will use ext family serial number from employee wellness details
                        //get the trained attendant get members call
                        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                        if (loadSessionResponse != null) {

                            homeHealthCareViewModel.getMembers(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo(),
                                    loadSessionResponse.getGroupInfoData().getGroupcode(),
                                    "10").observe(getViewLifecycleOwner(), membersResponse -> {
                                if (membersResponse != null) {
                                    try {
                                        extPersonSrNo = membersResponse.getFamilyMembers().get(0).getExtPersonSrNo();
                                        dcViewModel.buyDcPackage(membersResponse.getFamilyMembers().get(0).getExtPersonSrNo(), extFamilySrNo, packages.getPackagePlanSrNo());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(requireContext(), "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            //we dont have the loadsession
                            //something went wrong
                        }

                    }


                } else {
                    if (!BOTTOM_SHEET_SHOW) {
                        callPackageAgreement(requireContext());
                        BOTTOM_SHEET_SHOW = true;

                    }
                }
            }
        });
    }
}