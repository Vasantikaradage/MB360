package com.csform.android.MB360.insurance.claims.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.insurance.claims.repository.requestclass.NewIntimateRequest;
import com.csform.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;
import com.csform.android.MB360.insurance.claims.repository.responseclass.LoadPersonsIntimationResponse;
import com.csform.android.MB360.insurance.claims.repository.responseclass.Result;
import com.csform.android.MB360.insurance.escalations.repository.EscalationRepository;
import com.csform.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;

public class ClaimsViewModel extends AndroidViewModel {

    ClaimsRepository claimsRepository;

    public ClaimsViewModel(@NonNull Application application) {
        super(application);
        claimsRepository = new ClaimsRepository(application);
    }

    public MutableLiveData<ClaimsResponse> getClaims(String employeeSrNo, String grpChildSrNo, String oeGrpBasInfSrNo) {
        return claimsRepository.getClaims(employeeSrNo, grpChildSrNo, oeGrpBasInfSrNo);
    }

    public MutableLiveData<ClaimsResponse> getClaimsData() {
        return claimsRepository.getClaimsData();
    }

    public LiveData<LoadPersonsIntimationResponse> getPersons(String employeeSrNo, String grpChildSrNo, String oeGrpBasInfSrNo) {

        return claimsRepository.getPersons(employeeSrNo, grpChildSrNo, oeGrpBasInfSrNo);
    }

    public LiveData<LoadPersonsIntimationResponse> getPersonsData() {
        return claimsRepository.getPersonData();
    }

    //new intimation
    public MutableLiveData<Result> startIntimation(NewIntimateRequest newIntimateRequest) {
        return claimsRepository.startIntimation(newIntimateRequest);
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return claimsRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return claimsRepository.errorState;
    }

    public MutableLiveData<Boolean> getReloginState() {
        return claimsRepository.reloginState;
    }


}
