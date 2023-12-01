package com.csform.android.MB360.insurance.enrollment.ui;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentGmcTopUpBinding;
import com.csform.android.MB360.insurance.enrollment.adapters.TopUpSumInsuredAdapter;
import com.csform.android.MB360.insurance.enrollment.interfaces.TopUpSelected;
import com.csform.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.csform.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.Si;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUpPlanMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUpSiMainResponse;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.TopUpItemTouchHelper;
import com.csform.android.MB360.utilities.WindowPeriodCounter;


import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;


public class TopUpFragment extends Fragment implements TopUpSelected, TopUpItemTouchHelper.RecyclerItemTouchHelperListener {

    FragmentGmcTopUpBinding binding;
    View view;
    EnrollmentViewModel enrollmentViewModel;
    LoadSessionViewModel loadSessionViewModel;

    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;

    String page = "";
    boolean isWindowPeriodActive = false;

    int animation_count = 0;
    boolean animations = true;
    TopUpSumInsuredAdapter topUpSumInsuredAdapter;

    List<Si> topUpInsuredResponseList;

    public TopUpFragment() {
        // Required empty public constructor
    }

    public TopUpFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper, String page) {
        // Required empty public constructor
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;
        this.page = page;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGmcTopUpBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        binding.lblInst.setText(MessageFormat.format(getString(R.string.lblTopupQuery), page));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getWindowPeriodTimer(page);

    }

    private void getWindowPeriodTimer(String page) {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();

        if (windowPeriod != null) {

            String windowPeriodEndDate = "";
            if (page.equalsIgnoreCase("gmc")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGmc();
            } else if (page.equalsIgnoreCase("gpa")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGpa();
            } else if (page.equalsIgnoreCase("gtl")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGtl();

            }

            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriodEndDate, requireContext(), requireActivity());

            //  //counter comment
            try {
                CountDownTimer timer = windowPeriodCounter.getTimer(binding.timerTopUpDetails);
                if (timer != null) {
                    isWindowPeriodActive = true;
                    timer.start();
                } else {
                    isWindowPeriodActive = false;
                    binding.timerTopUpDetails.setText("Window period has expired");
                }
            } catch (ParseException e) {
                //unable to parse date
                isWindowPeriodActive = false;
                Toast.makeText(requireContext(), "Unable to retrieve window period date.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            //no window period available
        }
    }

    private void getTopUP(String type_of_policy) {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {

            String windowPeriodEndDate = "";
            if (page.equalsIgnoreCase("gmc")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGmc();
            } else if (page.equalsIgnoreCase("gpa")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGpa();
            } else if (page.equalsIgnoreCase("gtl")) {
                windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGtl();

            }

            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriodEndDate, requireContext(), requireActivity());
            try {
                CountDownTimer timer = windowPeriodCounter.countDownTimer(false);
                if (timer != null) {
                    isWindowPeriodActive = true;
                } else {
                    isWindowPeriodActive = false;
                }
            } catch (ParseException e) {
                isWindowPeriodActive = false;
                Toast.makeText(requireContext(), "Unable to parse window period date.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();


            try {
                switch (type_of_policy) {
                    case "GMC":
                        if (loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().size() != 0) {

/*
                            String oegrpGMCNo=null;

                            List<GroupPolicyData> list=loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData();
                            GroupPolicyData groupPolicyData;
                            for (GroupPolicyData listData : list) {
                                if(listData.getPolicyType().equalsIgnoreCase("TOPUP")) {
                                    groupPolicyData=listData;
                                    oegrpGMCNo = groupPolicyData.getOeGrpBasInfSrNo();
                                    LogMyBenefits.d("topuup","topuup GMC"+oegrpGMCNo);
                                }
                            }*/
                            enrollmentViewModel.getTopUpSumInsured(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo(),
                                    loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                                    loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(), page.toUpperCase()).observe(getViewLifecycleOwner(), topUpSumInsuredResponse -> {

                                //set data to adapter
                                topupSumInsuredData(topUpSumInsuredResponse);
                            });
                        } else {
                            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "GPA":

                        String oegrpGPANo=null;

                        List<GroupPolicyData> groupGPAPoliciesData=loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData();
                        GroupPolicyData groupPolicyData;
                        for (GroupPolicyData listData : groupGPAPoliciesData) {
                            if(listData.getPolicyType().equalsIgnoreCase("TOPUP")) {
                                groupPolicyData=listData;
                                oegrpGPANo = groupPolicyData.getOeGrpBasInfSrNo();
                                LogMyBenefits.d("topuup","topuup GPA"+oegrpGPANo);
                            }
                        }
                        if (loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().size() != 0) {
                            Toast.makeText(getActivity(), "" + loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(), Toast.LENGTH_SHORT).show();
                            enrollmentViewModel.getTopUpSumInsured(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getEmployeeSrNo(),
                                    loadSessionResponse.getGroupInfoData().getGroupchildsrno(),oegrpGPANo
                                    /*loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getOeGrpBasInfSrNo()*/, page.toUpperCase()).observe(getViewLifecycleOwner(), topUpSumInsuredResponse -> {
                                //set data to adapter
                                topupSumInsuredData(topUpSumInsuredResponse);
                            });
                        } else {
                            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();

                        }

                        break;

                    case "GTL":
                        String oegrpGTLNo=null;

                        List<GroupPolicyData> groupGTLPoliciesData=loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData();
                        GroupPolicyData groupPolicyData1;
                        for (GroupPolicyData listData : groupGTLPoliciesData) {
                            if(listData.getPolicyType().equalsIgnoreCase("TOPUP")) {
                                groupPolicyData1=listData;

                                oegrpGTLNo = groupPolicyData1.getOeGrpBasInfSrNo();
                                LogMyBenefits.d("topuup","topuup GTL"+oegrpGTLNo);
                            }
                        }
                        if (loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().size() != 0) {

                            enrollmentViewModel.getTopUpSumInsured(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getEmployeeSrNo(),
                                    loadSessionResponse.getGroupInfoData().getGroupchildsrno(),oegrpGTLNo
                                    /*loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getOeGrpBasInfSrNo()*/, page.toUpperCase()).observe(getViewLifecycleOwner(), topUpSumInsuredResponse -> {
                                //set data to adapter
                                topupSumInsuredData(topUpSumInsuredResponse);
                            });
                        } else {
                            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();

                        }
                        break;


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(requireContext(), "Unable to retrieve window period", Toast.LENGTH_SHORT).show();
        }


    }

    private void topupSumInsuredData(TopUpSiMainResponse topUpSiMainResponse) {
        if (topUpSiMainResponse.getTopUpResponseData() != null) {

            if (!topUpSiMainResponse.getLoadingState() && !topUpSiMainResponse.getErrorState()) {

                topUpInsuredResponseList = topUpSiMainResponse.getTopUpResponseData().getSiList();

                topUpSumInsuredAdapter = new TopUpSumInsuredAdapter(requireContext(), topUpInsuredResponseList, this, isWindowPeriodActive, animations);
                binding.spnTopUpamt.setAdapter(topUpSumInsuredAdapter);
                topUpSumInsuredAdapter.notifyDataSetChanged();

                if (isWindowPeriodActive) {
                    ItemTouchHelper.SimpleCallback itemTouchHelperCallbackGMC = new TopUpItemTouchHelper(0, ItemTouchHelper.LEFT, this, topUpInsuredResponseList);
                    new ItemTouchHelper(itemTouchHelperCallbackGMC).attachToRecyclerView(binding.spnTopUpamt);
                    topUpSumInsuredAdapter.notifyDataSetChanged();
                } else {
                    //do nothing

                }
            } else if (topUpSiMainResponse.getLoadingState()) {
                //show loading
                binding.progressBar.setVisibility(View.VISIBLE);

            } else if (topUpSiMainResponse.getErrorState()) {
                //show the error
                Toast.makeText(getContext(), "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();

            }
        } else {
            // 500 error
            Toast.makeText(getContext(), "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnTopUpSelected(Si topUp, int position) {
        //here we mock the data that user selected the
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        try {
            if (loadSessionResponse != null) {

                String amount = topUp.getSumInsuredWoComma();
                String premiumValue = topUp.getPremiumValue().replace(",", "");
                switch (page.toUpperCase()) {
                    case "GMC":

                       /*
                            String oegrpGMCNo=null;

                            List<GroupPolicyData> list=loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData();
                            GroupPolicyData groupPolicyData;
                            for (GroupPolicyData listData : list) {
                                if(listData.getPolicyType().equalsIgnoreCase("TOPUP")) {
                                    groupPolicyData=listData;
                                    oegrpGMCNo = groupPolicyData.getOeGrpBasInfSrNo();
                                    LogMyBenefits.d("topuup","topuup GMC"+oegrpGMCNo);
                                }
                            }*/
                        enrollmentViewModel.getTopUpGMCPlanResponse(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo(),
                                loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                                loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(), amount, premiumValue).observe(getViewLifecycleOwner(), topUpPlanResponse -> {


                                    if (topUpPlanResponse.getTopUpPlanResponse() != null) {
                                // choose topup plan for gmc
                                chooseTopupPlan(topUpPlanResponse, topUp, position, true);
                                topUpSumInsuredAdapter.notifyItemChanged(position);
                            }

                        });

                        break;

                    case "GPA":

                        String oegrpGPANo=null;

                        List<GroupPolicyData> groupGPAPoliciesData=loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData();
                        GroupPolicyData groupPolicyData;
                        for (GroupPolicyData listData : groupGPAPoliciesData) {
                            if(listData.getPolicyType().equalsIgnoreCase("TOPUP")) {
                                groupPolicyData=listData;
                                oegrpGPANo = groupPolicyData.getOeGrpBasInfSrNo();
                                LogMyBenefits.d("topuup","topuup GPA"+oegrpGPANo);
                            }
                        }
                        enrollmentViewModel.getTopUpGPAPlanResponse(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getEmployeeSrNo(),
                                loadSessionResponse.getGroupInfoData().getGroupchildsrno(),oegrpGPANo
                                /*loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getOeGrpBasInfSrNo()*/, amount, premiumValue).observe(getViewLifecycleOwner(), topUpPlanResponse -> {
                            if (topUpPlanResponse.getTopUpPlanResponse() != null) {
                                // choose topup plan for gpa
                                chooseTopupPlan(topUpPlanResponse, topUp, position, true);
                                topUpSumInsuredAdapter.notifyItemChanged(position);
                            }


                        });

                        break;

                    case "GTL":


                        String oegrpGTLNo=null;

                        List<GroupPolicyData> groupGTLPoliciesData=loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData();
                        GroupPolicyData groupPolicyData1;
                        for (GroupPolicyData listData : groupGTLPoliciesData) {
                            if(listData.getPolicyType().equalsIgnoreCase("TOPUP")) {
                                groupPolicyData1=listData;

                                oegrpGTLNo = groupPolicyData1.getOeGrpBasInfSrNo();
                                LogMyBenefits.d("topuup","topuup GTL"+oegrpGTLNo);
                            }
                        }
                        enrollmentViewModel.getTopUpGTLPlanResponse(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getEmployeeSrNo(),
                                loadSessionResponse.getGroupInfoData().getGroupchildsrno(),oegrpGTLNo
                                /*loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getOeGrpBasInfSrNo()*/, amount, premiumValue).observe(getViewLifecycleOwner(), topUpPlanResponse -> {
                            if (topUpPlanResponse.getTopUpPlanResponse() != null) {
                                // choose topup plan for gtl
                                chooseTopupPlan(topUpPlanResponse, topUp, position, true);
                                topUpSumInsuredAdapter.notifyItemChanged(position);
                            }

                        });

                        break;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void chooseTopupPlan(TopUpPlanMainResponse topUpPlanResponse, Si topUp, int position, boolean selected) {
        if (!topUpPlanResponse.getLoadingState() && !topUpPlanResponse.getErrorState()) {

            if (!topUpPlanResponse.getTopUpPlanResponse().getStatus()) {
                Toast.makeText(getContext(), "" + topUpPlanResponse.getTopUpPlanResponse().getMessage(), Toast.LENGTH_SHORT).show();
            } else {

                for (Si topUpValue : topUpInsuredResponseList) {
                    if (!topUpValue.equals(topUp)) {
                        topUpValue.setSelected(false);
                        topUpSumInsuredAdapter.notifyItemChanged(topUpInsuredResponseList.indexOf(topUpValue));
                        topUpSumInsuredAdapter.notifyItemChanged(position);
                        topUpSumInsuredAdapter.notifyDataSetChanged();
                        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackGMC = new TopUpItemTouchHelper(0, ItemTouchHelper.LEFT, this, topUpInsuredResponseList);
                        new ItemTouchHelper(itemTouchHelperCallbackGMC).attachToRecyclerView(binding.spnTopUpamt);
                    } else {
                        topUpValue.setSelected(true);
                        topUpSumInsuredAdapter.notifyItemChanged(topUpInsuredResponseList.indexOf(topUpValue));
                        topUpSumInsuredAdapter.notifyItemChanged(position);
                        topUpSumInsuredAdapter.notifyDataSetChanged();
                        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackGMC = new TopUpItemTouchHelper(0, ItemTouchHelper.LEFT, this, topUpInsuredResponseList);
                        new ItemTouchHelper(itemTouchHelperCallbackGMC).attachToRecyclerView(binding.spnTopUpamt);

                    }
                }

                topUpInsuredResponseList.get(position).setSelected(selected);
                topUpSumInsuredAdapter.notifyItemChanged(position);
                ItemTouchHelper.SimpleCallback itemTouchHelperCallbackGMC = new TopUpItemTouchHelper(0, ItemTouchHelper.LEFT, this, topUpInsuredResponseList);
                new ItemTouchHelper(itemTouchHelperCallbackGMC).attachToRecyclerView(binding.spnTopUpamt);
            }
        } else if (topUpPlanResponse.getLoadingState()) {
            //show loading
            binding.progressBar.setVisibility(View.VISIBLE);

        } else if (topUpPlanResponse.getErrorState()) {
            //show the error
            Toast.makeText(getContext(), "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (animation_count == 0) {
            animations = true;

            Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_down);
            binding.titleIcon.startAnimation(animation);
            binding.lblTopupType.startAnimation(animation);
            binding.timerHolder.startAnimation(animation);
            binding.lblInst.startAnimation(animation);

            binding.titleIcon.setVisibility(View.VISIBLE);
            binding.lblTopupType.setVisibility(View.VISIBLE);
            binding.timerHolder.setVisibility(View.VISIBLE);
            binding.lblInst.setVisibility(View.VISIBLE);

        } else {
            animations = false;

            binding.titleIcon.setVisibility(View.VISIBLE);
            binding.lblTopupType.setVisibility(View.VISIBLE);
            binding.timerHolder.setVisibility(View.VISIBLE);
            binding.lblInst.setVisibility(View.VISIBLE);
        }
        switch (page) {
            case "GMC":
                binding.titleIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_gmctopup));
                binding.lblTopupType.setText("GMC Top-Up");
                //get the topupsdata
                getTopUP(page);
                break;
            case "GPA":
                binding.titleIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_gpa_sheild));
                binding.lblTopupType.setText("GPA Top-Up");
                //get the topupsdata
                getTopUP(page);
                break;
            case "GTL":
                binding.titleIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_gtl_sheild));
                binding.lblTopupType.setText("GTL Top-Up");
                //get the topupsdata
                getTopUP(page);
                break;


        }
        animations = false;
        animation_count++;


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.topup_item_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));


        DisplayMetrics dm = new DisplayMetrics();

        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        dialog.getWindow().setLayout((int) (width * .6), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        dialog.getWindow().setAttributes(params);
        AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
        AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);

        btnSubmit.setOnClickListener(v -> {
            topUpInsuredResponseList.get(position).setSelected(false);
            deleteItem(position);
            dialog.dismiss();
            topUpSumInsuredAdapter.notifyItemChanged(position);


        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
            topUpSumInsuredAdapter.notifyItemChanged(position);

        });
        dialog.setCancelable(false);
        dialog.show();


    }

    private void deleteItem(int position) {


        Si topUp = topUpInsuredResponseList.get(position);
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        try {
            if (loadSessionResponse != null) {

                String amount = "0";
                String premiumValue = "0";
                switch (page.toUpperCase()) {
                    case "GMC":
                        enrollmentViewModel.getTopUpGMCPlanResponse(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo(),
                                loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                                loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(), amount, premiumValue).observe(getViewLifecycleOwner(), topUpPlanResponse -> {
                            if (topUpPlanResponse.getTopUpPlanResponse() != null) {
                                // choose topup plan for gmc
                                chooseTopupPlan(topUpPlanResponse, topUp, position, false);
                                topUpSumInsuredAdapter.notifyDataSetChanged();

                            }

                        });

                        break;

                    case "GPA":
                        enrollmentViewModel.getTopUpGPAPlanResponse(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getEmployeeSrNo(),
                                loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                                loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(), amount, premiumValue).observe(getViewLifecycleOwner(), topUpPlanResponse -> {
                            if (topUpPlanResponse.getTopUpPlanResponse() != null) {

                                // choose topup plan for gpa
                                chooseTopupPlan(topUpPlanResponse, topUp, position, false);
                                topUpSumInsuredAdapter.notifyDataSetChanged();

                            }

                        });

                        break;

                    case "GTL":
                        enrollmentViewModel.getTopUpGTLPlanResponse(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getEmployeeSrNo(),
                                loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                                loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(), amount, premiumValue).observe(getViewLifecycleOwner(), topUpPlanResponse -> {
                            if (topUpPlanResponse.getTopUpPlanResponse() != null) {

                                // choose topup plan for gtl
                                chooseTopupPlan(topUpPlanResponse, topUp, position, false);
                                topUpSumInsuredAdapter.notifyDataSetChanged();

                            }

                        });

                        break;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}