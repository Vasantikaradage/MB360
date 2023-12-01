package com.csform.android.MB360.fitness.response;

import com.csform.android.MB360.fitness.AktivoData.Team_Score_VH;

import java.io.Serializable;
import java.util.List;

public class ChallengeItemsInfo  implements Serializable {

        public String challenge_type, challenge_name, challenge_time, challenge_count;
        public String start_date, end_Date, imgURL,description;
        public boolean enroll,new_Challenges;
        private List<Team_Score_VH> TeamMembers;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    public ChallengeItemsInfo(String challenge_type, String challenge_name,
                String challenge_time, String challenge_count,
                String start_date, String end_Date, boolean enroll,
        boolean new_Challenges, List<Team_Score_VH> teamMembers ){
            this.challenge_type = challenge_type;
            this.challenge_name = challenge_name;
            this.challenge_time = challenge_time;
            this.challenge_count = challenge_count;
            this.start_date = start_date;
            this.end_Date = end_Date;
            this.enroll = enroll;
            this.new_Challenges = new_Challenges;
            TeamMembers = teamMembers;
        }

        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_Date() {
            return end_Date;
        }

        public void setEnd_Date(String end_Date) {
            this.end_Date = end_Date;
        }

    @Override
    public String toString() {
        return "ChallengeItemsInfo{" +
                "challenge_type='" + challenge_type + '\'' +
                ", challenge_name='" + challenge_name + '\'' +
                ", challenge_time='" + challenge_time + '\'' +
                ", challenge_count='" + challenge_count + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_Date='" + end_Date + '\'' +
                ", imgURL='" + imgURL + '\'' +
                ", description='" + description + '\'' +
                ", enroll=" + enroll +
                ", new_Challenges=" + new_Challenges +
                ", TeamMembers=" + TeamMembers +
                '}';
    }

    public ChallengeItemsInfo(String challenge_type, String challenge_name, String challenge_time,
                              String challenge_count, String start_date, String end_Date,
                              boolean enroll, boolean new_Challenges,
                              List<Team_Score_VH> teamMembers, String imgURL, String description) {
            this.challenge_type = challenge_type;
            this.challenge_name = challenge_name;
            this.challenge_time = challenge_time;
            this.challenge_count = challenge_count;
            this.start_date = start_date;
            this.end_Date = end_Date;
            this.enroll = enroll;
            this.new_Challenges = new_Challenges;
            TeamMembers = teamMembers;
            this.imgURL = imgURL;
            this.description = description;
        }


        public boolean isNew_Challenges() {
            return new_Challenges;
        }

        public void setNew_Challenges(boolean new_Challenges) {
            this.new_Challenges = new_Challenges;
        }

        public List<Team_Score_VH> getTeamMembers() {
            return TeamMembers;
        }

        public void setTeamMembers(List<Team_Score_VH> teamMembers) {
            TeamMembers = teamMembers;
        }

        public String getChallenge_count() {
            return challenge_count;
        }

        public void setChallenge_count(String challenge_count) {
            this.challenge_count = challenge_count;
        }

        public String getChallenge_type() {
            return challenge_type;
        }

        public void setChallenge_type(String challenge_type) {
            this.challenge_type = challenge_type;
        }

        public String getChallenge_name() {
            return challenge_name;
        }

        public void setChallenge_name(String challenge_name) {
            this.challenge_name = challenge_name;
        }

        public String getChallenge_time() {
            return challenge_time;
        }

        public void setChallenge_time(String challenge_time) {
            this.challenge_time = challenge_time;
        }

        public boolean isEnroll() {
            return enroll;
        }

        public void setEnroll(boolean enroll) {
            this.enroll = enroll;
        }
    }


