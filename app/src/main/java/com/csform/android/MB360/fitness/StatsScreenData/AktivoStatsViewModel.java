package com.csform.android.MB360.fitness.StatsScreenData;

import androidx.lifecycle.ViewModel;

import com.aktivolabs.aktivocore.data.models.heartrate.HeartRateStats;
import com.aktivolabs.aktivocore.data.models.score.ScoreStats;
import com.aktivolabs.aktivocore.data.models.sleep.SleepStats;
import com.aktivolabs.aktivocore.data.models.steps.StepStats;

import org.threeten.bp.LocalDate;

import java.util.TreeMap;

public class AktivoStatsViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private String txt_Score;
    private TreeMap<LocalDate, SleepStats> localDateSleepStatsTreeMap;
    private TreeMap<LocalDate, StepStats> localDateStepStatsTreeMap;
    private TreeMap<LocalDate, ScoreStats> localDateScoreStatsTreeMap;
    private TreeMap<LocalDate, HeartRateStats> localDateHeartRateStatsTreeMap;

     public TreeMap<LocalDate, SleepStats> getLocalDateSleepStatsTreeMap() {
        return localDateSleepStatsTreeMap;
    }

     public void setLocalDateSleepStatsTreeMap(TreeMap<LocalDate, SleepStats> localDateSleepStatsTreeMap) {
        this.localDateSleepStatsTreeMap = localDateSleepStatsTreeMap;
    }

     public TreeMap<LocalDate, StepStats> getLocalDateStepStatsTreeMap() {
        return localDateStepStatsTreeMap;
    }

     public void setLocalDateStepStatsTreeMap(TreeMap<LocalDate, StepStats> localDateStepStatsTreeMap) {
        this.localDateStepStatsTreeMap = localDateStepStatsTreeMap;
    }

     public TreeMap<LocalDate, ScoreStats> getLocalDateScoreStatsTreeMap() {
        return localDateScoreStatsTreeMap;
    }

     public void setLocalDateScoreStatsTreeMap(TreeMap<LocalDate, ScoreStats> localDateScoreStatsTreeMap) {
        this.localDateScoreStatsTreeMap = localDateScoreStatsTreeMap;
    }

     public TreeMap<LocalDate, HeartRateStats> getLocalDateHeartRateStatsTreeMap() {
        return localDateHeartRateStatsTreeMap;
    }

     public void setLocalDateHeartRateStatsTreeMap(TreeMap<LocalDate, HeartRateStats> localDateHeartRateStatsTreeMap) {
        this.localDateHeartRateStatsTreeMap = localDateHeartRateStatsTreeMap;
    }

    public String getTxt_Score() {
        return txt_Score;
    }

    public void setTxt_Score(String txt_Score) {
        this.txt_Score = txt_Score;
    }

    public AktivoStatsViewModel() {
    }
}
