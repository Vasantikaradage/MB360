package com.csform.android.MB360.wellness.homehealthcare.responseclass.summary;

import com.csform.android.MB360.wellness.homehealthcare.responseclass.Message;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryDM;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SummaryResponseDM {

    @SerializedName("ScheduledSummary")
    @Expose
    private List<OnGoingSummaryDM> scheduledSummary;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<OnGoingSummaryDM> getScheduledSummary() {
        return scheduledSummary;
    }

    public void setScheduledSummary(List<OnGoingSummaryDM> scheduledSummary) {
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
        return "SummaryResponseDM{" +
                "scheduledSummary=" + scheduledSummary +
                ", message=" + message +
                '}';
    }
}
