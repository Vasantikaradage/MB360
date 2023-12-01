package com.csform.android.MB360.wellness.homehealthcare.responseclass.summary;

import com.csform.android.MB360.wellness.homehealthcare.responseclass.Message;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryNC;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SummaryResponseNC {

    @SerializedName("ScheduledSummary")
    @Expose
    private List<OnGoingSummaryNC> scheduledSummary = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    public List<OnGoingSummaryNC> getScheduledSummary() {
        return scheduledSummary;
    }

    public void setScheduledSummary(List<OnGoingSummaryNC> scheduledSummary) {
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
        return "SummaryResponse{" +
                "scheduledSummary=" + scheduledSummary +
                ", message=" + message +
                '}';
    }
}