package com.csform.android.MB360.fitness.StatsScreenData;

import java.util.Date;

public class MnthandDates {
    private Date fromDate, ToDate;
    private String MonthNo;
    private boolean CurrentMonth;

    public MnthandDates(Date fromDate, Date toDate, String monthNo, boolean currentMonth) {
        this.fromDate = fromDate;
        ToDate = toDate;
        MonthNo = monthNo;
        CurrentMonth = currentMonth;
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

    public String getMonthNo() {
        return MonthNo;
    }

    public void setMonthNo(String monthNo) {
        MonthNo = monthNo;
    }

    public boolean isCurrentMonth() {
        return CurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        CurrentMonth = currentMonth;
    }
}
