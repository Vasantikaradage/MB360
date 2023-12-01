package com.csform.android.MB360.fitness.retrofit;

import static com.csform.android.MB360.BuildConfig.BASE_URL;
import static com.csform.android.MB360.BuildConfig.BASE_URL_FITNESS;
import static com.csform.android.MB360.BuildConfig.BASIC_AUTH_PASSWORD;
import static com.csform.android.MB360.BuildConfig.BASIC_AUTH_USERNAME;

import android.util.Log;

import com.csform.android.MB360.utilities.AppServerConstants;
import com.csform.android.MB360.utilities.LogTags;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FitnessRetrofitClient {

    private static FitnessRetrofitClient instance;
    FitnessApi fitnessApi;

    public FitnessRetrofitClient() {
        // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    Log.d(LogTags.CLAIM_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        fitnessApi = retrofit.create(FitnessApi.class);

    }

    public static synchronized FitnessRetrofitClient getInstance() {
        if (instance == null) {
            instance = new FitnessRetrofitClient();
        }
        return instance;
    }

    public FitnessApi getFitnessApi() {
        return fitnessApi;
    }


    public static Retrofit getApiRetrofit(String token){
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();

                        Request.Builder builder2;
                        if(token != null)
                            builder2 = originalRequest.newBuilder()
                                    .addHeader("Authorization", "Bearer " + token);
                        else
                            builder2 = originalRequest.newBuilder()
                                    .addHeader("Accept", "application/json")
                                    .addHeader("Cache-Control", "no-cache")
                                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                    .addHeader("Postman-Token", "dynamic");
                                /*.header("Authorization",
                                        Credentials.basic("Common", "Password"));*/

                        Request newRequest = builder2.build();

                        return chain.proceed(newRequest);
                    }).build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            return new Retrofit.Builder()
                    .baseUrl("https://api.aktivolabs.com/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Retrofit getRetrofit(){

        try{

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

            return new Retrofit.Builder()
                    .baseUrl(AppServerConstants.JSONURL3)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Retrofit getAktivoRetrofit(){

        try{

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

            return new Retrofit.Builder()
                    .baseUrl(BASE_URL_FITNESS)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
