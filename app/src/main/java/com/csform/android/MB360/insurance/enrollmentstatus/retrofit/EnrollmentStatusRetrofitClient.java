package com.csform.android.MB360.insurance.enrollmentstatus.retrofit;

import android.content.Context;
import android.util.Log;

import com.csform.android.MB360.BuildConfig;
import com.csform.android.MB360.insurance.ecards.retrofit.EcardApi;
import com.csform.android.MB360.insurance.ecards.retrofit.EcardRetrofitClientJson;
import com.csform.android.MB360.utilities.EncryptionPreference;
import com.csform.android.MB360.utilities.LogMyBenefits;

import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnrollmentStatusRetrofitClient {
    private static EnrollmentStatusRetrofitClient instance;
    EnrollmentStatusApi enrollmentStatusApi;
    private EncryptionPreference encryptionPreference;

    public EnrollmentStatusRetrofitClient(Context context) {//constructor


        //getting the token
        encryptionPreference = new EncryptionPreference(context);

        //ssl pinning
        CertificatePinner certPinner = new CertificatePinner.Builder()
                .add(BuildConfig.DOMAIN_STAR,
                        BuildConfig.CERT_256).build();

        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certPinner)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = null;
                    try {
                        builder = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Request newRequest = builder.build();
                    LogMyBenefits.d("", newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();
        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        enrollmentStatusApi = retrofit.create(EnrollmentStatusApi.class);
    }

    public static synchronized EnrollmentStatusRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new EnrollmentStatusRetrofitClient(context);
        }
        return instance;
    }


    public EnrollmentStatusApi getEnrollmentStatusApi() {
        return enrollmentStatusApi;
    }

}