package com.csform.android.MB360.fitness.ProfileScreenData;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.R;

import com.csform.android.MB360.fitness.AktivoData.AktivoResponse;

import com.csform.android.MB360.fitness.retrofit.FitnessApi;
import com.csform.android.MB360.fitness.retrofit.FitnessRetrofitClient;
import com.csform.android.MB360.utilities.AppServerConstants;
import com.csform.android.MB360.wellness.homehealthcare.cancellation.responseclass.ApiStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OnBoardRepo {

    public static volatile OnBoardRepo instance;
    private final Context context;
    private final SharedPreferences.Editor sharedprefEdit;
    public String Gender = "MALE", HeightMeasuredIN = "cm", WeightMeasuredIN = "kg";
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String onBoardtoken = null;
    private FitnessApi profileAPI = FitnessRetrofitClient.getApiRetrofit(onBoardtoken).create(FitnessApi.class);
    private MutableLiveData<Boolean> onBoardedEmp = new MutableLiveData<>();
    private SharedPreferences sharedpreferences;

    private OnBoardRepo(Context context) {
        this.context = context;
        getAktivoID("semantic");
        onBoardedEmp.postValue(false);
        sharedprefEdit = context.getSharedPreferences(AppServerConstants.adminsettings,
                Context.MODE_PRIVATE).edit();
        sharedpreferences = context.getSharedPreferences(AppServerConstants.adminsettings, Context.MODE_PRIVATE);
    }

    public static OnBoardRepo getInstance(Context context) {
        if (instance == null) {
            instance = new OnBoardRepo(context);
        }

        return instance;
    }

    public MutableLiveData<Boolean> getOnBoardedEmp() {
        return onBoardedEmp;
    }

    public void setOnBoardedEmp(MutableLiveData<Boolean> onBoardedEmp) {
        this.onBoardedEmp = onBoardedEmp;
    }

    private void getAktivoID(String clientid) {

        Single<AktivoResponse> response = profileAPI.getAccessToken(
                "x-www-form-urlencoded", clientid,
                context.getResources().getString(R.string.client_secret),
                "client_credentials",
                "company:read_write:as_a_client,member:read_write:as_a_client"
        );

        compositeDisposable.add(response.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribeWith(new DisposableSingleObserver<AktivoResponse>() {

                    @Override
                    public void onSuccess(AktivoResponse aktivoResponse) {
                        Log.e("Access_Token", aktivoResponse.getStrAccessToken());
                        onBoardtoken = aktivoResponse.getStrAccessToken();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RxError", e.toString());
                    }
                }));
    }

    public void onBoardMember(OnBoardRequest jsonObject, String strEmpSrno) {
        try {
            profileAPI = FitnessRetrofitClient.getApiRetrofit(onBoardtoken).create(FitnessApi.class);

            Single<OnBoardAktivoBeans> response = profileAPI.setOnBoardNewEmp(jsonObject);

            compositeDisposable.add(response.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribeWith(new DisposableSingleObserver<OnBoardAktivoBeans>() {

                        @Override
                        public void onSuccess(@NotNull OnBoardAktivoBeans onBoardAktivoBeans) {

                            String member_id = onBoardAktivoBeans.getData().getStrMemberData().getMemberID();
                            Log.e("Onboard Response", member_id);
                            String Id = sharedpreferences.getString("Employee_SR_NO", null);
                            sharedprefEdit.putString("_id", member_id);
                            sharedprefEdit.putString("height_cm", jsonObject.getMember().getStrHeightCM());
                            sharedprefEdit.putString("weight_kg", jsonObject.getMember().getStrWeightKG());

                            getAktivoID(member_id);
                            sharedprefEdit.apply();

                            UploadProfileBeans uploadProfileBeans = new UploadProfileBeans();
                            uploadProfileBeans.setStrBedTime(jsonObject.getMember().getStrBedTime());
                            uploadProfileBeans.setStrWakeUpTime(jsonObject.getMember().getStrWakeUp());
                            uploadProfileBeans.setStrHeight(jsonObject.getMember().getStrHeightCM());
                            uploadProfileBeans.setStrWeight(jsonObject.getMember().getStrWeightKG());
                            uploadProfileBeans.setStrEmpSrno(strEmpSrno);
                            uploadProfileBeans.setStrCompanyId(onBoardAktivoBeans.getData().getStrCompanyData().getMemberID());
                            uploadProfileBeans.setStrCompantExtId(onBoardAktivoBeans.getData().getStrCompanyData().getExternalId());

                            uploadProfileBeans.setStrMemberExternalId(onBoardAktivoBeans.getData().getStrMemberData().getExternalId());

                            uploadProfileBeans.setStrMemberOnboard(onBoardAktivoBeans.getData().getStrMemberData().isNew() ? "1" : "0");
                            uploadProfileBeans.setStrCompanyOnboard(onBoardAktivoBeans.getData().getStrCompanyData().isNew() ? "1" : "0");

                            uploadProfileBeans.setStrMemberID(onBoardAktivoBeans.getData().getStrMemberData().getMemberID());
                            onBoardSemantic(uploadProfileBeans);

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(context, "Unable to Connect the server", Toast.LENGTH_SHORT).show();
                            /*Intent i;
                            i = new Intent(context, AppLandingPage.class);
                            i.putExtra("PolicyCheckingStatus", 1);
                            context.startActivity(i);
                            Log.e("Rx Onboard error", e.toString());*/
                            new Handler().postDelayed(() -> onBoardedEmp.postValue(true), 1000);
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onBoardSemantic(UploadProfileBeans jsonObject) {
        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();

                        Request.Builder builder = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .header("Authorization",
                                        Credentials.basic("Common", "Password"));

                        Request newRequest = builder.build();

                        return chain.proceed(newRequest);
                    }).build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppServerConstants.JSONURL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            profileAPI = retrofit.create(FitnessApi.class);

            Single<SemanticResponse> response = profileAPI.uploadInfo(jsonObject);

            compositeDisposable.add(response.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribeWith(new DisposableSingleObserver<SemanticResponse>() {
                        @Override
                        public void onSuccess(SemanticResponse onBoardAktivoBeans) {
                            ApiStatus apiStatus = onBoardAktivoBeans.getApiStatus();

                            new Handler().postDelayed(() -> onBoardedEmp.postValue(true), 1000);

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("Rx Onboard error", e.toString());
                            new Handler().postDelayed(() -> onBoardedEmp.postValue(true), 1000);
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onDestroy() {
        compositeDisposable.clear();
    }


}
