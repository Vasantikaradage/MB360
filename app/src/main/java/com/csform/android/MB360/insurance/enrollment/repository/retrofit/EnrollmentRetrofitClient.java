package com.csform.android.MB360.insurance.enrollment.repository.retrofit;


import static com.csform.android.MB360.BuildConfig.BASE_URL;
import static com.csform.android.MB360.BuildConfig.BASE_URL_ACTUAL_ENROLLMENT;
import static com.csform.android.MB360.BuildConfig.BASIC_AUTH_PASSWORD;
import static com.csform.android.MB360.BuildConfig.BASIC_AUTH_USERNAME;

import com.csform.android.MB360.utilities.AesEncryption;
import com.csform.android.MB360.utilities.LogMyBenefits;

import com.csform.android.MB360.insurance.claims.repository.retrofit.ClaimsApi;
import com.csform.android.MB360.utilities.AppServerConstants;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;

import okhttp3.Credentials;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnrollmentRetrofitClient {

    private static EnrollmentRetrofitClient instance;


    EnrollmentApi enrollmentApi;

    public EnrollmentRetrofitClient() {//construction

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request originalRequest = chain.request();

            Request.Builder builder = null;
            try {
                builder = originalRequest.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .header("Authorization",
                                Credentials.basic(AesEncryption.encrypt(BASIC_AUTH_USERNAME), AesEncryption.encrypt(BASIC_AUTH_PASSWORD)));
            } catch (Exception e) {
              e.printStackTrace();
            }


            Request newRequest = builder.build();
            LogMyBenefits.d(LogTags.ENROLLMENT, newRequest.url().toString());

            return chain.proceed(newRequest);
        }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL_ACTUAL_ENROLLMENT).addConverterFactory(GsonConverterFactory.create()).client(client).build();


        enrollmentApi = retrofit.create(EnrollmentApi.class);


    }

    public static synchronized EnrollmentRetrofitClient getInstance() {
        if (instance == null) {
            instance = new EnrollmentRetrofitClient();
        }
        return instance;
    }

    public EnrollmentApi getEnrollmentApi() {
        return enrollmentApi;
    }
}
