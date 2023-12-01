package com.csform.android.MB360.wellness.homehealthcare.responseclass.summary;

import com.csform.android.MB360.wellness.homehealthcare.responseclass.Message;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryPT;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SummaryResponsePT {
    @SerializedName("ScheduledSummary")
    @Expose
    private List<OnGoingSummaryPT> scheduledSummary = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    public List<OnGoingSummaryPT> getScheduledSummary() {
        return scheduledSummary;
    }

    public void setScheduledSummary(List<OnGoingSummaryPT> scheduledSummary) {
        this.scheduledSummary = scheduledSummary;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
