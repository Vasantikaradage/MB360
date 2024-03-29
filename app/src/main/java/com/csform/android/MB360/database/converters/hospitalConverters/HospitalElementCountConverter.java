package com.csform.android.MB360.database.converters.hospitalConverters;

import androidx.room.TypeConverter;

import com.csform.android.MB360.insurance.hospitalnetwork.reponseclass.HospitalsCount;
import com.google.gson.Gson;

public class HospitalElementCountConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static HospitalsCount stringToHospitalCount(String hospitalCount) {
        return gson.fromJson(hospitalCount, HospitalsCount.class);
    }

    @TypeConverter
    public static String hospitalToString(HospitalsCount hospitalsCount) {

        return gson.toJson(hospitalsCount);
    }
}
