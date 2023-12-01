package com.csform.android.MB360.fitness.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTracker;
import com.csform.android.MB360.fitness.AktivoData.BadgesRepo;


import java.util.List;

public class DeviceConnectionViewModel extends AndroidViewModel {
    private BadgesRepo badgesRepo;

    public DeviceConnectionViewModel(@NonNull Application application) {
        super(application);
        badgesRepo = BadgesRepo.getInstance(application);
        badgesRepo.getDevicePlatforms();
    }

    public MutableLiveData<List<FitnessTracker>> getMutableData(){
        return badgesRepo.mutableLiveDataFitnesstracker;
    }


}
