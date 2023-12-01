package com.csform.android.MB360.insurance.hospitalnetwork.retrofit;

import static com.csform.android.MB360.BuildConfig.BASE_URL_NETWORK_HOSPITAL;

import com.csform.android.MB360.BuildConfig;
import com.csform.android.MB360.utilities.LogMyBenefits;

import com.csform.android.MB360.insurance.policyfeatures.repository.retrofit.PolicyFeaturesApi;
import com.csform.android.MB360.insurance.policyfeatures.repository.retrofit.PolicyFeaturesRetrofitClient;
import com.csform.android.MB360.utilities.AppServerConstants;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;

import okhttp3.CertificatePinner;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class HospitalNetworkRetrofitClient {
    private static HospitalNetworkRetrofitClient instance;
    HospitalNetworkApi hospitalNetworkApi;


    public HospitalNetworkRetrofitClient() {
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
                    LogMyBenefits.d(LogTags.HOSPITAL_NETWORK, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL_NETWORK_HOSPITAL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();
        hospitalNetworkApi = retrofit.create(HospitalNetworkApi.class);

    }

    public static synchronized HospitalNetworkRetrofitClient getInstance() {
        if (instance == null) {
            instance = new HospitalNetworkRetrofitClient();
        }
        return instance;
    }

    public HospitalNetworkApi getHospitalNetworkApi() {
        return hospitalNetworkApi;
    }
}


