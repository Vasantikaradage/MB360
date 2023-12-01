package com.csform.android.MB360.insurance.enrollment.ui;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentEnrollmentSummaryBinding;
import com.csform.android.MB360.insurance.HomeFragmentDirections;
import com.csform.android.MB360.insurance.adminsetting.ui.AdminSettingViewModel;
import com.csform.android.MB360.insurance.enrollment.adapters.EnrolmentSummaryAdapter;
import com.csform.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.csform.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentSummary;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.SummaryItem;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.summary.SummaryResponse;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.utilities.LogMyBenefits;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class EnrollmentSummaryFragment extends Fragment {

    FragmentEnrollmentSummaryBinding binding;
    AdminSettingViewModel adminSettingViewModel;

    View view;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    EnrollmentViewModel enrollmentViewModel;

    LoadSessionViewModel loadSessionViewModel;
    List<SummaryItem> summaryList = new ArrayList<>();
    EnrolmentSummaryAdapter adapter;
    NavController navController;

    String gmcEmployeeSrNo = "";
    String gpaEmployeeSrNo = "";
    String gtlEmployeeSrNo = "";
    String daysCount;

    public EnrollmentSummaryFragment() {
        //required empty constructor
    }

    public EnrollmentSummaryFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {

        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEnrollmentSummaryBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);
        adminSettingViewModel = new ViewModelProvider(requireActivity()).get(AdminSettingViewModel.class);

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        viewPagerNavigationMenuHelper.hideSummaryOption();
        viewPagerNavigationMenuHelper.hideHomeButton();
      //  viewPagerNavigationMenuHelper.hide

        binding.prevPage2.setOnClickListener(v -> {
            viewPagerNavigationMenuHelper.previousPage();
        });
        //enrollment Complete
        binding.confirmButton.setOnClickListener(v -> {

            conformationDialogBox();

        });
        return view;
    }

    private void conformationDialogBox() {
         Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.confiramtion_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));


        DisplayMetrics dm = new DisplayMetrics();

        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

       /* dialog.getWindow().setLayout((int) (width * .6), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;*/
       /* params.x = 0;
        params.y = -20;
        dialog.getWindow().setAttributes(params);*/
        AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
        AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
        TextView textView=dialog.findViewById(R.id.appCompatTextView);
        //need to send the proper date here
        //call enrollment window api call for days

        adminSettingViewModel.getAdminSettingData().observe(getViewLifecycleOwner(), adminSettingResponse -> {
            if (adminSettingResponse != null) {
                String serverDate = adminSettingResponse.getGroup_Admin_Basic_Settings().getServerDate();
                String endDate = adminSettingResponse.getGroup_Window_Period_Info().getOpenEnroll_WP_Information_data().getWINDOW_PERIOD_END_DATE();
                Calendar calendarEnd = Calendar.getInstance();
                Calendar calendarStart = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);

                try {
                    calendarEnd.setTime(Objects.requireNonNull(sdf.parse(endDate + " 23:59:59")));
                    calendarStart.setTime(Objects.requireNonNull(sdf.parse(serverDate + " 00:00:00")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //end date in millis
                long millis = calendarEnd.getTimeInMillis();

                //current date in millis
                long now = calendarStart.getTimeInMillis();

                //difference of millis
                long milisInFuture = millis - now;

                if (milisInFuture > 0) {
                    long diff = calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = (hours / 24) + 1;
                    LogMyBenefits.d("days", "" + days);
                     daysCount = String.valueOf(days);
                }
            }




        });

        textView.setText(MessageFormat.format(getActivity().getString(R.string.confirmation_title_info),daysCount));

        btnSubmit.setOnClickListener(v -> {

            dialog.dismiss();
            callConforamtionApi();


        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();


        });
        dialog.setCancelable(false);
        dialog.show();

    }

    private void callConforamtionApi() {
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        if (loadSessionResponse != null) {

            List<GroupGMCPolicyEmpDependantsDatum> groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData();

            GroupGMCPolicyEmpDependantsDatum employeeData = null;


            for (GroupGMCPolicyEmpDependantsDatum listData : groupGMCPolicyEmployeeDatum) {
                if (listData.getRelationid().equals("17")) {
                    employeeData = listData;
                }
            }

            Map<String, String> summaryQueryMap = new HashMap<>();
            summaryQueryMap.put("EmpSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
            summaryQueryMap.put("GroupChildSrNo", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
            summaryQueryMap.put("OeGrpBasInfSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo());
            summaryQueryMap.put("TypeOfPolicySrno", "1");
            summaryQueryMap.put("IsConfirmed", "1");
            summaryQueryMap.put("OffEmailId", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOfficialEMailId());
            summaryQueryMap.put("Name", employeeData.getPersonName());
            summaryQueryMap.put("EmpID", employeeData.getRelationid());
            summaryQueryMap.put("IsTopupConfirmation", "0");
            summaryQueryMap.put("PI_EMPLOYEE_SR_NO_GMC", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
            summaryQueryMap.put("PI_EMPLOYEE_SR_NO_GPA", gpaEmployeeSrNo);
            summaryQueryMap.put("PI_EMPLOYEE_SR_NO_GTL", gtlEmployeeSrNo);


            enrollmentViewModel.confirmSummary(summaryQueryMap)
                    .observe(getViewLifecycleOwner(),
                            enrollmentSummaryConfirmResponse -> {
                                if (enrollmentSummaryConfirmResponse != null) {
                                    if (!enrollmentSummaryConfirmResponse.isError() && !enrollmentSummaryConfirmResponse.isLoading()) {

                                        if (enrollmentSummaryConfirmResponse.getAddDependantResponse().getStatus()) {
                                            Toast.makeText(requireContext(), enrollmentSummaryConfirmResponse.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                            playAnimation();


                                        } else {
                                            Toast.makeText(requireContext(), "" + enrollmentSummaryConfirmResponse.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (enrollmentSummaryConfirmResponse.isLoading()) {
                                        binding.llprogressbar.setVisibility(View.VISIBLE);
                                        //show loading
                                    } else if (enrollmentSummaryConfirmResponse.isError()) {
                                        //show error
                                        Toast.makeText(getActivity(), "Something went wrong try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            });
        }


    }


    private void getSummary() {

        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        if (loadSessionResponse != null) {

            if (loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo() != null) {
                gmcEmployeeSrNo = loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData().get(0).getEmployeeSrNo();

                if (loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().size() != 0) {
                    gpaEmployeeSrNo = loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGPAPolicyEmpDependantsData().get(0).getEmployeeSrNo();

                } else {
                    gpaEmployeeSrNo = "0";
                }

                if (loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().size() != 0) {
                    gtlEmployeeSrNo = loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGTLPolicyEmpDependantsData().get(0).getEmployeeSrNo();
                } else {
                    gtlEmployeeSrNo = "0";
                }

                // enrollmentViewModel.getSummaryDetails("70274", "22816", "20251", "1754", "2603").observe(getViewLifecycleOwner(), enrollmentSummaryResponse -> {
                enrollmentViewModel.getSummaryDetails(gmcEmployeeSrNo,
                        gpaEmployeeSrNo,
                        gtlEmployeeSrNo,
                        loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                        loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo()).observe(getViewLifecycleOwner(), enrollmentSummaryResponse -> {

                    if (enrollmentSummaryResponse != null) {
                        binding.llprogressbar.setVisibility(View.GONE);
                        //here we get the summary
                        //we need to build the summary here.
                        SummaryResponse summary = enrollmentSummaryResponse.getSummaryResponse();


                        //Group Sum insured
                        summaryList.add(0, new SummaryItem(0, "Group Sum Insured", getString(R.string.summary_rs), true, false));
                        if (summary.getPoGmcBaseSi() != null && !summary.getPoGmcBaseSi().equalsIgnoreCase("")) {
                            summaryList.add(new SummaryItem(1, "Health Insurance", summary.getPoGmcBaseSi(), false, false));
                        }
                    /*if (summary.getGmcBaseSiParentSet1() != null && !summary.getGmcBaseSiParentSet1().equalsIgnoreCase("")) {
                        summaryList.add(new SummaryItem(1, "Parents (1st Set)", summary.getGmcBaseSiParentSet1(), false, false));
                    }
                    if (summary.getGmcBaseSiParentSet2() != null && !summary.getGmcBaseSiParentSet2().equalsIgnoreCase("")) {
                        summaryList.add(new SummaryItem(1, "Parents (2nd Set)", summary.getGmcBaseSiParentSet2(), false, false));
                    }*/
                        if (summary.getPoGmcTopupSi() != null && !summary.getPoGmcTopupSi().equalsIgnoreCase("")) {
                            summaryList.add(new SummaryItem(1, "Health Insurance Top-up", summary.getPoGmcTopupSi(), false, false));
                        }
                        summaryList.add(new SummaryItem(2, "footer", "footer", false, true));

                        if (summary.getPoGpaBaseSi() != null && !summary.getPoGpaBaseSi().equalsIgnoreCase("")) {
                            summaryList.add(new SummaryItem(1, "Personal Accident", summary.getPoGpaBaseSi(), false, false));
                        }
                        if (summary.getPoGpaTopupSi() != null && !summary.getPoGpaTopupSi().equalsIgnoreCase("")) {
                            summaryList.add(new SummaryItem(1, "Personal Accident Top-up", summary.getPoGpaTopupSi(), false, false));
                        }
                        summaryList.add(new SummaryItem(2, "footer", "footer", false, true));

                        if (summary.getPoGtlBaseSi() != null && !summary.getPoGtlBaseSi().equalsIgnoreCase("")) {
                            summaryList.add(new SummaryItem(1, "Term Life", summary.getPoGtlBaseSi(), false, false));
                        }
                        if (summary.getPoGtlTopupSi() != null && !summary.getPoGtlTopupSi().equalsIgnoreCase("")) {
                            summaryList.add(new SummaryItem(1, "Term Life Top-up", summary.getPoGtlTopupSi(), false, false));
                        }
                        summaryList.add(new SummaryItem(2, "footer", "footer", false, true));

                        //Health insurance Parent Premium
                        summaryList.add(new SummaryItem(0, "Health Insurance Parent Premium", getString(R.string.summary_rs), true, false));

                   /* if (summary.getParent1Premium() != null && !summary.getParent1Premium().equalsIgnoreCase("")) {
                        summaryList.add(new SummaryItem(1, "Parents (1st Set)", summary.getParent1Premium(), false, false));
                    }
                    if (summary.getParent2Premium() != null && !summary.getParent2Premium().equalsIgnoreCase("")) {
                        summaryList.add(new SummaryItem(1, "Parents (2nd Set)", summary.getParent2Premium(), false, false));
                    }*/
                        summaryList.add(new SummaryItem(2, "footer", "footer", false, true));

                        //Group TOP up
                        summaryList.add(new SummaryItem(0, "Group Top-up Premium", getString(R.string.summary_rs), true, false));
                        if (summary.getPoGmcTopupPremium() != null && !summary.getPoGmcTopupPremium().equalsIgnoreCase("")) {
                            summaryList.add(new SummaryItem(1, "Health Insurance", summary.getPoGmcTopupPremium(), false, false));
                        }
                        if (summary.getPoGpaTopupPremium() != null && !summary.getPoGpaTopupPremium().equalsIgnoreCase("")) {
                            summaryList.add(new SummaryItem(1, "Personal Accident", summary.getPoGpaTopupPremium(), false, false));
                        }
                        if (summary.getPoGtlTopupPremium() != null && !summary.getPoGtlTopupPremium().equalsIgnoreCase("")) {
                            summaryList.add(new SummaryItem(1, "Term Life", summary.getPoGtlTopupPremium(), false, false));
                        }
                        summaryList.add(new SummaryItem(2, "footer", "footer", false, true));


                        //you pay
                        binding.tvValue.setText(summary.getPoTotalPremium());

                        binding.installPremium.setText(MessageFormat.format(getString(R.string.prem_inst), summary.getPoTotalPremium(), summary.getPoNoOfInstallmentGmcbase()));


                        List<SummaryItem> toRemoveArray = new ArrayList<>();


                        for (int i = 0; i < summaryList.size(); i++) {
                            if (summaryList.get(i).getId() == 0) {
                                if (summaryList.get(i + 1).getId() == 2) {
                                    //remove the both item
                                    toRemoveArray.add(summaryList.get(i));
                                    toRemoveArray.add(summaryList.get(i + 1));
                                }
                            }
                        }

                        if (!toRemoveArray.isEmpty()) {
                            for (SummaryItem position : toRemoveArray) {
                                summaryList.remove(position);
                            }
                        }

                    } else if (enrollmentSummaryResponse.getErrorState()) {
                        //something is wrong
                        Toast.makeText(getContext(), "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                        binding.llprogressbar.setVisibility(View.GONE);
                    } else if (enrollmentSummaryResponse.getLoadingState()) {
                        binding.llprogressbar.setVisibility(View.VISIBLE);
                    }


                    adapter = new EnrolmentSummaryAdapter(summaryList, requireContext());
                    binding.rvSummaryView.setAdapter(adapter);


                });

            } else {
                Toast.makeText(getActivity(), "No Data Available for Summary", Toast.LENGTH_SHORT).show();
                binding.llprogressbar.setVisibility(View.GONE);
            }


        } else {
            Toast.makeText(getActivity(), "No Data Available for Summary", Toast.LENGTH_SHORT).show();
            binding.llprogressbar.setVisibility(View.GONE);
        }
    }

    private void playAnimation() {
        binding.lottieLayerName.enableMergePathsForKitKatAndAbove(true);
        binding.lottieLayerName2.enableMergePathsForKitKatAndAbove(true);
        binding.scroll1.smoothScrollTo(0, 0);
        binding.lottieLayerName.setSpeed(0.75f);
        binding.lottieLayerName2.setSpeed(0.75f);
        binding.lottieLayerName2.playAnimation();

        binding.lottieLayerName2.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                binding.lottieLayerName.setVisibility(View.VISIBLE);
                binding.lottieLayerName.playAnimation();

                new Handler().postDelayed(() -> {
                    binding.lottieLayerName.setVisibility(View.GONE);
                }, 750);

                new Handler().postDelayed(() -> {
                    NavDirections action = EnrollmentSummaryFragmentDirections.actionEnrollmentSummaryFragmentToHomeFragment();
                    navController.navigateUp();
                }, 1500);


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getSummary();


    }
}