package com.csform.android.MB360.fitness;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aktivolabs.aktivocore.data.repositories.LocalRepository;
import com.aktivolabs.aktivocore.managers.AktivoManager;
import com.csform.android.MB360.R;
import com.csform.android.MB360.fitness.AktivoData.AktivoAuthPerm;
import com.csform.android.MB360.fitness.AktivoData.AktivoMethods;
import com.csform.android.MB360.fitness.request.FitnessEmpGroupInfoRequest;
import com.csform.android.MB360.fitness.response.FitnessEmpGroupInfoResponse;
import com.csform.android.MB360.fitness.retrofit.FitnessRetrofitClient;
import com.csform.android.MB360.insurance.myclaims.responseclass.DocumentElement;
import com.csform.android.MB360.utilities.LogTags;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.jetbrains.annotations.NotNull;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FitnessRepository implements AktivoMethods {
    private final MutableLiveData<FitnessEmpGroupInfoResponse> fitnessMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;

    //aktivo state data
    public final MutableLiveData<Boolean> userAuthenticated;
    Application application;
    FirebaseCrashlytics crashlytics;

    //akitivo manager
    AktivoManager mAktivoManager;
    AktivoAuthPerm aktivoAuthPerm;

    //askPermission live Data
    public final MutableLiveData<Boolean> fitnessPermission;

    public FitnessRepository(Application application) {
        this.fitnessMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        crashlytics = FirebaseCrashlytics.getInstance();

        //instantiate the akitvo manager in repo
        mAktivoManager = AktivoManager.getInstance(application);
        aktivoAuthPerm = new AktivoAuthPerm(application, this);
        aktivoAuthPerm.getPermissions();


        fitnessPermission = new MutableLiveData<>(false);
        userAuthenticated = new MutableLiveData<>(false);
    }

    public LiveData<FitnessEmpGroupInfoResponse> getFitnessEmpGroupInfoData() {
        return fitnessMutableLiveData;
    }

    public MutableLiveData<FitnessEmpGroupInfoResponse> getFitnessEmpGroupInfo(FitnessEmpGroupInfoRequest empGroupInfoRequest) {
        Log.d(LogTags.FITNESS_ACTIVITY, "request body" + empGroupInfoRequest.toString());
        Call<FitnessEmpGroupInfoResponse> fitnessCall = FitnessRetrofitClient.getInstance().getFitnessApi().getFotnessEmpGroupInfo(empGroupInfoRequest);
        fitnessCall.enqueue(new Callback<FitnessEmpGroupInfoResponse>() {
            @Override
            public void onResponse(Call<FitnessEmpGroupInfoResponse> call, Response<FitnessEmpGroupInfoResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.FITNESS_ACTIVITY, "onResponse: " + response.body().toString());
                        fitnessMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.FITNESS_ACTIVITY, "Error: ", e);
                        fitnessMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } /*else if (response.body().getResult() != null && !response.body().getResult().getStatus()) {
                    fitnessMutableLiveData.setValue(response.body());
                    Log.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body().toString());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    // Toast.makeText(application, "Something Went wrong: " + response.body().getResult().getMessage(), Toast.LENGTH_LONG).show();

                }*/
            }

            @Override
            public void onFailure(Call<FitnessEmpGroupInfoResponse> call, Throwable t) {
                errorState.setValue(true);
                loadingState.setValue(false);
                Toast.makeText(application, "Something Went wrong: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });

        return fitnessMutableLiveData;
    }


    @Override
    public void authenticated() {
        //foreground service
        LocalRepository localRepository = new LocalRepository(application);
        localRepository.putNotificationTitle(application.getString(R.string.app_name));
        localRepository.putNotificationForSmallIcon(R.drawable.add_white);

        //need to update the fitness data here
        updateFitnessData();
    }

    private void updateFitnessData() {
        mAktivoManager.isPermissionGranted().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull Boolean value) {
                if (value) {
                    syncFitnessData();
                } else {
                    gotPermissions();

                }
            }

            @Override
            public void onError(@NotNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void syncFitnessData() {
        Log.d(LogTags.FITNESS_ACTIVITY, "attempting to sync fitness data from sdk");
        Toast.makeText(application.getApplicationContext(), "Syncing Data", Toast.LENGTH_SHORT).show();
        mAktivoManager.syncFitnessData().subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.d(LogTags.FITNESS_ACTIVITY, "Sync Complete! ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(LogTags.FITNESS_ACTIVITY, "Sync onError: ", e);
            }
        });
    }

    @Override
    public void gotPermissions() {
        Log.d(LogTags.FITNESS_ACTIVITY, "Permission request");


        //ask user for singing in google
        fitnessPermission.setValue(true);

    }

    @Override
    public void getToken(String token) {

    }

    @Override
    public void authenticationFailed() {

    }


    //google sign-in permission
    public MutableLiveData<Boolean> requestGooglePermission() {
        return fitnessPermission;
    }

    public void resetGooglePermission() {
        fitnessPermission.setValue(false);
    }
}
