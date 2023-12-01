package com.csform.android.MB360.wellness.fragment;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentShortTermNursingBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.monthpicker.DatePickerTheme;
import com.csform.android.MB360.monthpicker.GetMonthsFromDialog;
import com.csform.android.MB360.monthpicker.MonthModel;
import com.csform.android.MB360.monthpicker.MonthPickerDialog;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageST;
import com.csform.android.MB360.wellness.homehealthcare.ui.DatesRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ShortTermNursingFragment extends Fragment {

    FragmentShortTermNursingBinding binding;
    View view;
    NavController navController;

    // View Model
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    ArrayList<PackageST> packagesList;

    PackageST appointment = new PackageST();
    Appointment appointmentRequest = new Appointment();

    Calendar calendar;
    PrimeCalendar Date = new CivilCalendar();

    DatesRecyclerViewAdapter adapter;
    ArrayList<PrimeCalendar> dailDateList = new ArrayList<>();
    ArrayList<MonthModel> monthDateList = new ArrayList<>();

    public static String SERVICE_COUNT_ONE = "1";
    public static String SERVICE_COUNT_TWO = "2";
    public static String SERVICE_COUNT_THREE = "3";

    int MONTH_COUNT = 1;
    String time = "";
    String ampm = "am";

    Boolean DATE_SET = false;

    public ShortTermNursingFragment() {
        // Required empty public constructor
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
        binding = FragmentShortTermNursingBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //  to Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);


        //common request attributes
        appointmentRequest.setRej_appt_sr_no("-1");
        appointmentRequest.setIs_rescheduled(0);

        //initial date that changes later
        appointmentRequest.setFrom_date("01-01-1990");
        appointmentRequest.setTo_date("01-01-1990");
        appointmentRequest.setDate_preference("01-01-1990");
        binding.btnReview.setEnabled(false);

        //get person and family-sr no
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

//        adapter = new DatesRecyclerViewAdapter(dailDateList, monthDateList);
//        binding.datesCycle.setAdapter(adapter);

        // to disable sundays as that library doesn't supports sundays disable;
        PrimeCalendar sunday;
        List<PrimeCalendar> weekends = new ArrayList<>();
        int weeks = 1000;
        for (int i = 0; i < (weeks * 7); i = i + 7) {
            sunday = new CivilCalendar();
            sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
            weekends.add(sunday);
        }

        //Specialized Nurse Produres
        binding.btnSpecializedNurseProdures.setOnClickListener(v -> {

            binding.btnSpecializedNurseProdures.setSelected(true);
            binding.btnAsciticTapping.setSelected(false);
            binding.btnPeritonealDialysis.setSelected(false);

            binding.dateLayout.setVisibility(View.VISIBLE);

            //appointment
            appointment.setHhcPkgPricing(SERVICE_COUNT_ONE);
            appointmentRequest.setService("Specialized Nursing Procedures");

            //filter package
            getPackage();
        });


        //Ascitic Tapping
        binding.btnAsciticTapping.setOnClickListener(v -> {

            binding.btnSpecializedNurseProdures.setSelected(false);
            binding.btnAsciticTapping.setSelected(true);
            binding.btnPeritonealDialysis.setSelected(false);

            binding.dateLayout.setVisibility(View.VISIBLE);

            //appointment
            appointment.setHhcPkgPricing(SERVICE_COUNT_TWO);
            appointmentRequest.setService("Ascitic Tapping");

            //filter package
            getPackage();
        });

        //Peritoneal Dialysis
        binding.btnPeritonealDialysis.setOnClickListener(v -> {

            binding.btnSpecializedNurseProdures.setSelected(false);
            binding.btnAsciticTapping.setSelected(false);
            binding.btnPeritonealDialysis.setSelected(true);

            binding.dateLayout.setVisibility(View.VISIBLE);

            //appointment
            appointment.setHhcPkgPricing(SERVICE_COUNT_THREE);
            appointmentRequest.setService("Peritoneal Dialysis");

            //filter package
            getPackage();
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

            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today).pickSingleDay(callback).initiallyPickedSingleDay(Date).disabledDays(weekends).minPossibleDate(today).applyTheme(themeFactory).build();

            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
//
//
//


        });


        return view;
    }

    private void getPackage() {
        List<PackageST> packageSTFiltered = new ArrayList<>();

        packageSTFiltered = packagesList.stream().filter(naPackageST -> (naPackageST.getHhcPkgPricing().equals(appointment.getHhcPkgPricing()))).collect(Collectors.toList());

        if (!binding.txtDate.getText().toString().isEmpty() && DATE_SET) {
            appointmentRequest.setPackage_price_sr_no(Integer.parseInt(packageSTFiltered.get(0).getHhcPkgPricing()));
            appointmentRequest.setPrice(packagesList.get(0).getPkgPriceMb());

            appointment.setHhcPkgPricing(packageSTFiltered.get(0).getHhcPkgPricing());
            appointment.setPkgPriceMb(packageSTFiltered.get(0).getPkgPriceMb());

            LogMyBenefits.d(LogTags.SHORTTERM_NURSING, "SelectedPackage: " + packageSTFiltered.get(0));


            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
            homeHealthCareViewModel.setSelectedShorttermNursing(packageSTFiltered.get(0));

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

        LogMyBenefits.d(LogTags.SHORTTERM_NURSING, "USER-APPOINTMENT: " + appointment);
        LogMyBenefits.d(LogTags.SHORTTERM_NURSING, "REQUEST: " + appointmentRequest);


        binding.btnReview.setOnClickListener(v -> {
            homeHealthCareViewModel.setSelectedShorttermNursing(appointment);
            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);

            //navigate
            NavDirections actions = ShortTermNursingFragmentDirections.actionShortTermFragmentToHomeHealthSummaryFragment();
            navController.navigate(actions);
        });

    }

    private void getNAPackages() {
        homeHealthCareViewModel.getPackagesST().observe(getViewLifecycleOwner(), packages -> {
            packagesList = (ArrayList<PackageST>) packages.getPackages();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (appointment != null) {
            if (appointment.getPkgPriceMb() != null) {
                binding.btnPeritonealDialysis.performClick();
            } else if (appointment.getPkgPriceMb() != null) {
                binding.btnAsciticTapping.performClick();
            } else if (appointment.getPkgPriceMb() != null) {
                binding.btnSpecializedNurseProdures.performClick();
            }

        }
    }
}
