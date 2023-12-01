package com.csform.android.MB360.fitness.AktivoData;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.aktivolabs.aktivocore.data.models.Stats;
import com.aktivolabs.aktivocore.data.models.badges.BadgeSummary;
import com.aktivolabs.aktivocore.data.models.badges.DailyBadge;
import com.aktivolabs.aktivocore.data.models.badges.HistoryBadgeType;
import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTracker;
import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTrackerAuthUrlInfo;
import com.aktivolabs.aktivocore.data.models.queries.BadgeByDateQuery;
import com.aktivolabs.aktivocore.data.models.queries.BadgeHistoryQuery;
import com.aktivolabs.aktivocore.data.models.queries.BadgeSummaryQuery;
import com.aktivolabs.aktivocore.data.models.queries.Query;
import com.aktivolabs.aktivocore.data.models.queries.ScoreQuery;
import com.aktivolabs.aktivocore.data.models.queries.SleepQuery;
import com.aktivolabs.aktivocore.data.models.score.ScoreElement;
import com.aktivolabs.aktivocore.data.models.score.ScoreStats;
import com.aktivolabs.aktivocore.data.models.sleep.SleepStats;
import com.aktivolabs.aktivocore.managers.AktivoManager;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class BadgesRepo {

    public static BadgesRepo mInstance;
    private Context mContext;

    private AktivoManager mAktivoManager;
    private CompositeDisposable compositeDisposable;
    public SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public MutableLiveData<DailyBadge> dailyBadgeMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<String> yesterdayLightActivity = new MutableLiveData<>();

    public MutableLiveData<AktivoDataStore> aktivoDataStoreMutableLiveData = new MutableLiveData<>();
    private AktivoDataStore aktivoDataStore = new AktivoDataStore();
    public MutableLiveData<TreeMap<LocalDate, HistoryBadgeType>> historyBadges = new MutableLiveData<>();
    public MutableLiveData<BadgeSummary> badgeSummaryMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<BadgeSummary> badgeSummaryforPeriod = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(MutableLiveData<Boolean> isLoading) {
        this.isLoading = isLoading;
    }

    public BadgesRepo(Context context) {
        this.mContext = context;
        mAktivoManager = AktivoManager.getInstance(context);
        compositeDisposable = new CompositeDisposable();
        getDailyBadge();
        getAllData();
        isLoading.postValue(true);
    }

    public static BadgesRepo getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BadgesRepo(context);
        }
        return mInstance;
    }

    private void getDailyBadge() {
        Calendar weekCal = Calendar.getInstance();
        weekCal.add(Calendar.DATE, -1);
        String toDate = sdf1.format(weekCal.getTime());
        BadgeByDateQuery badgeByDateQuery = new BadgeByDateQuery(toDate);

        mAktivoManager.queryBadgeByDate(badgeByDateQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DailyBadge>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(DailyBadge dailyBadge) {
                        try {
                            dailyBadgeMutableLiveData.postValue(dailyBadge);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    public void getAllData() {
        ArrayList<Query> queryList = new ArrayList<>();
        Calendar weekCal = Calendar.getInstance();
        weekCal.add(Calendar.DATE, -1);
        String fromDate = sdf1.format(weekCal.getTime());
        String toDate = sdf1.format(new Date());

        queryList.add(new ScoreQuery(fromDate, toDate));
//        queryList.add(new StepsQuery(fromDate, toDate));
        queryList.add(new SleepQuery(fromDate, toDate));

        compositeDisposable.add(mAktivoManager.query(queryList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(liveMutableData -> liveMutableData)
                .subscribeWith(new DisposableSingleObserver<List<Map<LocalDate, Stats>>>() {

                    @Override
                    public void onSuccess(List<Map<LocalDate, Stats>> maps) {
                        try {

                            LocalDate fromDate2 = LocalDate.parse(fromDate);

                            ScoreData(maps, fromDate2);

//                            StepData(maps, fromDate2);

                            SleepData(maps, fromDate2);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            isLoading.postValue(false);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));

    }

    private void SleepData(List<Map<LocalDate, Stats>> maps, LocalDate fromDate2) {
        try {
            TreeMap<LocalDate, SleepStats> treeSleepMap = new TreeMap(maps.get(1));

            long sleepTime = treeSleepMap.get(fromDate2).getValue();

            aktivoDataStore.setSleepTime(convSectohours(sleepTime));

            aktivoDataStore.setSleepTarget(25200 < sleepTime);

            aktivoDataStoreMutableLiveData.postValue(aktivoDataStore);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void ScoreData(List<Map<LocalDate, Stats>> maps, LocalDate fromDate2) {
        try {
            TreeMap<LocalDate, ScoreStats> treeScoreMap = new TreeMap(maps.get(0));


            ScoreElement lightActivity = treeScoreMap.get(fromDate2).getScoreLipa();
            yesterdayLightActivity.postValue(convSectohours(lightActivity.getValue()));

            lightActivity = treeScoreMap.get(fromDate2).getScoreSb();

            long sedentaryTime = lightActivity.getValue();
            aktivoDataStore.setSedendataryTime(convSectohours(sedentaryTime));
//            28800
            aktivoDataStore.setSedentarTarget(28800 > sedentaryTime);

            lightActivity = treeScoreMap.get(fromDate2).getScoreMvpa();
            long stepTime = lightActivity.getValue();

            aktivoDataStore.setExerciseTime(convSectohours(stepTime));
            aktivoDataStore.setExerciseTarget(stepTime > 1800);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String convSectohours(long Val) {

        int hours = (int) Val / 3600;
        int remainder = (int) Val % 3600;
        int mins = remainder / 60;

        String hour = hours > 0 ? hours + " hours " : "";

        return hour + mins + " minutes";
    }

    public void getHistoryBadges() {

        Calendar yearCalendar = Calendar.getInstance();
        yearCalendar.add(Calendar.DAY_OF_MONTH, -1);
        String toDate = sdf1.format(yearCalendar.getTime());

        yearCalendar.set(Calendar.MONTH, 1);

        String fromDate = sdf1.format(yearCalendar.getTime());

        BadgeHistoryQuery badgeHistoryQuery = new BadgeHistoryQuery(fromDate,
                toDate);

        compositeDisposable.add(mAktivoManager.queryBadgeHistory(badgeHistoryQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(liveMutableData -> liveMutableData)
                .subscribeWith(new DisposableSingleObserver<Map<LocalDate, HistoryBadgeType>>() {

                    @Override
                    public void onSuccess(Map<LocalDate, HistoryBadgeType> localDateHistoryBadgeTypeMap) {
                        try {
                            TreeMap<LocalDate, HistoryBadgeType> localDateHistoryBadgeTypeTreeMap = new TreeMap<>(localDateHistoryBadgeTypeMap);

                            Set<LocalDate> historyBadgeTypes = localDateHistoryBadgeTypeTreeMap.keySet();
                          /*  for (LocalDate localDate : historyBadgeTypes) {
                                Log.e("HistoryBadges", "Date: " + localDate + " URL: " +
                                        localDateHistoryBadgeTypeTreeMap.get(localDate).getIconUrl());
                            }*/

                            historyBadges.postValue(localDateHistoryBadgeTypeTreeMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    public void getBadgesPeriod(BadgeSummaryQuery badgeSummaryQuery) {
        compositeDisposable.add(mAktivoManager.queryBadgeSummary(badgeSummaryQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BadgeSummary>() {

                    @Override
                    public void onSuccess(BadgeSummary badgeSummary) {
                        badgeSummaryforPeriod.postValue(badgeSummary);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    public void getBadgesSummary() {
        Calendar yearCalendar = Calendar.getInstance();
        yearCalendar.add(Calendar.DAY_OF_MONTH, -1);
        String toDate = sdf1.format(yearCalendar.getTime());

        yearCalendar.set(Calendar.MONTH, 1);

        String fromDate = sdf1.format(yearCalendar.getTime());

        BadgeSummaryQuery badgeHistoryQuery = new BadgeSummaryQuery(fromDate,
                toDate);

        compositeDisposable.add(mAktivoManager.queryBadgeSummary(badgeHistoryQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BadgeSummary>() {

                    @Override
                    public void onSuccess(BadgeSummary badgeSummary) {
                        badgeSummaryMutableLiveData.postValue(badgeSummary);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Badges Error", e.toString());
                        badgeSummaryMutableLiveData.postValue(null);
                    }
                }));

    }

    public MutableLiveData<List<FitnessTracker>> mutableLiveDataFitnesstracker = new MutableLiveData<>();

    public void getDevicePlatforms() {
        getAuthURL();
        compositeDisposable.add(mAktivoManager.getFitnessTrackers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<FitnessTracker>>() {

                    @Override
                    public void onSuccess(List<FitnessTracker> fitnessTrackers) {
                        mutableLiveDataFitnesstracker.postValue(fitnessTrackers);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    private void getAuthURL() {
        compositeDisposable.add(mAktivoManager
                .getFitnessPlatformAuthUrl(FitnessTracker.Platform.FITBIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FitnessTrackerAuthUrlInfo>() {

                    @Override
                    public void onSuccess(FitnessTrackerAuthUrlInfo fitnessTrackerAuthUrlInfo) {
                        Log.e("AUthURL", fitnessTrackerAuthUrlInfo.getAuthUrl());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }


}
