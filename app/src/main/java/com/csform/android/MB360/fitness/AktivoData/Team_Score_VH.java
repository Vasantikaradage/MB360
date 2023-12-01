package com.csform.android.MB360.fitness.AktivoData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Team_Score_VH implements Parcelable {

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public static Creator<Team_Score_VH> getCREATOR() {
        return CREATOR;
    }

    @SerializedName("rank")
    private int rank;

    private int usrId;

    @SerializedName("name")
    private String usrName;

    @SerializedName("score")
    private String usrScore;

    @SerializedName("_id")
    private String _id;

    private Boolean usrBoolean;
    @SerializedName("impact")
    private int impact;

    public Team_Score_VH(int usrId, String usrName, String usrScore, String _id, Boolean usrBoolean, int impact) {
        this.usrId = usrId;
        this.usrName = usrName;
        this.usrScore = usrScore;
        this._id = _id;
        this.usrBoolean = usrBoolean;
        this.impact = impact;
    }

    protected Team_Score_VH(Parcel in) {
        usrId = in.readInt();
        usrName = in.readString();
        usrScore = in.readString();
        _id = in.readString();
        byte tmpUsrBoolean = in.readByte();
        usrBoolean = tmpUsrBoolean == 0 ? null : tmpUsrBoolean == 1;
        impact = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(usrId);
        dest.writeString(usrName);
        dest.writeString(usrScore);
        dest.writeString(_id);
        dest.writeByte((byte) (usrBoolean == null ? 0 : usrBoolean ? 1 : 2));
        dest.writeInt(impact);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Team_Score_VH> CREATOR = new Creator<Team_Score_VH>() {
        @Override
        public Team_Score_VH createFromParcel(Parcel in) {
            return new Team_Score_VH(in);
        }

        @Override
        public Team_Score_VH[] newArray(int size) {
            return new Team_Score_VH[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getImpact() {
        return impact;
    }

    public void setImpact(int impact) {
        this.impact = impact;
    }



    public Team_Score_VH(int usrId, String usrName, String usrScore, Boolean usrBoolean) {
        this.usrId = usrId;
        this.usrName = usrName;
        this.usrScore = usrScore;
        this.usrBoolean = usrBoolean;
    }

    public Boolean getUsrBoolean() {
        return usrBoolean;
    }

    public void setUsrBoolean(Boolean usrBoolean) {
        this.usrBoolean = usrBoolean;
    }

    public int getUsrId() {
        return usrId;
    }

    public void setUsrId(int usrId) {
        this.usrId = usrId;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrScore() {
        return usrScore;
    }

    public void setUsrScore(String usrScore) {
        this.usrScore = usrScore;
    }
}

