package com.csform.android.MB360.wellness.homehealthcare.responseclass.summary;

import com.csform.android.MB360.wellness.homehealthcare.responseclass.Message;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryNA;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SummaryResponseLT {
    @SerializedName("ScheduledSummary")
    @Expose
    private List<OnGoingSummaryLT> scheduledSummary = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    public List<OnGoingSummaryLT> getScheduledSummary() {
        return scheduledSummary;
    }

    public void setScheduledSummary(List<OnGoingSummaryLT> scheduledSummary) {
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
        return "SummaryResponseLT{" +
                "scheduledSummary=" + scheduledSummary +
                ", message=" + message +
                '}';
    }
}
