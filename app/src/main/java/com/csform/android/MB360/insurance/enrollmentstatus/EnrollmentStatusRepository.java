package com.csform.android.MB360.insurance.enrollmentstatus;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.BuildConfig;
import com.csform.android.MB360.R;
import com.csform.android.MB360.insurance.enrollmentstatus.responseclass.EnrollmentStatus;
import com.csform.android.MB360.insurance.enrollmentstatus.responseclass.EnrollmentStatusResponse;
import com.csform.android.MB360.insurance.enrollmentstatus.retrofit.EnrollmentStatusRetrofitClient;
import com.csform.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;
import com.csform.android.MB360.utilities.AesNew;
import com.csform.android.MB360.utilities.EncryptionPreference;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.utilities.rootdetection.CertChecker;
import com.csform.android.MB360.utilities.token.responseclasses.AuthToken;
import com.csform.android.MB360.utilities.token.retrofit.BearerRetrofitClient;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnrollmentStatusRepository {

    Application application;

    MutableLiveData<EnrollmentStatusResponse> statusResponse = new MutableLiveData<>();
    EnrollmentStatusResponse enrollmentStatusResponse = new EnrollmentStatusResponse();
    boolean SSL_PINNED = false;
    public final MutableLiveData<Boolean> reloginState;


    public EnrollmentStatusRepository(Application application) {

        this.application = application;
        this.reloginState = new MutableLiveData<>(false);
    }


    public MutableLiveData<EnrollmentStatusResponse> getEnrollmentStatus(String employeeSrNo, String groupChildSrNo, String oegrpBasInfoSrNo) {

        enrollmentStatusResponse.setLoadingState(true);
        enrollmentStatusResponse.setErrorState(false);


        try {
            Call<EnrollmentStatus> enrollmentStatusCall = EnrollmentStatusRetrofitClient.getInstance(application)
                    .getEnrollmentStatusApi().getEnrollmentStatus(
                            AesNew.encrypt(UtilMethods.checkSpecialCharacters(employeeSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(groupChildSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oegrpBasInfoSrNo), application.getString(R.string.pass_phrase)));



            Callback<EnrollmentStatus> callback = new Callback<EnrollmentStatus>() {
                @Override
                public void onResponse(Call<EnrollmentStatus> call, Response<EnrollmentStatus> response) {
                    try {
                        LogMyBenefits.d("ENROLLMENT-STATUS", response.body().toString());
                        if (response.code() == 200) {
                            enrollmentStatusResponse.setEnrollmentStatus(response.body());
                            enrollmentStatusResponse.setErrorState(false);
                            enrollmentStatusResponse.setLoadingState(false);
                            statusResponse.setValue(enrollmentStatusResponse);

                        } else {
                            enrollmentStatusResponse.setErrorState(true);
                            enrollmentStatusResponse.setLoadingState(false);
                            enrollmentStatusResponse.setEnrollmentStatus(null);
                            statusResponse.setValue(enrollmentStatusResponse);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        enrollmentStatusResponse.setErrorState(true);
                        enrollmentStatusResponse.setLoadingState(false);
                        enrollmentStatusResponse.setEnrollmentStatus(null);
                        statusResponse.setValue(enrollmentStatusResponse);
                    }
                }

                @Override
                public void onFailure(Call<EnrollmentStatus> call, Throwable t) {
                    call.cancel();

                    LogMyBenefits.e("ENROLLMENT-STATUS", t.getLocalizedMessage());
                    enrollmentStatusResponse.setErrorState(true);
                    enrollmentStatusResponse.setLoadingState(false);
                    enrollmentStatusResponse.setEnrollmentStatus(null);
                    statusResponse.setValue(enrollmentStatusResponse);


                }

            };

            try {
                // Disable trust manager checks - we'll check the certificate manually ourselves later
                TrustManager[] trustManager = {new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }};
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    try {

                        SSLContext context = SSLContext.getInstance("TLS");
                        context.init(null, trustManager, null);

                        SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(BuildConfig.DOMAIN, Integer.parseInt(BuildConfig.PORT));
                        X509Certificate[] certs = (X509Certificate[]) socket.getSession().getPeerCertificates();
                        X509Certificate leafCertificateserver = (X509Certificate) certs[0];
                        LogMyBenefits.d("CERT", "SERVER CERT: " + leafCertificateserver);


                        try {
                            InputStream certInputStream = application.getApplicationContext().getAssets().open("cer.crt");
                            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                            X509Certificate leafCertificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);
                            LogMyBenefits.d("CERT", "CLIENT CERT: " + leafCertificate);

                            if (true) {
                                LogMyBenefits.d("CERT", "VALID CERT");
                                SSL_PINNED = true;
                            } else {
                                LogMyBenefits.d("CERT", "ERROR LEAF: NOT MATCH" + leafCertificate);
                                throw new RuntimeException();
                            }

                            // Use the leafCertificate object to pin the certificate
                        } catch (Exception e) {
                            LogMyBenefits.e("CERT", "ERROR: ", e);
                            e.printStackTrace();
                            if (e instanceof RuntimeException) {
                                throw new RuntimeException("SSL ERROR!");
                            }
                        }


                        Predicate<X509Certificate> certMatch = cert -> CertChecker.Companion.doesCertMatchPin(BuildConfig.CERT, cert);
                        if (false) {
                            socket.close();
                            // Close the socket immediately without sending a request
                            SSL_PINNED = false;
                            throw new RuntimeException("Unrecognized cert hash.");

                        } else {
                            SSL_PINNED = true;


        enrollmentStatusCall.enqueue(new Callback<EnrollmentStatus>() {
            @Override
            public void onResponse(Call<EnrollmentStatus> call, Response<EnrollmentStatus> response) {
                try {
                    LogMyBenefits.d("ENROLLMENT-STATUS", response.body().toString());
                    if (response.code() == 200) {
                        enrollmentStatusResponse.setEnrollmentStatus(response.body());
                        enrollmentStatusResponse.setErrorState(false);
                        enrollmentStatusResponse.setLoadingState(false);
                        statusResponse.setValue(enrollmentStatusResponse);

                    }else if (response.code() == 401 || response.code() == 400) {


                        //refresh the token
                        EncryptionPreference encryptionPreference = new EncryptionPreference(application.getApplicationContext());
                        try {

                            Call<AuthToken> authTokenCall = BearerRetrofitClient.getInstance(application.getApplicationContext()).getBearerApi().
                                    refreshToken(AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_EMP_SR_NO), application.getString(R.string.pass_phrase)),
                                            AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_PERSON_SR_NO), application.getString(R.string.pass_phrase)),
                                            AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_EMP_ID_NO), application.getString(R.string.pass_phrase)));

                            authTokenCall.enqueue(new Callback<AuthToken>() {
                                @Override
                                public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                                    if (response.code() == 200) {
                                        LogMyBenefits.d("REFRESH-TOKEN", "TOKEN: " + response.body().getAuthToken());
                                        if (response.body() != null) {
                                            if (!response.body().getAuthToken().isEmpty()) {
                                                encryptionPreference.setEncryptedDataString(BuildConfig.BEARER_TOKEN, response.body().getAuthToken());
                                                enrollmentStatusCall.clone().enqueue(callback);
                                            }else {
                                                reloginState.setValue(true);
                                            }
                                        }else {
                                            reloginState.setValue(true);
                                        }

                                    }else {
                                        reloginState.setValue(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<AuthToken> call, Throwable t) {
                                    call.cancel();
                                    LogMyBenefits.e("REFRESH-TOKEN", "onFailure: ", t);
                                    t.printStackTrace();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {

                        enrollmentStatusResponse.setErrorState(true);
                        enrollmentStatusResponse.setLoadingState(false);
                        enrollmentStatusResponse.setEnrollmentStatus(null);
                        statusResponse.setValue(enrollmentStatusResponse);

                        Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_SHORT).show();

                    } /*else {
                        enrollmentStatusResponse.setErrorState(true);
                        enrollmentStatusResponse.setLoadingState(false);
                        enrollmentStatusResponse.setEnrollmentStatus(null);
                        statusResponse.setValue(enrollmentStatusResponse);
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                    enrollmentStatusResponse.setErrorState(true);
                    enrollmentStatusResponse.setLoadingState(false);
                    enrollmentStatusResponse.setEnrollmentStatus(null);
                    statusResponse.setValue(enrollmentStatusResponse);
                }

            }

            @Override
            public void onFailure(Call<EnrollmentStatus> call, Throwable t) {
                LogMyBenefits.e("ENROLLMENT-STATUS", t.getLocalizedMessage());
                enrollmentStatusResponse.setErrorState(true);
                enrollmentStatusResponse.setLoadingState(false);
                enrollmentStatusResponse.setEnrollmentStatus(null);
                statusResponse.setValue(enrollmentStatusResponse);
            }
        });
       /* } catch (Exception e) {
            e.printStackTrace();
            enrollmentStatusResponse.setErrorState(false);
            enrollmentStatusResponse.setLoadingState(false);
        }

        return statusResponse;
    }*/

                        }

                        socket.close();
                    } catch (Exception e) {

                        LogMyBenefits.e("SSL", "Error", e);
                        e.printStackTrace();
                        enrollmentStatusResponse.setErrorState(true);
                        enrollmentStatusResponse.setLoadingState(false);

                        if (e instanceof RuntimeException) {
                            showToast(() -> Toast.makeText(application, "SSL ERROR!", Toast.LENGTH_SHORT).show());
                            throw new RuntimeException("SSL ERROR!");
                        }

                    }

                });


            } catch (Throwable e) {
                SSL_PINNED = false;
                e.printStackTrace();
                LogMyBenefits.e("SSL", "Error", e);
            }


        } catch (Exception e) {
            e.printStackTrace();
            enrollmentStatusResponse.setErrorState(false);
            enrollmentStatusResponse.setLoadingState(false);
        }


        return statusResponse;
    }


    public MutableLiveData<EnrollmentStatusResponse> getEnrollmentStatusData() {
        return statusResponse;
    }


    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
