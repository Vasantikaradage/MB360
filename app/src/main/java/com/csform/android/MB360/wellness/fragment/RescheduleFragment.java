package com.csform.android.MB360.wellness.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.utilities.LogMyBenefits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentRescheduleBinding;
import com.csform.android.MB360.monthpicker.DatePickerTheme;
import com.csform.android.MB360.monthpicker.GetMonthsFromDialog;
import com.csform.android.MB360.monthpicker.MonthModel;
import com.csform.android.MB360.monthpicker.MonthPickerDialog;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryDS;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryNA;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryPT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryST;
import com.csform.android.MB360.wellness.homehealthcare.ui.DatesRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RescheduleFragment extends Fragment implements GetMonthsFromDialog {

    FragmentRescheduleBinding binding;
    HomeHealthCareViewModel homeHealthCareViewModel;
    View view;
    String service_name = "";

    int MONTH_COUNT = 1;
    int WEEK_COUNT = 1;
    String time = "";
    String ampm = "am";

    Boolean DATE_SET = false;

    PrimeCalendar startDate = new CivilCalendar();

    DatesRecyclerViewAdapter adapter;
    ArrayList<PrimeCalendar> dailDateList = new ArrayList<>();
    ArrayList<MonthModel> monthDateList = new ArrayList<>();

    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;
    Appointment appointment = new Appointment();

    NavController navController;

    ArrayList<String> monthList = new ArrayList<>();


    //changing onGoingSummaries
    OnGoingSummaryNA onGoingSummaryNA;
    OnGoingSummaryST onGoingSummaryST;
    OnGoingSummaryLT onGoingSummaryLT;
    OnGoingSummaryDS onGoingSummaryDS;
    OnGoingSummaryPT onGoingSummaryPT;
    Dialog dialog;


    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormatForMonth = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

    public RescheduleFragment() {
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
        binding = FragmentRescheduleBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();


        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);

// to add month list statically
        monthList.add("Jan");
        monthList.add("Feb");
        monthList.add("Mar");
        monthList.add("Apr");
        monthList.add("May");
        monthList.add("Jun");
        monthList.add("Jul");
        monthList.add("Aug");
        monthList.add("Sep");
        monthList.add("Oct");
        monthList.add("Nov");
        monthList.add("Dec");


        // to disable sundays as that library doesn't supports sundays disable;
        PrimeCalendar sunday;
        List<PrimeCalendar> weekends = new ArrayList<>();
        int weeks = 1000;
        for (int i = 0; i < (weeks * 7); i = i + 7) {
            sunday = new CivilCalendar();
            sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
            weekends.add(sunday);
        }
        //duration buttons
        binding.btnMonthly.setOnClickListener(v -> {
            binding.btnMonthly.setSelected(true);
            binding.btnDaily.setSelected(false);
            binding.btnWeekly.setSelected(false);
            binding.mainDailyDateLayout.setVisibility(View.GONE);
            binding.monthPolicy.setVisibility(View.VISIBLE);
            binding.llWeeklyPolicy.setVisibility(View.GONE);

            dailDateList.clear();
            adapter.notifyItemRangeChanged(0, dailDateList.size());
        });
        binding.btnDaily.setOnClickListener(v -> {

            //appointment request
            appointment.setDate_condition(1);

            binding.btnDaily.setSelected(true);
            binding.btnMonthly.setSelected(false);
            binding.btnWeekly.setSelected(false);
            binding.mainDailyDateLayout.setVisibility(View.VISIBLE);
            binding.monthPolicy.setVisibility(View.GONE);
            binding.llWeeklyPolicy.setVisibility(View.GONE);
            monthDateList.clear();
            adapter.notifyItemRangeChanged(0, monthDateList.size());
        });

        binding.btnWeekly.setOnClickListener(v -> {

            //appointment request
            appointment.setDate_condition(4);

            binding.btnWeekly.setSelected(true);
            binding.btnMonthly.setSelected(false);
            binding.btnDaily.setSelected(false);
            binding.mainDailyDateLayout.setVisibility(View.GONE);
            binding.monthPolicy.setVisibility(View.GONE);
            binding.llWeeklyPolicy.setVisibility(View.VISIBLE);
            monthDateList.clear();
            adapter.notifyItemRangeChanged(0, monthDateList.size());
        });


        //dates layouts
        //date radio
        binding.radioDates.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {

                //appointment request
                appointment.setDate_condition(3);
                binding.rlMonthCount.setVisibility(View.VISIBLE);
                binding.llMonthSelection.setVisibility(View.GONE);
            }
        });
        binding.radioMonth.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                //appointment request
                appointment.setDate_condition(2);

                binding.rlMonthCount.setVisibility(View.GONE);
                binding.llMonthSelection.setVisibility(View.VISIBLE);
            }
        });


        //calendars
        binding.dailyDateLayout.setOnClickListener(v -> {

            MultipleDaysPickCallback callback = multipleDays -> {
                dailDateList.clear();
                dailDateList.addAll(multipleDays);

                adapter = new DatesRecyclerViewAdapter(dailDateList, monthDateList);
                binding.datesCycle.setAdapter(adapter);

                adapter.notifyItemRangeChanged(0, dailDateList.size());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                StringBuilder dateString = new StringBuilder();
                StringBuilder displayDateBuilder = new StringBuilder();
                for (PrimeCalendar date : dailDateList) {
                    dateString.append(dateFormat.format(date.getTime())).append(",");
                    displayDateBuilder.append(dateFormat.format(date.getTime())).append("\n");
                }
                String date = dateString.toString().replaceFirst(".$", "");
                appointment.setDate_condition(1);
                appointment.setDate_preference(date);
                appointment.setCount(String.valueOf(dailDateList.size()));
                appointment.setFrom_date("01-01-1900");
                appointment.setTo_date("01-01-1900");

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
        });

        binding.llMonthSelection.setOnClickListener(view -> {
            MonthPickerDialog monthPickerDialog = new MonthPickerDialog(getActivity(), this, monthDateList);
            monthPickerDialog.showMonthDialogue();
        });


        //DateRange Text
        binding.txtFromDate.setText("Start Date");
        binding.txtToDate.setText("End Date");

        //date range system
        binding.txtFromDate.setOnClickListener(view -> {

            SingleDayPickCallback callback = fromDate -> {
                startDate = fromDate.clone();
                SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Calendar toDate = Calendar.getInstance();
                toDate.setTime(startDate.getTime());
                toDate.add(Calendar.MONTH, MONTH_COUNT);


                binding.txtFromDate.setText(dateRangeFormat.format(startDate.getTime()));
                binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));

                //setting the date
                appointment.setDate_condition(3);
                appointment.setFrom_date(dateRangeFormat.format(startDate.getTime()));
                appointment.setTo_date(dateRangeFormat.format(toDate.getTime()));
                appointment.setDate_preference("01-01-1990");
                appointment.setCount(String.valueOf(dailDateList.size()));

            };

            PrimeCalendar today = new CivilCalendar();
            PrimeCalendar tomorrow = new CivilCalendar();
            tomorrow.add(Calendar.DATE, 1);
            LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(startDate)
                    .disabledDays(weekends)
                    .minPossibleDate(today)
                    .applyTheme(themeFactory)
                    .build();

            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
        });


        binding.btnAdd.setOnClickListener(v -> {
            MONTH_COUNT++;
            binding.txtCount.setText(String.valueOf(MONTH_COUNT));
//            appointmentRequest.setCount(String.valueOf(MONTH_COUNT));
            Calendar toDate = Calendar.getInstance();
            toDate.setTime(startDate.getTime());
            toDate.add(Calendar.MONTH, MONTH_COUNT);
            SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));
            binding.txtFromDate.setText(dateRangeFormat.format(startDate.getTime()));


            //setting the date
            appointment.setDate_condition(3);
            appointment.setFrom_date(dateRangeFormat.format(startDate.getTime()));
            appointment.setTo_date(dateRangeFormat.format(toDate.getTime()));

            appointment.setDate_preference("01-01-1990");
            appointment.setCount(String.valueOf(dailDateList.size()));
        });

        binding.btnSubtract.setOnClickListener(v -> {
            if (MONTH_COUNT > 1) {
                MONTH_COUNT--;
                binding.txtCount.setText(String.valueOf(MONTH_COUNT));
                Calendar toDate = Calendar.getInstance();
                toDate.setTime(startDate.getTime());
                toDate.add(Calendar.MONTH, MONTH_COUNT);
                SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));
                binding.txtFromDate.setText(dateRangeFormat.format(startDate.getTime()));

                //setting the date
                appointment.setDate_condition(3);
                appointment.setFrom_date(dateRangeFormat.format(startDate.getTime()));
                appointment.setTo_date(dateRangeFormat.format(toDate.getTime()));
                appointment.setDate_preference("01-01-1990");
                appointment.setCount(String.valueOf(dailDateList.size()));
            }
        });

        binding.btnWeekAdd.setOnClickListener(v -> {
            WEEK_COUNT++;
            binding.txtWeekCount.setText(String.valueOf(WEEK_COUNT));
//            appointmentRequest.setCount(String.valueOf(MONTH_COUNT));
            Calendar toDate = Calendar.getInstance();
            toDate.setTime(startDate.getTime());
            toDate.add(Calendar.WEEK_OF_MONTH, WEEK_COUNT);
            SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            binding.txtWeekToDate.setText(dateRangeFormat.format(toDate.getTime()));
            binding.txtWeekFromDate.setText(dateRangeFormat.format(startDate.getTime()));


            //setting the date
            appointment.setDate_condition(4);
            appointment.setFrom_date(dateRangeFormat.format(startDate.getTime()));
            appointment.setTo_date(dateRangeFormat.format(toDate.getTime()));

            appointment.setDate_preference("01-01-1990");
            appointment.setCount(String.valueOf(dailDateList.size()));
        });

        binding.btnWeekSubtract.setOnClickListener(v -> {
            if (WEEK_COUNT > 1) {
                WEEK_COUNT--;
                binding.txtWeekCount.setText(String.valueOf(WEEK_COUNT));
                Calendar toDate = Calendar.getInstance();
                toDate.setTime(startDate.getTime());
                toDate.add(Calendar.WEEK_OF_MONTH, WEEK_COUNT);
                SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                binding.txtWeekToDate.setText(dateRangeFormat.format(toDate.getTime()));
                binding.txtWeekFromDate.setText(dateRangeFormat.format(startDate.getTime()));

                //setting the date
                appointment.setDate_condition(4);
                appointment.setFrom_date(dateRangeFormat.format(startDate.getTime()));
                appointment.setTo_date(dateRangeFormat.format(toDate.getTime()));
                appointment.setDate_preference("01-01-1990");
                appointment.setCount(String.valueOf(dailDateList.size()));
            }
        });


        ///main view
        service_name = homeHealthCareViewModel.getService();
        adapter = new DatesRecyclerViewAdapter(dailDateList, monthDateList);
        binding.datesCycle.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onGoingSummaryNA = RescheduleFragmentArgs.fromBundle(getArguments()).getOnGoingSummaryNA();
        onGoingSummaryST = RescheduleFragmentArgs.fromBundle(getArguments()).getOnGoingSummaryST();
        onGoingSummaryLT = RescheduleFragmentArgs.fromBundle(getArguments()).getOnGoingSummaryLT();
        onGoingSummaryDS = RescheduleFragmentArgs.fromBundle(getArguments()).getOnGoingSummaryDS();
        onGoingSummaryPT = RescheduleFragmentArgs.fromBundle(getArguments()).getOnGoingSummaryPT();

        //get the summary and the dates from the object and let user decide the new dates.
        switch (service_name.toUpperCase()) {
            case "TRAINED ATTENDANT":
                LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "Data to Reschedule: " + onGoingSummaryNA);

                //common settings for reschedule screen
                binding.llDuration.setVisibility(View.GONE);

                binding.btnReview.setEnabled(true);
                binding.txtTitle2.setText("Selected Package: " + onGoingSummaryNA.getNaDurations());
                binding.txtPackageDetails.setText("Hours: " + onGoingSummaryNA.getNaHours() + "\n" +
                        "Duration: " + onGoingSummaryNA.getNaDurations() + "\n" +
                        "Nursing Attendants: " + onGoingSummaryNA.getNaNacount());


                //date
                if (onGoingSummaryNA.getDateCondition().equalsIgnoreCase("Daywise")) {
                    binding.btnDaily.performClick();
                } else if (onGoingSummaryNA.getDateCondition().equalsIgnoreCase("Monthwise")) {
                    binding.btnMonthly.performClick();

                    //todo need to write for other date condition
                }
                //getting the dates to set initially

                if (onGoingSummaryNA.getDateCondition().equalsIgnoreCase("Monthwise")) {
                    //check fromdate
                    String dates_array[] = onGoingSummaryNA.getFromDate().split(",");
                    monthDateList.clear();
                    dailDateList.clear();

                    for (int i = 0; dates_array.length > i; i++) {

                        try {
                            Date d = simpleDateFormatForMonth.parse(dates_array[i]);
                            CivilCalendar civilCalendar = new CivilCalendar();
                            civilCalendar.setTime(d);
                            civilCalendar.getMonth();
                            civilCalendar.getYear();
                            LogMyBenefits.d("Reschedule Date", "from date" + civilCalendar.getYear());

                            monthDateList.add(new MonthModel(String.valueOf(civilCalendar.getYear()), monthList.get(civilCalendar.getMonth())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else if (onGoingSummaryNA.getDateCondition().equalsIgnoreCase("Daywise")) {
                    String dates_array[] = onGoingSummaryNA.getDatePreference().split(",");

                    LogMyBenefits.d("RESCHEDULE NA", "onViewCreated: " + Arrays.toString(dates_array));

                    //for every dates found in date preference
                    dailDateList.clear();
                    monthDateList.clear();
                    for (int i = 0; dates_array.length > i; i++) {

                        try {
                            Date d = simpleDateFormat.parse(dates_array[i]);
                            CivilCalendar civilCalendar = new CivilCalendar();
                            civilCalendar.setTime(d);
                            dailDateList.add(civilCalendar);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LogMyBenefits.d("RESCHEDULE NA", "onViewCreated: " + dailDateList);


                    }


                } else if (onGoingSummaryNA.getDateCondition().equalsIgnoreCase("DateRangewise")) {

                    MONTH_COUNT = Integer.parseInt(onGoingSummaryNA.getNoOfMonths());
                    binding.txtCount.setText(onGoingSummaryNA.getNoOfMonths());
                    binding.txtFromDate.setText(onGoingSummaryNA.getFromDate());
                    binding.txtToDate.setText(onGoingSummaryNA.getToDate());
                    binding.radioDates.setChecked(true);
                }
                break;

            case "SHORT TERM NURSING":
                appointment.setDate_condition(1);
                binding.btnDaily.setSelected(true);
                binding.btnMonthly.setSelected(false);
                binding.mainDailyDateLayout.setVisibility(View.VISIBLE);
                binding.monthPolicy.setVisibility(View.GONE);
                binding.llDuration.setVisibility(View.GONE);
                monthDateList.clear();
                adapter.notifyItemRangeChanged(0, monthDateList.size());
                adapter.notifyDataSetChanged();


                binding.txtPackageDetails.setVisibility(View.GONE);
                binding.txtTitle2.setText("Selected category: " + onGoingSummaryST.getStCategory());
                binding.txtDate.setText(onGoingSummaryST.getDatePreference() + " " + onGoingSummaryST.getTimePreference());
                selectDateTimeForShortTermNursing();
                break;

            case "LONG TERM NURSING":

                binding.btnReview.setEnabled(true);
                binding.llDuration.setVisibility(View.GONE);
                binding.txtTitle2.setText("Selected Package: " + onGoingSummaryLT.getLtDurations());
                binding.txtPackageDetails.setText("Hours: " + onGoingSummaryLT.getLtHours() + "\n" +
                        "Duration: " + onGoingSummaryLT.getLtDurations() + "\n" +
                        "Category: " + onGoingSummaryLT.getLtCategory() + "\n" +
                        "Nursing Attendants: " + onGoingSummaryLT.getLtNacount());


                //date
                if (onGoingSummaryLT.getDateCondition().equalsIgnoreCase("Daywise")) {
                    binding.btnDaily.performClick();
                } else if (onGoingSummaryLT.getDateCondition().equalsIgnoreCase("Monthwise")) {
                    binding.btnMonthly.performClick();

                    //todo need to write for other date condition
                }
                //getting the dates to set initially

                if (onGoingSummaryLT.getDateCondition().equalsIgnoreCase("Monthwise")) {
                    //check fromdate
                    String dates_array[] = onGoingSummaryLT.getFromDate().split(",");
                    monthDateList.clear();
                    dailDateList.clear();
                    for (int i = 0; dates_array.length > i; i++) {
                        try {
                            Date d = simpleDateFormatForMonth.parse(dates_array[i]);
                            CivilCalendar civilCalendar = new CivilCalendar();
                            civilCalendar.setTime(d);
                            civilCalendar.getMonth();
                            civilCalendar.getYear();
                            LogMyBenefits.d("Reschedule Date", "from date" + civilCalendar.getYear());

                            monthDateList.add(new MonthModel(String.valueOf(civilCalendar.getYear()), monthList.get(civilCalendar.getMonth())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    appointment.setDate_condition(2);
                } else if (onGoingSummaryLT.getDateCondition().equalsIgnoreCase("Daywise")) {
                    String dates_array[] = onGoingSummaryLT.getDatePreference().split(",");

                    LogMyBenefits.d("RESCHEDULE NA", "onViewCreated: " + Arrays.toString(dates_array));

                    //for every dates found in date preference
                    dailDateList.clear();
                    monthDateList.clear();
                    for (int i = 0; dates_array.length > i; i++) {
                        try {
                            Date d = simpleDateFormat.parse(dates_array[i]);
                            CivilCalendar civilCalendar = new CivilCalendar();
                            civilCalendar.setTime(d);
                            dailDateList.add(civilCalendar);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LogMyBenefits.d("RESCHEDULE NA", "onViewCreated: " + dailDateList);
                    }
                } else if (onGoingSummaryLT.getDateCondition().equalsIgnoreCase("DateRangewise")) {
                    MONTH_COUNT = Integer.parseInt(onGoingSummaryLT.getNoOfMonths());
                    binding.radioDates.setChecked(true);
                    binding.txtCount.setText(onGoingSummaryLT.getNoOfMonths());
                    binding.txtFromDate.setText(onGoingSummaryLT.getFromDate());
                    binding.txtToDate.setText(onGoingSummaryLT.getToDate());
                    appointment.setDate_condition(3);

                } else if (onGoingSummaryLT.getDateCondition().equalsIgnoreCase("Weekwise")) {
                    binding.btnWeekly.setVisibility(View.VISIBLE);

                    binding.llWeeklyPolicy.setVisibility(View.VISIBLE);
                    binding.btnWeekly.performClick();
                    binding.monthPolicy.setVisibility(View.GONE);
                  //  binding.btnReviewWeek.setVisibility(View.GONE);
                    binding.llMonthRecycler.setVisibility(View.GONE);
                    WEEK_COUNT = Integer.parseInt(onGoingSummaryLT.getNoOfWeeks());

                    appointment.setDate_condition(4);
                    binding.txtWeekCount.setText(String.valueOf(WEEK_COUNT));
                    binding.txtWeekToDate.setText(onGoingSummaryLT.getToDate());
                    binding.txtWeekFromDate.setText(onGoingSummaryLT.getFromDate());
                }
                break;

            case "DOCTOR SERVICES":

                //appointment request
                appointment.setDate_condition(1);
                binding.btnDaily.setSelected(true);
                binding.btnMonthly.setSelected(false);
                binding.mainDailyDateLayout.setVisibility(View.VISIBLE);
                binding.monthPolicy.setVisibility(View.GONE);
                binding.llDuration.setVisibility(View.GONE);
                binding.dailyDateLayout.setVisibility(View.VISIBLE);
                monthDateList.clear();
                adapter.notifyItemRangeChanged(0, monthDateList.size());

                binding.txtPackageDetails.setVisibility(View.GONE);
                binding.txtTitle2.setText("Selected category: " + onGoingSummaryDS.getDsCategory());
                binding.txtDate.setText(onGoingSummaryDS.getDatePreference() + " " + onGoingSummaryDS.getTimePreference());

                selectDateTimeForShortTermNursing();
                break;

            case "PHYSIOTHERAPY":
                //common settings for reschedule screen
                binding.llDuration.setVisibility(View.GONE);
                binding.btnReview.setEnabled(true);
                binding.txtTitle2.setText("Selected Category: " + onGoingSummaryPT.getPtCategory());
                binding.txtPackageDetails.setVisibility(View.GONE);

                //select category
                if (onGoingSummaryPT.getPtCategory().equalsIgnoreCase("Per Day")) {
                    appointment.setDate_condition(1);
                    binding.btnDaily.setSelected(true);
                    binding.btnMonthly.setSelected(false);
                    binding.mainDailyDateLayout.setVisibility(View.VISIBLE);
                    binding.monthPolicy.setVisibility(View.GONE);
                    binding.llWeeklyPolicy.setVisibility(View.GONE);
                    appointment.setCount(String.valueOf(dailDateList.size()));
                    appointment.setFrom_date("01-01-1900");
                    appointment.setTo_date("01-01-1900");
                    selectDateTimeForShortTermNursing();

                    binding.txtDate.setText(onGoingSummaryPT.getDatePreference() + " " + onGoingSummaryPT.getTimePreference());

                } else if (onGoingSummaryPT.getPtCategory().equalsIgnoreCase("10 Days")) {
                    binding.llTenDayPolicy.setVisibility(View.VISIBLE);
                    binding.dailyDateLayout.setVisibility(View.VISIBLE);
                    binding.txtDate.setVisibility(View.VISIBLE);
                    binding.btnReviewTenDay.setVisibility(View.GONE);
                    binding.txtTenDayView.setVisibility(View.GONE);
                    binding.llMonthRecycler.setVisibility(View.GONE);
                    monthDateList.clear();
                    adapter.notifyItemRangeChanged(0, monthDateList.size());
                    binding.txtstartDate.setText(onGoingSummaryPT.getFromDate());
                    binding.txtendDate.setText(onGoingSummaryPT.getToDate());
                    selectDatesForPT();
                }
        }
        binding.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(requireActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_appt_schedule);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                ImageView iv_close = dialog.findViewById(R.id.iv_close);
                Button btnyes = dialog.findViewById(R.id.btnyes);
                Button btnno = dialog.findViewById(R.id.btnno);

                btnyes.setOnClickListener(view1 -> {
                    dialog.dismiss();
                    confirmAppointment();

                });

                btnno.setOnClickListener(view1 -> dialog.dismiss());
                iv_close.setOnClickListener(view1 -> dialog.dismiss());
                dialog.show();

            }
        });
    }

    private void selectDatesForPT() {
        int DAY_COUNT = 10;
        binding.txtstartDate.setOnClickListener(view1 -> {

            SingleDayPickCallback callback = fromDate -> {
                startDate = fromDate.clone();
                SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Calendar toDate = Calendar.getInstance();
                toDate.setTime(startDate.getTime());
//                toDate.add(Calendar.MONTH, MONTH_COUNT);
                toDate.add(Calendar.DAY_OF_MONTH, DAY_COUNT);

                binding.txtstartDate.setText(dateRangeFormat.format(startDate.getTime()));
                binding.txtendDate.setText(dateRangeFormat.format(toDate.getTime()));
                DATE_SET = true;

                //set the appointment  request
                appointment.setFrom_date(dateRangeFormat.format(startDate.getTime()));
                appointment.setTo_date(dateRangeFormat.format(toDate.getTime()));
                appointment.setDate_preference("01-01-1990");
                appointment.setTime_preference("10:00 AM");
                appointment.setDate_condition(2);
            };

            PrimeCalendar today = new CivilCalendar();
            PrimeCalendar tomorrow = new CivilCalendar();
            tomorrow.add(Calendar.DATE, 1);
            LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

            PrimeCalendar sunday;
            List<PrimeCalendar> weekends = new ArrayList<>();
            int weeks = 1000;
            for (int i = 0; i < (weeks * 7); i = i + 7) {
                sunday = new CivilCalendar();
                sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
                weekends.add(sunday);
            }

            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(startDate)
                    .disabledDays(weekends)
                    .minPossibleDate(today)
                    .applyTheme(themeFactory)
                    .build();

            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
        });

    }

    private void selectDateTimeForShortTermNursing() {
        // to disable sundays as that library doesn't supports sundays disable;
        PrimeCalendar sunday;
        List<PrimeCalendar> weekends = new ArrayList<>();
        int weeks = 1000;
        for (int i = 0; i < (weeks * 7); i = i + 7) {
            sunday = new CivilCalendar();
            sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
            weekends.add(sunday);
        }
        binding.dailyDateLayout.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            SingleDayPickCallback callback = fromDate -> {

                startDate = fromDate.clone();
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
                        (view1, hourOfDay, minute1) -> {
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
                                    //  getPackage();
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

                            String final_date_time = dateFormat.format(startDate.getTime());
                            binding.txtDate.setText(final_date_time + " " + time + " " + ampm);
                            appointment.setDate_preference(final_date_time);
                            appointment.setTime_preference(time + " " + ampm.toUpperCase());

                            DATE_SET = true;
                            }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.setCancelable(false);
                timePickerDialog.show();

                timePickerDialog.setOnDismissListener(dialog -> {
                });

                timePickerDialog.setOnCancelListener(dialog -> {
                    DATE_SET = false;
                    binding.txtDate.setText("Select Dates");
                    appointment.setDate_preference("");
                });
            };
            PrimeCalendar today = new CivilCalendar();
            PrimeCalendar tomorrow = new CivilCalendar();
            tomorrow.add(Calendar.DATE, 1);

            LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();
            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today).pickSingleDay(callback).initiallyPickedSingleDay(startDate).disabledDays(weekends).minPossibleDate(today).applyTheme(themeFactory).build();
            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");

        });
    }



    private void confirmAppointment() {
        binding.progressBar.setVisibility(View.VISIBLE);

        String family_sr_no = dashboardWellnessViewModel.getEmployeeWellnessDetailsData().getValue().getExtFamilySrNo();
        if (family_sr_no != null) {

            switch (service_name.toUpperCase()) {
                case "TRAINED ATTENDANT":
                    appointment.setFamily_sr_no(family_sr_no);
                    appointment.setPerson_sr_no(onGoingSummaryNA.getAppntPersonSrNo());
                    appointment.setIs_rescheduled(1);
                    appointment.setRej_appt_sr_no(String.valueOf(-1));
                    appointment.setPackage_price_sr_no(Integer.parseInt((onGoingSummaryNA.getSelectedPkgSrNo())));
                    appointment.setReschedule_sr_no(Integer.parseInt((onGoingSummaryNA.getHhcApptInfoSrNo())));
                    appointment.setRemarks(onGoingSummaryNA.getHhcNaRemarks());

                    homeHealthCareViewModel.scheduleAppointment(appointment).observe(getViewLifecycleOwner(), messageResponse -> {
                        if (messageResponse != null) {
                            if (messageResponse.getMessage().getStatus()) {
                                if (messageResponse.getMessage().getStatus()) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    ConfirmAppointmentDailog();
                                }
                            }
                        }
                    });
                    break;
                case "SHORT TERM NURSING":

                    appointment.setFamily_sr_no(family_sr_no);
                    appointment.setPerson_sr_no(onGoingSummaryST.getAppntPersonSrNo());
                    appointment.setIs_rescheduled(1);
                    appointment.setRej_appt_sr_no(String.valueOf(-1));
                    appointment.setPackage_price_sr_no(Integer.parseInt((onGoingSummaryST.getSelectedPkgSrNo())));
                    appointment.setReschedule_sr_no(Integer.parseInt((onGoingSummaryST.getHhcStApptInfoSrNo())));
                    appointment.setRemarks(onGoingSummaryST.getHhcStRemarks());

                    homeHealthCareViewModel.scheduleAppointmentST(appointment).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {
                                if (messageResponse.getMessage().getStatus()) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    ConfirmAppointmentDailog();
                                }
                            }
                        }


                    });
                    break;
                case "LONG TERM NURSING":
                    appointment.setFamily_sr_no(family_sr_no);
                    appointment.setPerson_sr_no(onGoingSummaryLT.getAppntPersonSrNo());
                    appointment.setIs_rescheduled(1);
                    appointment.setRej_appt_sr_no(String.valueOf(-1));
                    appointment.setPackage_price_sr_no(Integer.parseInt((onGoingSummaryLT.getSelectedPkgSrNo())));
                    appointment.setReschedule_sr_no(Integer.parseInt((onGoingSummaryLT.getHhcLtApptInfoSrNo())));
                    appointment.setRemarks(onGoingSummaryLT.getHhcLtRemarks());

                    homeHealthCareViewModel.scheduleAppointmentLT(appointment).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {
                                if (messageResponse.getMessage().getStatus()) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    ConfirmAppointmentDailog();
                                }
                            }
                        }
                    });
                    break;

                case "DOCTOR SERVICES":
                    appointment.setFamily_sr_no(family_sr_no);
                    appointment.setPerson_sr_no(onGoingSummaryDS.getAppntPersonSrNo());
                    appointment.setIs_rescheduled(1);
                    appointment.setRej_appt_sr_no(String.valueOf(-1));
                    appointment.setPackage_price_sr_no(Integer.parseInt((onGoingSummaryDS.getSelectedPkgSrNo())));
                    appointment.setReschedule_sr_no(Integer.parseInt((onGoingSummaryDS.getHhcDsApptInfoSrNo())));
                    appointment.setRemarks(onGoingSummaryDS.getHhcDsRemarks());

                    homeHealthCareViewModel.scheduleAppointmentDS(appointment).observe(getViewLifecycleOwner(), messageResponse -> {
                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {
                                if (messageResponse.getMessage().getStatus()) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    ConfirmAppointmentDailog();
                                }
                            }
                        }


                    });

                    break;

                case "PHYSIOTHERAPY":
                    appointment.setFamily_sr_no(family_sr_no);
                    appointment.setPerson_sr_no(onGoingSummaryPT.getAppntPersonSrNo());
                    appointment.setIs_rescheduled(1);
                    appointment.setRej_appt_sr_no(String.valueOf(-1));
                    appointment.setPackage_price_sr_no(Integer.parseInt((onGoingSummaryPT.getSelectedPkgSrNo())));
                    appointment.setReschedule_sr_no(Integer.parseInt((onGoingSummaryPT.getHhcPtApptInfoSrNo())));
                    appointment.setRemarks(onGoingSummaryPT.getHhcPtRemarks());

                    homeHealthCareViewModel.scheduleAppointmentPT(appointment).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {
                                if (messageResponse.getMessage().getStatus()) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    ConfirmAppointmentDailog();
                                }
                            }
                        }


                    });
                    break;
            }

        } else {
            Toast.makeText(requireActivity(), "Something went wrong, Please try again later", Toast.LENGTH_LONG).show();
        }


    }

    private void ConfirmAppointmentDailog() {
        try {
           if(dialog==null) {
               dialog = new Dialog(requireContext());
               dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
               dialog.setContentView(R.layout.schedule_status);
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
               dialog.setCanceledOnTouchOutside(false);
               dialog.setCancelable(false);
               ImageView imageView = dialog.findViewById(R.id.image_confirm);
               imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_confirm));
               TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
               TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
               appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.greenlightbg2));
               Button button = dialog.findViewById(R.id.done);

               button.setOnClickListener(view -> {
                   dialog.dismiss();

                   //navigate
                  // NavDirections navDirections = RescheduleFragmentDirections.actionRescheduleFragmentToOnGoingFragment();
                   //navController.navigate(navDirections);
                   navController.navigateUp();

               });


               dialog.show();
           }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void getMonths(ArrayList<MonthModel> monthList) {
        monthDateList.clear();
        dailDateList.clear();
        monthDateList.addAll(monthList);
        adapter = new DatesRecyclerViewAdapter(dailDateList, monthDateList);
        binding.datesCycle.setAdapter(adapter);
        adapter.notifyItemRangeChanged(0, monthDateList.size());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        StringBuilder displayDateBuilder = new StringBuilder();
        StringBuilder dateString = new StringBuilder();
        StringBuilder dateStringto = new StringBuilder();
        for (MonthModel date : monthDateList) {

            String displayDate = date.getMonth() + "-" + date.getYear();
            displayDateBuilder.append(displayDate).append("\n");

            String dateFromStringMonth = "01-" + date.getMonth() + "-" + date.getYear();
            try {

                Calendar fromCalendar = Calendar.getInstance();
                fromCalendar.setTime(dateFormat.parse(dateFromStringMonth));

                Calendar toCalendar = Calendar.getInstance();
                toCalendar.setTime(dateFormat.parse(dateFromStringMonth));
                toCalendar.set(Calendar.DATE, toCalendar.getActualMaximum(Calendar.DATE));

                dateString.append(dateFormat.format(fromCalendar.getTime())).append(",");
                dateStringto.append(dateFormat.format(toCalendar.getTime())).append(",");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String date = dateString.toString().replaceFirst(".$", "");
        String dateto = dateStringto.toString().replaceFirst(".$", "");
        appointment.setDate_condition(2);
        appointment.setFrom_date(date);
        appointment.setTo_date(dateto);
        appointment.setDate_preference("01-01-1990");
        appointment.setCount(String.valueOf(monthList.size()));
    }
}
