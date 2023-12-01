package com.csform.android.MB360.onboarding.authentication.repository;

import static com.csform.android.MB360.BuildConfig.BASE_URL;

import com.appmattus.certificatetransparency.CTInterceptorBuilder;
import com.csform.android.MB360.BuildConfig;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;

import java.security.cert.Certificate;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginClient {

    private static LoginClient instance;
    private LoginApi loginApi;

    private LoginClient() { //constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, newRequest.url().toString());
                    return chain.proceed(newRequest);
                }).build();


        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        loginApi = retrofit.create(LoginApi.class);
    }

    public static synchronized LoginClient getInstance() {
        if (instance == null) {
            instance = new LoginClient();
        }
        return instance;

    }

    public LoginApi getLoginApi() {
        return loginApi;
    }
}