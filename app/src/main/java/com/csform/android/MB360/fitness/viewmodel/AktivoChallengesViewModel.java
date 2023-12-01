package com.csform.android.MB360.fitness.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.fitness.AktivoChallengeRepository;
import com.csform.android.MB360.fitness.ProfileScreenData.AktivoBeanProfile;


public class AktivoChallengesViewModel extends AndroidViewModel {
    AktivoChallengeRepository aktivoChanllengerepository;

    public AktivoChallengesViewModel(@NonNull Application application) {
        super(application);
        aktivoChanllengerepository= new AktivoChallengeRepository(application);
    }



    public LiveData<AktivoBeanProfile> getAktivoChanllengerInfoData() {
        return aktivoChanllengerepository.getAktivoChanllengerInfoData();
    }

    public MutableLiveData<AktivoBeanProfile> getAktivoChanllengerInfo(String token,String _id, String leaderboards, String startDate, String endDate) {
        return aktivoChanllengerepository.getAktivoChanllengerInfo(token,_id,leaderboards,startDate,endDate);
    }



    /*public MutableLiveData<Boolean> getLoadingState() {
        return aktivoChanllengerepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return aktivoChanllengerepository.errorState;
    }*/
}
