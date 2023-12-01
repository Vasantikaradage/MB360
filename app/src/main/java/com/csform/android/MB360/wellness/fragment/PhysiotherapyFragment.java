package com.csform.android.MB360.wellness.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.csform.android.MB360.utilities.LogMyBenefits;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentLongTermNursingBinding;
import com.csform.android.MB360.databinding.FragmentPhysiotherapyBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.monthpicker.DatePickerTheme;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackagePT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class PhysiotherapyFragment extends Fragment {

    FragmentPhysiotherapyBinding binding;
    View view;
    NavController navController;

    // View Model
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    ArrayList<PackagePT> packagesList;

    PackagePT appointment = new PackagePT();
    Appointment appointmentRequest = new Appointment();

    Calendar calendar;
    PrimeCalendar Date = new CivilCalendar();
    ArrayList<PrimeCalendar> dailDateList = new ArrayList<>();

    public static String SERVICE_COUNT_ONE = "1";
    public static String SERVICE_COUNT_TWO = "2";

    int DAY_COUNT = 10;

    String time = "";
    String ampm = "am";

    Boolean DATE_SET = false;

    public PhysiotherapyFragment() {
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
        binding = FragmentPhysiotherapyBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);

        binding.btnReview.setEnabled(false);

        //todo remove testing location
        appointment.setHhcCityMappSrNo((homeHealthCareViewModel.getSelectedCity().getValue().getSrno()));


        //common request attributes
        appointmentRequest.setRej_appt_sr_no("-1");
        appointmentRequest.setIs_rescheduled(0);

        //initial date that changes later
        appointmentRequest.setFrom_date("01-01-1990");
        appointmentRequest.setTo_date("01-01-1990");
        appointmentRequest.setDate_preference("01-01-1990");

        //get person and familysr no
        dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {
            appointmentRequest.setFamily_sr_no(employeeCheckResponse.getExtFamilySrNo());
        });

        homeHealthCareViewModel.getSelectedPerson().observe(getViewLifecycleOwner(), person -> {
            appointmentRequest.setPerson_sr_no(person.getExtPersonSrNo());
            binding.dependantName.setText(person.getPersonName());
            String personage = person.getRelationName() + "(" + person.getAge() + " years)";
            binding.txtRelationAge.setText(personage);
        });

        //setting the service name
        appointmentRequest.setService(homeHealthCareViewModel.getService());

        //get the common packages!
        getNAPackages();

        // to disable sundays as that library doesn't supports sundays disable;
        PrimeCalendar sunday;
        List<PrimeCalendar> weekends = new ArrayList<>();
        int weeks = 1000;
        for (int i = 0; i < (weeks * 7); i = i + 7) {
            sunday = new CivilCalendar();
            sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
            weekends.add(sunday);
        }

        //per Day
        binding.perDay.setOnClickListener(v -> {
            if (binding.tenDays.isSelected()) {
                DATE_SET = false;
            }
            binding.perDay.setSelected(true);
            binding.tenDays.setSelected(false);

            binding.llMonthSelection.setVisibility(View.VISIBLE);

            binding.dailyDateLayout.setVisibility(View.VISIBLE);
            binding.txtDate1.setVisibility(View.VISIBLE);
            binding.dateRange.setVisibility(View.GONE);

            binding.txtToDate.setText("");
            binding.txtFromDate.setText("");
            binding.txtDate1.setText("");

            appointment.setCategory("PER DAY");


            // Date Condition
            appointmentRequest.setDate_condition(1);

            //filter package
            getPackage();
        });

        //ten Day
        binding.tenDays.setOnClickListener(v -> {
            if (binding.perDay.isSelected()) {
                DATE_SET = false;
            }
            binding.perDay.setSelected(false);
            binding.tenDays.setSelected(true);

            binding.llMonthSelection.setVisibility(View.VISIBLE);

            binding.dailyDateLayout.setVisibility(View.GONE);
            binding.txtDate1.setVisibility(View.GONE);
            binding.dateRange.setVisibility(View.VISIBLE);

            binding.txtToDate.setText("");
            binding.txtFromDate.setText("");
            binding.txtDate1.setText("");

            appointment.setCategory("10 DAYS");


            // Date Condition
            appointmentRequest.setDate_condition(2);

            //filter package
            getPackage();

            // 10 Day date range
            binding.txtFromDate.setOnClickListener(view -> {

                SingleDayPickCallback callback = fromDate -> {


                    Date = fromDate.clone();
                    SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    Calendar toDate = Calendar.getInstance();
                    toDate.setTime(Date.getTime());
//                toDate.add(Calendar.MONTH, MONTH_COUNT);
                    toDate.add(Calendar.DAY_OF_MONTH, DAY_COUNT);


                    binding.txtFromDate.setText(dateRangeFormat.format(Date.getTime()));

                    binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));
                    DATE_SET = true;

                    //set the appointment  request
                    appointmentRequest.setFrom_date(dateRangeFormat.format(Date.getTime()));
                    appointmentRequest.setTo_date(dateRangeFormat.format(toDate.getTime()));
                    appointmentRequest.setDate_preference("01-01-1990");
                    appointmentRequest.setTime_preference("10:00 AM");

                    //filter package
                    getPackage();
                };

                PrimeCalendar today = new CivilCalendar();
                PrimeCalendar tomorrow = new CivilCalendar();
                tomorrow.add(Calendar.DATE, 1);


                LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

                PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                        .pickSingleDay(callback)
                        .initiallyPickedSingleDay(Date)
                        .disabledDays(weekends)
                        .minPossibleDate(today)
                        .applyTheme(themeFactory)
                        .build();

                datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");


            });

        });


        //calendars
        binding.dailyDateLayout.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            SingleDayPickCallback callback = fromDate -> {

                Date = fromDate.clone();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                StringBuilder dateString = new StringBuilder();
                StringBuilder displayDateBuilder = new StringBuilder();
                for (PrimeCalendar date : dailDateList) {
                    dateString.append(dateFormat.format(date.getTime())).append(",");
                    displayDateBuilder.append(dateFormat.format(date.getTime())).append("\n");
                }
                String date = dateString.toString().replaceFirst(".$", "");

                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(),
                        (view, hourOfDay, minute1) -> {
                            // on below line we are setting selected time
                            // in our text view.
                            //am pm logic
                            switch (hourOfDay) {
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                case 12:
                                    ampm = "am";
                                    if (minute1 <= 9) {
                                        time = hourOfDay + ":0" + minute1;
                                    } else {
                                        time = hourOfDay + ":" + minute1;
                                    }
                                    DATE_SET = true;
                                    getPackage();
                                    break;
                                default:
                                    ampm = "pm";
                                    if (minute1 <= 9) {
                                        time = String.format(Locale.ENGLISH, "%02d", hourOfDay - 12) + ":0" + minute1;
                                    } else {
                                        time = String.format(Locale.ENGLISH, "%02d", hourOfDay - 12) + ":" + minute1;
                                    }
                                    break;
                            }
                            //todo write a logic for time limit here

                            String final_date_time = dateFormat.format(Date.getTime());
                            binding.txtDate.setText(final_date_time + " " + time + " " + ampm);
                            appointmentRequest.setDate_preference(final_date_time);
                            appointmentRequest.setTime_preference(time + " " + ampm.toUpperCase());
                            DATE_SET = true;
                            getPackage();

                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.setCancelable(false);
                timePickerDialog.show();


                timePickerDialog.setOnDismissListener(dialog -> {

                    getPackage();
                });

                timePickerDialog.setOnCancelListener(dialog -> {
                    DATE_SET = false;
                    binding.txtDate.setText("Select Dates");
                    appointmentRequest.setDate_preference("");
                    getPackage();
                });

                //filter package
                getPackage();


            };
            PrimeCalendar today = new CivilCalendar();
            PrimeCalendar tomorrow = new CivilCalendar();
            tomorrow.add(Calendar.DATE, 1);


            LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(Date)
                    .disabledDays(weekends)
                    .minPossibleDate(today)
                    .applyTheme(themeFactory)
                    .build();

            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
//
//
//
        });

        return view;
    }

    private void getPackage() {
        List<PackagePT> packagePTFiltered = new ArrayList<>();

        packagePTFiltered = packagesList.stream().filter(naPackagePT ->
                (naPackagePT.getCategory().equalsIgnoreCase(appointment.getCategory()) &&
                        naPackagePT.getHhcCityMappSrNo().equalsIgnoreCase(homeHealthCareViewModel.getSelectedCity().getValue().getSrno())
                )).collect(Collectors.toList());

        if (packagePTFiltered.size() > 0) {
            LogMyBenefits.d(LogTags.PHYSIOTHERAPY, "FILTERED: " + packagePTFiltered.get(0));
        }

        if (appointmentRequest.getDate_condition() == 1) {
            String date = binding.txtDate.getText().toString().trim();
            if (!date.equalsIgnoreCase("Select Dates") && DATE_SET) {
                appointmentRequest.setPackage_price_sr_no(Integer.parseInt(packagePTFiltered.get(0).getHhcPkgPricing()));
                appointmentRequest.setPrice(packagesList.get(0).getPkgPriceMb());

                appointment.setHhcPkgPricing(packagePTFiltered.get(0).getHhcPkgPricing());
                appointment.setPkgPriceMb(packagePTFiltered.get(0).getPkgPriceMb());

                LogMyBenefits.d(LogTags.PHYSIOTHERAPY, "SelectedPackage: " + packagePTFiltered.get(0));


                homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
                homeHealthCareViewModel.setSelectedPhysiotherapy(packagePTFiltered.get(0));

                binding.btnReview.setEnabled(true);
                binding.btnReview.setClickable(true);
                binding.btnReview.setFocusable(true);
                binding.btnReview.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.greenlightbg2));
                binding.btnReview.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare));

            }


        } else if (appointmentRequest.getDate_condition() == 2 && DATE_SET && !binding.txtDate1.getText().toString().equalsIgnoreCase("Start Date")) {

            appointmentRequest.setPackage_price_sr_no(Integer.parseInt(packagePTFiltered.get(0).getHhcPkgPricing()));
            appointmentRequest.setPrice(packagesList.get(0).getPkgPriceMb());

            appointment.setHhcPkgPricing(packagePTFiltered.get(0).getHhcPkgPricing());
            appointment.setPkgPriceMb(packagePTFiltered.get(0).getPkgPriceMb());

            LogMyBenefits.d(LogTags.PHYSIOTHERAPY, "SelectedPackage: " + packagePTFiltered.get(0));


            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
            homeHealthCareViewModel.setSelectedPhysiotherapy(packagePTFiltered.get(0));

            binding.btnReview.setEnabled(true);
            binding.btnReview.setClickable(true);
            binding.btnReview.setFocusable(true);
            binding.btnReview.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.greenlightbg2));
            binding.btnReview.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare));


        } else {
            appointmentRequest.setPrice("0");
            appointmentRequest.setTotal_price("0");
            binding.btnReview.setEnabled(false);
            binding.btnReview.setClickable(false);
            binding.btnReview.setFocusable(false);
            binding.btnReview.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey1));
            binding.btnReview.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare));

        }
        LogMyBenefits.d(LogTags.PHYSIOTHERAPY, "USER-APPOINTMENT: " + appointment);
        LogMyBenefits.d(LogTags.PHYSIOTHERAPY, "REQUEST: " + appointmentRequest);


        binding.btnReview.setOnClickListener(v -> {
            homeHealthCareViewModel.setSelectedPhysiotherapy(appointment);
            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);


            //navigate
            NavDirections actions = PhysiotherapyFragmentDirections.actionPhysiotherapyFragmentToHomeHealthSummaryFragment();
            navController.navigate(actions);
        });

    }

    private void getNAPackages() {
        homeHealthCareViewModel.getPackagesPT().observe(getViewLifecycleOwner(), packages -> {
            packagesList = (ArrayList<PackagePT>) packages.getPackages();
        });
    }

}