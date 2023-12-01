package com.csform.android.MB360.fitness.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AktivoChallengeResponse {
    @SerializedName("leaderboards")
    private ArrayList<ChallengeItemsInfo> challengeItemsInfos;

    public ArrayList<ChallengeItemsInfo> getChallengeItemsInfos() {
        return challengeItemsInfos;
    }

    public void setChallengeItemsInfos(ArrayList<ChallengeItemsInfo> challengeItemsInfos) {
        this.challengeItemsInfos = challengeItemsInfos;
    }

    @Override
    public String toString() {
        return "AktivoChallengeResponse{" +
                "challengeItemsInfos=" + challengeItemsInfos +
                '}';
    }
}
