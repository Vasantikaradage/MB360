package com.csform.android.MB360.wellness.homehealthcare.responseclass.summary;


import com.csform.android.MB360.wellness.homehealthcare.responseclass.Message;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryEC;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SummaryResponseEc {
    @SerializedName("ScheduledSummary")
    @Expose
    private List<OnGoingSummaryEC> scheduledSummary = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<OnGoingSummaryEC> getScheduledSummary() {
        return scheduledSummary;
    }

    public void setScheduledSummary(List<OnGoingSummaryEC> scheduledSummary) {
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

