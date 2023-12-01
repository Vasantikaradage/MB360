package com.csform.android.MB360.database.converters;

import androidx.room.TypeConverter;

import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MaxMinAgeResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MaxMinAgeConverter {
    private static Gson gson = new Gson();


    @TypeConverter
    public static List<MaxMinAgeResponse> toList(String value) {
        Type listType = new TypeToken<List<MaxMinAgeResponse>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<MaxMinAgeResponse> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


}
