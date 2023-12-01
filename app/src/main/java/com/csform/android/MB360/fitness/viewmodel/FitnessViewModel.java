package com.csform.android.MB360.fitness.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.fitness.FitnessRepository;
import com.csform.android.MB360.fitness.request.FitnessEmpGroupInfoRequest;
import com.csform.android.MB360.fitness.response.FitnessEmpGroupInfoResponse;


public class FitnessViewModel extends AndroidViewModel {
    FitnessRepository fitnessRepository;

    public FitnessViewModel(@NonNull Application application) {
        super(application);
        fitnessRepository = new FitnessRepository(application);
    }

    public MutableLiveData<FitnessEmpGroupInfoResponse> getFitnessEmpGroupInfo(FitnessEmpGroupInfoRequest empGroupInfoRequest) {
        return fitnessRepository.getFitnessEmpGroupInfo(empGroupInfoRequest);
    }

    public LiveData<FitnessEmpGroupInfoResponse> getFitnessEmpGroupInfoData() {
        return fitnessRepository.getFitnessEmpGroupInfoData();
    }


    public MutableLiveData<Boolean> getLoadingState() {
        return fitnessRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return fitnessRepository.errorState;
    }


    //fitness permissions
    public LiveData<Boolean> requestGoogleSignIn() {
        return fitnessRepository.requestGooglePermission();
    }

    public void resetGoogleSignIn() {
        fitnessRepository.resetGooglePermission();
    }


}
