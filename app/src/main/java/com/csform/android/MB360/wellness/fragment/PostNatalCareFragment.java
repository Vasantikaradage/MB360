package com.csform.android.MB360.wellness.fragment;

import android.os.Bundle;

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
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentPostNatalCareBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.monthpicker.DatePickerTheme;
import com.csform.android.MB360.monthpicker.MonthModel;
import com.csform.android.MB360.monthpicker.MonthPickerDialog;
import com.csform.android.MB360.utilities.AppLocalConstant;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.City;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageNC;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackagePT;
import com.csform.android.MB360.wellness.homehealthcare.ui.DatesRecyclerViewAdapter;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PostNatalCareFragment extends Fragment {
    FragmentPostNatalCareBinding binding;
    View view;
    NavController navController;

    // View Model
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    ArrayList<PackageNC> packagesList;

    PackageNC appointment = new PackageNC();
    Appointment appointmentRequest = new Appointment();

    Calendar calendar;
    PrimeCalendar Date = new CivilCalendar();
    ArrayList<PrimeCalendar> dailDateList = new ArrayList<>();
    DatesRecyclerViewAdapter adapter;
    public static String MONTHLY = "MONTHLY";
    int MONTH_COUNT = 30;
    int DAY_COUNT = 15;
    ArrayList<MonthModel> monthDateList = new ArrayList<>();

    public static String SERVICE_COUNT_ONE = "1";
    public static String SERVICE_COUNT_TWO = "13";
    public static String SERVICE_COUNT_THREE = "20";

    public PostNatalCareFragment() {
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
        binding = FragmentPostNatalCareBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);


        //common request attributes
        appointmentRequest.setRej_appt_sr_no("-1");
        appointmentRequest.setIs_rescheduled(0);
        appointment.setHhcCityMappSrNo(homeHealthCareViewModel.getSelectedCity().getValue().getSrno());


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
        getNCPackages();

        adapter = new DatesRecyclerViewAdapter(dailDateList, monthDateList);
        binding.datesCycle.setAdapter(adapter);

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
        binding.btnPerDay.setOnClickListener(v -> {
            binding.btnPerDay.setSelected(true);
            binding.btnFifteenDays.setSelected(false);
            binding.btnThirtyDays.setSelected(false);

            //appointment
            appointment.setHhcPkgPricing(SERVICE_COUNT_ONE);
            appointment.setCategory("PER DAY");

            binding.llMonthSelection.setVisibility(View.VISIBLE);

            binding.dailyDateLayout.setVisibility(View.VISIBLE);
            binding.datesCycle.setVisibility(View.VISIBLE);
            binding.dateRange.setVisibility(View.GONE);

            binding.txtToDate.setText("");
            binding.txtFromDate.setText("");
            dailDateList.clear();
            adapter.notifyItemRangeRemoved(0, 0);

            // Date Condition
            appointmentRequest.setDate_condition(1);

            //filter package
            getPackage();
        });

        //fifteen Day
        binding.btnFifteenDays.setOnClickListener(v -> {

            binding.btnPerDay.setSelected(false);
            binding.btnFifteenDays.setSelected(true);
            binding.btnThirtyDays.setSelected(false);

            //appointment
            appointment.setHhcPkgPricing(SERVICE_COUNT_TWO);
            appointment.setCategory("15 DAYS");

            // Date Condition
            appointmentRequest.setDate_condition(2);

            binding.llMonthSelection.setVisibility(View.VISIBLE);

            binding.dailyDateLayout.setVisibility(View.GONE);
            binding.datesCycle.setVisibility(View.GONE);
            binding.dateRange.setVisibility(View.VISIBLE);

            binding.txtToDate.setText("");
            binding.txtFromDate.setText("");
            dailDateList.clear();
            adapter.notifyItemRangeRemoved(0, 0);

            //filter package
            getPackage();

            // 15 Day date range
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

                    //set the appointment  request
                    appointmentRequest.setFrom_date(dateRangeFormat.format(Date.getTime()));
                    appointmentRequest.setTo_date(dateRangeFormat.format(toDate.getTime()));
                    appointmentRequest.setDate_preference("");

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


        //thirty Day
        binding.btnThirtyDays.setOnClickListener(v -> {

            binding.btnPerDay.setSelected(false);
            binding.btnFifteenDays.setSelected(false);
            binding.btnThirtyDays.setSelected(true);

            //appointment
            appointment.setHhcPkgPricing(SERVICE_COUNT_THREE);
            appointment.setCategory("30 DAYS");

            binding.llMonthSelection.setVisibility(View.VISIBLE);

            binding.dailyDateLayout.setVisibility(View.GONE);
            binding.datesCycle.setVisibility(View.GONE);
            binding.dateRange.setVisibility(View.VISIBLE);
            binding.txtToDate.setText("");
            binding.txtFromDate.setText("");
            dailDateList.clear();
            adapter.notifyItemRangeRemoved(0, 0);

            // Date Condition
            appointmentRequest.setDate_condition(3);

            //filter package
            getPackage();

            // 30 Day date range
            binding.txtFromDate.setOnClickListener(view -> {

                SingleDayPickCallback callback = fromDate -> {


                    Date = fromDate.clone();
                    SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    Calendar toDate = Calendar.getInstance();
                    toDate.setTime(Date.getTime());
//                toDate.add(Calendar.MONTH, MONTH_COUNT);
                    toDate.add(Calendar.DAY_OF_MONTH, MONTH_COUNT);


                    binding.txtFromDate.setText(dateRangeFormat.format(Date.getTime()));

                    binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));

                    //set the appointment  request
                    appointmentRequest.setFrom_date(dateRangeFormat.format(Date.getTime()));
                    appointmentRequest.setTo_date(dateRangeFormat.format(toDate.getTime()));
                    appointmentRequest.setDate_preference("");

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

            MultipleDaysPickCallback callback = multipleDays -> {
                dailDateList.clear();
                dailDateList.addAll(multipleDays);
                monthDateList.clear();
                adapter = new DatesRecyclerViewAdapter(dailDateList, monthDateList);
                binding.datesCycle.setAdapter(adapter);


                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                StringBuilder dateString = new StringBuilder();
                StringBuilder displayDateBuilder = new StringBuilder();
                for (PrimeCalendar date : dailDateList) {
                    dateString.append(dateFormat.format(date.getTime())).append(",");
                    displayDateBuilder.append(dateFormat.format(date.getTime())).append("\n");
                }
                String date = dateString.toString().replaceFirst(".$", "");
                appointmentRequest.setDate_preference(date);
                appointmentRequest.setCount(String.valueOf(dailDateList.size()));
                appointmentRequest.setFrom_date("01-01-1900");
                appointmentRequest.setTo_date("01-01-1900");

            };
            PrimeCalendar today = new CivilCalendar();
            PrimeCalendar tomorrow = new CivilCalendar();
            tomorrow.add(Calendar.DATE, 1);

            LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                    .pickMultipleDays(callback)
                    .initiallyPickedMultipleDays(dailDateList)
                    .disabledDays(weekends)
                    .minPossibleDate(today)
                    .applyTheme(themeFactory)
                    .build();
            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
            //filter package
            getPackage();
        });

        binding.btnReview.setOnClickListener(v -> {
            homeHealthCareViewModel.setSelectedPostNatal(appointment);
            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);

            //navigate
            NavDirections actions = PostNatalCareFragmentDirections.actionPostNatalCareFragmentToHomeHealthSummaryFragment();
            navController.navigate(actions);
        });

        return view;
    }

    private void getPackage() {
        List<PackageNC> packageNCFiltered = new ArrayList<>();
        String city = homeHealthCareViewModel.getSelectedCity().getValue().getSrno();

        packageNCFiltered = packagesList.stream().filter(naPackageNC ->
                (naPackageNC.getHhcCityMappSrNo().equalsIgnoreCase(city) &&
                        naPackageNC.getCategory().equalsIgnoreCase(appointment.getCategory())
                )).collect(Collectors.toList());


        LogMyBenefits.d("FILTER-NC", "LIST: " + packageNCFiltered);

        if (!packageNCFiltered.isEmpty() && !dailDateList.isEmpty()) {


            if (appointmentRequest.getDate_condition() == 1) {
                //daily / per-day
                if (dailDateList.size() <= 0) {
                    binding.btnReview.setEnabled(false);
                } else {
                    String date_preference = "";
                    for (PrimeCalendar dateObject : dailDateList
                    ) {
                        date_preference = date_preference + AppLocalConstant.DATE_FORMAT.format(dateObject.getDate());
                    }

                    appointmentRequest.setDate_preference(date_preference);
                    appointmentRequest.setTime_preference("");
                    appointmentRequest.setFrom_date("01-01-1990");
                    appointmentRequest.setTo_date("01-01-1990");
                    appointmentRequest.setPackage_price_sr_no(Integer.parseInt(packageNCFiltered.get(0).getHhcPkgPricing()));
                    appointmentRequest.setIs_rescheduled(0);
                    appointmentRequest.setPrice(packageNCFiltered.get(0).getPkgPriceMb());

                    homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
                    binding.btnReview.setEnabled(true);
                }

            } else if (appointmentRequest.getDate_condition() == 2) {

            } else if (appointmentRequest.getDate_condition() == 3) {

            }


           /* if (!binding.txtFromDate.getText().toString().isEmpty()) {
                appointmentRequest.setPackage_price_sr_no(Integer.parseInt(packageNCFiltered.get(0).getHhcPkgPricing()));
                appointmentRequest.setPrice(packagesList.get(0).getPkgPriceMb());

                appointment.setHhcPkgPricing(packageNCFiltered.get(0).getHhcPkgPricing());
                appointment.setPkgPriceMb(packageNCFiltered.get(0).getPkgPriceMb());

                LogMyBenefits.d(LogTags.POST_NATAL, "SelectedPackage: " + packageNCFiltered.get(0));


                homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
                homeHealthCareViewModel.setSelectedPostNatal(packageNCFiltered.get(0));

                if (!binding.txtFromDate.getText().toString().equalsIgnoreCase("") && !binding.txtToDate.getText().toString().equalsIgnoreCase("")) {
                    binding.btnReview.setEnabled(true);
                    binding.btnReview.setClickable(true);
                    binding.btnReview.setFocusable(true);
                    binding.btnReview.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.greenlightbg2));
                    binding.btnReview.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare));
                }
            }*/

        } else {
            appointmentRequest.setPrice("0");
            appointmentRequest.setTotal_price("0");
            binding.btnReview.setEnabled(false);
            binding.btnReview.setClickable(false);
            binding.btnReview.setFocusable(false);
            binding.btnReview.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey1));
            binding.btnReview.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare));
        }
        LogMyBenefits.d(LogTags.POST_NATAL, "USER-APPOINTMENT: " + appointment);
        LogMyBenefits.d(LogTags.POST_NATAL, "REQUEST: " + appointmentRequest);


    }

    private void getNCPackages() {
        homeHealthCareViewModel.getPackagesNC().observe(getViewLifecycleOwner(), packages -> {
            packagesList = (ArrayList<PackageNC>) packages.getPackages();
        });
    }

}