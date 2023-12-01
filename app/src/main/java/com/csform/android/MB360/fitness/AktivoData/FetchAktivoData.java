package com.csform.android.MB360.fitness.AktivoData;

import com.aktivolabs.aktivocore.data.models.heartrate.HeartRateStats;
import com.aktivolabs.aktivocore.data.models.score.ScoreStats;
import com.aktivolabs.aktivocore.data.models.sleep.SleepStats;
import com.aktivolabs.aktivocore.data.models.steps.StepStats;


import org.threeten.bp.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.threeten.bp.LocalDate;

public interface FetchAktivoData {
   void stepQuery(Map<LocalDate, StepStats> localDateSleepStatsMap, Boolean Month);
    void sleepQuery(Map<LocalDate, SleepStats> localDateStepStatsMap, Boolean Month);
   void scoreQuery(Map<LocalDate, ScoreStats> localDateScoreStatsMap, Boolean Month);
   void Heartrate(Map<LocalDate, HeartRateStats> localHeartRateMaps, Boolean Month);
    void challengesData(List<Team_Score_VH> lstTeamScore);
    //void challengeList(List<Challenge_layout_items> lstChallenge);
    void NetworkData(HashMap<String, Integer> NetworkAvg);
}
