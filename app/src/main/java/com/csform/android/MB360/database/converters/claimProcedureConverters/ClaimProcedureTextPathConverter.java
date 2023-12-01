package com.csform.android.MB360.database.converters.claimProcedureConverters;

import androidx.room.TypeConverter;

import com.csform.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcTextPath;
import com.csform.android.MB360.insurance.claimsprocedure.repository.responseclass.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ClaimProcedureTextPathConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<ClaimProcTextPath> toList(String value) {
        Type listType = new TypeToken<List<ClaimProcTextPath>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<ClaimProcTextPath> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static Message stringToMessage(String message) {
        return gson.fromJson(message, Message.class);
    }

    @TypeConverter
    public static String messageToString(Message message) {

        return gson.toJson(message);
    }
}
