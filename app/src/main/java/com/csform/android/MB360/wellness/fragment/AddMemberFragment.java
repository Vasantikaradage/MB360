package com.csform.android.MB360.wellness.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import com.csform.android.MB360.utilities.LogMyBenefits;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentAddMemberBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.monthpicker.DatePickerTheme;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.healthcheckup.repository.requestclass.DependentRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.requestclass.ScheduleRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Relation;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.ui.PackageAdapter;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.ui.PersonAdapter;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.ui.RelationSpinnerAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddMemberFragment extends BottomSheetDialogFragment {
    FragmentAddMemberBinding binding;
    View view;
    NavController navController;
    DependentRequest dependentRequest = new DependentRequest();

    //PackageLT ViewModel
    LoadSessionViewModel loadSessionViewModel;
    PackagesViewModel packagesViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    PrimeCalendar DOBDate = new CivilCalendar();

    RelationSpinnerAdapter adapter;

    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    BottomSheetBehavior bottomSheetBehavior;

    public AddMemberFragment() {
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
        binding = FragmentAddMemberBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        // Spinner Custom Adapter
        binding.spinnerRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Relation relation = (Relation) adapterView.getItemAtPosition(i);

                dependentRequest.setRelationID("" + relation.getRelationId());

                if (relation.getRelationName().equalsIgnoreCase("Brother")
                        || relation.getRelationName().equalsIgnoreCase("Brother-In-Law")
                        || relation.getRelationName().equalsIgnoreCase("Friend")
                        || relation.getRelationName().equalsIgnoreCase("Nephew")
                        || relation.getRelationName().equalsIgnoreCase("Son")
                        || relation.getRelationName().equalsIgnoreCase("Son-In-Law")
                ) {
                    dependentRequest.setGender("Male");

                } else if (relation.getRelationName().equalsIgnoreCase("Daughter")
                        || relation.getRelationName().equalsIgnoreCase("Daughter-In-Law")
                        || relation.getRelationName().equalsIgnoreCase("Niece")
                        || relation.getRelationName().equalsIgnoreCase("Sister")
                        || relation.getRelationName().equalsIgnoreCase("Sister-In-Law")
                ) {
                    dependentRequest.setGender("Female");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);

       /* bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        bottomSheetBehavior.setDraggable(true);*/

        //  ViewGroup.LayoutParams layoutParams = binding.bottomSheet.getLayoutParams();


       /* int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }*/
        // binding.bottomSheet.setLayoutParams(layoutParams);
        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        /*bottomSheetBehavior.setFitToContents(true);*/


        binding.etname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    binding.etname.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    binding.etage.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.dobTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty() && binding.ckdateofbirth.isChecked()) {
                    binding.dobTV.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnAddMember.setOnClickListener(view -> {

            confirmAddMember();
        });

        binding.etdateofbirth.setOnClickListener(view -> {

            DOBDate();
        });



        setupSpinner();


//        if (isSelected()) {
//
//            binding.ckdateofbirth.setChecked(true);
//            binding.etdateofbirth.setVisibility(View.GONE);
//
//        } else {
//            binding.ckdateofbirth.setChecked(false);
//            binding.etdateofbirth.setVisibility(View.VISIBLE);
//        }
//
        binding.ckdateofbirth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    LogMyBenefits.d("TAG=>isChecked", "True");//replace your own stuffs here
                    binding.dobTV.setVisibility(View.GONE);
                    binding.etdateofbirth.setVisibility(View.GONE);
//                binding.etdateofbirth.getText().clear();
                    binding.etage.getText().clear();


                } else {
                    LogMyBenefits.d("TAG=>isChecked", "false");//replace your own stuffs here
                    binding.dobTV.setVisibility(View.VISIBLE);
                    binding.etdateofbirth.setVisibility(View.VISIBLE);
                }
            }
        });
//
        return view;
    }

    public void DOBDate() {

        Calendar calendar = Calendar.getInstance();
        SingleDayPickCallback callback = fromDate -> {

            DOBDate = fromDate.clone();
            SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            binding.etdateofbirth.setText(dateRangeFormat.format(DOBDate.getTime()));
            binding.etage.setText(UtilMethods.getAge(dateRangeFormat.format(DOBDate.getTime())));

        };
        PrimeCalendar today = new CivilCalendar();
        PrimeCalendar tomorrow = new CivilCalendar();
        tomorrow.add(Calendar.DATE, 0);


        LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

        PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                .pickSingleDay(callback)
                .initiallyPickedSingleDay(DOBDate)
                .maxPossibleDate(today)
                .applyTheme(themeFactory)
                .build();

        datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
    }


    private void confirmAddMember() {

        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                dependentRequest.setFamilySrNo(employeeCheckResponse.getExtFamilySrNo());
//                dependentRequest.setFamilySrNo("5625");

            });
        });

        dependentRequest.setPersonName("" + binding.etname.getText().toString());
        dependentRequest.setAge("" + binding.etage.getText().toString());

        if (binding.ckdateofbirth.isChecked()) {
            dependentRequest.setDateOfBirth("10/10/1990");
        } else {
            dependentRequest.setDateOfBirth("" + binding.etdateofbirth.getText().toString());
        }

        if (checkValidInput()) {

            packagesViewModel.dependent(dependentRequest).observe(getViewLifecycleOwner(), messageResponseDependent -> {
                LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "confirmAppointment: " + messageResponseDependent.toString());
                Toast.makeText(getActivity(), "Add Member Successfully", Toast.LENGTH_LONG).show();
                requireActivity().onBackPressed();
            });
        }


    }

    private boolean checkValidInput() {
        String name = binding.etname.getText().toString().trim();
        String dob = binding.dobTV.getText().toString().trim();
        String age = binding.etage.getText().toString().trim();
        boolean age_check_box = binding.ckdateofbirth.isChecked();

        if (dob.isEmpty() && !age_check_box && !age.isEmpty()) {
            binding.ckdateofbirth.setError("Please fill all the details!");
            return false;
        }
        if (name.isEmpty()) {
            binding.etname.setError("Name can't be empty");
            return false;
        }
        if (age_check_box && age.isEmpty()) {
            binding.etage.setError("Age can't be empty");
            return false;
        }
        if (!age_check_box && age.isEmpty()) {
            binding.etage.setError("Age can't be empty");
            return false;
        }
        return true;


    }

    private void setupSpinner() {

        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                packagesViewModel.getAllRelation(employeeCheckResponse.getExtFamilySrNo()).observe(getViewLifecycleOwner(), allRelationResponse -> {

//                    ArrayAdapter adRelation = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.tvspinneritem, allRelationResponse.getRelations());
//                    binding.spinnerRelation.setAdapter(adRelation);

                    adapter = new RelationSpinnerAdapter(getContext(), allRelationResponse.getRelations());
                    binding.spinnerRelation.setAdapter(adapter);


                });
//                dependentRequest.setFamilySrNo("5625");

            });
        });

    }

}
