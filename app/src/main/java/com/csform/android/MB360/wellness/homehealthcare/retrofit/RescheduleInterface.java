package com.csform.android.MB360.wellness.homehealthcare.retrofit;

import com.csform.android.MB360.wellness.homehealthcare.responseclass.ScheduledSummary;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryDS;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryNA;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryPT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryST;

public interface RescheduleInterface {

    void rescheduleAppointmentNA(OnGoingSummaryNA appointment);
    void rescheduleAppointmentST(OnGoingSummaryST appointment);
    void rescheduleAppointmentLT(OnGoingSummaryLT appointment);
    void rescheduleAppointmentDS(OnGoingSummaryDS appointment);
    void rescheduleAppointmentPT(OnGoingSummaryPT appointment);

}
