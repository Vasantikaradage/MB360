package com.csform.android.MB360.insurance.hospitalnetwork.responseclassV1;

import androidx.room.TypeConverters;

import com.csform.android.MB360.database.converters.hospitalConverters.HospitalConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class HospitalResponse {

    @SerializedName("Hospital_Information")
    @Expose
    @TypeConverters(HospitalConverter.class)
    private List<HospitalInformation> hospitalInformation;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<HospitalInformation> getHospitalInformation() {
        return hospitalInformation;
    }

    public void setHospitalInformation(List<HospitalInformation> hospitalInformation) {
        this.hospitalInformation = hospitalInformation;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }



}
