package com.csform.android.MB360.wellness.homehealthcare.retrofit;

import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.City;

public interface CityInterface {

    // home health care
    void getCity(City city);

    // health checkup
    void getCityHC(String city);


}
