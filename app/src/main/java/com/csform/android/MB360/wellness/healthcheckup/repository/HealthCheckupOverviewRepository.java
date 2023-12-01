package com.csform.android.MB360.wellness.healthcheckup.repository;

import android.app.Application;
import com.csform.android.MB360.utilities.LogMyBenefits;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.wellness.healthcheckup.repository.responseclass.HealthCheckupOverviewResponse;
import com.csform.android.MB360.wellness.healthcheckup.repository.retrofit.HealthCheckupOverviewRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for HealthCheckupOverview {@link HealthCheckupOverviewRepository }
 * in users
 **/
public class HealthCheckupOverviewRepository {

    private final MutableLiveData<HealthCheckupOverviewResponse> healthCheckupOverviewResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;


    public HealthCheckupOverviewRepository(Application application) {
        this.healthCheckupOverviewResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @PolicyFeatures {@link HealthCheckupOverviewRepository } response
     **/

    public MutableLiveData<HealthCheckupOverviewResponse> getHealthCheckupOverview(String externalGroupSrNo, String agent) {

        Call<HealthCheckupOverviewResponse> healthCheckupOverviewCall = HealthCheckupOverviewRetrofitClient.getInstance().getHealthCheckupOverviewApi().getHealthCheckupOverview(externalGroupSrNo, agent);
        healthCheckupOverviewCall.enqueue(new Callback<HealthCheckupOverviewResponse>() {
            @Override
            public void onResponse(Call<HealthCheckupOverviewResponse> call, Response<HealthCheckupOverviewResponse> response) {
                if (response.code() == 200 && response.body().getStatus()) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body().toString());
                        healthCheckupOverviewResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "Error: ", e);
                        healthCheckupOverviewResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<HealthCheckupOverviewResponse> call, Throwable t) {

            }
        });

        return healthCheckupOverviewResponseMutableLiveData;
    }

    public MutableLiveData<HealthCheckupOverviewResponse> getHealthCheckupOverviewData() {
        return healthCheckupOverviewResponseMutableLiveData;

    }
}
