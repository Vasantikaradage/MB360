package com.csform.android.MB360.fitness.StatsScreenData;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class DayAxisValueFormatter extends ValueFormatter {

    private final BarLineChartBase<?> chart;
    private final String[] mDays = new String[]{
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", ""
    };

    public DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {

            return mDays[(int) value -1];

    }
}
