package com.csform.android.MB360.fitness.StatsScreenData;



import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public abstract class DateTimeHandler {

    public abstract void ApplyTabs(HashMap<Integer, WeekandDtes> localeDates);
    public abstract void ApplymonthTabs(HashMap<Integer, MnthandDates> localeMDates);
    private final String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };



    private HashMap<Integer, WeekandDtes> localeDates1;
    private HashMap<Integer, WeekandDtes> localeDates;
    private HashMap<Integer, MnthandDates> localeMDates;
    private Date todayDate;

    protected DateTimeHandler() {
        Calendar weekCal = Calendar.getInstance(Locale.US);
        weekCal.setFirstDayOfWeek(Calendar.SUNDAY);
        todayDate = weekCal.getTime();
        localeDates = new HashMap();
        localeMDates = new HashMap();
        int j = 1,month = 0;

        if (weekCal.get(Calendar.MONTH) < 4 || weekCal.get(Calendar.WEEK_OF_YEAR) < 16) {
            int year = weekCal.get(Calendar.YEAR);
            weekCal.set(Calendar.YEAR, year - 1);
            int maxweek = weekCal.getActualMaximum(Calendar.WEEK_OF_YEAR);
            for (int i = 1; i <= maxweek; i++) {
                localeDates.put(j++, getLocaleDates(weekCal, i));
            }

            for(int i = 0;i<mMonths.length; i++) {
                localeMDates.put(month++, getMonthDates(weekCal,i));
            }

        }

        weekCal = Calendar.getInstance(Locale.US);
        weekCal.setFirstDayOfWeek(Calendar.SUNDAY);

        int maxweek = weekCal.getActualMaximum(Calendar.WEEK_OF_YEAR);
        for (int i = 1; i <= maxweek; i++) {
            localeDates.put(j++, getLocaleDates(weekCal, i));
        }
        weekCal = Calendar.getInstance();
        for(int i = 0;i<mMonths.length; i++) {
            localeMDates.put(month++, getMonthDates(weekCal,i));
        }
        ApplyTabs(localeDates);
        ApplymonthTabs(localeMDates);
    }

    public MnthandDates getMonthDates(Calendar monthCal, int i){
        monthCal.set(Calendar.MONTH, i);
        monthCal.set(Calendar.DAY_OF_MONTH, 1);
        Date fromDay = monthCal.getTime();

        monthCal.set(Calendar.DATE, monthCal.getActualMaximum(Calendar.DAY_OF_MONTH));

        Date toDay = monthCal.getTime();
        boolean val = fromDay.compareTo(todayDate) * toDay.compareTo(todayDate) <= 0;
        MnthandDates weekDates = new MnthandDates(fromDay, toDay, mMonths[i], val);
        return weekDates;
    }

     private WeekandDtes getLocaleDates(Calendar weekCal, int i) {
        weekCal.set(Calendar.WEEK_OF_YEAR, i);
        weekCal.set(Calendar.DAY_OF_WEEK, weekCal.getFirstDayOfWeek());
        Date fromday = weekCal.getTime();

        weekCal.add(Calendar.DATE, 6);
        Date todate = weekCal.getTime();
        boolean val = fromday.compareTo(todayDate) * todate.compareTo(todayDate) <= 0;
        WeekandDtes weekDates = new WeekandDtes(fromday, todate, i, val);
        return weekDates;

    }

}
