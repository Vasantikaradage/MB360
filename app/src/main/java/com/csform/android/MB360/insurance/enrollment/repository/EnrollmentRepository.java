package com.csform.android.MB360.insurance.enrollment.repository;

import android.app.Application;

import com.csform.android.MB360.database.AppDatabase;
import com.csform.android.MB360.database.Dao.MaxMinAgeDao;
import com.csform.android.MB360.database.Dao.ServiceNamesDao;
import com.csform.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MaxMinAgeMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MaxMinAgeResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.coverage.CoverageMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.coverage.CoverageResponseData;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.DependantMessageMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.DependantMessageResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.Dependant;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.DependantMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.Relation;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.RelationMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails.EmployeeFieldDisplaySubResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails.EmployeeFiledMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails.EmployeeUpdateInfo;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.AvailPaernt;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.ParentDetailMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.ParentalRelationResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.summary.SummaryMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.summary.SummaryResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUpPlanMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUpPlanResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUSiResponseData;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUpSiMainResponse;
import com.csform.android.MB360.utilities.LogMyBenefits;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.insurance.enrollment.repository.responseclass.DependantDetailsResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.DependantDetailsResponseNew;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.Dependent;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.EmployeeResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentMessage;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentSummaryResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.InstructionResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.Message;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MyCoveragesResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.Parent;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.TopupResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.csform.android.MB360.insurance.enrollment.repository.retrofit.EnrollmentRetrofitClient;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.UtilMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnrollmentRepository {

    Application application;
    public final MutableLiveData<Boolean> loadingState; //main enrollment loading.
    public final MutableLiveData<Boolean> errorState; //main enrollment error.
    public final MutableLiveData<InstructionResponse> instructionLiveData;
    public final MutableLiveData<MyCoveragesResponse> coveragesLiveData;
    public final MutableLiveData<EmployeeResponse> employeeLiveData;
    public final MutableLiveData<DependantDetailsResponse> dependantDetailLiveData;
    public final MutableLiveData<WindowPeriodEnrollmentResponse> windowPeriodMutableLiveData;
    //dependant data from the server
    public final MutableLiveData<List<Dependent>> dependantLiveData;
    MutableLiveData<EmployeeUpdateInfo> upadEmployeeUpdateInfoMutableLiveData;

    MutableLiveData<RelationMainResponse> relationMainData = new MutableLiveData<>();
    MutableLiveData<ParentalRelationResponse> parentalRelationResponseMutableLiveData = new MutableLiveData<>();


    //Window Period loading and error
    public MutableLiveData<Boolean> windowPeriodLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> windowPeriodErrorState = new MutableLiveData<>(false);

    //instruction loading and error
    public MutableLiveData<Boolean> instructionLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> instructionErrorState = new MutableLiveData<>(false);

    //my coverages loading and error
    public MutableLiveData<Boolean> coverageLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> coverageErrorState = new MutableLiveData<>(false);

    //employee details loading and error
    public MutableLiveData<Boolean> employeeLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> employeeErrorState = new MutableLiveData<>(false);


    //dependant details loading and error
    public MutableLiveData<Boolean> dependantLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> dependantErrorState = new MutableLiveData<>(false);

    //parental details loading and error
    public MutableLiveData<Boolean> parentalLoadingState = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> parentalErrorState = new MutableLiveData<>(false);


    public MutableLiveData<EnrollmentSummaryResponse> summaryLiveData;
    public MutableLiveData<DependantHelperModel> twin1 = new MutableLiveData<>();
    public MutableLiveData<DependantHelperModel> twin2 = new MutableLiveData<>();

    public final MutableLiveData<List<Parent>> parentalData;
    public final MutableLiveData<TopupResponse> topupData;

    MutableLiveData<DependantMainResponse> dependantsMainData = new MutableLiveData<>();

    MutableLiveData<ParentDetailMainResponse> parentDetailMAinResponseMutableLiveData = new MutableLiveData<>();

    MutableLiveData<EmployeeFiledMainResponse> employeeFiledMainResponseMutableLiveData = new MutableLiveData<>();
    //new dependant details
    public final MutableLiveData<DependantDetailsResponseNew> dependantDetailsLiveDataNew;
    MutableLiveData<MaxMinAgeMainResponse> maxMinAgeMainResponseMutableLiveData;
    private AppDatabase appDatabase;
    private MaxMinAgeDao maxMinAgeDao;

    public EnrollmentRepository(Application application) {
        this.application = application;
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>(false);
        this.instructionLiveData = new MutableLiveData<>();
        this.coveragesLiveData = new MutableLiveData<>();
        this.employeeLiveData = new MutableLiveData<>();
        this.dependantDetailLiveData = new MutableLiveData<>();
        this.parentalData = new MutableLiveData<>();
        this.topupData = new MutableLiveData<>();
        this.summaryLiveData = new MutableLiveData<>();
        this.windowPeriodMutableLiveData = new MutableLiveData<>();
        this.dependantDetailsLiveDataNew = new MutableLiveData<>();
        this.dependantLiveData = new MutableLiveData<>();
        this.parentDetailMAinResponseMutableLiveData=new MutableLiveData<>();
       // this.parentalRelationResponseMutableLiveData=new MutableLiveData<>();
        appDatabase = AppDatabase.getInstance(application);
        maxMinAgeDao = appDatabase.maxMinAgeDao();
         upadEmployeeUpdateInfoMutableLiveData = new MutableLiveData<>();


    }

    public LiveData<InstructionResponse> getInstructionResponse() {

        loadingState.setValue(false);
        //todo api call for instruction
        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * **/
        Call<InstructionResponse> instructionCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getEnrollmentInstructions();

        instructionCall.enqueue(new Callback<InstructionResponse>() {
            @Override
            public void onResponse(Call<InstructionResponse> call, Response<InstructionResponse> response) {
                if (response.code() == 200) {
                    // we render the response

                } else {
                    //something went wrong.
                }
            }

            @Override
            public void onFailure(Call<InstructionResponse> call, Throwable t) {
                //something went wrong in failure.
            }
        });

        String response = UtilMethods.getAssetJsonData(application, "InstructionResponse.json");
        Type type = new TypeToken<InstructionResponse>() {
        }.getType();
        InstructionResponse instructionResponse = new Gson().fromJson(response, type);
        instructionLiveData.setValue(instructionResponse);
        LogMyBenefits.d(LogTags.ENROLLMENT, "getInstructionResponse: " + instructionResponse.toString());

        return instructionLiveData;
    }


    public LiveData<InstructionResponse> getInstructionResponseData() {
        return instructionLiveData;
    }

    public LiveData<MyCoveragesResponse> getCoveragesResponse() {
        coverageLoadingState.setValue(true);
        Call<MyCoveragesResponse> enrollmentCoveragesCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getEnrollmentCoverages();

        enrollmentCoveragesCall.enqueue(new Callback<MyCoveragesResponse>() {
            @Override
            public void onResponse(Call<MyCoveragesResponse> call, Response<MyCoveragesResponse> response) {
                try {
                    if (response.code() == 200) {
                        //success
                        //todo here we parse the response.
                        coverageLoadingState.setValue(false);
                        coverageErrorState.setValue(false);


                    } else {
                        //something went wrong.
                    }

                } catch (Exception e) {
                    //error

                }
            }

            @Override
            public void onFailure(Call<MyCoveragesResponse> call, Throwable t) {

            }
        });

        loadingState.setValue(false);
        //todo api call for Coverages
        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * **/
        String response = UtilMethods.getAssetJsonData(application, "CoveragesResponse.json");
        Type type = new TypeToken<MyCoveragesResponse>() {
        }.getType();
        MyCoveragesResponse coveragesResponse = new Gson().fromJson(response, type);
        coveragesLiveData.setValue(coveragesResponse);

        return coveragesLiveData;
    }

    public LiveData<MyCoveragesResponse> getCoveragesData() {
        return coveragesLiveData;
    }

    public LiveData<EmployeeResponse> getEmployeeResponse() {
        employeeLoadingState.setValue(true);
        Call<EmployeeResponse> employeeCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getEnrollmentEmployeeDetails();

     /*   employeeCall.enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {
                if (response.code() == 200) {
                    try {
                        employeeLoadingState.setValue(false);
                        employeeErrorState.setValue(false);
                        employeeLiveData.setValue(response.body());
                    } catch (Exception exception) {
                        employeeLiveData.setValue(null);
                        employeeLoadingState.setValue(false);
                        employeeErrorState.setValue(true);
                        exception.printStackTrace();
                    }

                } else {
                    //if response code is not 200
                    employeeLiveData.setValue(null);
                    employeeLoadingState.setValue(false);
                    employeeErrorState.setValue(true);

                }
            }

            @Override
            public void onFailure(Call<EmployeeResponse> call, Throwable t) {
                //if call is not handled
                LogMyBenefits.e(LogTags.ENROLLMENT, "Error: " + t.getLocalizedMessage());

                employeeLiveData.setValue(null);
                employeeLoadingState.setValue(false);
                employeeErrorState.setValue(true);
            }
        });*/


        loadingState.setValue(false);
        //todo api call for Employee details


        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "EmployeeResponse.json");
        Type type = new TypeToken<EmployeeResponse>() {
        }.getType();
        EmployeeResponse employeeResponse = new Gson().fromJson(response, type);
        employeeLiveData.setValue(employeeResponse);

        return employeeLiveData;
    }

    public LiveData<EmployeeResponse> getEmployeeData() {
        return employeeLiveData;
    }

    /**
     * get dependants from the server
     */
    public MutableLiveData<DependantMainResponse> getDependants(String emp_sr_no,
                                                                String GroupChildSrNo,
                                                                String oeGrpBasInfoSrNo,
                                                                String isRelGrpDivided) {


        DependantMainResponse dependantMainResponse = new DependantMainResponse();

        Call<List<Dependant>> dependantsCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi()
                .getEmployeeDetialsForEnrollment(emp_sr_no, GroupChildSrNo, oeGrpBasInfoSrNo, isRelGrpDivided);

        dependantsCall.enqueue(new Callback<List<Dependant>>() {
            @Override
            public void onResponse(Call<List<Dependant>> call, Response<List<Dependant>> response) {
                if (response.code() == 200) {
                    try {
                        dependantMainResponse.setDependants(response.body());
                        dependantMainResponse.setLoading(false);
                        dependantMainResponse.setError(false);
                        dependantsMainData.setValue(dependantMainResponse);

                    } catch (Exception e) {
                        dependantMainResponse.setDependants(null);
                        dependantMainResponse.setLoading(false);
                        dependantMainResponse.setError(true);
                        dependantsMainData.setValue(dependantMainResponse);
                    }

                } else {
                    dependantMainResponse.setDependants(null);
                    dependantMainResponse.setLoading(false);
                    dependantMainResponse.setError(true);
                    dependantsMainData.setValue(dependantMainResponse);
                }
            }

            @Override
            public void onFailure(Call<List<Dependant>> call, Throwable t) {
                dependantMainResponse.setDependants(null);
                dependantMainResponse.setLoading(false);
                dependantMainResponse.setError(true);
                dependantsMainData.setValue(dependantMainResponse);
            }
        });

        return dependantsMainData;
    }

    public MutableLiveData<DependantMainResponse> getDependantsData() {
        return dependantsMainData;
    }

    public MutableLiveData<RelationMainResponse> getRelationshipGroup(String empSrNo, String groupChildSrNo, String oeGrpBasInfoSrNo, String windowPeriod) {

        RelationMainResponse relationMainResponse = new RelationMainResponse();

        Call<List<Relation>> relationCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi()
                .getRelationShipGroup(empSrNo, groupChildSrNo, oeGrpBasInfoSrNo, windowPeriod);

        relationCall.enqueue(new Callback<List<Relation>>() {
            @Override
            public void onResponse(Call<List<Relation>> call, Response<List<Relation>> response) {
                try {
                    if (response.code() == 200) {
                        relationMainResponse.setRelations(response.body());
                        relationMainResponse.setError(false);

                    } else {
                        relationMainResponse.setRelations(null);
                        relationMainResponse.setError(true);
                    }
                    relationMainResponse.setLoading(false);
                    relationMainData.setValue(relationMainResponse);

                } catch (Exception e) {
                    relationMainResponse.setRelations(null);
                    relationMainResponse.setError(true);
                    relationMainResponse.setLoading(false);
                    relationMainData.setValue(relationMainResponse);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Relation>> call, Throwable t) {
                relationMainResponse.setRelations(null);
                relationMainResponse.setError(true);
                relationMainResponse.setLoading(false);
                relationMainData.setValue(relationMainResponse);
                call.cancel();
            }
        });

        return relationMainData;
    }

    public MutableLiveData<RelationMainResponse> getRelationshipGroupData() {
        return relationMainData;
    }

    public MutableLiveData<List<Parent>> getParental(String isWindowPeriodActive, String groupChildSrNo, String oeGrpBasInfoSrNo, String employeeSrNo, String parentalPremium) {
        parentalLoadingState.setValue(true);
        //todo api call for Parental details

        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "Parental.json");
        Type type = new TypeToken<DependantDetailsResponse>() {
        }.getType();
        DependantDetailsResponse dependantDetailsResponse = new Gson().fromJson(response, type);


        Call<List<Parent>> parentCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getParentalDependant(isWindowPeriodActive, groupChildSrNo, oeGrpBasInfoSrNo, employeeSrNo, parentalPremium);

        parentCall.enqueue(new Callback<List<Parent>>() {
            @Override
            public void onResponse(Call<List<Parent>> call, Response<List<Parent>> response) {
                if (response.code() == 200) {
                    try {
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(false);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "getParental: " + response.body());
                        parentalData.setValue(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(true);
                        parentalData.setValue(null);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "getParental: " + response.body());
                    }
                } else {
                    //something went wrong from server
                    LogMyBenefits.d(LogTags.ENROLLMENT, "getParental: " + response.body());
                    parentalLoadingState.setValue(false);
                    parentalErrorState.setValue(true);
                    parentalData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Parent>> call, Throwable t) {
                LogMyBenefits.d(LogTags.ENROLLMENT, "getParental: " + t);
                parentalLoadingState.setValue(false);
                parentalErrorState.setValue(true);
                parentalData.setValue(null);
            }
        });


        return parentalData;
    }

    public MutableLiveData<List<Parent>> getParentalData() {
        return parentalData;
    }


    /**
     * Add Parent call
     */
    public MutableLiveData<EnrollmentMessage> addParent(Map<String, String> dependantOptions) {
        MutableLiveData<EnrollmentMessage> dependantMessage = new MutableLiveData<>();

        Call<EnrollmentMessage> addDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().addDependant(dependantOptions);

        addDependantCall.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                try {

                    if (response.code() == 200) {
                        //dependant added successfully
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                        dependantMessage.setValue(response.body());

                    } else {
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                        dependantMessage.setValue(null);
                    }
                } catch (Exception e) {
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                    dependantMessage.setValue(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                dependantMessage.setValue(null);
                t.printStackTrace();
            }
        });

        return dependantMessage;
    }


    //edit parent
    public MutableLiveData<EnrollmentMessage> updateParent(Map<String, String> parentOptions) {
        MutableLiveData<EnrollmentMessage> enrollmentMessage = new MutableLiveData<>();
        Call<EnrollmentMessage> updateParentCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().updateParent(parentOptions);

        updateParentCall.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                if (response.code() == 200) {
                    try {
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(false);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "updateParent: " + response.body());
                        enrollmentMessage.setValue(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(true);
                        enrollmentMessage.setValue(null);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "updateParent: " + response.body());
                    }
                } else {
                    //something went wrong from server
                    LogMyBenefits.d(LogTags.ENROLLMENT, "updateParent: " + response.body());
                    parentalLoadingState.setValue(false);
                    parentalErrorState.setValue(true);
                    enrollmentMessage.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                LogMyBenefits.d(LogTags.ENROLLMENT, "updateParent: " + t);
                parentalLoadingState.setValue(false);
                parentalErrorState.setValue(true);
                enrollmentMessage.setValue(null);
            }
        });

        return enrollmentMessage;
    }

    //delete parent
    public MutableLiveData<EnrollmentMessage> deleteParent(Map<String, String> parentOptions) {
        MutableLiveData<EnrollmentMessage> enrollmentMessage = new MutableLiveData<>();
        Call<EnrollmentMessage> deleteParental = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().deleteParent(parentOptions);

        deleteParental.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                if (response.code() == 200) {
                    try {
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(false);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "deleteParent: " + response.body());
                        enrollmentMessage.setValue(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        parentalLoadingState.setValue(false);
                        parentalErrorState.setValue(true);
                        enrollmentMessage.setValue(null);
                        LogMyBenefits.d(LogTags.ENROLLMENT, "deleteParent: " + response.body());
                    }
                } else {
                    //something went wrong from server
                    LogMyBenefits.d(LogTags.ENROLLMENT, "deleteParent: " + response.body());
                    parentalLoadingState.setValue(false);
                    parentalErrorState.setValue(true);
                    enrollmentMessage.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                LogMyBenefits.d(LogTags.ENROLLMENT, "deleteParent: " + t);
                parentalLoadingState.setValue(false);
                parentalErrorState.setValue(true);
                enrollmentMessage.setValue(null);
            }
        });

        return enrollmentMessage;
    }

    public MutableLiveData<TopupResponse> getTopUps() {
        loadingState.setValue(false);
        //todo api call for Top ups details

        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "TopUps.json");
        Type type = new TypeToken<TopupResponse>() {
        }.getType();
        TopupResponse topupResponse = new Gson().fromJson(response, type);
        topupData.setValue(topupResponse);

        return topupData;
    }


    public MutableLiveData<EnrollmentSummaryResponse> getSummary() {
        loadingState.setValue(false);
        //todo api call for summary of enrollment
        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "EnrollmentSummary.json");
        Type type = new TypeToken<EnrollmentSummaryResponse>() {
        }.getType();
        EnrollmentSummaryResponse enrollmentSummaryResponse = new Gson().fromJson(response, type);
        summaryLiveData.setValue(enrollmentSummaryResponse);

        return summaryLiveData;
    }

    public MutableLiveData<EnrollmentSummaryResponse> getSummaryData() {
        return summaryLiveData;
    }

    public MutableLiveData<TopupResponse> getTopUpsData() {
        return topupData;
    }

    //twins details

    public MutableLiveData<DependantHelperModel> getTwin1() {
        return twin1;
    }

    public void setTwin1(DependantHelperModel twin1) {
        this.twin1.setValue(twin1);
    }

    public MutableLiveData<DependantHelperModel> getTwin2() {
        return twin2;
    }

    public void setTwin2(DependantHelperModel twin2) {
        this.twin2.setValue(twin2);
    }


    //enrollment set up

    /**
     * we have to check if user is applicable for the window period
     **/

    public MutableLiveData<WindowPeriodEnrollmentResponse> getWindowPeriod() {
        // here we check if the window period is active.
        // this function will return

        Call<WindowPeriodEnrollmentResponse> windowPeriodCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getWindowPeriod();

        windowPeriodCall.enqueue(new Callback<WindowPeriodEnrollmentResponse>() {
            @Override
            public void onResponse(Call<WindowPeriodEnrollmentResponse> call, Response<WindowPeriodEnrollmentResponse> response) {
                try {

                    if (response.code() == 200) {
                        windowPeriodMutableLiveData.setValue(response.body());

                    } else {
                        //something went wrong
                    }
                    //fake response
                    windowPeriodLoadingState.setValue(false);
                    windowPeriodErrorState.setValue(false);
                    String responseWindow = UtilMethods.getAssetJsonData(application, "WindowPeriod.json");
                    Type type = new TypeToken<WindowPeriodEnrollmentResponse>() {
                    }.getType();
                    WindowPeriodEnrollmentResponse windowPeriodEnrollmentResponse = new Gson().fromJson(responseWindow, type);
                    windowPeriodMutableLiveData.setValue(windowPeriodEnrollmentResponse);
                    LogMyBenefits.d(LogTags.ENROLLMENT, "getWindowPeriod: " + windowPeriodEnrollmentResponse.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WindowPeriodEnrollmentResponse> call, Throwable t) {
                //something went wrong call did not happened
                windowPeriodMutableLiveData.setValue(null);
                windowPeriodLoadingState.setValue(false);
                windowPeriodErrorState.setValue(true);

                String responseWindow = UtilMethods.getAssetJsonData(application, "WindowPeriod.json");
                Type type = new TypeToken<WindowPeriodEnrollmentResponse>() {
                }.getType();
                WindowPeriodEnrollmentResponse windowPeriodEnrollmentResponse = new Gson().fromJson(responseWindow, type);
                windowPeriodMutableLiveData.setValue(windowPeriodEnrollmentResponse);
                LogMyBenefits.d(LogTags.ENROLLMENT, "getWindowPeriod: " + windowPeriodEnrollmentResponse.toString());

            }
        });

        return windowPeriodMutableLiveData;
    }


    //new dependant details work
    public MutableLiveData<DependantDetailsResponseNew> getDependantDetailsNew() {

        String response = UtilMethods.getAssetJsonData(application, "dependantdetails.json");
        Type type = new TypeToken<DependantDetailsResponseNew>() {
        }.getType();
        DependantDetailsResponseNew dependantDetailsResponseNew = new Gson().fromJson(response, type);
        dependantDetailsLiveDataNew.setValue(dependantDetailsResponseNew);

        LogMyBenefits.d("NEW DEPENDANT", "getDependantDetailsNew: " + dependantDetailsResponseNew.toString());

        return dependantDetailsLiveDataNew;
    }


    /**
     * Add Dependant call
     */
    public MutableLiveData<EnrollmentMessage> addDependant(Map<String, String> dependantOptions) {
        MutableLiveData<EnrollmentMessage> dependantMessage = new MutableLiveData<>();

        Call<EnrollmentMessage> addDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().addDependant(dependantOptions);

        addDependantCall.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                try {

                    if (response.code() == 200) {
                        //dependant added successfully
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Dependant Added: " + response.body());
                        dependantMessage.setValue(response.body());

                    } else {
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Dependant Added: " + response.body());
                        dependantMessage.setValue(null);
                    }
                } catch (Exception e) {
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Dependant Added: " + response.body());
                    dependantMessage.setValue(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                Message message_sub = new Message();
                message_sub.setStatus(true);
                message_sub.setMessage("DONE");
                EnrollmentMessage message = new EnrollmentMessage();
                message.setMessage(message_sub);
                dependantMessage.setValue(message);
                t.printStackTrace();
            }
        });

        return dependantMessage;
    }

    /**
     * Delete Dependant call
     */
   /* public MutableLiveData<EnrollmentMessage> deleteDependant(Map<String, String> dependantOptions) {
        MutableLiveData<EnrollmentMessage> dependantMessage = new MutableLiveData<>();
        Call<EnrollmentMessage> deleteDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().deleteDependant(dependantOptions);

        deleteDependantCall.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                if (response.code() == 200) {
                    //dependant added successfully
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Dependant Delete: " + response.body());
                    dependantMessage.setValue(response.body());

                } else {
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Dependant Delete: " + response.body());
                    dependantMessage.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                dependantMessage.setValue(null);
                t.printStackTrace();
            }
        });

        return dependantMessage;
    }
*/

    public MutableLiveData<EnrollmentMessage> updateDependant(Map<String, String> dependantOptions) {

        MutableLiveData<EnrollmentMessage> dependantMessage = new MutableLiveData<>();

        Call<EnrollmentMessage> editDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().updateDependant(dependantOptions);

        editDependantCall.enqueue(new Callback<EnrollmentMessage>() {
            @Override
            public void onResponse(Call<EnrollmentMessage> call, Response<EnrollmentMessage> response) {
                if (response.code() == 200) {
                    //successful
                    dependantMessage.setValue(response.body());
                    LogMyBenefits.d(LogTags.ENROLLMENT, "Update Dependant: " + response.body());
                } else {
                    //something went wrong
                    dependantMessage.setValue(null);
                    LogMyBenefits.d(LogTags.ENROLLMENT, "Update Dependant: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<EnrollmentMessage> call, Throwable t) {
                dependantMessage.setValue(null);
                t.printStackTrace();
            }
        });

        return dependantMessage;
    }


    //get topupSuminsured data
    public LiveData<TopUpSiMainResponse> getTopUpSumInsured(String empSrNo, String groupChildSrNo, String oeGrpBasInfSrNo, String prdttype) {
        MutableLiveData<TopUpSiMainResponse> topUpSumInsuredResponse = new MutableLiveData<>();

        TopUpSiMainResponse topUpSiMainResponseObj = new TopUpSiMainResponse();

        Call<TopUSiResponseData> topUpResponseDataCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getTopUpSumInsured(empSrNo, groupChildSrNo, oeGrpBasInfSrNo, prdttype);

        topUpResponseDataCall.enqueue(new Callback<TopUSiResponseData>() {
            @Override
            public void onResponse(Call<TopUSiResponseData> call, Response<TopUSiResponseData> response) {
                try {
                    if (response.code() == 200) {
                        LogMyBenefits.d("TopUpSiMainResponse", "response data" + response.body());

                        topUpSiMainResponseObj.setLoadingState(false);
                        topUpSiMainResponseObj.setErrorState(false);
                        topUpSiMainResponseObj.setTopUpResponseData(response.body());
                        topUpSumInsuredResponse.setValue(topUpSiMainResponseObj);

                    } else {
                        LogMyBenefits.d("TopUpSiMainResponse", "error response data" + response.body());

                        topUpSiMainResponseObj.setErrorState(true);
                        topUpSiMainResponseObj.setLoadingState(false);
                        topUpSiMainResponseObj.setTopUpResponseData(null);
                        topUpSumInsuredResponse.setValue(topUpSiMainResponseObj);
                        //something went wrong.
                    }

                } catch (Exception e) {
                    //error
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<TopUSiResponseData> call, Throwable t) {

                topUpSiMainResponseObj.setErrorState(true);
                topUpSiMainResponseObj.setLoadingState(false);
                topUpSiMainResponseObj.setTopUpResponseData(null);
                topUpSumInsuredResponse.setValue(topUpSiMainResponseObj);
            }
        });


        return topUpSumInsuredResponse;
    }


    //get topup GMC PlanResponse
    public LiveData<TopUpPlanMainResponse> getTopUpGMCPlanResponse(String empSrNo, String groupChildSrNo, String oeGrpBasInfSrNo, String amount, String premium) {
        MutableLiveData<TopUpPlanMainResponse> topUpPlanResponseMutableLiveData = new MutableLiveData<>();
        TopUpPlanMainResponse topUpPlanMainResponseObj = new TopUpPlanMainResponse();
        Call<TopUpPlanResponse> topUpPlanResponseCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getTopUpGMCPlanData(empSrNo, groupChildSrNo, oeGrpBasInfSrNo, amount, premium);

        topUpPlanResponseCall.enqueue(new Callback<TopUpPlanResponse>() {
            @Override
            public void onResponse(Call<TopUpPlanResponse> call, Response<TopUpPlanResponse> response) {
                try {
                    if (response.code() == 200) {
                        LogMyBenefits.d("TopUpGMCPlanData", "response data" + response.body());
                        topUpPlanMainResponseObj.setErrorState(false);
                        topUpPlanMainResponseObj.setLoadingState(false);
                        topUpPlanMainResponseObj.setTopUpPlanResponse(response.body());
                        topUpPlanResponseMutableLiveData.setValue(topUpPlanMainResponseObj);
                    } else {
                        LogMyBenefits.d("TopUpGMCPlanData", "error msg" + response.body());
                        topUpPlanMainResponseObj.setErrorState(true);
                        topUpPlanMainResponseObj.setLoadingState(false);
                        topUpPlanMainResponseObj.setTopUpPlanResponse(null);
                        topUpPlanResponseMutableLiveData.setValue(topUpPlanMainResponseObj);
                        //something went wrong.
                    }

                } catch (Exception e) {
                    //error
                    e.printStackTrace();
                    LogMyBenefits.d("TopUpGMCPlanData", "error msg");

                }
            }

            @Override
            public void onFailure(Call<TopUpPlanResponse> call, Throwable t) {

                topUpPlanMainResponseObj.setErrorState(true);
                topUpPlanMainResponseObj.setLoadingState(false);
                topUpPlanMainResponseObj.setTopUpPlanResponse(null);
                topUpPlanResponseMutableLiveData.setValue(topUpPlanMainResponseObj);

            }
        });


        return topUpPlanResponseMutableLiveData;
    }


    //get topup GPA PlanResponse
    public LiveData<TopUpPlanMainResponse> getTopUpGPAPlanResponse(String empSrNo, String groupChildSrNo, String oeGrpBasInfSrNo, String amount, String premium) {
        MutableLiveData<TopUpPlanMainResponse> topUpPlanResponseMutableLiveData = new MutableLiveData<>();
        TopUpPlanMainResponse topUpPlanMainResponseObj = new TopUpPlanMainResponse();
        Call<TopUpPlanResponse> topUpPlanResponseCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getTopUpGPAPlanData(empSrNo, groupChildSrNo, oeGrpBasInfSrNo, amount, premium);

        topUpPlanResponseCall.enqueue(new Callback<TopUpPlanResponse>() {
            @Override
            public void onResponse(Call<TopUpPlanResponse> call, Response<TopUpPlanResponse> response) {
                try {
                    if (response.code() == 200) {
                        LogMyBenefits.d("TopUpGPAPlanData", "response data" + response.body());
                        topUpPlanMainResponseObj.setErrorState(false);
                        topUpPlanMainResponseObj.setLoadingState(false);
                        topUpPlanMainResponseObj.setTopUpPlanResponse(response.body());
                        topUpPlanResponseMutableLiveData.setValue(topUpPlanMainResponseObj);
                    } else {
                        LogMyBenefits.d("TopUpGPAPlanData", "error msg" + response.body());
                        topUpPlanMainResponseObj.setErrorState(true);
                        topUpPlanMainResponseObj.setLoadingState(false);
                        topUpPlanMainResponseObj.setTopUpPlanResponse(null);
                        topUpPlanResponseMutableLiveData.setValue(topUpPlanMainResponseObj);
                        //something went wrong.
                    }

                } catch (Exception e) {
                    //error
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<TopUpPlanResponse> call, Throwable t) {

                topUpPlanMainResponseObj.setErrorState(true);
                topUpPlanMainResponseObj.setLoadingState(false);
                topUpPlanMainResponseObj.setTopUpPlanResponse(null);
                topUpPlanResponseMutableLiveData.setValue(topUpPlanMainResponseObj);

            }
        });


        return topUpPlanResponseMutableLiveData;
    }

    //get topup GTL PlanResponse
    public LiveData<TopUpPlanMainResponse> getTopUpGTLPlanResponse(String empSrNo, String groupChildSrNo, String oeGrpBasInfSrNo, String amount, String premium) {
        MutableLiveData<TopUpPlanMainResponse> topUpPlanResponseMutableLiveData = new MutableLiveData<>();
        TopUpPlanMainResponse topUpPlanMainResponseObj = new TopUpPlanMainResponse();
        Call<TopUpPlanResponse> topUpPlanResponseCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getTopUpGTLPlanData(empSrNo, groupChildSrNo, oeGrpBasInfSrNo, amount, premium);

        topUpPlanResponseCall.enqueue(new Callback<TopUpPlanResponse>() {
            @Override
            public void onResponse(Call<TopUpPlanResponse> call, Response<TopUpPlanResponse> response) {
                try {
                    if (response.code() == 200) {
                        LogMyBenefits.d("TopUpGTLPlanData", "response data" + response.body());
                        topUpPlanMainResponseObj.setErrorState(false);
                        topUpPlanMainResponseObj.setLoadingState(false);
                        topUpPlanMainResponseObj.setTopUpPlanResponse(response.body());
                        topUpPlanResponseMutableLiveData.setValue(topUpPlanMainResponseObj);
                    } else {
                        LogMyBenefits.d("TopUpGTLPlanData", "error msg" + response.body());
                        topUpPlanMainResponseObj.setErrorState(true);
                        topUpPlanMainResponseObj.setLoadingState(false);
                        topUpPlanMainResponseObj.setTopUpPlanResponse(null);
                        topUpPlanResponseMutableLiveData.setValue(topUpPlanMainResponseObj);
                        //something went wrong.
                    }

                } catch (Exception e) {
                    //error
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<TopUpPlanResponse> call, Throwable t) {

                topUpPlanMainResponseObj.setErrorState(true);
                topUpPlanMainResponseObj.setLoadingState(false);
                topUpPlanMainResponseObj.setTopUpPlanResponse(null);
                topUpPlanResponseMutableLiveData.setValue(topUpPlanMainResponseObj);

            }
        });


        return topUpPlanResponseMutableLiveData;
    }


    //get coverage ghi response
    public LiveData<CoverageMainResponse> getCoverageGHIResponse(String groupChildSrNo, String oeGrpBasInfSrNo, String empSrNo) {
        MutableLiveData<CoverageMainResponse> coverageMainResponseMutableLiveData = new MutableLiveData<>();
        CoverageMainResponse coverageMainResponseObj = new CoverageMainResponse();
        Call<CoverageResponseData> coverageResponseDataCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getCoverageGHIData(groupChildSrNo, oeGrpBasInfSrNo, empSrNo);

        coverageResponseDataCall.enqueue(new Callback<CoverageResponseData>() {
            @Override
            public void onResponse(Call<CoverageResponseData> call, Response<CoverageResponseData> response) {
                try {
                    if (response.code() == 200) {
                        LogMyBenefits.d("TopUpPlanData", "response data" + response.body());
                        coverageMainResponseObj.setErrorState(false);
                        coverageMainResponseObj.setLoadingState(false);
                        coverageMainResponseObj.setPolicyType("GMC");
                        response.body().setPolicy_selected("GMC");
                        coverageMainResponseObj.setCoverageResponseData(response.body());
                        coverageMainResponseMutableLiveData.setValue(coverageMainResponseObj);
                    } else {
                        LogMyBenefits.d("TopUpPlanData", "error msg" + response.body());
                        coverageMainResponseObj.setErrorState(true);
                        coverageMainResponseObj.setLoadingState(false);
                        coverageMainResponseObj.setCoverageResponseData(null);
                        coverageMainResponseMutableLiveData.setValue(coverageMainResponseObj);
                        //something went wrong.
                    }

                } catch (Exception e) {
                    //error
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<CoverageResponseData> call, Throwable t) {

                coverageMainResponseObj.setErrorState(true);
                coverageMainResponseObj.setLoadingState(false);
                coverageMainResponseObj.setCoverageResponseData(null);
                coverageMainResponseMutableLiveData.setValue(coverageMainResponseObj);

            }
        });


        return coverageMainResponseMutableLiveData;
    }


    //get coverage gpa response
    public LiveData<CoverageMainResponse> getCoverageGPAResponse(String groupChildSrNo, String oeGrpBasInfSrNo, String empSrNo) {
        MutableLiveData<CoverageMainResponse> coverageMainResponseMutableLiveData = new MutableLiveData<>();
        CoverageMainResponse coverageMainResponseObj = new CoverageMainResponse();
        Call<CoverageResponseData> coverageResponseDataCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getCoverageGPAData(groupChildSrNo, oeGrpBasInfSrNo, empSrNo);

        coverageResponseDataCall.enqueue(new Callback<CoverageResponseData>() {
            @Override
            public void onResponse(Call<CoverageResponseData> call, Response<CoverageResponseData> response) {
                try {
                    if (response.code() == 200) {
                        LogMyBenefits.d("TopUpPlanData", "response data" + response.body());
                        coverageMainResponseObj.setErrorState(false);
                        coverageMainResponseObj.setLoadingState(false);
                        coverageMainResponseObj.setPolicyType("GPA");
                        response.body().setPolicy_selected("GPA");
                        coverageMainResponseObj.setCoverageResponseData(response.body());

                        coverageMainResponseMutableLiveData.setValue(coverageMainResponseObj);
                    } else {
                        LogMyBenefits.d("TopUpPlanData", "error msg" + response.body());
                        coverageMainResponseObj.setErrorState(true);
                        coverageMainResponseObj.setLoadingState(false);
                        coverageMainResponseObj.setCoverageResponseData(null);
                        coverageMainResponseMutableLiveData.setValue(coverageMainResponseObj);
                        //something went wrong.
                    }

                } catch (Exception e) {
                    //error
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<CoverageResponseData> call, Throwable t) {

                coverageMainResponseObj.setErrorState(true);
                coverageMainResponseObj.setLoadingState(false);
                coverageMainResponseObj.setCoverageResponseData(null);
                coverageMainResponseMutableLiveData.setValue(coverageMainResponseObj);

            }
        });


        return coverageMainResponseMutableLiveData;
    }


    //get coverage gtl response
    public LiveData<CoverageMainResponse> getCoverageGTLResponse(String groupChildSrNo, String oeGrpBasInfSrNo, String empSrNo) {
        MutableLiveData<CoverageMainResponse> coverageMainResponseMutableLiveData = new MutableLiveData<>();
        CoverageMainResponse coverageMainResponseObj = new CoverageMainResponse();
        Call<CoverageResponseData> coverageResponseDataCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getCoverageGTLData(groupChildSrNo, oeGrpBasInfSrNo, empSrNo);

        coverageResponseDataCall.enqueue(new Callback<CoverageResponseData>() {
            @Override
            public void onResponse(Call<CoverageResponseData> call, Response<CoverageResponseData> response) {
                try {
                    if (response.code() == 200) {
                        LogMyBenefits.d("TopUpPlanData", "response data" + response.body());
                        coverageMainResponseObj.setErrorState(false);
                        coverageMainResponseObj.setLoadingState(false);
                        coverageMainResponseObj.setPolicyType("GTL");
                        response.body().setPolicy_selected("GTL");


                        coverageMainResponseObj.setCoverageResponseData(response.body());
                        coverageMainResponseMutableLiveData.setValue(coverageMainResponseObj);
                    } else {
                        LogMyBenefits.d("TopUpPlanData", "error msg" + response.body());
                        coverageMainResponseObj.setErrorState(true);
                        coverageMainResponseObj.setLoadingState(false);
                        coverageMainResponseObj.setCoverageResponseData(null);
                        coverageMainResponseMutableLiveData.setValue(coverageMainResponseObj);
                        //something went wrong.
                    }

                } catch (Exception e) {
                    //error
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<CoverageResponseData> call, Throwable t) {

                coverageMainResponseObj.setErrorState(true);
                coverageMainResponseObj.setLoadingState(false);
                coverageMainResponseObj.setCoverageResponseData(null);
                coverageMainResponseMutableLiveData.setValue(coverageMainResponseObj);

            }
        });


        return coverageMainResponseMutableLiveData;
    }


    //get employee field Display  response
    public MutableLiveData<EmployeeFiledMainResponse> getEmployeeFieldResponse(String oeGrpBasInfSrNo) {

        EmployeeFiledMainResponse employeeFiledMainResponseObj = new EmployeeFiledMainResponse();
        Call<List<EmployeeFieldDisplaySubResponse>> dependantsCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi()
                .getEmployeeFiledDisplay(oeGrpBasInfSrNo);

        dependantsCall.enqueue(new Callback<List<EmployeeFieldDisplaySubResponse>>() {
            @Override
            public void onResponse(Call<List<EmployeeFieldDisplaySubResponse>> call, Response<List<EmployeeFieldDisplaySubResponse>> response) {
                if (response.code() == 200) {
                    try {
                       LogMyBenefits.d("","response"+response.body());
                        employeeFiledMainResponseObj.setEmployeeFieldDisplaySubResponses(response.body());
                        employeeFiledMainResponseObj.setLoadingState(false);
                        employeeFiledMainResponseObj.setErrorState(false);
                        employeeFiledMainResponseMutableLiveData.setValue(employeeFiledMainResponseObj);

                    } catch (Exception e) {
                        LogMyBenefits.d("","Exception"+response.body());
                        employeeFiledMainResponseObj.setEmployeeFieldDisplaySubResponses(response.body());
                        employeeFiledMainResponseObj.setLoadingState(false);
                        employeeFiledMainResponseObj.setErrorState(false);
                        employeeFiledMainResponseMutableLiveData.setValue(employeeFiledMainResponseObj);
                    }

                } else {
                    LogMyBenefits.d("",""+response.body());

                    employeeFiledMainResponseObj.setEmployeeFieldDisplaySubResponses(response.body());
                    employeeFiledMainResponseObj.setLoadingState(false);
                    employeeFiledMainResponseObj.setErrorState(false);
                    employeeFiledMainResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeFieldDisplaySubResponse>> call, Throwable t) {
                LogMyBenefits.d("",""+t.toString());
                employeeFiledMainResponseObj.setEmployeeFieldDisplaySubResponses(null);
                employeeFiledMainResponseObj.setLoadingState(false);
                employeeFiledMainResponseObj.setErrorState(false);
                employeeFiledMainResponseMutableLiveData.setValue(employeeFiledMainResponseObj);
            }
        });

        return employeeFiledMainResponseMutableLiveData;
    }


    //update Employee data
    public MutableLiveData<EmployeeUpdateInfo> updateEmployee(Map<String, String> dependantOptions) {
        Call<EmployeeUpdateInfo> editDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().updateEmployeeDetails(dependantOptions);
        editDependantCall.enqueue(new Callback<EmployeeUpdateInfo>() {
            @Override
            public void onResponse(Call<EmployeeUpdateInfo> call, Response<EmployeeUpdateInfo> response) {
                if (response.code() == 200) {
                    //successful
                    upadEmployeeUpdateInfoMutableLiveData.setValue(response.body());
                    LogMyBenefits.d(LogTags.ENROLLMENT, "Update employee: " + response.body());
                } else {
                    //something went wrong
                    upadEmployeeUpdateInfoMutableLiveData.setValue(null);
                    LogMyBenefits.d(LogTags.ENROLLMENT, "Update employee: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<EmployeeUpdateInfo> call, Throwable t) {
                upadEmployeeUpdateInfoMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });

        return upadEmployeeUpdateInfoMutableLiveData;
    }

    public MutableLiveData<EmployeeUpdateInfo> getEmployeeUpdateData() {
        return upadEmployeeUpdateInfoMutableLiveData;
    }

    //summary data
    public LiveData<SummaryMainResponse> getSummeryDetails(String gmcEMpSrNo,
                                                           String gpaEmpSrNo,
                                                           String gtlEmpSrNo,
                                                           String GroupChildSrNo,
                                                           String OeGrpBasInfSrNo) {
        MutableLiveData<SummaryMainResponse> summaryMainResponseMutableLiveData = new MutableLiveData<>();
        SummaryMainResponse summaryMainResponse = new SummaryMainResponse();
        Call<SummaryResponse> summaryResponseCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().summaryDetails(gmcEMpSrNo, gpaEmpSrNo, gtlEmpSrNo, GroupChildSrNo, OeGrpBasInfSrNo);

        summaryResponseCall.enqueue(new Callback<SummaryResponse>() {
            @Override
            public void onResponse(Call<SummaryResponse> call, Response<SummaryResponse> response) {
                try {
                    if (response.code() == 200) {
                        LogMyBenefits.d("summary", "response data" + response.body());
                        summaryMainResponse.setErrorState(false);
                        summaryMainResponse.setLoadingState(false);
                        summaryMainResponse.setSummaryResponse(response.body());


                        summaryMainResponseMutableLiveData.setValue(summaryMainResponse);
                    } else {
                        LogMyBenefits.d("summary", "error msg" + response.body());
                        summaryMainResponse.setErrorState(true);
                        summaryMainResponse.setLoadingState(false);
                        summaryMainResponse.setSummaryResponse(response.body());


                        summaryMainResponseMutableLiveData.setValue(summaryMainResponse);
                        //something went wrong.
                    }

                } catch (Exception e) {
                    //error
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<SummaryResponse> call, Throwable t) {

                summaryMainResponse.setErrorState(true);
                summaryMainResponse.setLoadingState(false);
                summaryMainResponse.setSummaryResponse(null);


                summaryMainResponseMutableLiveData.setValue(summaryMainResponse);

            }
        });


        return summaryMainResponseMutableLiveData;
    }

    //add dependant data new api call
    public LiveData<DependantMessageMainResponse> addDependantData(Map<String, String> dependantOptions) {
        MutableLiveData<DependantMessageMainResponse> addDependantMainResponseMutableLiveData = new MutableLiveData<>();
        DependantMessageMainResponse addDependantMainResponse=new DependantMessageMainResponse();
        Call<DependantMessageResponse> addDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().addDependantData(dependantOptions);

        addDependantCall.enqueue(new Callback<DependantMessageResponse>() {
            @Override
            public void onResponse(Call<DependantMessageResponse> call, Response<DependantMessageResponse> response) {
                try {

                    if (response.code() == 200) {
                        //dependant added successfully
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                       addDependantMainResponse.setError(false);
                       addDependantMainResponse.setLoading(false);
                        addDependantMainResponse.setAddDependantResponse(response.body());
                        addDependantMainResponseMutableLiveData.setValue(addDependantMainResponse);

                    } else {
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                        addDependantMainResponse.setError(true);
                        addDependantMainResponse.setLoading(false);
                        addDependantMainResponseMutableLiveData.setValue(null);
                    }
                } catch (Exception e) {
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                    addDependantMainResponse.setError(true);
                    addDependantMainResponse.setLoading(false);
                    addDependantMainResponseMutableLiveData.setValue(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DependantMessageResponse> call, Throwable t) {
                addDependantMainResponse.setError(true);
                addDependantMainResponse.setLoading(false);
                addDependantMainResponseMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });

        return addDependantMainResponseMutableLiveData;
    }



    //update  dependant data new api call
    public LiveData<DependantMessageMainResponse> updateDependantData(Map<String, String> dependantOptions) {
        MutableLiveData<DependantMessageMainResponse> updateDependantMainResponseMutableLiveData = new MutableLiveData<>();
        DependantMessageMainResponse addDependantMainResponse=new DependantMessageMainResponse();
        Call<DependantMessageResponse> addDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().updateDependantData(dependantOptions);

        addDependantCall.enqueue(new Callback<DependantMessageResponse>() {
            @Override
            public void onResponse(Call<DependantMessageResponse> call, Response<DependantMessageResponse> response) {
                try {

                    if (response.code() == 200) {
                        //dependant added successfully
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                        addDependantMainResponse.setError(false);
                        addDependantMainResponse.setLoading(false);
                        addDependantMainResponse.setAddDependantResponse(response.body());
                        updateDependantMainResponseMutableLiveData.setValue(addDependantMainResponse);

                    } else {
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                        addDependantMainResponse.setError(true);
                        addDependantMainResponse.setLoading(false);
                        updateDependantMainResponseMutableLiveData.setValue(null);
                    }
                } catch (Exception e) {
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                    addDependantMainResponse.setError(true);
                    addDependantMainResponse.setLoading(false);
                    updateDependantMainResponseMutableLiveData.setValue(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DependantMessageResponse> call, Throwable t) {
                addDependantMainResponse.setError(true);
                addDependantMainResponse.setLoading(false);
                updateDependantMainResponseMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });

        return updateDependantMainResponseMutableLiveData;
    }



    //delete  dependant data new api call
    public LiveData<DependantMessageMainResponse> deleteDependantData(Map<String, String> dependantsOption) {
        MutableLiveData<DependantMessageMainResponse> deleteDependantMainResponseMutableLiveData = new MutableLiveData<>();
        DependantMessageMainResponse addDependantMainResponse=new DependantMessageMainResponse();
        Call<DependantMessageResponse> addDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().deleteDependantData( dependantsOption);

        addDependantCall.enqueue(new Callback<DependantMessageResponse>() {
            @Override
            public void onResponse(Call<DependantMessageResponse> call, Response<DependantMessageResponse> response) {
                try {

                    if (response.code() == 200) {
                        //dependant added successfully
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                        addDependantMainResponse.setError(false);
                        addDependantMainResponse.setLoading(false);
                        addDependantMainResponse.setAddDependantResponse(response.body());
                        deleteDependantMainResponseMutableLiveData.setValue(addDependantMainResponse);

                    } else {
                        LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                        addDependantMainResponse.setError(true);
                        addDependantMainResponse.setLoading(false);
                        deleteDependantMainResponseMutableLiveData.setValue(null);
                    }
                } catch (Exception e) {
                    LogMyBenefits.d(LogTags.ENROLLMENT, "On Parent Added: " + response.body());
                    addDependantMainResponse.setError(true);
                    addDependantMainResponse.setLoading(false);
                    deleteDependantMainResponseMutableLiveData.setValue(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DependantMessageResponse> call, Throwable t) {
                addDependantMainResponse.setError(true);
                addDependantMainResponse.setLoading(false);
                deleteDependantMainResponseMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });

        return deleteDependantMainResponseMutableLiveData;
    }

// parental relation  new api
    public MutableLiveData<ParentalRelationResponse> getParentalRelationshipGroup(String empSrNo, String groupChildSrNo, String oeGrpBasInfoSrNo, String windowPeriod) {

        ParentalRelationResponse relationMainResponse = new ParentalRelationResponse();

        Call<ParentalRelationResponse> relationCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi()
                .getParentalRelationShipGroup(empSrNo, groupChildSrNo, oeGrpBasInfoSrNo, windowPeriod);

        relationCall.enqueue(new Callback<ParentalRelationResponse>() {
            @Override
            public void onResponse(Call<ParentalRelationResponse> call, Response<ParentalRelationResponse> response) {
                try {
                    if (response.code() == 200) {
                        relationMainResponse.setAvailPaernts(response.body().getAvailPaernts());
                        relationMainResponse.setError(false);
                        relationMainResponse.setLoading(false);

                        parentalRelationResponseMutableLiveData.setValue(relationMainResponse);

                    } else {
                        relationMainResponse.setAvailPaernts(null);
                        relationMainResponse.setError(true);
                        relationMainResponse.setLoading(false);
                        parentalRelationResponseMutableLiveData.setValue(relationMainResponse);
                    }


                } catch (Exception e) {
                    relationMainResponse.setAvailPaernts(null);
                    relationMainResponse.setError(true);
                    relationMainResponse.setLoading(false);
                    parentalRelationResponseMutableLiveData.setValue(relationMainResponse);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ParentalRelationResponse> call, Throwable t) {
                relationMainResponse.setAvailPaernts(null);
                relationMainResponse.setError(true);
                relationMainResponse.setLoading(false);
                parentalRelationResponseMutableLiveData.setValue(relationMainResponse);
                call.cancel();
            }
        });

        return parentalRelationResponseMutableLiveData;
    }
    //new api  call parental
    public MutableLiveData<ParentDetailMainResponse> getParentalDataNew(String windowPeriodActive,
                                                                        String groupChildSrNo,
                                                                        String oeGrpBasInfoSrNo,
                                                                        String employeeSrNo,
                                                                        String parentalPremium,
                                                                        String typaPolicySrNo,
                                                                        String employeeType) {

        ParentDetailMainResponse parentalMainResponse = new ParentDetailMainResponse();

        Call<List<Parent>> dependantsCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi()
                .getParentalDependantData(windowPeriodActive, groupChildSrNo, oeGrpBasInfoSrNo, employeeSrNo,parentalPremium,typaPolicySrNo,employeeType);

        dependantsCall.enqueue(new Callback<List<Parent>>() {
            @Override
            public void onResponse(Call<List<Parent>> call, Response<List<Parent>> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d("","response"+response.body());
                        parentalMainResponse.setRelations(response.body());
                        parentalMainResponse.setLoading(false);
                        parentalMainResponse.setError(false);

                        parentDetailMAinResponseMutableLiveData.setValue(parentalMainResponse);

                    } catch (Exception e) {
                        LogMyBenefits.d("","data"+response.body());
                        parentalMainResponse.setRelations(null);
                        parentalMainResponse.setLoading(false);
                        parentalMainResponse.setError(true);
                        parentDetailMAinResponseMutableLiveData.setValue(parentalMainResponse);
                    }

                } else {
                    LogMyBenefits.d("","info"+response.body());
                    parentalMainResponse.setRelations(null);
                    parentalMainResponse.setLoading(false);
                    parentalMainResponse.setError(true);
                    parentDetailMAinResponseMutableLiveData.setValue(parentalMainResponse);
                }
            }

            @Override
            public void onFailure(Call<List<Parent>> call, Throwable t) {
                LogMyBenefits.d("","error"+t.toString());
                parentalMainResponse.setRelations(null);
                parentalMainResponse.setLoading(false);
                parentalMainResponse.setError(true);
                parentDetailMAinResponseMutableLiveData.setValue(parentalMainResponse);
            }
        });

        return parentDetailMAinResponseMutableLiveData;
    }

    public MutableLiveData<ParentalRelationResponse> getParentalRelationshipGroupData() {
        return parentalRelationResponseMutableLiveData;
    }


    //summary confirm  new api call
    public LiveData<DependantMessageMainResponse> confirmSummary(Map<String, String> summaryOptions) {
        MutableLiveData<DependantMessageMainResponse> updateDependantMainResponseMutableLiveData = new MutableLiveData<>();
        DependantMessageMainResponse addDependantMainResponse=new DependantMessageMainResponse();
        Call<DependantMessageResponse> addDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().summaryConfirm(summaryOptions);

        addDependantCall.enqueue(new Callback<DependantMessageResponse>() {
            @Override
            public void onResponse(Call<DependantMessageResponse> call, Response<DependantMessageResponse> response) {
                try {

                    if (response.code() == 200) {
                        //dependant added successfully
                        LogMyBenefits.d(LogTags.ENROLLMENT, "summary confirm: " + response.body());
                        addDependantMainResponse.setError(false);
                        addDependantMainResponse.setLoading(false);
                        addDependantMainResponse.setAddDependantResponse(response.body());
                        updateDependantMainResponseMutableLiveData.setValue(addDependantMainResponse);

                    } else {
                        LogMyBenefits.d(LogTags.ENROLLMENT, "summary confirm:" + response.body());
                        addDependantMainResponse.setError(true);
                        addDependantMainResponse.setLoading(false);
                        updateDependantMainResponseMutableLiveData.setValue(null);
                    }
                } catch (Exception e) {
                    LogMyBenefits.d(LogTags.ENROLLMENT, "summary confirm: " + response.body());
                    addDependantMainResponse.setError(true);
                    addDependantMainResponse.setLoading(false);
                    updateDependantMainResponseMutableLiveData.setValue(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DependantMessageResponse> call, Throwable t) {
                addDependantMainResponse.setError(true);
                addDependantMainResponse.setLoading(false);
                updateDependantMainResponseMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });

        return updateDependantMainResponseMutableLiveData;
    }



    //get min and max age
    public LiveData<MaxMinAgeMainResponse>getMaxMinAge(String oeGrpBasInfoSrNo) {


       maxMinAgeMainResponseMutableLiveData = new MutableLiveData<>();
        MaxMinAgeMainResponse maxMinAgeMainResponse=new MaxMinAgeMainResponse();
        //Call<MaxMinAgeResponse> addDependantCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().summaryConfirm(summaryOptions);

        Call<List<MaxMinAgeResponse>> maxMinAgeResponseCall = EnrollmentRetrofitClient.getInstance().getEnrollmentApi().getMAXMinAge(oeGrpBasInfoSrNo);

        maxMinAgeResponseCall.enqueue(new Callback<List<MaxMinAgeResponse>>() {
            @Override
            public void onResponse(Call<List<MaxMinAgeResponse>> call, Response<List<MaxMinAgeResponse>>response) {
                try {

                    if (response.code() == 200) {
                        //dependant added successfully
                        maxMinAgeMainResponse.setOeGrpBasInfoSrNo(oeGrpBasInfoSrNo);
                        maxMinAgeMainResponse.setMaxMinAgeResponseList(response.body());
                        maxMinAgeMainResponseMutableLiveData.setValue(maxMinAgeMainResponse);

                        maxMinAgeDao.insertAge(maxMinAgeMainResponse);
                        LogMyBenefits.d(LogTags.ENROLLMENT , "age response 1:" + response.body());

                    } else {
                        maxMinAgeMainResponse.setOeGrpBasInfoSrNo(oeGrpBasInfoSrNo);
                        maxMinAgeMainResponse.setMaxMinAgeResponseList(response.body());
                        maxMinAgeMainResponseMutableLiveData.setValue(maxMinAgeMainResponse);
                        LogMyBenefits.d(LogTags.ENROLLMENT , "age response  2:" + response.body());

                    }
                } catch (Exception e) {
                    LogMyBenefits.d(LogTags.ENROLLMENT , "age response 3:" + response.body());
                    maxMinAgeMainResponseMutableLiveData.setValue(null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<MaxMinAgeResponse>> call, Throwable t) {
               /* maxMinAgeMainResponseMutableLiveData.setValue(null);
                t.printStackTrace();*/
                try {
                    maxMinAgeMainResponseMutableLiveData.setValue(maxMinAgeDao.getAGE(oeGrpBasInfoSrNo));
                    //loadingState.setValue(false);
                   // errorState.setValue(false);
                } catch (Exception e) {
                    maxMinAgeMainResponseMutableLiveData.setValue(null);
                    //loadingState.setValue(false);
                   // errorState.setValue(true);
                }
            }
        });

        return maxMinAgeMainResponseMutableLiveData;
    }

    public MutableLiveData<MaxMinAgeMainResponse> getMinMaxData() {
        return maxMinAgeMainResponseMutableLiveData;
    }




}
