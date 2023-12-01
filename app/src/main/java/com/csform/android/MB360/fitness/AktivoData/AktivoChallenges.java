package com.csform.android.MB360.fitness.AktivoData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AktivoChallenges {

    @SerializedName("_id")
    private String _id;

    @SerializedName("title")
    private String title;

    @SerializedName("days")
    private int days;

    @SerializedName("challenge_type")
    private String challenge_type;

    @SerializedName("description")
    private String description;

    @SerializedName("scored_by")
    private String scored_by;

    @SerializedName("category_name")
    private String category_name;

    @SerializedName("target")
    private String target;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("start_date")
    private String start_date;

    @SerializedName("end_date")
    private String end_date;

    @SerializedName("numberOfParticipants")
    private int numberOfParticipants;

    @SerializedName("leaderboard")
    private List<Team_Score_VH> lstLeaderBoards;

    public List<Team_Score_VH> getLstLeaderBoards() {
        return lstLeaderBoards;
    }

    public void setLstLeaderBoards(List<Team_Score_VH> lstLeaderBoards) {
        this.lstLeaderBoards = lstLeaderBoards;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getChallenge_type() {
        return challenge_type;
    }

    public void setChallenge_type(String challenge_type) {
        this.challenge_type = challenge_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScored_by() {
        return scored_by;
    }

    public void setScored_by(String scored_by) {
        this.scored_by = scored_by;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

}
