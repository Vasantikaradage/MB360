package com.csform.android.MB360.fitness.StatsScreenData;

import java.util.Date;

public class WeekandDtes {
    private Date fromDate, ToDate;
    private int weekno;
    private boolean currentWeek;

    public WeekandDtes(Date fromDate, Date toDate, int weekno, boolean currentWeek) {
        this.fromDate = fromDate;
        ToDate = toDate;
        this.weekno = weekno;
        this.currentWeek = currentWeek;
    }

    public boolean isCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(boolean currentWeek) {
        this.currentWeek = currentWeek;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return ToDate;
    }

    public void setToDate(Date toDate) {
        ToDate = toDate;
    }

    public int getWeekno() {
        return weekno;
    }

    public void setWeekno(int weekno) {
        this.weekno = weekno;
    }

}
