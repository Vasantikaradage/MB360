package com.csform.android.MB360.fitness;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aktivolabs.aktivocore.data.models.badges.BadgeTypeEnum;
import com.aktivolabs.aktivocore.data.models.heartrate.HeartRateStats;
import com.aktivolabs.aktivocore.data.models.score.ScoreElement;
import com.aktivolabs.aktivocore.data.models.score.ScoreStats;
import com.aktivolabs.aktivocore.data.models.sleep.SleepStats;
import com.aktivolabs.aktivocore.data.models.steps.StepStats;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FitnessHomeFragmentBinding;
import com.csform.android.MB360.fitness.AktivoData.AktivoFetchingData;
import com.csform.android.MB360.fitness.AktivoData.FetchAktivoData;
import com.csform.android.MB360.fitness.AktivoData.Team_Score_VH;
import com.csform.android.MB360.fitness.response.AktivoChallengeResponse;
import com.csform.android.MB360.fitness.retrofit.FitnessApi;
import com.csform.android.MB360.fitness.retrofit.FitnessRetrofitClient;
import com.csform.android.MB360.fitness.utils.AKtivoCommentaryStatic;
import com.csform.android.MB360.fitness.viewmodel.AktivoChallengesViewModel;
import com.csform.android.MB360.insurance.DashBoardActivity;
import com.csform.android.MB360.insurance.DashboardItemClickListener;
import com.csform.android.MB360.insurance.profile.repository.ProfileViewModel;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.csform.android.MB360.utilities.AppServerConstants;
import com.csform.android.MB360.wellness.WellnessDashBoardActivity;

import org.threeten.bp.LocalDate;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FitnessHomeFragment extends Fragment implements DashboardItemClickListener, FetchAktivoData, View.OnClickListener {

    FitnessHomeFragmentBinding fitnessHomeFragmentBinding;
    AktivoChallengesViewModel aktivoChallengesViewModel;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    String ScoreMsg = "";
    int Score2 = 0;
    View view;
    private List<String> SleepTime, StepData, SedentaryData, StrExData, Scores1;
    LoadSessionViewModel loadSessionViewModel;
    private AktivoFetchingData aktivoFetchingData;

    ProfileViewModel profileViewModel;
    private String email = "";
    private int stepWalk = 0, todaySteps = 0, yestSteps = 0;
    private String stepMsg = "";
    private Boolean SlpData = false, ScoreData = false, ExData = false;
    private String SleepMSg = "Your Sleep is Inadequate\n" +
            "Adequate sleep is essential for a healthy lifestyle.";

    private String SleepImpact = "NEGATIVE", ExerciseImpact, SedentaryImpact;
    private Context mContext;
    private TeamScoresAdapter rvTeamScore_adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        aktivoFetchingData = new AktivoFetchingData(context, this);
        this.mContext = context;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fitnessHomeFragmentBinding = FitnessHomeFragmentBinding.inflate(inflater, container, false);
        view = fitnessHomeFragmentBinding.getRoot();
        loadSessionViewModel = new ViewModelProvider(this).get(LoadSessionViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        aktivoChallengesViewModel = new ViewModelProvider(this).get(AktivoChallengesViewModel.class);




        tabClickListerner();



        loadData();

        AppCompatImageView imageViewTarget = new AppCompatImageView(mContext);

        //...now load that gif which we put inside the drawable folder here with the help of Glide



        Glide.with(this)
                .load(R.drawable.loader)
                .placeholder(R.drawable.loader)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(imageViewTarget));

        return view;


    }

    private void tabClickListerner() {
        //on clicked Wellness
        fitnessHomeFragmentBinding.titleMenu.rlTabInsurance.setOnClickListener(view1 -> {
            Intent insuranceIntent = new Intent(requireActivity(), DashBoardActivity.class);
            startActivity(insuranceIntent);
            requireActivity().finish();
        });

        //on clicked Wellness
        fitnessHomeFragmentBinding.titleMenu.rlTabWellness.setOnClickListener(view1 -> {
            Intent insuranceIntent = new Intent(requireActivity(), WellnessDashBoardActivity.class);
            startActivity(insuranceIntent);
            requireActivity().finish();
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(false /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent insuranceIntent = new Intent(requireActivity(), DashBoardActivity.class);
                startActivity(insuranceIntent);
                requireActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }


    private void loadData() {
        initData();
        // showLoading.showLoading(View.VISIBLE, "Loading");
    }

    private void initData() {
        try {

            SleepTime = new ArrayList<>();
            StepData = new ArrayList<>();
            SedentaryData = new ArrayList<>();
            StrExData = new ArrayList<>();
            Scores1 = new ArrayList<>();

            Calendar weekCal = Calendar.getInstance();
            weekCal.setTimeZone(TimeZone.getTimeZone("UTC"));

            weekCal.add(Calendar.DATE, -1);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            String lastDate = sdf1.format(weekCal.getTime());

            weekCal.add(Calendar.DATE, 1);
            String todayDate = sdf1.format(weekCal.getTime());
            sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            aktivoFetchingData.ScoreQuery(lastDate, sdf1.format(new Date()));
            weekCal = Calendar.getInstance();
            ScoreMsg = getResources().getString(R.string.aktivo_unable);

            weekCal.add(Calendar.DAY_OF_WEEK, -(weekCal.get(Calendar.DAY_OF_WEEK) - 1));
            String firstDay = sdf1.format(weekCal.getTime());

            weekCal = Calendar.getInstance();
            weekCal.add(Calendar.DATE, -1);
            String LastDay = sdf1.format(weekCal.getTime());
            aktivoFetchingData.SleepQuery(LastDay, sdf1.format(new Date()));
            aktivoFetchingData.StepQuery(firstDay, sdf1.format(new Date()));

            Calendar cal = Calendar.getInstance();


            loadSessionData();


          //  String _id = getActivity().getSharedPreferences(AppServerConstants.adminsettings, Context.MODE_PRIVATE).getString("_id", null);

            String _id="63afd1d89ea2a604a96123d2";
            int year = cal.get(Calendar.YEAR);
           /* String url = "https://api.aktivolabs.com/api/users/" + _id + "?include=leaderboards&" +
                    "startDate=" + year + "-01-01&endDate=" + year + "-12-31";*/

            String url = _id + "?include=leaderboards&" +
                    "startDate=" + year + "-01-01&endDate=" + year + "-12-31";

            String token = getActivity().getSharedPreferences(AppServerConstants.adminsettings,
                    Context.MODE_PRIVATE).getString("access_token", null);


           // aktivoChallengesViewModel.getAktivoChanllengerInfo(token, url);
            //  new FetchAktivoChallenges(getActivity(), this).execute(token, url);
            setListeners();




            aktivoFetchingData.badgesMutableLiveData.observe(getViewLifecycleOwner(), dailyBadge -> {
                try {
                    if (dailyBadge.getBadgeType() != null) {
                        fitnessHomeFragmentBinding.singleBadge.txtHeadline.setVisibility(View.VISIBLE);
                        fitnessHomeFragmentBinding.singleBadge.txtHeadline.setText("You have been awarded");
                        fitnessHomeFragmentBinding.singleBadge.txtRewards.setText(dailyBadge.getBadgeType().getTitle());
                        fitnessHomeFragmentBinding.singleBadge.txtRewardSummary.setText(dailyBadge.getBadgeType().getCongratsMessageShort());
                        fitnessHomeFragmentBinding.singleBadge.imgBadge.setImageResource(setBadgeImage(dailyBadge.getBadgeType().getBadgeTypeEnum()));
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            });


        } catch (Exception e) {
            Log.d("@@","@@"+e);

        }
    }

   /* private void getAktivoChallengeInfo(String url) {

           // fitnessHomeFragmentBinding.llLoader.loadingBar.setVisibility(View.GONE);
            try {
                FitnessApi aktivoAPI = FitnessRetrofitClient.getAktivoRetrofit().create(FitnessApi.class);

                Single<AktivoChallengeResponse> aktivoBeanProfileSingle = aktivoAPI.getAktivoChallenge();
                compositeDisposable.add(aktivoBeanProfileSingle.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .map(result -> result)
                        .subscribeWith(new DisposableSingleObserver<AktivoChallengeResponse>() {
                            @Override
                            public void onSuccess(AktivoChallengeResponse aktivoBeanProfile) {
                                Log.d("@@","@@"+aktivoBeanProfile.getChallengeItemsInfos().get(0).challenge_name);


                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("Rx Aktivo id Error", "Errorr 2" + e.toString());

                                //showOnBoard();
                               // authenticationFailed();
                            }
                        }));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
*/

    private int setBadgeImage(BadgeTypeEnum badgeTypeEnum) {


        switch (badgeTypeEnum) {
            case ACHIEVER:
                return R.drawable.ic_achiever_badge;
            case CONTENDER:
                return R.drawable.ic_contender_badge;
            case CHALLENGER:
                return R.drawable.ic_challenger_badge;
        }
        return 0;
    }

    private void setListeners() {

        fitnessHomeFragmentBinding.btnSedentary.setOnClickListener(this);
        fitnessHomeFragmentBinding.llSleep.setOnClickListener(this);
        fitnessHomeFragmentBinding.llExercise.setOnClickListener(this);
        fitnessHomeFragmentBinding.llLightActivity.setOnClickListener(this);
        fitnessHomeFragmentBinding.llLearnMoreBtn.setOnClickListener(this);

        fitnessHomeFragmentBinding.btnSleep.setOnClickListener(this);
        fitnessHomeFragmentBinding.btnExercise.setOnClickListener(this);
        fitnessHomeFragmentBinding.btnLightActivity.setOnClickListener(this);
        fitnessHomeFragmentBinding.llSedentary.setOnClickListener(this);

    }

    private void loadSessionData() {

        loadSessionViewModel.getLoadSessionData().observe(getActivity(), loadSessionResponse -> {
            String groupChild = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
            GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);
            String oeGrpBasInfoSrNo = groupGMCPolicyEmployeeDatum.getOeGrpBasInfSrNo();
            String employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();

            profileViewModel.getProfile(groupChild, oeGrpBasInfoSrNo, employeeSrNo);

            profileViewModel.getProfileData().observe(getActivity(), profileResponse -> {

                if (profileResponse != null) {
                    email = profileResponse.getUserPersonalDetails().getEMailId();

                }


            });
        });
    }


    @Override
    public void onDashboardItemClicked(String menuName) {

    }

    @Override
    public void stepQuery(Map<LocalDate, StepStats> localDateSleepStatsMap, Boolean Month) {
        try {
            stepWalk = 0;
            todaySteps = 0;
            yestSteps = 0;
            TreeMap<LocalDate, StepStats> treeMap = new TreeMap<>(localDateSleepStatsMap);

            if (treeMap.size() > 0) {

                Set<LocalDate> keySet = treeMap.keySet();
                for (LocalDate localDate : keySet) {
//                    Log.e(TAG, "Steps stats: " + "Date: " + localDate + " Steps: " + localDateSleepStatsMap.get(localDate).getValue());
                    stepWalk += treeMap.get(localDate).getValue();
                }

                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

                String todayDate = sdf1.format(new Date());
                Calendar weekCal = Calendar.getInstance();
                weekCal.add(Calendar.DATE, -1);
                String yestDate = sdf1.format(weekCal.getTime());

                Log.e(sdf1.format(weekCal.getTime()), sdf1.format(new Date()));

             /*   for (Map.Entry<LocalDate, StepStats> pair : treeMap.entrySet()) {
                    StepStats stpData = pair.getValue();*/

//                    if (pair.getKey().toString().equals(todayDate))
                if (treeMap.containsKey(LocalDate.parse(todayDate)))
                    todaySteps = treeMap.get(LocalDate.parse(todayDate)).getValue();
                else
                    todaySteps = 0;

//                    LocalDate yestKey = pair.getKey();

//                    if (yestKey.toString().equals(yestDate))
                if (treeMap.containsKey(LocalDate.parse(yestDate)))
                    yestSteps = treeMap.get(LocalDate.parse(yestDate)).getValue();
                else yestSteps = 0;
//                }

                if (yestSteps > todaySteps) {

                    int steps = yestSteps - todaySteps;
                    double percent;
                    percent = steps * 100 / yestSteps;
                    stepMsg = MessageFormat.format("{0} % less", "" + Math.round(percent));

                } else {
                    int steps = todaySteps - yestSteps;
                    double percent = steps * 100 / yestSteps;
                    stepMsg = MessageFormat.format("{0} % more", "" + Math.round(percent));
                }
                fitnessHomeFragmentBinding.txtSteps.setText(MessageFormat.format("Today you have covered {0} steps than yesterday.That''s" +
                                " a total of {1} steps this week!",
                        stepMsg, String.valueOf(stepWalk)));
            } else
                fitnessHomeFragmentBinding.txtSteps.setText(getString(R.string.step_unable));
        } catch (Exception e) {
            e.printStackTrace();
            fitnessHomeFragmentBinding.txtSteps.setText(getActivity().getString(R.string.step_unable));
        } finally {
            ExData = true;
            hideLoader();
        }

    }

    private void hideLoader() {
        if (ExData & SlpData & ScoreData) {

            //   showLoading.hideView();
            fitnessHomeFragmentBinding.nstScroll.setVisibility(View.VISIBLE);
            // swplayout.setRefreshing(false);
        }
    }

    @Override
    public void sleepQuery(Map<LocalDate, SleepStats> localDateStepStatsMap, Boolean Month) {
        try {
            for (LocalDate str : localDateStepStatsMap.keySet()) {
                Log.e("SleepKey", str.toString());
            }

            int i = 0;
            for (SleepStats stpData : localDateStepStatsMap.values()) {

                Log.e("Sleepdata", stpData.getValue() + "");

               /* double sleeptime = Math.floor(stpData.getValue()/60);

                int min =(int) sleeptime; //
                int hours = min / 60;

                Date d = new Date(hours * 1000L);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm"); // HH for 0-23
                df.setTimeZone(TimeZone.getTimeZone("IST"));
                String time = df.format(sleeptime);*/

                SleepTime.add(convSectohours(stpData.getValue()));
                i++;
                SleepImpact = stpData.getImpact();

            }
            String[] timings = SleepTime.
                    get(1).split(":");

            SleepMSg = MessageFormat.format(Objects.requireNonNull(Objects.requireNonNull(mContext)).getResources().getString(R.string.sleep_comment), timings[0], timings[1]);

            fitnessHomeFragmentBinding.txtSleep.setText(SleepMSg);
        } catch (Exception e) {

            SleepMSg = "Your Sleep is Inadequate\n" +
                    "Adequate sleep is essential for a healthy lifestyle.";
            e.printStackTrace();
        }
        SlpData = true;

        hideLoader();
    }

    private String convSectohours(long Val) {

        int hours = (int) Val / 3600;
        int remainder = (int) Val % 3600;
        int mins = remainder / 60;

        return hours + ":" + mins;
    }


    @Override
    public void scoreQuery(Map<LocalDate, ScoreStats> localDateScoreStatsMap, Boolean Month) {
        try {
            for (ScoreStats stpData : localDateScoreStatsMap.values()) {

                Scores1.add(String.valueOf(stpData.getScore()));
                if (Scores1.size() != 0) {

                    try {
                        ScoreElement sec = stpData.getScoreSb(); //
                        if (sec.getValue() > 0) {
                            SedentaryData.add(convSectohours(sec.getValue()));
                        }
                        SedentaryImpact = sec.getImpact();

                        ScoreElement ExData = stpData.getScoreMvpa();
                        if (ExData.getValue() > 0) {
                            StrExData.add(convSectohours(ExData.getValue()));
                        }

                        ExerciseImpact = ExData.getImpact(); // Exercise DATa

                        ScoreElement LightActivity = stpData.getScoreLipa();

                        if (LightActivity.getValue() > 0) {
                            long longVal = LightActivity.getValue();
                            int hours = (int) longVal / 3600;
                            int remainder = (int) longVal % 3600;
                            int mins = remainder / 60;
                            StepData.add(hours + ":" + mins);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                int score = Integer.valueOf(Scores1.get(0));
          /*  if (score < 60)
                ScoreMsg = AKtivoCommentaryStatic.AKTIVO_SCORE_BELOW_60_C;
            else*/
                if (score >= 60 && score <= 65)
                    ScoreMsg = AKtivoCommentaryStatic.getSixtyplusAA;
                else if (score >= 65 && score <= 69)
                    ScoreMsg = AKtivoCommentaryStatic.sixtyfiveplusB;
                else if (score >= 70 && score <= 74)
                    ScoreMsg = AKtivoCommentaryStatic.seventyplusC;
                else if (score >= 75 && score <= 79)
                    ScoreMsg = AKtivoCommentaryStatic.seventyfiveplus_A;
                else if (score >= 80)
                    ScoreMsg = AKtivoCommentaryStatic.EightyB;
                else
                    ScoreMsg = AKtivoCommentaryStatic.AKTIVO_SCORE_BELOW_60_A;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ScoreData = true;
            hideLoader();
        }


        Score2 = 0;
        fitnessHomeFragmentBinding.btnSedentary.performClick();
        fitnessHomeFragmentBinding.txtScoreMsg.setText(ScoreMsg);

        if (Scores1.size() > 0) {

            fitnessHomeFragmentBinding.GIFimage.setVisibility(View.GONE);

            int Score = Integer.parseInt(Scores1.get(0));
            Handler handler1 = new Handler();

            for (int i = 1; i <= Score; i++) {

                handler1.postDelayed(() -> {
                    if (Score2 <= Score)
                        Score2++;
                    fitnessHomeFragmentBinding.txtScore.setText(MessageFormat.format("{0}.", Score2));
                }, i * 50);
            }

            fitnessHomeFragmentBinding.cirlce.setProgress(Score);
            fitnessHomeFragmentBinding.cirlce.setProgressColor(mContext.getResources().getColor(R.color.cpb_black));
            fitnessHomeFragmentBinding.cirlce.useRoundedCorners(true);
            fitnessHomeFragmentBinding.cirlce.setProgressWidth(14);
            fitnessHomeFragmentBinding.cirlce.showProgressText(false);

        } else {
            fitnessHomeFragmentBinding.GIFimage.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void Heartrate(Map<LocalDate, HeartRateStats> localHeartRateMaps, Boolean Month) {

    }

    @Override
    public void challengesData(List<Team_Score_VH> lstTeamScore) {

        List<Team_Score_VH> lstTeamScore1 = new ArrayList<>();
        String id = mContext.getSharedPreferences(AppServerConstants.adminsettings, Context.MODE_PRIVATE).getString("_id", null);
        for (int i = 0; i < lstTeamScore.size(); i++) {
            Team_Score_VH teamScoreVh = lstTeamScore.get(i);
            if (teamScoreVh.get_id().equals(id))
                lstTeamScore1.add(teamScoreVh);
            else if (lstTeamScore1.size() < 3)
                lstTeamScore1.add(teamScoreVh);

        }

        /*lstTeamScore.add(new Team_Score_VH(1, "Shreyas Khole", "50", true));
        lstTeamScore.add(new Team_Score_VH(2, "Shrikant Pawar", "32", false));
        lstTeamScore.add(new Team_Score_VH(3, "Sachin Rawool", "66", false));*/

        rvTeamScore_adapter = new TeamScoresAdapter(lstTeamScore1, mContext);

        fitnessHomeFragmentBinding.rvTeamScore.setAdapter(rvTeamScore_adapter);
        fitnessHomeFragmentBinding.rvTeamScore.setLayoutManager(new LinearLayoutManager(mContext));
        fitnessHomeFragmentBinding.rvTeamScore.setFocusable(false);
    }

   /* @Override
    public void challengeList(List<Challenge_layout_items> lstChallengeFiltered) {
        mViewModel.setLstChallengeFiltered(lstChallengeFiltered);
        this.lstChallengeItems = mViewModel.getLstChallengeFiltered();
        Log.d("Data"," Lisit Count"+lstChallengeFiltered.size());

        switch(lstChallengeFiltered.size())
        {
            case 0:
                llTeamScore.setVisibility(View.GONE);
                v4.setVisibility(View.GONE);
                break;
            default:
                llTeamScore.setVisibility(View.VISIBLE);
                v4.setVisibility(View.VISIBLE);
                break;

        }
        llTeamScore.setOnClickListener(v -> {
            Intent sendIntent = new Intent(mContext, Compete_details_activity.class);

            Bundle args = new Bundle();

            args.putSerializable("challengeList", (Serializable)
                    lstChallengeFiltered.get(0).getTeamMembers());
            args.putString("Date_time", lstChallengeFiltered.get(0).getChallenge_time());
            args.putString("ImgString", lstChallengeFiltered.get(0).getImgURL());
            args.putString("Title", lstChallengeFiltered.get(0).getChallenge_name());
            args.putString("description", lstChallengeFiltered.get(0).getDescription());

            sendIntent.putExtra("Bundle", args);
            mContext.startActivity(sendIntent);
        });
    }*/

    @Override
    public void NetworkData(HashMap<String, Integer> NetworkAvg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_exercise:
            case R.id.btn_exercise:

                fitnessHomeFragmentBinding.llSleep.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llSedentary.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llLightActivity.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llExercise.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons));

                fitnessHomeFragmentBinding.btnSleep.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnSedentary.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnLightActivity.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnExercise.setTextColor(mContext.getResources().getColor(R.color.cpb_black));
                fitnessHomeFragmentBinding.imgActivity.setImageResource(R.drawable.ic_exercise_black);

                fitnessHomeFragmentBinding.imgSleep.setImageResource(R.drawable.ic_sleep_white);
                fitnessHomeFragmentBinding.imgExercise.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_black));
                fitnessHomeFragmentBinding.imgLightActivity.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_white));
                fitnessHomeFragmentBinding.sedenary.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_white));
                try {
                    String[] timings;
                    if (StrExData.size() > 1) {
                        timings = StrExData.get(1).split(":");
                        fitnessHomeFragmentBinding.workTime.setText(MessageFormat.format("{0} hours {1} minutes", timings[0], timings[1]));
                    } else {
                        fitnessHomeFragmentBinding.workTime.setText("0 minutes");
                    }
                    if (StrExData.size() > 0) {
                        timings = StrExData.get(0).split(":");
                        fitnessHomeFragmentBinding.timeYest.setText(MessageFormat.format("{0} hours {1} minutes", timings[0], timings[1]));
                    } else {
                        fitnessHomeFragmentBinding.timeYest.setText(" 0 minutes");
                    }

                    fitnessHomeFragmentBinding.txtImpact.setText(ExerciseImpact);
                    if (ExerciseImpact.equals("POSITIVE")) {
                        fitnessHomeFragmentBinding.imgArrow.setRotation(180f);
                        fitnessHomeFragmentBinding.imgArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.greenlightbg));
                    } else {
                        fitnessHomeFragmentBinding.imgArrow.setRotation(0f);
                        fitnessHomeFragmentBinding.imgArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.red));
                    }

                } catch (Exception e) {
                    fitnessHomeFragmentBinding.timeYest.setText(" 0 minutes");
                    fitnessHomeFragmentBinding.workTime.setText("0 minutes");

                    Log.e("Step Error", e.toString());

                }
                fitnessHomeFragmentBinding.txtActivity.setText("Exercise Time Yesterday");
                fitnessHomeFragmentBinding.selectedItem.setText("Exercise Time Today");

                break;

            case R.id.llSleep:
            case R.id.btn_sleep:

                fitnessHomeFragmentBinding.llExercise.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llSedentary.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llLightActivity.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llSleep.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons));

                fitnessHomeFragmentBinding.btnExercise.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnSedentary.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnLightActivity.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnSleep.setTextColor(mContext.getResources().getColor(R.color.cpb_black));

                fitnessHomeFragmentBinding.imgActivity.setImageResource(R.drawable.ic_sleep_black);

                fitnessHomeFragmentBinding.imgSleep.setImageResource(R.drawable.ic_sleep_black);
                fitnessHomeFragmentBinding.imgExercise.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_white));
                fitnessHomeFragmentBinding.imgLightActivity.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_white));
                fitnessHomeFragmentBinding.sedenary.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_white));
                try {

                    String[] timings;
                    if (SleepTime.size() > 1) {
                        timings = SleepTime.get(1).split(":");
                        fitnessHomeFragmentBinding.workTime.setText(MessageFormat.format("{0} hours {1} minutes", timings[0], timings[1]));
                    } else {
                        fitnessHomeFragmentBinding.workTime.setText("0 minutes");
                        fitnessHomeFragmentBinding.timeYest.setText("0 minutes");
                    }

                    if (SleepTime.size() > 0) {
                        timings = SleepTime.get(0).split(":");
                        fitnessHomeFragmentBinding.timeYest.setText(MessageFormat.format("{0} hours {1} minutes", timings[0], timings[1]));
                    } else {
                        fitnessHomeFragmentBinding.timeYest.setText("0 minutes");
                    }

                    fitnessHomeFragmentBinding.txtImpact.setText(SleepImpact);
                    if (SleepImpact.equals("POSITIVE")) {
                        fitnessHomeFragmentBinding.imgArrow.setRotation(180f);
                        fitnessHomeFragmentBinding.imgArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.greenlightbg));
                    } else {
                        fitnessHomeFragmentBinding.imgArrow.setRotation(0f);
                        fitnessHomeFragmentBinding.imgArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.red));
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
                fitnessHomeFragmentBinding.txtActivity.setText("Sleep Time Yesterday");
                fitnessHomeFragmentBinding.selectedItem.setText("Sleep Time Today");

                break;

            case R.id.ll_light_activity:
            case R.id.btn_light_activity:

                fitnessHomeFragmentBinding.llExercise.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llSedentary.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llSleep.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llLightActivity.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons));

                fitnessHomeFragmentBinding.btnSleep.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnSedentary.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnExercise.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnLightActivity.setTextColor(mContext.getResources().getColor(R.color.cpb_black));

                fitnessHomeFragmentBinding.imgSleep.setImageResource(R.drawable.ic_sleep_white);
                fitnessHomeFragmentBinding.imgExercise.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_white));
                fitnessHomeFragmentBinding.imgLightActivity.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_black));
                fitnessHomeFragmentBinding.imgActivity.setImageResource(R.drawable.ic_footprint);
                fitnessHomeFragmentBinding.imgActivity.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_black));
                fitnessHomeFragmentBinding.sedenary.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_white));
                try {

                    String[] timings;
                    timings = StepData.get(0).split(":");
                    fitnessHomeFragmentBinding.timeYest.setText(MessageFormat.format("{0} hours {1} minutes", timings[0], timings[1]));


                    if (StepData.size() > 1) {
                        timings = StepData.get(1).split(":");
                        fitnessHomeFragmentBinding.workTime.setText(MessageFormat.format("{0} hours {1} minutes", timings[0], timings[1]));
                    } else {
                        fitnessHomeFragmentBinding.timeYest.setText("0 minutes");
                    }
//                    workTime.setText(StepData.get(0));
//                    timeYest.setText(StepData.get(1));

                } catch (Exception e) {
                    Log.e("Step Error", e.toString());
                    fitnessHomeFragmentBinding.workTime.setText("0 minutes");
                    fitnessHomeFragmentBinding.timeYest.setText("0 minutes");
                }

                fitnessHomeFragmentBinding.txtActivity.setText("Light Activity Time Yesterday");
                fitnessHomeFragmentBinding.selectedItem.setText("Light Activity Time Today");

                break;

            case R.id.btn_sedentary:

                fitnessHomeFragmentBinding.llExercise.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llSleep.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llLightActivity.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fitnessHomeFragmentBinding.llSedentary.setBackground(mContext.getResources().getDrawable(R.drawable.fitness_buttons));

                fitnessHomeFragmentBinding.btnSleep.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnExercise.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnLightActivity.setTextColor(mContext.getResources().getColor(R.color.white));
                fitnessHomeFragmentBinding.btnSedentary.setTextColor(mContext.getResources().getColor(R.color.cpb_black));

                fitnessHomeFragmentBinding.txtActivity.setText(mContext.getString(R.string.sedentary_time));
                fitnessHomeFragmentBinding.selectedItem.setText(mContext.getString(R.string.sedentary_today));
                fitnessHomeFragmentBinding.imgActivity.setImageResource(R.drawable.sedentary);

                fitnessHomeFragmentBinding.imgSleep.setImageResource(R.drawable.ic_sleep_white);
                fitnessHomeFragmentBinding.imgExercise.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_white));
                fitnessHomeFragmentBinding.imgLightActivity.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_white));
                fitnessHomeFragmentBinding.sedenary.setColorFilter(ContextCompat.getColor(mContext, R.color.cpb_black));
                try {
                    String[] timings;
                    timings = SedentaryData.get(1).split(":");
                    fitnessHomeFragmentBinding.workTime.setText(MessageFormat.format("{0} hours {1} minutes", timings[0], timings[1]));
                    if (SedentaryData.size() > 1) {
                        timings = SedentaryData.get(0).split(":");
                        fitnessHomeFragmentBinding.timeYest.setText(MessageFormat.format("{0} hours {1} minutes", timings[0], timings[1]));
                    } else {
                        fitnessHomeFragmentBinding.timeYest.setText("0 minutes");
                    }

                    fitnessHomeFragmentBinding.txtImpact.setText(SedentaryImpact);
                    if (SedentaryImpact.equals("POSITIVE")) {
                        fitnessHomeFragmentBinding.imgArrow.setRotation(180f);
                        fitnessHomeFragmentBinding.imgArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.greenlightbg));
                    } else {
                        fitnessHomeFragmentBinding.imgArrow.setRotation(0f);
                        fitnessHomeFragmentBinding.imgArrow.setColorFilter(ContextCompat.getColor(mContext, R.color.red));
                    }

                } catch (Exception e) {
                    fitnessHomeFragmentBinding.workTime.setText("0 minutes");
                    fitnessHomeFragmentBinding.timeYest.setText("0 minutes");

                    Log.e("Step Error", e.toString());
                }
                break;

            case R.id.llLearnMore_btn:

                // mContext.startActivity(new Intent(mContext, AktivoScoreHTML.class));

                break;
        }

    }
}
