package com.csform.android.MB360.wellness.homehealthcare.responseclass.summary;

import com.csform.android.MB360.wellness.homehealthcare.responseclass.Message;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryDS;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryNA;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SummaryResponseDS {
    @SerializedName("ScheduledSummary")
    @Expose
    private List<OnGoingSummaryDS> scheduledSummary = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    public List<OnGoingSummaryDS> getScheduledSummary() {
        return scheduledSummary;
    }

    public void setScheduledSummary(List<OnGoingSummaryDS> scheduledSummary) {
        this.scheduledSummary = scheduledSummary;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SummaryResponseDS{" +
                "scheduledSummary=" + scheduledSummary +
                ", message=" + message +
                '}';
    }
}
