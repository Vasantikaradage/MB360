package com.csform.android.MB360.fitness.AktivoData;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.aktivolabs.aktivocore.data.models.User;
import com.aktivolabs.aktivocore.managers.AktivoManager;

import com.csform.android.MB360.R;
import com.csform.android.MB360.fitness.retrofit.FitnessApi;
import com.csform.android.MB360.fitness.retrofit.FitnessRetrofitClient;
import com.csform.android.MB360.utilities.AppServerConstants;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;


import io.reactivex.schedulers.Schedulers;


public class AktivoAuthPerm {
    private static Context cnt;
    private static String url = "https://api.aktivolabs.com/oauth/token";
    private AktivoManager mAktivoManager;
    private SharedPreferences sharedpreferences;
    private AktivoMethods aktivoMethods;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();




    public AktivoAuthPerm(Context cnt, AktivoMethods aktivoMethods) {

        AktivoAuthPerm.cnt = cnt;
        mAktivoManager = AktivoManager.getInstance(cnt);
        sharedpreferences = cnt.getSharedPreferences(AppServerConstants.adminsettings,
                Context.MODE_PRIVATE);
        this.aktivoMethods = aktivoMethods;

    }

    public void getAccessToken(String client_id) {
        try {

            FitnessApi profileAPI = FitnessRetrofitClient.getApiRetrofit(null).create(FitnessApi.class);

            Single<AktivoResponse> response = profileAPI.getAccessToken(
                    "x-www-form-urlencoded", client_id,
                    cnt.getResources().getString(R.string.client_secret),
                    "client_credentials");

            compositeDisposable.add(response.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribeWith(new DisposableSingleObserver<AktivoResponse>() {
                        @Override
                        public void onSuccess(AktivoResponse aktivoResponse) {

                            Log.e("Access_Token", aktivoResponse.getStrAccessToken());
                            SharedPreferences.Editor editor = cnt.getSharedPreferences(AppServerConstants.adminsettings,
                                    Context.MODE_PRIVATE).edit();
                            String response = aktivoResponse.getStrAccessToken();
                            editor.putString("access_token", response);
                            editor.apply();
                            aktivoMethods.getToken(response);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("RxError", e.toString());
                            aktivoMethods.authenticationFailed();
                        }
                    }));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void authenticateUser(String userToken) {
        Log.d("Data",userToken);
        if (TextUtils.isEmpty(userToken)) {

        } else {

            compositeDisposable.add(
                    mAktivoManager.authenticateUser(new User(userToken))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(new DisposableCompletableObserver() {

                                @Override
                                public void onComplete() {

                /*                    Toast.makeText(cnt,
                                            "Logged in", Toast.LENGTH_SHORT).show();
                */                    Log.d("Data","User Toke "+userToken + " sucess");
                                    SharedPreferences.Editor edit = sharedpreferences.edit();
                                    edit.putString("user_token", userToken);
                                    edit.putBoolean("Logged_in", true);
                                    edit.apply();
                                    aktivoMethods.authenticated();
                                }

                                @Override
                                public void onError(Throwable e) {

                                    Toast.makeText(cnt
                                            , e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.e("fitness error in User Token generation", e.toString());
                                }
                            }));
        }
    }

    public void getPermissions() {
        mAktivoManager.isPermissionGranted().subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    String id = sharedpreferences.getString("_id", null);
                    if (id == null) {
                        getAccessToken("semantic");
                    } else
                        getAccessToken(id);
                } else {
                     // aktivoMethods.gotPermissions();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Permission error", e.toString());
            }

            @Override
            public void onComplete() {
                   /* SharedPreferences.Editor edit = sharedpreferences.edit();
                    edit.putBoolean("Logged_in", true);
                    edit.apply();*/
            }
        });
    }

}
