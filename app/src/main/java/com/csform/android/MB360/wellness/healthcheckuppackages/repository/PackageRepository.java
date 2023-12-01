package com.csform.android.MB360.wellness.healthcheckuppackages.repository;

import android.app.Application;
import com.csform.android.MB360.utilities.LogMyBenefits;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.ResponseException;
import com.csform.android.MB360.wellness.healthcheckup.repository.requestclass.DependentRequest;
import com.csform.android.MB360.wellness.healthcheckup.repository.responseclass.MessageResponseDependent;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.requestclass.ScheduleRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.AllRelationResponse;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.CityResponseHC;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.DiagnosticCenterResponse;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupCancelAppointmentRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupOngoingResponse;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupUpdatePaymentRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.MessageResponseCancelAppointment;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.MessageResponseDependentDelete;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.MessageResponseUpdatePayment;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.MessageResponseVerifyNo;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.PackageDetailsResponse;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.PackageResponse;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.SummaryResponse;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.VerifyNoRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.fetchpayment.FetchPaymentResponse;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PackageRetrofitClient;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.MessageResponse;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for CLaims {@link PackageRepository }
 * in users
 **/
public class PackageRepository {

    private final MutableLiveData<PackageResponse> packageMutableLiveData;
    private final MutableLiveData<DiagnosticCenterResponse> diagnosticCenterResponseMutableLiveData;
    private final MutableLiveData<PackageDetailsResponse> packageDetailsResponseMutableLiveData;
    private final MutableLiveData<MessageResponse> messageResponseMutableLiveData;
    private final MutableLiveData<MessageResponseDependent> messageResponseDependentMutableLiveData;
    private final MutableLiveData<AllRelationResponse> allRelationResponseMutableLiveData;
    private final MutableLiveData<SummaryResponse> summaryResponseMutableLiveData;
    private final MutableLiveData<MessageResponseDependentDelete> messageResponseDependentDeleteMutableLiveData;
    private final MutableLiveData<MessageResponseVerifyNo> messageResponseVerifyNoMutableLiveData;
    private final MutableLiveData<MessageResponseUpdatePayment> messageResponseUpdatePaymentMutableLiveData;
    private final MutableLiveData<MessageResponseCancelAppointment> messageResponseCancelAppointmentMutableLiveData;
    private final MutableLiveData<CityResponseHC> cityResponseMutableLiveData;
    private final MutableLiveData<HealthCheckupOngoingResponse> healthCheckupOngoingResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;

    public final MutableLiveData<Boolean> packageLoadingState;
    public final MutableLiveData<Boolean> packageErrorState;

    public final MutableLiveData<Boolean> paymentLoadingState;
    public final MutableLiveData<Boolean> paymentErrorState;
    Application application;
    FirebaseCrashlytics crashlytics;


    //health Check Up cities
    public MutableLiveData<String> citiesLiveDataHC;


    public PackageRepository(Application application) {
        this.packageMutableLiveData = new MutableLiveData<>();
        this.diagnosticCenterResponseMutableLiveData = new MutableLiveData<>();
        this.packageDetailsResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();

        this.packageErrorState = new MutableLiveData<>(true);
        this.packageLoadingState = new MutableLiveData<>();
        this.paymentErrorState = new MutableLiveData<>(true);
        this.paymentLoadingState = new MutableLiveData<>();

        this.application = application;
        messageResponseMutableLiveData = new MutableLiveData<>();
        messageResponseDependentMutableLiveData = new MutableLiveData<>();
        allRelationResponseMutableLiveData = new MutableLiveData<>();
        summaryResponseMutableLiveData = new MutableLiveData<>();
        messageResponseDependentDeleteMutableLiveData = new MutableLiveData<>();
        messageResponseVerifyNoMutableLiveData = new MutableLiveData<>();
        messageResponseUpdatePaymentMutableLiveData = new MutableLiveData<>();
        messageResponseCancelAppointmentMutableLiveData = new MutableLiveData<>();
        healthCheckupOngoingResponseMutableLiveData = new MutableLiveData<>();
        this.cityResponseMutableLiveData = new MutableLiveData<>();
        this.citiesLiveDataHC = new MutableLiveData<>();
        crashlytics = FirebaseCrashlytics.getInstance();
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @PolicyFeatures {@link PackageResponse } response
     **/

    public MutableLiveData<PackageResponse> getPackages(String extGroupSrNo, String groupCode, String empIdNo) {

        if (packageDetailsResponseMutableLiveData.getValue() == null) {
            packageLoadingState.setValue(true);
            Call<PackageResponse> packagesCall = PackageRetrofitClient.getInstance().getPackageApi().getPackage(extGroupSrNo, groupCode, empIdNo);
            packagesCall.enqueue(new Callback<PackageResponse>() {
                @Override
                public void onResponse(Call<PackageResponse> call, Response<PackageResponse> response) {


                    if (response.code() == 200 && response.body().getMessage().getStatus()) {
                        try {
                            LogMyBenefits.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                            packageMutableLiveData.setValue(response.body());
                            packageErrorState.setValue(false);
                            packageLoadingState.setValue(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                            packageMutableLiveData.setValue(null);
                            packageErrorState.setValue(true);
                            packageLoadingState.setValue(false);
                            Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        packageErrorState.setValue(true);
                        packageLoadingState.setValue(false);
                        Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<PackageResponse> call, Throwable t) {

                    LogMyBenefits.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: " + t.getLocalizedMessage());
                    packageErrorState.setValue(true);
                    packageLoadingState.setValue(false);
                    throwFirebaseError(t);

                }
            });
        }

        return packageMutableLiveData;
    }

    private void throwFirebaseError(Throwable t) {
        //throwing the issues in firebase
        ResponseException exception = new ResponseException(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY + " : Response Code : " + " | Message " + t.getLocalizedMessage());
        Throwable throwable = new Throwable(exception);
        crashlytics.recordException(throwable);
    }

    public MutableLiveData<PackageResponse> getPackagesData() {
        return packageMutableLiveData;

    }

    public MutableLiveData<DiagnosticCenterResponse> getDiagnosticCenter(String city) {
        loadingState.setValue(true);
        Call<DiagnosticCenterResponse> diagnosticCenterResponseCall = PackageRetrofitClient.getInstance().getPackageApi().getDiagnosticCenter(city);
        diagnosticCenterResponseCall.enqueue(new Callback<DiagnosticCenterResponse>() {
            @Override
            public void onResponse(Call<DiagnosticCenterResponse> call, Response<DiagnosticCenterResponse> response) {

                if (response.code() == 200 && response.body().getMessage().getStatus()) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                        diagnosticCenterResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                        diagnosticCenterResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    LogMyBenefits.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onError: " + response.body().toString());

                    Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<DiagnosticCenterResponse> call, Throwable t) {

            }
        });

        return diagnosticCenterResponseMutableLiveData;
    }

    public MutableLiveData<DiagnosticCenterResponse> getDiagnosticCenterData() {
        return diagnosticCenterResponseMutableLiveData;
    }

    public MutableLiveData<PackageDetailsResponse> getPackageDetails(String packageSrNo) {
        loadingState.setValue(true);
        Call<PackageDetailsResponse> packageDetailsResponseCall = PackageRetrofitClient.getInstance().getPackageApi().getPackageDetails(packageSrNo);
        packageDetailsResponseCall.enqueue(new Callback<PackageDetailsResponse>() {
            @Override
            public void onResponse(Call<PackageDetailsResponse> call, Response<PackageDetailsResponse> response) {

                if (response.code() == 200 && response.body().getMessage().getStatus()) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                        packageDetailsResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                        packageDetailsResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<PackageDetailsResponse> call, Throwable t) {

            }
        });

        return packageDetailsResponseMutableLiveData;
    }

    public MutableLiveData<PackageDetailsResponse> getPackageDetailsData() {
        return packageDetailsResponseMutableLiveData;
    }

    public MutableLiveData<MessageResponse> scheduleHealthCheckup(ScheduleRequest scheduleRequest) {
        loadingState.setValue(true);

        MutableLiveData<MessageResponse> messageResponse = new MutableLiveData<>(null);

        Call<MessageResponse> scheduleCall = PackageRetrofitClient.getInstance().getPackageApi().startSchedule(scheduleRequest);

        scheduleCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponse;
    }

    public MutableLiveData<MessageResponseDependent> dependent(DependentRequest dependentRequest) {
        LogMyBenefits.d("DEPENDANT", "dependent: " + dependentRequest.toString());
        loadingState.setValue(true);
        Call<MessageResponseDependent> dependentCall = PackageRetrofitClient.getInstance().getPackageApi().postAddMember(dependentRequest);

        dependentCall.enqueue(new Callback<MessageResponseDependent>() {
            @Override
            public void onResponse(Call<MessageResponseDependent> call, Response<MessageResponseDependent> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseDependentMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseDependentMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseDependentMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseDependent> call, Throwable t) {
                LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseDependentMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponseDependentMutableLiveData;
    }

    public MutableLiveData<AllRelationResponse> getAllRelation(String familySrNo) {
        loadingState.setValue(true);
        Call<AllRelationResponse> allRelationResponseCall = PackageRetrofitClient.getInstance().getPackageApi().getAllRelation(familySrNo);
        allRelationResponseCall.enqueue(new Callback<AllRelationResponse>() {
            @Override
            public void onResponse(Call<AllRelationResponse> call, Response<AllRelationResponse> response) {

                if (response.code() == 200 && response.body().getMessage().getStatus()) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                        allRelationResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                        allRelationResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<AllRelationResponse> call, Throwable t) {

            }
        });

        return allRelationResponseMutableLiveData;
    }

    public MutableLiveData<AllRelationResponse> getAllRelationData() {
        return allRelationResponseMutableLiveData;
    }

    public MutableLiveData<SummaryResponse> getSummary(String familySrNo, String groupCode) {

        Call<SummaryResponse> summaryResponseCall = PackageRetrofitClient.getInstance().getPackageApi().getSummary(familySrNo, groupCode);
        summaryResponseCall.enqueue(new Callback<SummaryResponse>() {
            @Override
            public void onResponse(Call<SummaryResponse> call, Response<SummaryResponse> response) {

                if (response.code() == 200 && response.body().getMessage().getStatus()) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                        summaryResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                        summaryResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(true);
                        loadingState.setValue(false);
//                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                    errorState.setValue(true);
                    summaryResponseMutableLiveData.setValue(null);
                    loadingState.setValue(false);
//                    Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<SummaryResponse> call, Throwable t) {
                errorState.setValue(true);
                loadingState.setValue(false);
                summaryResponseMutableLiveData.setValue(null);
                Toast.makeText(application, "Unable to fetch your summary, please try again later.", Toast.LENGTH_LONG).show();

            }
        });

        return summaryResponseMutableLiveData;
    }

    public MutableLiveData<SummaryResponse> getSummaryData() {

        return summaryResponseMutableLiveData;
    }

    public MutableLiveData<MessageResponseDependentDelete> dependentDelete(String personSrNo) {
        loadingState.setValue(true);
        packageLoadingState.setValue(true);
        Call<MessageResponseDependentDelete> dependentDeleteCall = PackageRetrofitClient.getInstance().getPackageApi().deleteMember(personSrNo);

        dependentDeleteCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MessageResponseDependentDelete> call, Response<MessageResponseDependentDelete> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseDependentDeleteMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                        packageLoadingState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseDependentDeleteMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                        packageLoadingState.setValue(false);
                    }
                } else {
                    LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseDependentDeleteMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    packageLoadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseDependentDelete> call, Throwable t) {
                LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseDependentMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
                packageLoadingState.setValue(false);
                Toast.makeText(application, "Some error occurred, Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        return messageResponseDependentDeleteMutableLiveData;
    }

    public MutableLiveData<MessageResponseVerifyNo> verifyNo(VerifyNoRequest verifyNoRequest) {
        loadingState.setValue(true);
        Call<MessageResponseVerifyNo> verifyNoCall = PackageRetrofitClient.getInstance().getPackageApi().putVerifyNo(verifyNoRequest);

        verifyNoCall.enqueue(new Callback<MessageResponseVerifyNo>() {
            @Override
            public void onResponse(Call<MessageResponseVerifyNo> call, Response<MessageResponseVerifyNo> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseVerifyNoMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseVerifyNoMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseVerifyNoMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseVerifyNo> call, Throwable t) {
                LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseVerifyNoMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponseVerifyNoMutableLiveData;
    }

    public MutableLiveData<FetchPaymentResponse> fetchPaymentResponse(String extfamilySrNo, String extGroupChildSrNo, String employeeId, String totalPayment, String groupCode) {
        paymentLoadingState.setValue(true);
        MutableLiveData<FetchPaymentResponse> fetchPaymentResponseData = new MutableLiveData<>(null);
        Call<FetchPaymentResponse> fetchPaymentResponseCall = PackageRetrofitClient.getInstance().getPackageApi().fetchPaymentDetails(extfamilySrNo, extGroupChildSrNo, employeeId, totalPayment, groupCode);

        fetchPaymentResponseCall.enqueue(new Callback<FetchPaymentResponse>() {
            @Override
            public void onResponse(Call<FetchPaymentResponse> call, Response<FetchPaymentResponse> response) {

                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: Payment" + response.body());
                      Log.d("@@","@@"+response.body());
                        fetchPaymentResponseData.setValue(response.body());
                        paymentLoadingState.setValue(false);
                        paymentErrorState.setValue(false);
                    } catch (Exception e) {
                        fetchPaymentResponseData.setValue(null);
                        paymentLoadingState.setValue(false);
                        paymentErrorState.setValue(false);
                    }
                } else {
                    fetchPaymentResponseData.setValue(response.body());
                    paymentLoadingState.setValue(false);
                    paymentErrorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<FetchPaymentResponse> call, Throwable t) {
                fetchPaymentResponseData.setValue(null);
                paymentLoadingState.setValue(false);
                paymentErrorState.setValue(true);
            }
        });

        return fetchPaymentResponseData;
    }

    public MutableLiveData<MessageResponseUpdatePayment> putUpdatePayment(HealthCheckupUpdatePaymentRequest healthCheckupUpdatePaymentRequest) {
        loadingState.setValue(true);

        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "UPDATE-REQUEST: " + healthCheckupUpdatePaymentRequest);

        Call<MessageResponseUpdatePayment> updatePaymentCall = PackageRetrofitClient.getInstance().getPackageApi().putUpdatePayment(healthCheckupUpdatePaymentRequest);

        updatePaymentCall.enqueue(new Callback<MessageResponseUpdatePayment>() {
            @Override
            public void onResponse(Call<MessageResponseUpdatePayment> call, Response<MessageResponseUpdatePayment> response) {
                LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseUpdatePaymentMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseUpdatePaymentMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseUpdatePaymentMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseUpdatePayment> call, Throwable t) {
                LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseUpdatePaymentMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponseUpdatePaymentMutableLiveData;
    }

    public MutableLiveData<MessageResponseCancelAppointment> putCancelAppointment(HealthCheckupCancelAppointmentRequest healthCheckupCancelAppointmentRequest) {
        loadingState.setValue(true);
        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "Cancel appointment: " + healthCheckupCancelAppointmentRequest);
        Call<MessageResponseCancelAppointment> cancelAppointmentCall = PackageRetrofitClient.getInstance().getPackageApi().putCancelAppointment(healthCheckupCancelAppointmentRequest);

        cancelAppointmentCall.enqueue(new Callback<MessageResponseCancelAppointment>() {
            @Override
            public void onResponse(Call<MessageResponseCancelAppointment> call, Response<MessageResponseCancelAppointment> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseCancelAppointmentMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseCancelAppointmentMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseCancelAppointmentMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseCancelAppointment> call, Throwable t) {
                LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseCancelAppointmentMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponseCancelAppointmentMutableLiveData;
    }


    public MutableLiveData<CityResponseHC> getCitiesHC() {
        loadingState.setValue(true);
        if (cityResponseMutableLiveData.getValue() == null) {
            Call<CityResponseHC> citycall = PackageRetrofitClient.getInstance().getPackageApi().getcitiesHC();
            citycall.enqueue(new Callback<CityResponseHC>() {
                @Override
                public void onResponse(Call<CityResponseHC> call, Response<CityResponseHC> response) {
                    if (response.code() == 200 && response.body().getMessage().getStatus()) {
                        try {
                            LogMyBenefits.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                            cityResponseMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                            cityResponseMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                            Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<CityResponseHC> call, Throwable t) {

                }
            });
        }

        return cityResponseMutableLiveData;
    }

    public MutableLiveData<CityResponseHC> getcitiesData() {
        return cityResponseMutableLiveData;

    }

    public MutableLiveData<HealthCheckupOngoingResponse> getOngoingAppointment(String familySrNo, String extGroupSrNoString, String empIdNo, String groupCode) {
        loadingState.setValue(true);
        Call<HealthCheckupOngoingResponse> healthCheckupOngoingResponseCall = PackageRetrofitClient.getInstance().getPackageApi().getOngoingAppointment(familySrNo, extGroupSrNoString, empIdNo, groupCode);
        healthCheckupOngoingResponseCall.enqueue(new Callback<HealthCheckupOngoingResponse>() {
            @Override
            public void onResponse(Call<HealthCheckupOngoingResponse> call, Response<HealthCheckupOngoingResponse> response) {

                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                        healthCheckupOngoingResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                        healthCheckupOngoingResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
//                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                    errorState.setValue(true);
                    loadingState.setValue(false);
//                    Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                } else {
//                    Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<HealthCheckupOngoingResponse> call, Throwable t) {
                errorState.setValue(true);
                loadingState.setValue(false);
            }
        });

        return healthCheckupOngoingResponseMutableLiveData;
    }

    public MutableLiveData<HealthCheckupOngoingResponse> getOngoingAppointmentData() {

        return healthCheckupOngoingResponseMutableLiveData;
    }

    public void resetCancelHC() {
        messageResponseCancelAppointmentMutableLiveData.setValue(null);
    }

    public MutableLiveData<String> getCitiesLiveDataHC() {
        return citiesLiveDataHC;
    }

    public void setCitiesLiveDataHC(String city) {
        citiesLiveDataHC.setValue(city);
    }

    public MutableLiveData<Boolean> getloadingState() {
        return loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return errorState;
    }

    public void setloadingState(Boolean loading) {
        loadingState.setValue(loading);
    }

}
