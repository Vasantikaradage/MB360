package com.csform.android.MB360.wellness.homehealthcare.responseclass.summary;

import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryNA;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.Message;

import java.util.ArrayList;
import java.util.List;

public class SummaryResponseNA {
    @SerializedName("ScheduledSummary")
    @Expose
    private List<OnGoingSummaryNA> scheduledSummary = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    public List<OnGoingSummaryNA> getScheduledSummary() {
        return scheduledSummary;
    }

    public void setScheduledSummary(List<OnGoingSummaryNA> scheduledSummary) {
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
        return "SummaryResponseNA{" +
                "scheduledSummary=" + scheduledSummary +
                ", message=" + message +
                '}';
    }
}
