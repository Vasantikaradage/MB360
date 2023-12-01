package com.csform.android.MB360.fitness.AktivoData;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.aktivolabs.aktivocore.data.models.badges.DailyBadge;
import com.aktivolabs.aktivocore.data.models.heartrate.HeartRateStats;
import com.aktivolabs.aktivocore.data.models.queries.BadgeByDateQuery;
import com.aktivolabs.aktivocore.data.models.queries.HeartRateQuery;
import com.aktivolabs.aktivocore.data.models.queries.ScoreQuery;
import com.aktivolabs.aktivocore.data.models.queries.SleepQuery;
import com.aktivolabs.aktivocore.data.models.queries.StepsQuery;
import com.aktivolabs.aktivocore.data.models.score.ScoreStats;
import com.aktivolabs.aktivocore.data.models.sleep.SleepStats;
import com.aktivolabs.aktivocore.data.models.steps.StepStats;
import com.aktivolabs.aktivocore.managers.AktivoManager;


import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;


import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;







import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class AktivoFetchingData {

    private Context cnt;
    private FetchAktivoData fetchAktivoData;
    private AktivoManager mAktivoManager;
    private static volatile AktivoFetchingData instance;
    private CompositeDisposable compositeDisposable;
    public MutableLiveData<DailyBadge> badgesMutableLiveData = new MutableLiveData<>();

    public AktivoFetchingData(Context cnt, FetchAktivoData fetchAktivoData) {
        this.cnt = cnt;
        this.fetchAktivoData = fetchAktivoData;
        mAktivoManager = AktivoManager.getInstance(cnt);
        compositeDisposable = new CompositeDisposable();
    }

    public static AktivoFetchingData getInstance(Context cnt, FetchAktivoData fetchAktivoData) {
        if (instance == null) {
            instance = new AktivoFetchingData(cnt, fetchAktivoData);
        }

        return instance;
    }

    public void SleepQuery(String fromDate, String toDate) {

        compositeDisposable.add(mAktivoManager.query(new SleepQuery(fromDate, toDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(localDateSleepStatsMap -> localDateSleepStatsMap).subscribeWith(new DisposableSingleObserver<Map<LocalDate, SleepStats>>() {

                    @Override
                    public void onSuccess(Map<LocalDate, SleepStats> localDateSleepStatsMap) {
                        Set<LocalDate> keySet = localDateSleepStatsMap.keySet();
                        for (LocalDate localDate : keySet) {
                            Log.e("Tag", "Sleep Stats: " + "Date: " + localDate + " Sleep: " + localDateSleepStatsMap.get(localDate).getValue());
                        }

                        TreeMap<LocalDate, SleepStats> treeMap = new TreeMap<>(localDateSleepStatsMap);

                        fetchAktivoData.sleepQuery(treeMap, false);

                    }

                    @Override
                    public void onError(Throwable e) {

                        fetchAktivoData.sleepQuery(new TreeMap<>(), false);
                        if (e.getMessage() != null) {
                            Log.e("Sleep Stats Error", e.toString());
                        }
                    }
                }));
    }


    public void ScoreQuery(String fromDate, String toDate) {
        compositeDisposable.add(mAktivoManager.query(new ScoreQuery(fromDate, toDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(localDateScoreStatsMap -> localDateScoreStatsMap).subscribeWith(new DisposableSingleObserver<Map<LocalDate, ScoreStats>>() {

                    @Override
                    public void onSuccess(Map<LocalDate, ScoreStats> localDateScoreStatsMap) {
                        TreeMap<LocalDate, ScoreStats> treeMap = new TreeMap<>(localDateScoreStatsMap);

                        Set<LocalDate> keySet = treeMap.keySet();
                        for (LocalDate localDate : keySet) {
                            Log.e("@@", "Score Stats: " + "Date: " + localDate + " Score: " + localDateScoreStatsMap.get(localDate).getScore());
                        }

                        fetchAktivoData.scoreQuery(treeMap, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchAktivoData.scoreQuery(new TreeMap<>(), false);
                        if (e.getMessage() != null) {
                            Log.e("Score Stats Error", e.toString());
                        }
                    }
                }));



    }

    public void StepQuery(String fromDate, String toDate) {

        compositeDisposable.add(mAktivoManager.query(new StepsQuery(fromDate, toDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(localDateStepStatsMap -> localDateStepStatsMap).subscribeWith(new DisposableSingleObserver<Map<LocalDate, StepStats>>() {

                    @Override
                    public void onSuccess(Map<LocalDate, StepStats> localDateStepStatsMap) {
                        TreeMap<LocalDate, StepStats> treeMap = new TreeMap<>(localDateStepStatsMap);

                        Set<LocalDate> keySet = treeMap.keySet();
                        for (LocalDate localDate : keySet) {
                            Log.e("TAG", "Steps Stats: " + "Date: " + localDate + " Steps: " + localDateStepStatsMap.get(localDate).getValue());
                        }

                        fetchAktivoData.stepQuery(treeMap, false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchAktivoData.stepQuery(new TreeMap<>(), false);
                        Log.e("Step Stats Error", e.toString());

                    }
                }));
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Calendar weekCal = Calendar.getInstance();
        weekCal.add(Calendar.DATE, -1);
        toDate = sdf1.format(weekCal.getTime());

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
                            badgesMutableLiveData.postValue(dailyBadge);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }


    public void HeartRate(String fromDate, String toDate) {
        compositeDisposable.add(mAktivoManager.query(new HeartRateQuery(fromDate, toDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(localDateHeartRateStatsMap -> localDateHeartRateStatsMap)
                .subscribeWith(new DisposableSingleObserver<Map<LocalDate, HeartRateStats>>() {

                    @Override
                    public void onSuccess(Map<LocalDate, HeartRateStats> localDateHeartRateStatsMap) {
                        Set<LocalDate> keySet = localDateHeartRateStatsMap.keySet();
                        for (LocalDate localDate : keySet) {
                            Log.e("TAG", "Heart rate Stats: " + "Date: " + localDate + " Heart rate: " + localDateHeartRateStatsMap.get(localDate).getValue());
                        }
                        fetchAktivoData.Heartrate(localDateHeartRateStatsMap, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchAktivoData.Heartrate(new TreeMap<>(), false);
                        Log.e("Heart Rate Stats Error", e.toString());


                    }
                }));
    }
}
