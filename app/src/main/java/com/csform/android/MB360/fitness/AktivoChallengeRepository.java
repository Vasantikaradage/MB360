package com.csform.android.MB360.fitness;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.fitness.ProfileScreenData.AktivoBeanProfile;
import com.csform.android.MB360.fitness.retrofit.FitnessApi;
import com.csform.android.MB360.fitness.retrofit.FitnessRetrofitClient;
import com.csform.android.MB360.utilities.LogTags;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AktivoChallengeRepository {

    private final MutableLiveData<AktivoBeanProfile> aktivoMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;
    FirebaseCrashlytics crashlytics;



    public AktivoChallengeRepository(Application application) {
        this.aktivoMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        crashlytics = FirebaseCrashlytics.getInstance();
    }

    public LiveData<AktivoBeanProfile> getAktivoChanllengerInfoData() {
        return aktivoMutableLiveData;
    }

   public MutableLiveData<AktivoBeanProfile> getAktivoChanllengerInfo(String token, String _id, String leaderboard, String startDate, String endDate) {
       // Log.d(LogTags.FITNESS_ACTIVITY,"request body"+url.toString());
        FitnessApi challengeAPI = FitnessRetrofitClient.getApiRetrofit(null).create(FitnessApi.class);

        Call<AktivoBeanProfile> challengeCall = (Call<AktivoBeanProfile>) challengeAPI.getAktivoChallenge("Bearer "+token,_id,leaderboard,startDate,endDate);
       challengeCall.enqueue(new Callback<AktivoBeanProfile>() {
            @Override
            public void onResponse(Call<AktivoBeanProfile> call, Response<AktivoBeanProfile> response) {
                if (response.code() == 200 ) {
                    try {
                        Log.d(LogTags.FITNESS_ACTIVITY, "onResponse: " + response.body().toString());
                        aktivoMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.FITNESS_ACTIVITY, "Error: ", e);
                        aktivoMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AktivoBeanProfile> call, Throwable t) {
                errorState.setValue(true);
                loadingState.setValue(false);
                Toast.makeText(application, "Something Went wrong: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });

        return aktivoMutableLiveData;
    }
}

