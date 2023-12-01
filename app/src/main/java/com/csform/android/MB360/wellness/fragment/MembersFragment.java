package com.csform.android.MB360.wellness.fragment;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import com.csform.android.MB360.utilities.LogMyBenefits;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentMembersBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.utilities.AlertDialogueLocationHelper;
import com.csform.android.MB360.utilities.AlertDialogueView;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.wellness.WellnessDashBoardActivity;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.healthcheckup.HealthCheckupFragmentDirections;
import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.City;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;
import com.csform.android.MB360.wellness.homehealthcare.ui.AddAddressListener;
import com.csform.android.MB360.wellness.homehealthcare.ui.MembersAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MembersFragment extends Fragment implements AddAddressListener {

    FragmentMembersBinding binding;
    View view;
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    MaterialToolbar toolbar;

    //adapters
    MembersAdapter adapter;

    NavController navController;

    List<FamilyMember> memberList = new ArrayList<>();
    Boolean POP_UP_SHOWN = false;


    public MembersFragment() {
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
        binding = FragmentMembersBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.membersCycle.showShimmer();


        // Overview
        binding.btnOverview.setOnClickListener(view -> {

            NavDirections actions = MembersFragmentDirections.actionMembersFragmentToOverviewHealthcareFragment();
            navController.navigate(actions);
        });

        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

//        String city = homeHealthCareViewModel.getSelectedCity().getValue() != null ? getCityFromUser() : homeHealthCareViewModel.getSelectedCity().getValue().getCity();


        getMembers();


        binding.txtCityName.setOnClickListener(view1 -> {
            NavDirections actions = MembersFragmentDirections.actionMembersFragmentToCitiesFragmentHHC();
            navController.navigate(actions);
        });

        getCity();
        return view;
    }

    private void getCity() {

        homeHealthCareViewModel.getSelectedCity().observe(getViewLifecycleOwner(), city -> {

            if (city != null) {
                if (city.getCity().equals("")) {
                    selectCityPopup();
                } else {
                    binding.txtCityName.setText(city.getCity());
                }
            } else {
                Toast.makeText(getContext(), "Something went wrong. ", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void selectCityPopup() {
        //when city is not selected and matched from the response
        //we have to inflate this pop up
        final Dialog alertDialog = new Dialog(requireActivity());
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));

        LayoutInflater mLayoutInflater = getLayoutInflater();
        View alertLayout = mLayoutInflater.inflate(R.layout.dialog_internet_error, null);
        alertDialog.setContentView(alertLayout);
        ImageView alertIcon = alertLayout.findViewById(R.id.alertIcon);
        alertIcon.setImageResource(R.drawable.ic_popupal);
        Button btnDismiss = alertDialog.findViewById(R.id.btnDismiss);
        alertDialog.setCancelable(false);

        btnDismiss.setText("           Select City           ");

        btnDismiss.setOnClickListener(view -> {
            alertDialog.dismiss();
            binding.txtCityName.performClick();

        });

        TextView alertMessage = alertDialog.findViewById(R.id.tvAlertMessage);
        alertMessage.setText("           Unable to get location           ");
        alertDialog.show();

        POP_UP_SHOWN = true;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void getMembers() {
        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            String empIdNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
            String groupCode = loadSessionResponse.getGroupInfoData().getGroupcode();
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                String wellnessSrNo = homeHealthCareViewModel.getWellnessSrNo();
                if (wellnessSrNo.equalsIgnoreCase("")) {
                    //for dental
                    wellnessSrNo = "10";
                }

                if (wellnessSrNo != null) {
                    homeHealthCareViewModel.getMembers(empIdNo, groupCode, wellnessSrNo);
                } else {
                    //something went wrong
                }

            });

        });

        homeHealthCareViewModel.getMembersData().observe(getViewLifecycleOwner(), membersResponse -> {
            memberList.clear();
            String service = homeHealthCareViewModel.getService().toUpperCase();

            //setup the recyclerview

            if (membersResponse != null) {
                if (membersResponse.getMessage() != null) {
                    if (membersResponse.getMessage().getStatus()) {

                        if (service.equalsIgnoreCase("POST NATAl CARE")) {
                            for (FamilyMember member : membersResponse.getFamilyMembers()) {
                                try {
                                    int age = Integer.parseInt(member.getAge());

                                    if (member.getGender().equalsIgnoreCase("female") && age >= 18) {
                                        memberList.add(member);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } else if (service.equals("ELDER CARE")) {

                            for (FamilyMember member : membersResponse.getFamilyMembers()) {
                                try {

                                    if (member.getRelationid().equalsIgnoreCase("1")
                                            || member.getRelationid().equalsIgnoreCase("2")
                                            || member.getRelationid().equalsIgnoreCase("5")
                                            || member.getRelationid().equalsIgnoreCase("6")) {
                                        memberList.add(member);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } else {
                            memberList.addAll(membersResponse.getFamilyMembers());
                        }


                        adapter = new MembersAdapter(getContext(), memberList, this);
                        binding.membersCycle.setAdapter(adapter);
                        adapter.notifyItemRangeChanged(0, memberList.size());

                        binding.membersCycle.hideShimmer();
                    }
                }
            }

        });


    }

    @Override
    public void getMember(FamilyMember familyMember) {

        //here we add address
        homeHealthCareViewModel.setSelectedPerson(familyMember);
        NavDirections actions = MembersFragmentDirections.actionMembersFragmentToAddAddressFragment(familyMember);
        navController.navigate(actions);

    }

    @Override
    public void selectPackage(FamilyMember familyMember) {
        // this function means the address is already
        // added and need to go on next page with
        // correct service name
        homeHealthCareViewModel.setSelectedPerson(familyMember);

        switch (homeHealthCareViewModel.getService()) {

            case "TRAINED ATTENDANT":
                NavDirections actions = MembersFragmentDirections.actionMembersFragmentToTrainedAttendedFragment(familyMember);
                navController.navigate(actions);
                break;
            case "DENTAL":
                NavDirections actionsDental = MembersFragmentDirections.actionMembersFragmentToDentalPackagesFragment(familyMember);
                navController.navigate(actionsDental);
                break;
            case "MEDICINE DELIVERY":
                NavDirections actionsMD = MembersFragmentDirections.actionMembersFragmentToUploadPrescriptionFragment(familyMember);
                navController.navigate(actionsMD);
                break;
            case "LONG TERM NURSING":
                NavDirections actionsLT = MembersFragmentDirections.actionMembersFragmentToLongTermFragment(familyMember);
                navController.navigate(actionsLT);
                break;
            case "SHORT TERM NURSING":
                NavDirections actionsST = MembersFragmentDirections.actionMembersFragmentToShortTermFragment(familyMember);
                navController.navigate(actionsST);
                break;
            case "PHYSIOTHERAPY":
                NavDirections actionsP = MembersFragmentDirections.actionMembersFragmentToPhysiotherapyFragment(familyMember);
                navController.navigate(actionsP);
                break;
            case "POST NATAL CARE":
                NavDirections actionsPNC = MembersFragmentDirections.actionMembersFragmentToPostNatalCareFragment(familyMember);
                navController.navigate(actionsPNC);
                break;
            case "ELDER CARE":
                NavDirections actionsEC = MembersFragmentDirections.actionMembersFragmentToElderCareFragment(familyMember);
                navController.navigate(actionsEC);
                break;
            case "DOCTOR SERVICES":
                NavDirections actionsDS = MembersFragmentDirections.actionMembersFragmentToDoctorServicesFragment(familyMember);
                navController.navigate(actionsDS);
                break;
            case "DIABETES MANAGEMENT":
                NavDirections actionsDM = MembersFragmentDirections.actionMembersFragmentToDiabetesManagementFragment(familyMember);
                navController.navigate(actionsDM);
                break;
        }
    }

    @Override
    public void selectMember(FamilyMember familyMember, int position) {
        try {
            memberList.get(position).setSelected(true);
            int index = 0;
            for (FamilyMember member : memberList
            ) {
                if (position != index) {
                    member.setSelected(false);
                }
                index++;
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            //something went wrong
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        POP_UP_SHOWN = true;
        City city = homeHealthCareViewModel.getSelectedCity().getValue();
        binding.txtCityName.setText(city.getCity());


    }
}