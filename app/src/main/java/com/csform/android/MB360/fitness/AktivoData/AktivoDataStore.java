package com.csform.android.MB360.fitness.AktivoData;

public class AktivoDataStore {
    private String SleepTime, ExerciseTime, SedendataryTime;
    private boolean SleepTarget = false, ExerciseTarget = false, SedentarTarget = false;

    public AktivoDataStore(){

    }
    public AktivoDataStore(String sleepTime,
                           String exerciseTime, String sedendataryTime,
                           boolean sleepTarget, boolean exerciseTarget, boolean sedentarTarget) {
        SleepTime = sleepTime;
        ExerciseTime = exerciseTime;
        SedendataryTime = sedendataryTime;
        SleepTarget = sleepTarget;
        ExerciseTarget = exerciseTarget;
        SedentarTarget = sedentarTarget;
    }

    public String getSleepTime() {
        return SleepTime;
    }

    public void setSleepTime(String sleepTime) {
        SleepTime = sleepTime;
    }

    public String getExerciseTime() {
        return ExerciseTime;
    }

    public void setExerciseTime(String exerciseTime) {
        ExerciseTime = exerciseTime;
    }

    public String getSedendataryTime() {
        return SedendataryTime;
    }

    public void setSedendataryTime(String sedendataryTime) {
        SedendataryTime = sedendataryTime;
    }

    public boolean isSleepTarget() {
        return SleepTarget;
    }

    public void setSleepTarget(boolean sleepTarget) {
        SleepTarget = sleepTarget;
    }

    public boolean isExerciseTarget() {
        return ExerciseTarget;
    }

    public void setExerciseTarget(boolean exerciseTarget) {
        ExerciseTarget = exerciseTarget;
    }

    public boolean isSedentarTarget() {
        return SedentarTarget;
    }

    public void setSedentarTarget(boolean sedentarTarget) {
        SedentarTarget = sedentarTarget;
    }
}
