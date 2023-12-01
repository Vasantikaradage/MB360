package com.csform.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CityResponse {
    @SerializedName("Cities")
    @Expose
    private List<City> cities = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CityResponse{" +
                "cities=" + cities +
                ", message=" + message +
                '}';
    }
}
