package com.csform.android.MB360.insurance.profile.repository.retrofit;

import static com.csform.android.MB360.BuildConfig.BASE_URL;
import static com.csform.android.MB360.BuildConfig.BASIC_AUTH_PASSWORD;
import static com.csform.android.MB360.BuildConfig.BASIC_AUTH_USERNAME;

import android.content.Context;

import com.csform.android.MB360.BuildConfig;
import com.csform.android.MB360.utilities.AesEncryption;
import com.csform.android.MB360.utilities.EncryptionPreference;
import com.csform.android.MB360.utilities.LogMyBenefits;

import com.csform.android.MB360.insurance.utilities.repository.retrofit.UtilitiesApi;
import com.csform.android.MB360.insurance.utilities.repository.retrofit.UtilitiesRetrofitClient;
import com.csform.android.MB360.utilities.AppServerConstants;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;

import okhttp3.CertificatePinner;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileRetrofitClient {
    private static ProfileRetrofitClient instance;
    ProfileApi profileApi;

    public ProfileRetrofitClient(Context context) {//constructor
        EncryptionPreference encryptionPreference = new EncryptionPreference(context);

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
                    LogMyBenefits.d(LogTags.PROFILE_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        profileApi = retrofit.create(ProfileApi.class);

    }

    public static synchronized ProfileRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new ProfileRetrofitClient(context);
        }
        return instance;

    }

    public ProfileApi getProfileApi() {
        return profileApi;
    }

}
