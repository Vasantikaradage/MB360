package com.csform.android.MB360.fitness.StatsScreenData;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.aktivolabs.aktivocore.data.models.heartrate.HeartRateStats;
import com.aktivolabs.aktivocore.data.models.score.ScoreStats;
import com.aktivolabs.aktivocore.data.models.sleep.SleepStats;
import com.aktivolabs.aktivocore.data.models.steps.StepStats;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentFitnessStatsBinding;
import com.csform.android.MB360.fitness.AktivoData.AktivoFetchingData;
import com.csform.android.MB360.fitness.AktivoData.FetchAktivoData;
import com.csform.android.MB360.fitness.AktivoData.Team_Score_VH;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.tabs.TabLayout;

import org.threeten.bp.LocalDate;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FitnessStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitnessStatsFragment extends Fragment implements OnChartValueSelectedListener, FetchAktivoData {

    FragmentFitnessStatsBinding fragmentFitnessStatsBinding;
    private HashMap<Integer, WeekandDtes> testData;
    private HashMap<Integer, MnthandDates> monthDates;
    private Boolean stepData = false, hrData = false, scoreData = false, sleepData = false;
    private AktivoStatsViewModel mViewModel;
    NetworkScoreHtml networkScoreHtml = new NetworkScoreHtml();
    private HashMap<String, Integer> NetworkData;
    private int weekcall = 0, monthCall = 0;

    private View view;
    public SimpleDateFormat Aktivo_Date_Format;

    private int weekScore = 0, weekSteps = 0;
    private int mnthScore = 0, mntSteps = 0;

    private int selectedMonth = 0, selectedWeek = 0;

    private int todayScore = 0, todaySteps = 0;
    private long todaySleep = 0, weekSleep = 0, mnthSleep = 0;
    private int todayHR = 0, WeekHR = 0, MonthHR = 0;

    private static final String timeZone = "Asia/Calcutta";

    private int CurrentWeek, CurrentMonth, currentWeek2 = 0;

    private Context cnt;
    private List<String> weeks;



    private AktivoFetchingData aktivoFetchingData;
    private Calendar weekCal, Monthcal;
    private String firstDayinWeek, firstDayinMonth, lastdayinweek, lastdayinMonth,
            FirstDayinCurrentWeek, FirstDayinCurrentMonth;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FitnessStatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FitnessStatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FitnessStatsFragment newInstance(String param1, String param2) {
        FitnessStatsFragment fragment = new FitnessStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.cnt = context;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentFitnessStatsBinding = FragmentFitnessStatsBinding.inflate(inflater, container, false);
        view = fragmentFitnessStatsBinding.getRoot();
        initData();
        return view;
    }

    private void initData() {
        aktivoFetchingData = new AktivoFetchingData(cnt, this);
        mViewModel = ViewModelProviders.of(this).get(AktivoStatsViewModel.class);
        Aktivo_Date_Format = new SimpleDateFormat("yyyy-MM-dd");
        Aktivo_Date_Format.setTimeZone(TimeZone.getTimeZone(timeZone));
        weeks = new ArrayList<>();

        Calendar cal = Calendar.getInstance();

        cal.setTimeZone(TimeZone.getTimeZone(timeZone));
        int MaxWeek = 0;
        CurrentMonth = cal.get(Calendar.MONTH);
        cal.setTime(new Date());

        CurrentWeek = cal.get(Calendar.WEEK_OF_YEAR);

        MaxWeek = cal.getMaximum(Calendar.WEEK_OF_YEAR);
        selectedWeek = CurrentWeek;


        for (int i = 1; i <= MaxWeek; i++) {
            weeks.add(String.valueOf(i));
        }

        CurrentDates();
        lineChart();
        setupViewPager();
    }


    private void CurrentDates() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        FirstDayinCurrentWeek = Aktivo_Date_Format.format(cal.getTime());

        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        FirstDayinCurrentMonth = Aktivo_Date_Format.format(cal.getTime());

    }


    private void setupViewPager() {

        try {
            weekCal = Calendar.getInstance();
            Monthcal = Calendar.getInstance();
            fragmentFitnessStatsBinding.tabStats.addTab(fragmentFitnessStatsBinding.tabStats.newTab().setText(R.string.aktivo_title2));
            fragmentFitnessStatsBinding.tabStats.addTab(fragmentFitnessStatsBinding.tabStats.newTab().setText("STEPS TAKEN"));
            fragmentFitnessStatsBinding.tabStats.addTab(fragmentFitnessStatsBinding.tabStats.newTab().setText("HEART RATE"));
            fragmentFitnessStatsBinding.tabStats.addTab(fragmentFitnessStatsBinding.tabStats.newTab().setText("SLEEP"));

            weekCal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
            Monthcal.set(Calendar.HOUR_OF_DAY, 0);

            monthCall = weekCal.get(Calendar.WEEK_OF_YEAR);
            weekcall = weekCal.get(Calendar.MONTH);


            new DateTimeHandler() {
                @Override
                public void ApplyTabs(HashMap<Integer, WeekandDtes> localeDates) {
                    testData = localeDates;
                    for (int k = 1; k <= localeDates.size(); k++) {
                        WeekandDtes valtest = localeDates.get(k);
                        fragmentFitnessStatsBinding.scrollWeek.addTab(fragmentFitnessStatsBinding.scrollWeek.newTab().setText(valtest.getWeekno() + ""));
                        if (valtest.isCurrentWeek()) {
                            currentWeek2 = k;
                        }
                    }
                }


                @Override
                public void ApplymonthTabs(HashMap<Integer, MnthandDates> localeMDates) {
                    monthDates = localeMDates;

                    for (int k = 0; k < localeMDates.size(); k++) {
                        MnthandDates mnthandDates = localeMDates.get(k);
                        fragmentFitnessStatsBinding.scrollMonth.addTab(fragmentFitnessStatsBinding.scrollMonth.newTab().setText(mnthandDates.getMonthNo()));
                        if (mnthandDates.isCurrentMonth()) {
                            CurrentMonth = k;
                        }
                    }

                }
            };


//            Objects.requireNonNull(scrollWeek.getTabAt(call-1)).select();

//           scrollMonth.setScrollX(scrollMonth.getTabAt(call).getPosition());

//        aktivoFetchingData.ScoreQuery(sdf1.format(c1.get(Calendar.DAY_OF_WEEK,1)), sdf1.format(cal.get(Calendar.DATE)));

            changeTabsFont(fragmentFitnessStatsBinding.tabStats);
            changeTabsFont(fragmentFitnessStatsBinding.scrollMonth);
            changeTabsFont(fragmentFitnessStatsBinding.scrollWeek);

//            getTodayDate(CurrentWeek);
            getFirstDayinWeek(CurrentWeek);
            getFirstDayinMonth(CurrentMonth);
            getTodayDate(CurrentWeek);



            new Handler().postDelayed(() -> aktivoFetchingData.StepQuery("2020-01-01", lastdayinweek), 1000);
            new Handler().postDelayed(() -> aktivoFetchingData.HeartRate("2020-01-01", lastdayinweek), 2000);
            new Handler().postDelayed(() -> aktivoFetchingData.SleepQuery("2020-01-01", lastdayinweek), 3000);

            aktivoFetchingData.ScoreQuery("2020-01-01", lastdayinweek);

//            aktivoFetchingData.MonthScoreQuery(firstDayinMonth, lastdayinweek);

           /* Cursor cur_personData = dbHelper.execQry("select * from EMPLOYEE_INFORMATION where productcode='GHI'");

            String email = cur_personData.getString(cur_personData.getColumnIndex("OFFICIAL_E_MAIL_ID"));
            String _id = getContext().getSharedPreferences(FinalVariables.adminsettings, Context.MODE_PRIVATE)
                    .getString("_id", null);
            String url = "https://api.aktivolabs.com/api/users/"+_id+"?include=*&" +
                    "startDate=" + Aktivo_Date_Format.format(new Date()) + "&endDate=" +
                    Aktivo_Date_Format.format(new Date());

            String token = getContext().getSharedPreferences(FinalVariables.adminsettings,
                    Context.MODE_PRIVATE).getString("access_token", null);
           // new FetchAktivoChallenges(getContext(), this).execute(token, url);
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFirstDayinWeek(int week) {
        selectedWeek = week;
        WeekandDtes valDates = testData.get(week);
        firstDayinWeek = Aktivo_Date_Format.format(valDates.getFromDate());

//        week_dates.setText(MessageFormat.format("{0}-{1}", firstDay, getLast));
    }

    private void getFirstDayinMonth(int month) {
        MnthandDates mnthandDates = monthDates.get(month);
        selectedMonth = month;
        firstDayinMonth = Aktivo_Date_Format.format(mnthandDates.getFromDate());
        lastdayinMonth = Aktivo_Date_Format.format(CurrentMonth == month ? new Date() : mnthandDates.getToDate());
    }

    public void getTodayDate(int week) {
        WeekandDtes valDates = testData.get(week);
        lastdayinweek = Aktivo_Date_Format.format(week == CurrentWeek ? new Date() : valDates.getToDate());
    }


    private void lineChart() {

        try {

            // background color
            fragmentFitnessStatsBinding.lineChart.setBackgroundColor(Color.WHITE);
            fragmentFitnessStatsBinding.lineChart.getXAxis().setDrawGridLines(false);
            fragmentFitnessStatsBinding.lineChart.getAxisLeft().setEnabled(true);
            fragmentFitnessStatsBinding.lineChart.getAxisRight().setEnabled(false);

            // disable description text
            fragmentFitnessStatsBinding.lineChart.getDescription().setEnabled(false);

            // enable touch gestures
            fragmentFitnessStatsBinding.lineChart.setTouchEnabled(true);

            // set listeners
            fragmentFitnessStatsBinding.lineChart.setOnChartValueSelectedListener(this);
            fragmentFitnessStatsBinding.lineChart.setDrawGridBackground(false);

           /* // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

            // Set the marker to the lineChart
            mv.setChartView(lineChart);
            lineChart.setMarker(mv);*/

            // enable scaling and dragging
            fragmentFitnessStatsBinding.lineChart.setDragEnabled(false);
            fragmentFitnessStatsBinding.lineChart.setScaleEnabled(false);
            // lineChart.setScaleXEnabled(true);
            // lineChart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            fragmentFitnessStatsBinding.lineChart.setPinchZoom(false);

            XAxis xAxis;
            {   // // X-Axis Style // //
                xAxis = fragmentFitnessStatsBinding.lineChart.getXAxis();

                xAxis.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));
                // vertical grid lines
//                xAxis.enableGridDashedLine(10f, 10f, 0f);
            }

            {   // // Create Limit Lines // //
                LimitLine llXAxis = new LimitLine(9f, "Index 10");
                llXAxis.setLineWidth(1f);
//                llXAxis.enableDashedLine(10f, 10f, 0f);
                llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                llXAxis.setTextSize(10f);
                llXAxis.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));
                // draw limit lines behind data instead of on top

//            yAxis.setEnabled(false);
                xAxis.setDrawLimitLinesBehindData(false);
                xAxis.setLabelCount(30);
                xAxis.setAxisMaximum(30);
                xAxis.setGranularity(3.0f);
                xAxis.setAxisMinimum(1f);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                //xAxis.addLimitLine(llXAxis);
            }

            // draw points over time
            fragmentFitnessStatsBinding.lineChart.animateX(1500);

            // get the legend (only possible after setting data)
            Legend l = fragmentFitnessStatsBinding.lineChart.getLegend();

            // draw legend entries as lines
            l.setForm(Legend.LegendForm.LINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeTabsFont(TabLayout page_title) {
        ViewGroup childTabLayout = (ViewGroup) page_title.getChildAt(0);
        for (int i = 0; i < childTabLayout.getChildCount(); i++) {

            ViewGroup viewTab = (ViewGroup) childTabLayout.getChildAt(i);

            for (int j = 0; j < viewTab.getChildCount(); j++) {
                View tabTextView = viewTab.getChildAt(j);
                if (tabTextView instanceof TextView) {

                   Typeface typeface= ResourcesCompat.getFont(getContext(), R.font.setpoppinssemibold);
                        ((TextView) tabTextView).setTypeface(typeface);
                    ((TextView) tabTextView).setTextSize(13f);
                }
            }
        }
    }

    @Override
    public void stepQuery(Map<LocalDate, StepStats> localDateSleepStatsMap, Boolean Month) {
        try {
            TreeMap<LocalDate, StepStats> treeMap = new TreeMap(localDateSleepStatsMap);
            mViewModel.setLocalDateStepStatsTreeMap(treeMap);

            if (treeMap.containsKey(LocalDate.parse(Aktivo_Date_Format.format(new Date()))))
                todaySteps = treeMap.get(LocalDate.parse(Aktivo_Date_Format.format(new Date()))).getValue();

            if (treeMap.containsKey(LocalDate.parse(FirstDayinCurrentWeek)))
                weekSteps = treeMap.get(LocalDate.parse(FirstDayinCurrentWeek)).getValue();

            if (treeMap.containsKey(LocalDate.parse(FirstDayinCurrentMonth)))
                mntSteps = treeMap.get(LocalDate.parse(FirstDayinCurrentMonth)).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stepData = true;
            doThis();
        }
    }

    private void doThis() {
        if (stepData && sleepData && hrData && scoreData) {
            setListeners();

        }
    }

    private void setListeners() {
        new Handler().postDelayed(
                () -> {
                    fragmentFitnessStatsBinding.scrollWeek.getTabAt(currentWeek2 == 0 ? 0 : currentWeek2 - 1).select();
                    fragmentFitnessStatsBinding.scrollMonth.getTabAt(CurrentMonth).select();
                }, 500);


        fragmentFitnessStatsBinding.scrollWeek.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Integer week = tab.getPosition() + 1;
                getFirstDayinWeek(tab.getPosition() + 1);
                getTodayDate(tab.getPosition() + 1);
                onTabSelected2();

                SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM");

                WeekandDtes sample1 = testData.get(week);
                String firstDay = sdf2.format(Objects.requireNonNull(sample1).getFromDate());
                String getLast = sdf2.format(sample1.getToDate());
                fragmentFitnessStatsBinding.weekDates.setText(MessageFormat.format("{0}-{1}", firstDay, getLast));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragmentFitnessStatsBinding.scrollMonth.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getFirstDayinMonth(tab.getPosition());
                onTabSelected2();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragmentFitnessStatsBinding.tabStats.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabSelected2();
                updateNetworkScore();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void updateNetworkScore() {
        if (Build.VERSION.SDK_INT < 18) {
            fragmentFitnessStatsBinding.networkScore.clearView();
        } else {
            fragmentFitnessStatsBinding.networkScore.loadUrl("about:blank");
        }
        try {
            switch (fragmentFitnessStatsBinding.tabStats.getSelectedTabPosition()) {
                case 0:
                    String finalOUTPUT = networkScoreHtml.NetworkScore(weekScore,
                            NetworkData.get("score"),
                            NetworkData.get("score_percentile10"),
                            NetworkData.get("score_percentile90"),
                            mnthScore,
                            todayScore);
                    WebChromeClient webChromeClient = new WebChromeClient();

                    webChromeClient.onJsAlert(fragmentFitnessStatsBinding.networkScore, finalOUTPUT, "hello", null);
                    fragmentFitnessStatsBinding.networkScore.setWebChromeClient(webChromeClient);
                    fragmentFitnessStatsBinding.networkScore.addJavascriptInterface(new JavaScriptInterface(), "interface");

                    fragmentFitnessStatsBinding.networkScore.loadDataWithBaseURL(null, finalOUTPUT,
                            "text/html", "UTF-8", null);

                    break;
                case 1:
                    fragmentFitnessStatsBinding.networkScore.loadDataWithBaseURL(null, networkScoreHtml.NetworkScore(weekSteps,
                                    NetworkData.get("steps"),
                                    NetworkData.get("steps_percentile10"),
                                    NetworkData.get("steps_percentile90"),
                                    mntSteps,
                                    todaySteps),
                            "text/html", "UTF-8", null);
                    break;
                case 2:
                    fragmentFitnessStatsBinding.networkScore.loadDataWithBaseURL(null, networkScoreHtml.NetworkScore(
                                    WeekHR,
                                    0,
                                    0,
                                    0,
                                    MonthHR,
                                    todayHR),
                            "text/html", "UTF-8", null);
                    break;

                case 3:
                    try {
                        fragmentFitnessStatsBinding.networkScore.loadDataWithBaseURL(null, networkScoreHtml.NetworkScore(
                                Math.round(convertTime(weekSleep)),
                                Math.round(convertTime(NetworkData.get("sleep"))),
                                Math.round(convertTime(NetworkData.get("sleep_percentile10"))),
                                Math.round(convertTime(NetworkData.get("sleep_percentile90"))),
                                Math.round(convertTime(mnthSleep)),
                                Math.round(convertTime(todaySleep))), null, "UTF-8", null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Float convertTime(long longVal) {
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal % 3600;
        int mins = remainder / 60;
        return Float.valueOf(hours + "." + mins);
    }

    @Override
    public void NetworkData(HashMap<String, Integer> NetworkAvg) {
        this.NetworkData = NetworkAvg;
        updateNetworkScore();
    }

    private void onTabSelected2() {
        switch (fragmentFitnessStatsBinding.tabStats.getSelectedTabPosition()) {
            case 0:
                fragmentFitnessStatsBinding.networkScoreCommentary.setText(cnt.getString(R.string.aktivo_stats_score_commentary));
                updateScore(false);
                updateScore(true);
                break;
            case 1:
                fragmentFitnessStatsBinding.networkScoreCommentary.setText(cnt.getString(R.string.aktivo_steps_commentary));
                updateSteps(true);
                updateSteps(false);
                break;
            case 2:
                fragmentFitnessStatsBinding.networkScoreCommentary.setText(cnt.getString(R.string.aktivo_HR_commentary));
                updateHeartRate(true);
                updateHeartRate(false);
                break;
            case 3:
                fragmentFitnessStatsBinding.networkScoreCommentary.setText(cnt.getString(R.string.aktivo_sleep_commentary));
                updateSleep(false);
                break;
        }
    }

    private void updateHeartRate(boolean month) {
        TreeMap<LocalDate, HeartRateStats> treeMap = mViewModel.getLocalDateHeartRateStatsTreeMap();

        try {
            if (month) {
                int date = 1, prev = 0;
                ArrayList<Entry> values = new ArrayList<>();

                List<LocalDate> Monthdates = getDates(firstDayinMonth, lastdayinMonth);

                for (LocalDate localDate : Monthdates) {
                    int score = 0;
                    if (treeMap.containsKey(localDate))
                        score = treeMap.get(localDate).getValue();
                    values.add(new Entry(date++, score));
                    prev += score;
                }

                setData(values, prev / values.size());
            } else {
                int date = 1, prev = 0;
                ArrayList<BarEntry> values = new ArrayList<>();


                List<LocalDate> weekDates = getDates(firstDayinWeek, lastdayinweek);

                for (LocalDate localDate : weekDates) {
                    int score = 0;
                    if (treeMap.containsKey(localDate))
                        score = treeMap.get(localDate).getValue();
                    values.add(new BarEntry(date++, score));
                    prev += score;
                }

                barChart(values, prev / values.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
           /* if (month) {
                setData(null, 0);
            } else {
                barChart(null, 0);
            }*/
        }

    }

    private void updateScore(boolean b) {
        try {
            TreeMap<LocalDate, ScoreStats> treeMap = mViewModel.getLocalDateScoreStatsTreeMap();

            if (b) {
                int date = 1, prev = 0;
                ArrayList<Entry> values = new ArrayList<>();

                Calendar cal = Calendar.getInstance();

                MnthandDates mnthandDates = monthDates.get(selectedMonth);

                cal.setTime(mnthandDates.getFromDate());
                cal.add(Calendar.DATE, -1);
                String firstDay = Aktivo_Date_Format.format(cal.getTime());

                cal.setTime(selectedMonth != CurrentMonth ? mnthandDates.getToDate() : new Date());
                cal.add(Calendar.DATE, -1);
                String todayDate = Aktivo_Date_Format.format(cal.getTime());


                List<LocalDate> Monthdates = getDates(firstDay, todayDate);

                for (LocalDate localDate : Monthdates) {
                    int Score = 0;
                    if (treeMap.containsKey(localDate))
                        Score = treeMap.get(localDate).getScore();

                    values.add(new Entry((float) date++, Score));
                    prev += Score;
                }

                setData(values, prev);

                if (treeMap.containsKey(LocalDate.parse(firstDayinMonth)))
                    mnthScore = treeMap.get(LocalDate.parse(firstDayinMonth)).getScore();

            } else {
                int date = 1, prev = 0;
                ArrayList<BarEntry> values = new ArrayList<>();
                List<BarEntry> val = new LinkedList<>();

                WeekandDtes valDates = testData.get(selectedWeek);
                Calendar calDates = Calendar.getInstance();
                calDates.setTime(valDates.getFromDate());
                calDates.add(Calendar.DATE, -1);

                String firstDay = Aktivo_Date_Format.format(calDates.getTime());
                calDates.setTime(valDates.isCurrentWeek() ? new Date() : valDates.getToDate());
                calDates.add(Calendar.DATE, -1);

                String todayDate = Aktivo_Date_Format.format(calDates.getTime());

                List<LocalDate> weekDates = getDates(firstDay, todayDate);

                for (LocalDate localDate : weekDates) {
                    int score = 0;
                    if (treeMap.containsKey(localDate)) {
                        score = treeMap.get(localDate).getScore();
                    }
                    values.add(new BarEntry((float) date++, score));
                    prev += score;
                }
                barChart(values, prev);

            }
        } catch (Exception e) {
            e.printStackTrace();
           /* if (month) {
                setData(null, 0);
            } else {
                barChart(null, 0);
            }*/
        }
    }

    public void updateSteps(Boolean month) {
        TreeMap<LocalDate, StepStats> treeMap = mViewModel.getLocalDateStepStatsTreeMap();
        try {

            if (month) {
                int date = 1, prev = 0;
                ArrayList<Entry> values = new ArrayList<>();

//                        values.add(new BarEntry(date++,stpData.getScore()));

                List<LocalDate> Monthdates = getDates(firstDayinMonth, lastdayinMonth);

                for (LocalDate localDate : Monthdates) {
                    int score = 0;
                    if (treeMap.containsKey(localDate))
                        score = treeMap.get(localDate).getValue();
                    values.add(new Entry(date++, score));
                    prev += score;
                }

                setData(values, prev);
                if (treeMap.containsKey(LocalDate.parse(firstDayinMonth)))
                    mntSteps = treeMap.get(LocalDate.parse(firstDayinMonth)).getValue();

            } else {
                int date = 1, prev = 0;
                ArrayList<BarEntry> values = new ArrayList<>();

                List<LocalDate> Monthdates = getDates(firstDayinWeek, lastdayinweek);

                for (LocalDate localDate : Monthdates) {
                    int score = 0;
                    if (treeMap.containsKey(localDate))
                        score = treeMap.get(localDate).getValue();
                    values.add(new BarEntry(date++, score));
                    prev += score;
                }

                barChart(values, prev);
                if (treeMap.containsKey(LocalDate.parse(Aktivo_Date_Format.format(new Date()))))
                    weekSteps = treeMap.get(LocalDate.parse(Aktivo_Date_Format.format(new Date()))).getValue();

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void updateSleep(Boolean Month) {

        TreeMap<LocalDate, SleepStats> treeMap = mViewModel.getLocalDateSleepStatsTreeMap();
        try {
            if (Month) {
                int date = 0, prev = 0;
                ArrayList<Entry> values = new ArrayList<>();

                List<LocalDate> Monthdates = getDates(firstDayinMonth, lastdayinMonth);

                for (LocalDate localDate : Monthdates) {
                    Float score = 0f;
                    if (treeMap.containsKey(localDate)) {
                        score = convertTime(treeMap.get(localDate).getValue()); //
                    }
                    values.add(new Entry(date++, score));
                    prev += score;

                }

                setData(values, prev);
                if (treeMap.containsKey(LocalDate.parse(firstDayinMonth)))
                    mnthSleep = treeMap.get(LocalDate.parse(firstDayinMonth)).getValue();

            } else {
                int date = 1, prev = 0;
                ArrayList<BarEntry> values = new ArrayList<>();
                List<LocalDate> Monthdates = getDates(firstDayinWeek, lastdayinweek);

                for (LocalDate localDate : Monthdates) {
                    Float score = 0f;
                    if (treeMap.containsKey(localDate)) {
                        score = convertTime(treeMap.get(localDate).getValue()); //
                    }
                    values.add(new BarEntry(date++, score));
                    prev += score;
                }

                barChart(values, prev);
                if (treeMap.containsKey(LocalDate.parse(firstDayinWeek)))
                    weekSleep = treeMap.get(LocalDate.parse(firstDayinWeek)).getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (Month) {
                setData(null, 0);
            } else {
                barChart(null, 0);
            }
        }

    }


    private void barChart(ArrayList<BarEntry> values, int Max) {
        getActivity().runOnUiThread(() -> {

            try {

                fragmentFitnessStatsBinding.chart1.invalidate();
                fragmentFitnessStatsBinding.chart1.clear();
                fragmentFitnessStatsBinding.chart1.removeAllViews();
                fragmentFitnessStatsBinding.chart1.notifyDataSetChanged();

                if (values.size() > 0) {
                    fragmentFitnessStatsBinding.chart1.setDrawBarShadow(false);
                    fragmentFitnessStatsBinding.chart1.setDrawValueAboveBar(true);

                    fragmentFitnessStatsBinding.chart1.getDescription().setEnabled(false);

                    BarChartRender barChartRender = new BarChartRender(fragmentFitnessStatsBinding.chart1, fragmentFitnessStatsBinding.chart1.getAnimator(), fragmentFitnessStatsBinding.chart1.getViewPortHandler());

                    barChartRender.setRadius(50);
//                    chart.setDefaultFocusHighlightEnabled(false);

                    fragmentFitnessStatsBinding.chart1.setDrawBarShadow(false);

                    fragmentFitnessStatsBinding.chart1.getAxisLeft().setDrawGridLines(false);
                    fragmentFitnessStatsBinding.chart1.getXAxis().setDrawGridLines(false);

                    fragmentFitnessStatsBinding.chart1.setHighlightFullBarEnabled(false);

                    fragmentFitnessStatsBinding.chart1.setRenderer(barChartRender);

                    // if more than 60 entries are displayed in the chart, no values will be
                    // drawn
                    fragmentFitnessStatsBinding.chart1.setMaxVisibleValueCount(60);


                    // scaling can now only be done on x- and y-axis separately
                    fragmentFitnessStatsBinding.chart1.setPinchZoom(false);
                    fragmentFitnessStatsBinding.chart1.setDoubleTapToZoomEnabled(false);
                    fragmentFitnessStatsBinding.chart1.setFitBars(true);
                    fragmentFitnessStatsBinding.chart1.setTouchEnabled(true);

                    IMarker marker = new CustomMarkerView(cnt, R.layout.custom_marker_view);
                    fragmentFitnessStatsBinding.chart1.setMarker(marker);

                    fragmentFitnessStatsBinding.chart1.setDrawGridBackground(false);

                    // chart.setDrawYLabels(false);

//                ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

                    XAxis xAxis = fragmentFitnessStatsBinding.chart1.getXAxis();
                    xAxis.setValueFormatter(new DayAxisValueFormatter(fragmentFitnessStatsBinding.chart1));

                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                    xAxis.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));
                    xAxis.setDrawGridLines(false);
                    xAxis.setGranularity(1f); // only intervals of 1 day
                    xAxis.setDrawLabels(true);

                    xAxis.setLabelCount(10);

                    xAxis.setAxisMaximum(8);

                    LimitLine ll1 = new LimitLine(Max / values.size(), String.valueOf(Max / values.size()));
                    ll1.setLineWidth(1f);
//                ll1.enableDashedLine(10f, 10f, 0f);
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setTextSize(10f);
                    ll1.setLineColor(R.color.cpb_black);
                    ll1.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));

                    int color = ContextCompat.getColor(getContext(), R.color.white);
                    xAxis.setGridColor(color);

                    YAxis leftAxis = fragmentFitnessStatsBinding.chart1.getAxisLeft();

                    leftAxis.removeAllLimitLines();
                    leftAxis.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));
                    leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
                    leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    leftAxis.setSpaceTop(15f);
                    leftAxis.setDrawAxisLine(true);
                    leftAxis.addLimitLine(ll1);
                    leftAxis.enableGridDashedLine(10f, 10f, 0f);

//            leftAxis.setEnabled(false);
                    leftAxis.setDrawLabels(false);
                    leftAxis.setDrawAxisLine(false);

                    leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                    YAxis rightAxis = fragmentFitnessStatsBinding.chart1.getAxisRight();
                    rightAxis.setDrawGridLines(false);

                    rightAxis.setEnabled(false);
//        rightAxis.setTypeface(tfLight);
                    rightAxis.setLabelCount(7, false);
//        rightAxis.setValueFormatter(custom);
                    rightAxis.setSpaceTop(15f);
                    rightAxis.setAxisMinimum(1f); // this replaces setStartAtZero(true)

                    Legend l = fragmentFitnessStatsBinding.chart1.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                    l.setDrawInside(false);
                    l.setForm(Legend.LegendForm.EMPTY);
                    l.setFormSize(9f);
                    l.setTextSize(11f);
                    l.setXEntrySpace(8f);
                    float val = 0f;
                    float indexValue = 1;

                    for (BarEntry bar : values) {
                        if (bar.getY() > val) {
                            val = bar.getY();
                            indexValue = bar.getX();
                        }
                    }
                    BarDataSet set1 = new BarDataSet(values, "");
                    set1.setDrawValues(false);
                    set1.setHighLightColor(Color.WHITE);

                    color = ContextCompat.getColor(getContext(), R.color.cpb_black);
                    set1.setColor(color);

                    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);

                    BarData data = new BarData(dataSets);
                    data.setBarWidth(0.5f);

                    data.setValueTextSize(10f);

//        data.setValueTypeface(tfLight);

                    fragmentFitnessStatsBinding.chart1.getAxisRight().removeAllLimitLines();

                    fragmentFitnessStatsBinding.chart1.getAxisRight().addLimitLine(ll1);

                    fragmentFitnessStatsBinding.chart1.setData(data);
                    fragmentFitnessStatsBinding.chart1.highlightValue(indexValue, 0);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sleepQuery(Map<LocalDate, SleepStats> localDateSleepStatsMap, Boolean Month) {
        try {
            TreeMap<LocalDate, SleepStats> treeMap = new TreeMap(localDateSleepStatsMap);
            mViewModel.setLocalDateSleepStatsTreeMap(treeMap);

            if (treeMap.containsKey(LocalDate.parse(Aktivo_Date_Format.format(new Date()))))
                todaySleep = treeMap.get(LocalDate.parse(Aktivo_Date_Format.format(new Date()))).getValue();

            if (treeMap.containsKey(LocalDate.parse(FirstDayinCurrentWeek)))
                weekSleep = treeMap.get(LocalDate.parse(FirstDayinCurrentWeek)).getValue();

            if (treeMap.containsKey(LocalDate.parse(FirstDayinCurrentMonth)))
                mnthSleep = treeMap.get(LocalDate.parse(FirstDayinCurrentMonth)).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sleepData = true;
            doThis();
        }
    }

    @Override
    public void scoreQuery(Map<LocalDate, ScoreStats> localDateScoreStatsMap, Boolean Month) {
        try {
            TreeMap<LocalDate, ScoreStats> treeMap = new TreeMap(localDateScoreStatsMap);
            mViewModel.setLocalDateScoreStatsTreeMap(treeMap);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String todayDate = Aktivo_Date_Format.format(cal.getTime());
            if (treeMap.containsKey(LocalDate.parse(todayDate)))
                todayScore = treeMap.get(LocalDate.parse(todayDate)).getScore();

            cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, selectedMonth - 1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String firstDay = Aktivo_Date_Format.format(cal.getTime());
            if (treeMap.containsKey(LocalDate.parse(firstDay)))
                weekScore = treeMap.get(LocalDate.parse(firstDay)).getScore();
            ;
            cal = Calendar.getInstance();

            cal.set(Calendar.WEEK_OF_YEAR, selectedWeek - 1);
            cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));

            firstDay = Aktivo_Date_Format.format(cal.getTime());

            if (treeMap.containsKey(LocalDate.parse(firstDay)))
                mnthScore = treeMap.get(LocalDate.parse(firstDay)).getScore();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scoreData = true;
            doThis();
        }
    }

    @Override
    public void Heartrate(Map<LocalDate, HeartRateStats> localHeartRateMaps, Boolean Month) {
        try {
            TreeMap<LocalDate, HeartRateStats> treeMap = new TreeMap(localHeartRateMaps);
            mViewModel.setLocalDateHeartRateStatsTreeMap(treeMap);

            if (treeMap.containsKey(LocalDate.parse(Aktivo_Date_Format.format(new Date()))))
                todayHR = treeMap.get(LocalDate.parse(Aktivo_Date_Format.format(new Date()))).getValue();

            if (treeMap.containsKey(LocalDate.parse(FirstDayinCurrentWeek)))
                WeekHR = treeMap.get(LocalDate.parse(FirstDayinCurrentWeek)).getValue();

            if (treeMap.containsKey(LocalDate.parse(FirstDayinCurrentMonth)))
                MonthHR = treeMap.get(LocalDate.parse(FirstDayinCurrentMonth)).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hrData = true;
            doThis();
        }
    }

    @Override
    public void challengesData(List<Team_Score_VH> lstTeamScore) {

    }



    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private List<LocalDate> getDates(String dateString1, String dateString2) {
        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
        DateFormat df1 = Aktivo_Date_Format;

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(LocalDate.parse(df1.format(cal1.getTime())));
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public class JavaScriptInterface {
        @JavascriptInterface
        public void callFromJS(String score) {

            fragmentFitnessStatsBinding.networkScore.loadUrl("<script>" +
                    "$(\".border-left1\").on('click',function(){" +
                    "alert('Workign ')" +
                    "              $(this).append($(\"<span style='position:absolute;top:-20px;left:-5px;white-space:nowrap;word-break:keep-all;" +
                    "font-weight:700;background-color:rgba(0,0,0,.8);color:white;padding:2px 5px;border-radius:5px' > \" + Math.round($(this).find(\".hiddenScore\").text()) + \"</span>\"));" +
                    "          }, function () {" +
                    "              $(this).find(\"span:last\").remove();" +
                    "          }" +
                    "</script>");

            Toast.makeText(getContext(), "SCore: " + score, Toast.LENGTH_LONG).show();
        }
    }

    private void setData(ArrayList<Entry> values, int Max) {
        getActivity().runOnUiThread(() -> {

            fragmentFitnessStatsBinding.lineChart.clear();
            fragmentFitnessStatsBinding.lineChart.invalidate();
            fragmentFitnessStatsBinding.lineChart.removeAllViews();
            fragmentFitnessStatsBinding.lineChart.notifyDataSetChanged();
            fragmentFitnessStatsBinding.lineChart.getAxisRight().removeAllLimitLines();

            try {
                if (values.size() > 0) {
                    LineDataSet set1;

                    LimitLine ll1 = new LimitLine(Max / values.size(), String.valueOf(Max / values.size()));
                    ll1.setLineWidth(1f);
//                ll1.enableDashedLine(10f, 10f, 0f);
                    ll1.setLineColor(R.color.cpb_black);
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setTextSize(10f);
                    ll1.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));

                    LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
                    ll2.setLineWidth(4f);
//                ll2.enableDashedLine(10f, 10f, 0f);
                    ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                    ll2.setTextSize(10f);
                    ll2.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));
                    YAxis yAxis;
                    {
                        // // Y-Axis Style // //
                        yAxis = fragmentFitnessStatsBinding.lineChart.getAxisLeft();
                        yAxis.setDrawGridLines(false);

                        yAxis.removeAllLimitLines();

                        // disable dual axis (only use LEFT axis)
                        yAxis.setDrawLabels(false);
                        yAxis.setDrawAxisLine(false);

                        // horizontal grid lines
                        yAxis.enableGridDashedLine(10f, 10f, 0f);
                        yAxis.setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins));
                        // axis range
                        yAxis.setAxisMinimum(0f);
//                        yAxis.setAxisMaximum(Max + );


                        // add limit lines
                        yAxis.setDrawLimitLinesBehindData(false);

                        yAxis.addLimitLine(ll1);
                        yAxis.addLimitLine(ll2);
                    }

                    if (fragmentFitnessStatsBinding.lineChart.getData() != null &&
                            fragmentFitnessStatsBinding.lineChart.getData().getDataSetCount() > 0) {
                        set1 = (LineDataSet) fragmentFitnessStatsBinding.lineChart.getData().getDataSetByIndex(0);

                        set1.setDrawHighlightIndicators(false);
                        set1.setValues(values);

                        set1.notifyDataSetChanged();
                        fragmentFitnessStatsBinding.lineChart.getData().notifyDataChanged();
                        fragmentFitnessStatsBinding.lineChart.notifyDataSetChanged();
                    } else {
                        // create a dataset and give it a type
                        set1 = new LineDataSet(values, "");

                        set1.setDrawIcons(false);

                        // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f);

                        // black lines and points
                        set1.setColor(Color.BLACK);
                        set1.setCircleColor(Color.BLACK);

                        // line thickness and point size
                        set1.setLineWidth(1f);
                        set1.setCircleRadius(3f);

                        // draw points as solid circles
                        set1.setDrawCircleHole(false);

                        // customize legend entry
                        set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                        set1.setFormSize(15.f);

                        // text size of values
                        set1.setValueTextSize(9f);
                        set1.setDrawValues(false);

                        IMarker marker = new CustomMarkerView(cnt, R.layout.custom_marker_view);

                        fragmentFitnessStatsBinding.lineChart.setMarker(marker);

                        // draw selection line as dashed
//            set1.enableDashedHighlightLine(10f, 5f, 0f);

                        set1.setHighLightColor(Color.BLACK);
                        // set the filled area
                        set1.setDrawFilled(false);
                        set1.setFillFormatter((dataSet, dataProvider) -> fragmentFitnessStatsBinding.lineChart.getAxisLeft().getAxisMinimum());
                        set1.setDrawHorizontalHighlightIndicator(false);

                        set1.setFillColor(Color.BLACK);
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

                        dataSets.add(set1); // add the data sets

                        // create a data object with the data sets
                        LineData data = new LineData(dataSets);
                        data.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return "" + (int) value;
                            }
                        });

                        // set data
                        fragmentFitnessStatsBinding.lineChart.setData(data);
                        float val = 0f;
                        float indexValue = 0f;
                        for (Entry e : values) {
                            if (e.getY() > val) {
                                val = e.getY();
                                indexValue = e.getX();
                            }
                        }

                        fragmentFitnessStatsBinding.lineChart.highlightValue(indexValue, 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}