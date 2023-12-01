package com.csform.android.MB360.insurance.myclaims.retrofit;

import static com.csform.android.MB360.BuildConfig.BASE_URL_MY_CLAIMS;

import com.csform.android.MB360.BuildConfig;
import com.csform.android.MB360.utilities.LogMyBenefits;

import com.csform.android.MB360.utilities.AppServerConstants;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MyClaimsRetrofitClient {
    private static MyClaimsRetrofitClient instance;
    MyClaimsApi myClaimsApi;


    public MyClaimsRetrofitClient() {
        //ssl pinning
        CertificatePinner certPinner = new CertificatePinner.Builder()
                .add(BuildConfig.DOMAIN_STAR,
                        BuildConfig.CERT_256).build();

        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certPinner)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/xml");


                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL_MY_CLAIMS)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();
        myClaimsApi = retrofit.create(MyClaimsApi.class);

    }

    public static synchronized MyClaimsRetrofitClient getInstance() {
        if (instance == null) {
            instance = new MyClaimsRetrofitClient();
        }
        return instance;
    }

    public MyClaimsApi getClaimsApi() {
        return myClaimsApi;
    }
}
