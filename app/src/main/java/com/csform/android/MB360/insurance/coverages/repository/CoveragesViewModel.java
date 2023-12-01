package com.csform.android.MB360.insurance.coverages.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.insurance.coverages.repository.responseclass.CoverageDetailsResponse;
import com.csform.android.MB360.insurance.coverages.repository.responseclass.CoverageResponse;

public class CoveragesViewModel extends AndroidViewModel {

    CoveragesRepository coveragesRepository;

    public CoveragesViewModel(@NonNull Application application) {
        super(application);
        coveragesRepository = new CoveragesRepository(application);

    }

    public MutableLiveData<CoverageResponse> getCoveragePolicyData(String groupChildSrNo, String oeGrpBasInfSrNo) {
        try {
            return coveragesRepository.getCoveragesPolicyData(groupChildSrNo, oeGrpBasInfSrNo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public MutableLiveData<CoverageDetailsResponse> getCoverageDetails(String groupChildSrNo, String oeGrpBasInfSrNo, String productType, String employeeSrNo, String employeeSrNo_gmc) {
        return coveragesRepository.getCoverageDetails(groupChildSrNo, oeGrpBasInfSrNo, productType, employeeSrNo,employeeSrNo_gmc);
    }

    public MutableLiveData<CoverageDetailsResponse> getCoveragesDetailsData() {
        return coveragesRepository.getCoveragesDetailsData();
    }

    public MutableLiveData<CoverageResponse> getCoverageData() {
        return coveragesRepository.getCoveragesData();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return coveragesRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return coveragesRepository.errorState;
    }


    public MutableLiveData<String> getRelationGroupData() {
        return coveragesRepository.getRelationGroupData();
    }

    public void setError(boolean state) {
        coveragesRepository.setError(state);
    }

    public MutableLiveData<Boolean> getReloginState() {
        return coveragesRepository.reloginState;
    }


}
