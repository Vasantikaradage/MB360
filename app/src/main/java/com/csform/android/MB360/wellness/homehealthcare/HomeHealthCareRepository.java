package com.csform.android.MB360.wellness.homehealthcare;

import android.app.Application;
import com.csform.android.MB360.utilities.LogMyBenefits;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.wellness.homehealthcare.cancellation.responseclass.CancelResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.Address;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.City;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.CityResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.HealthcareOverviewResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.HomeHealthCarePackage;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.MembersResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.MessageResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageDM;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageDMResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageDS;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageDSResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageEC;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageECResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageLTResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageNC;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageNCResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackagePT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackagePTResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageST;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageSTResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseDM;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseDS;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseNA;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseNC;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponsePT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseST;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.SummaryResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseEc;
import com.csform.android.MB360.wellness.homehealthcare.retrofit.HomeHealthCareRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This file will handle the datasource
 * for the health care services
 * and all the stuff related to the database / api
 **/
public class HomeHealthCareRepository {
    private MutableLiveData<String> serviceName;
    private MutableLiveData<String> wellSrNo;
    private final MutableLiveData<MembersResponse> membersMutableLiveData;
    private final MutableLiveData<Boolean> loadingState;
    private final MutableLiveData<Boolean> errorState;
    private final MutableLiveData<MessageResponse> addressResponse;
    private final MutableLiveData<FamilyMember> selectedPerson;
    private final MutableLiveData<City> selectedCity;
    private final MutableLiveData<CityResponse> cityData;
    private MutableLiveData<SummaryResponse> summaryResponse = new MutableLiveData<>();
    private MutableLiveData<SummaryResponseEc> summaryResponseEcLiveData = new MutableLiveData<>();

    private final MutableLiveData<CancelResponse> cancelResponse;


    private final MutableLiveData<MessageResponse> bookingResponse;

    //Overview
    private final MutableLiveData<HealthcareOverviewResponse> healthcareOverviewResponseMutableLiveData;

    //trained attendant
    private MutableLiveData<HomeHealthCarePackage> trainedAttendantSelectedPackage = new MutableLiveData<>();

    //Appointment - summary page data
    private MutableLiveData<Appointment> appointmentRequest = new MutableLiveData<>();

    private final MutableLiveData<PackageResponse> homeHealthCarePackageResponse;

    //longterm
    private MutableLiveData<PackageLT> longtermNursingSelectedPackage = new MutableLiveData<>();

    private final MutableLiveData<PackageLTResponse> homeHealthCarePackageLTResponse;

    //shortterm
    private MutableLiveData<PackageST> shorttermNursingSelectedPackage = new MutableLiveData<>();

    private final MutableLiveData<PackageSTResponse> homeHealthCarePackageSTResponse;

    //Physiotherapy
    private MutableLiveData<PackagePT> physiotherapySelectedPackage = new MutableLiveData<>();

    private final MutableLiveData<PackagePTResponse> homeHealthCarePackagePTResponse;

    //Post Natal
    private MutableLiveData<PackageNC> postNatalSelectedPackage = new MutableLiveData<>();

    private final MutableLiveData<PackageNCResponse> homeHealthCarePackageNCResponse;

    //Doctor Services
    private MutableLiveData<PackageDS> doctorServicesSelectedPackage = new MutableLiveData<>();

    private final MutableLiveData<PackageDSResponse> homeHealthCarePackageDSResponse;

    //Elder Care
    private MutableLiveData<PackageEC> elderCareSelectedPackage = new MutableLiveData<>();

    private final MutableLiveData<PackageECResponse> homeHealthCarePackageECResponse;

    //Diabetes Management
    private MutableLiveData<PackageDM> diabetesManagementSelectedPackage = new MutableLiveData<>();

    private final MutableLiveData<PackageDMResponse> homeHealthCarePackageDMResponse;

    //home health Care cities
    public MutableLiveData<City> citiesLiveDataHHC;

    Application application;


    public HomeHealthCareRepository(Application application) {
        serviceName = new MutableLiveData<>("");
        wellSrNo = new MutableLiveData<>("");
        membersMutableLiveData = new MutableLiveData<>();
        loadingState = new MutableLiveData<>();
        errorState = new MutableLiveData<>();
        addressResponse = new MutableLiveData<>();
        this.application = application;
        this.selectedPerson = new MutableLiveData<>();
        this.cityData = new MutableLiveData<>();
        this.selectedCity = new MutableLiveData<>(new City("", "", ""));
        this.bookingResponse = new MutableLiveData<>();
        this.healthcareOverviewResponseMutableLiveData = new MutableLiveData<>();
        this.homeHealthCarePackageResponse = new MutableLiveData<>();
        this.homeHealthCarePackageLTResponse = new MutableLiveData<>();
        this.homeHealthCarePackageSTResponse = new MutableLiveData<>();
        this.homeHealthCarePackagePTResponse = new MutableLiveData<>();
        this.homeHealthCarePackageNCResponse = new MutableLiveData<>();
        this.homeHealthCarePackageDSResponse = new MutableLiveData<>();
        this.homeHealthCarePackageECResponse = new MutableLiveData<>();
        this.homeHealthCarePackageDMResponse = new MutableLiveData<>();
        this.cancelResponse = new MutableLiveData<>();
        this.citiesLiveDataHHC = new MutableLiveData<City>();

    }

    public void setServiceName(String service) {
        serviceName.setValue(service);
    }

    public MutableLiveData<String> getServiceName() {
        return serviceName;
    }

    public MutableLiveData<String> getWellSrNo() {
        String service_name = getServiceName().getValue();
        String wellSrNo = "";
        if (service_name != null) {
            switch (service_name) {
                case "MEDICINE DELIVERY":
                case "DOCTOR CONSULTATION":
                case "DENTAL":
                    wellSrNo = "";
                    break;


                case "TRAINED ATTENDANT":
                    wellSrNo = "10";
                    break;

                case "LONG TERM NURSING":
                    wellSrNo = "11";
                    break;

                case "SHORT TERM NURSING":
                    wellSrNo = "12";
                    break;
                case "DOCTOR SERVICES":
                    wellSrNo = "13";
                    break;

                case "PHYSIOTHERAPY":
                    wellSrNo = "14";
                    break;

                case "DIABETES MANAGEMENT":
                    wellSrNo = "15";
                    break;

                case "POST NATAL CARE":
                    wellSrNo = "16";
                    break;

                case "ELDER CARE":
                    wellSrNo = "17";
                    break;
            }
        } else {
            //something went wrong
        }
        return new MutableLiveData<String>(wellSrNo);
    }


    /**
     * this function  has the business logic for calling
     * and parsing the @PolicyFeatures {@link HealthcareOverviewResponse } response
     **/

    public MutableLiveData<HealthcareOverviewResponse> getHealthcareOverview(String wellSrNo) {

        Call<HealthcareOverviewResponse> healthcareOverviewResponseCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getHealthcareOverview(wellSrNo);
        healthcareOverviewResponseCall.enqueue(new Callback<HealthcareOverviewResponse>() {
            @Override
            public void onResponse(Call<HealthcareOverviewResponse> call, Response<HealthcareOverviewResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.HOME_HEALTHCARE_ACTIVITY, "onResponse: " + response.body().toString());
                        healthcareOverviewResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.HOME_HEALTHCARE_ACTIVITY, "Error: ", e);
                        healthcareOverviewResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<HealthcareOverviewResponse> call, Throwable t) {

            }
        });

        return healthcareOverviewResponseMutableLiveData;
    }

    public MutableLiveData<HealthcareOverviewResponse> getHealthcareOverviewData() {
        return healthcareOverviewResponseMutableLiveData;

    }

//
//

    public MutableLiveData<MembersResponse> getMembers(String empId, String groupCode, String wellnessSrNo) {
        Call<MembersResponse> membersCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getMembers(empId, groupCode, wellnessSrNo);
        membersCall.enqueue(new Callback<MembersResponse>() {
            @Override
            public void onResponse(Call<MembersResponse> call, Response<MembersResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_MEMBERS, "onResponse: " + response.body());
                        membersMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.GET_MEMBERS, "onResponse: ", e);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_MEMBERS, "onResponse: " + response.body());
                    membersMutableLiveData.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<MembersResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_MEMBERS, "Error : ", t);
                membersMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return membersMutableLiveData;
    }

    public MutableLiveData<MembersResponse> getMembersData() {
        return membersMutableLiveData;
    }

    public MutableLiveData<MessageResponse> addAddress(Address address) {
        Call<MessageResponse> addressCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().SavePersonAddress(address.getStrLine1(), address.getStrLine2(), address.getStrLandmark(), address.getStrCity(), address.getStrState(), address.getStrPincode(), address.getStrMobileNumber(), address.getStrEmail(), address.getWellSrno(), address.getStrPersonSrno());

        addressCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.ADDRESS_ACTIVITY, "onResponse: " + response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                        addressResponse.setValue(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        loadingState.setValue(true);
                        errorState.setValue(true);
                        addressResponse.setValue(null);
                    }
                } else {
                    LogMyBenefits.d(LogTags.ADDRESS_ACTIVITY, "onResponse: " + response.body());
                    loadingState.setValue(false);
                    errorState.setValue(false);
                    addressResponse.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.ADDRESS_ACTIVITY, "onFailure: ", t);
                loadingState.setValue(true);
                errorState.setValue(true);
                addressResponse.setValue(null);
            }
        });

        return addressResponse;
    }

    public MutableLiveData<MessageResponse> getAddressData() {
        return addressResponse;
    }

    public void setSelectedPerson(FamilyMember member) {
        selectedPerson.setValue(member);
    }

    public MutableLiveData<FamilyMember> getSelectedPerson() {
        return selectedPerson;
    }

    public MutableLiveData<CityResponse> getCities() {
        Call<CityResponse> cityCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getCities();

        cityCall.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_CITIES, "onResponse: " + response.body());
                        cityData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        cityData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_CITIES, "onResponse: " + response.body());
                    cityData.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }

            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_CITIES, "onResponse: ", t);
                cityData.setValue(new CityResponse());
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return cityData;
    }

    public MutableLiveData<CityResponse> getCityData() {
        return cityData;
    }

    public void setSelectedCity(City city) {
        LogMyBenefits.d(LogTags.CITY, "setCity: " + city);
        selectedCity.setValue(city);
    }

    public LiveData<City> getSelectedCity() {
        return selectedCity;
    }


    public MutableLiveData<PackageResponse> getPackages() {
        Call<PackageResponse> packageResponseCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getPackages();
        loadingState.setValue(true);
        packageResponseCall.enqueue(new Callback<PackageResponse>() {
            @Override
            public void onResponse(Call<PackageResponse> call, Response<PackageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        homeHealthCarePackageResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        homeHealthCarePackageResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    homeHealthCarePackageResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<PackageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onResponse: ", t);
                homeHealthCarePackageResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return homeHealthCarePackageResponse;
    }


    public MutableLiveData<HomeHealthCarePackage> getTrainedAttendantSelectedPackage() {
        return trainedAttendantSelectedPackage;
    }

    public void setTrainedAttendantSelectedPackage(HomeHealthCarePackage selectedPackage) {
        trainedAttendantSelectedPackage.setValue(selectedPackage);
    }

    public MutableLiveData<Appointment> getAppointmentRequest() {
        return appointmentRequest;
    }

    public void setAppointmentRequest(Appointment appointmentRequest) {
        this.appointmentRequest.setValue(appointmentRequest);
    }

    /*retrofit query part
    @Query("PersonSrNo") String strPersonSrno,
    @Query("FamilySrNo") String strFamilySrno,
    @Query("ISRescheduled") int intIsRescheduled,
    @Query("RejtApptSrNo") String intApptSrNo,
    @Query("PkgPriceSrNo") int intPkgPriceSrNo,
    @Query("Date_Condition") int intDateCond,
    @Query(value = "Date_pref", encoded = true) String strdatePref,
    @Query(value = "From_date", encoded = true) String strFromDate,
    @Query(value = "To_date", encoded = true) String strToDate,
    @Query("RescSrNo") int intStrRescSrNo,
    @Query("Remarks") String test*/

    public LiveData<MessageResponse> scheduleAppointment(Appointment appointment) {
        //we can add switch case statements here! to keep only one function!
        loadingState.setValue(true);
        Call<MessageResponse> bookAppointmentCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().scheduleAppointment(appointment.getPerson_sr_no(), appointment.getFamily_sr_no(), appointment.getIs_rescheduled(), appointment.getRej_appt_sr_no(), appointment.getPackage_price_sr_no(), appointment.getDate_condition(), appointment.getDate_preference(), appointment.getFrom_date(), appointment.getTo_date(), appointment.getReschedule_sr_no(), appointment.getRemarks());

        bookAppointmentCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                        bookingResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        bookingResponse.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }

                } else {
                    bookingResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went Wrong \nError code: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.TRAINED_ATTENDANT, "onFailure: ", t);
                bookingResponse.setValue(null);
                errorState.setValue(true);
                loadingState.setValue(false);

            }
        });

        return bookingResponse;
    }

    public MutableLiveData<PackageLTResponse> getPackagesLT() {
        Call<PackageLTResponse> packageLTResponseCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getPackagesLT();

        packageLTResponseCall.enqueue(new Callback<PackageLTResponse>() {
            @Override
            public void onResponse(Call<PackageLTResponse> call, Response<PackageLTResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        homeHealthCarePackageLTResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        homeHealthCarePackageLTResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    homeHealthCarePackageLTResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<PackageLTResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onResponse: ", t);
                homeHealthCarePackageLTResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return homeHealthCarePackageLTResponse;
    }


    public MutableLiveData<PackageLT> getLongtermNursingSelectedPackage() {
        return longtermNursingSelectedPackage;
    }

    public void setLongtermNursingSelectedPackage(PackageLT selectedPackageLT) {
        longtermNursingSelectedPackage.setValue(selectedPackageLT);
    }

    public LiveData<MessageResponse> scheduleAppointmentLT(Appointment appointment) {
        //we can add switch case statements here! to keep only one function!
        loadingState.setValue(true);
        Call<MessageResponse> bookAppointmentLTCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().scheduleAppointmentLT(appointment.getPerson_sr_no(), appointment.getFamily_sr_no(), appointment.getIs_rescheduled(), appointment.getRej_appt_sr_no(), appointment.getPackage_price_sr_no(), appointment.getDate_condition(), appointment.getDate_preference(), appointment.getFrom_date(), appointment.getTo_date(), appointment.getReschedule_sr_no(), appointment.getRemarks());

        bookAppointmentLTCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.LONGTERM_NURSING, "onResponse: " + response.body());
                        bookingResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        bookingResponse.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }

                } else {
                    bookingResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went Wrong \nError code: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.LONGTERM_NURSING, "onFailure: ", t);
                bookingResponse.setValue(null);
                errorState.setValue(true);
                loadingState.setValue(false);

            }
        });

        return bookingResponse;
    }

//
//

    public MutableLiveData<PackageSTResponse> getPackagesST() {
        Call<PackageSTResponse> packageSTResponseCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getPackagesST();

        packageSTResponseCall.enqueue(new Callback<PackageSTResponse>() {
            @Override
            public void onResponse(Call<PackageSTResponse> call, Response<PackageSTResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        homeHealthCarePackageSTResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        homeHealthCarePackageSTResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    homeHealthCarePackageSTResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<PackageSTResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onResponse: ", t);
                homeHealthCarePackageSTResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return homeHealthCarePackageSTResponse;
    }


    public MutableLiveData<PackageST> getShorttermNursingSelectedPackage() {
        return shorttermNursingSelectedPackage;
    }

    public void setShorttermNursingSelectedPackage(PackageST selectedPackageST) {
        shorttermNursingSelectedPackage.setValue(selectedPackageST);
    }

    public LiveData<MessageResponse> scheduleAppointmentST(Appointment appointment) {
        //we can add switch case statements here! to keep only one function!
        loadingState.setValue(true);
        Call<MessageResponse> bookAppointmentSTCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().scheduleAppointmentST(appointment.getPerson_sr_no(),
                appointment.getFamily_sr_no(),
                appointment.getIs_rescheduled(),
                appointment.getRej_appt_sr_no(),
                appointment.getPackage_price_sr_no(),
                appointment.getDate_preference(),
                appointment.getTime_preference(),
                appointment.getReschedule_sr_no(),
                appointment.getRemarks());

        bookAppointmentSTCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.SHORTTERM_NURSING, "onResponse: " + response.body());
                        bookingResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        bookingResponse.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }

                } else {
                    bookingResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went Wrong \nError code: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.SHORTTERM_NURSING, "onFailure: ", t);
                bookingResponse.setValue(null);
                errorState.setValue(true);
                loadingState.setValue(false);

            }
        });

        return bookingResponse;
    }

//
//

    public MutableLiveData<PackagePTResponse> getPackagesPT() {
        Call<PackagePTResponse> packagePTResponseCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getPackagesPT();

        packagePTResponseCall.enqueue(new Callback<PackagePTResponse>() {
            @Override
            public void onResponse(Call<PackagePTResponse> call, Response<PackagePTResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        homeHealthCarePackagePTResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        homeHealthCarePackagePTResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    homeHealthCarePackagePTResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<PackagePTResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onResponse: ", t);
                homeHealthCarePackagePTResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return homeHealthCarePackagePTResponse;
    }


    public MutableLiveData<PackagePT> getPhysiotherapySelectedPackage() {
        return physiotherapySelectedPackage;
    }

    public void setPhysiotherapySelectedPackage(PackagePT selectedPackagePT) {
        physiotherapySelectedPackage.setValue(selectedPackagePT);
    }

    public LiveData<MessageResponse> scheduleAppointmentPT(Appointment appointment) {
        //we can add switch case statements here! to keep only one function!
        loadingState.setValue(true);
        Call<MessageResponse> bookAppointmentPTCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().scheduleAppointmentPT(appointment.getPerson_sr_no(), appointment.getFamily_sr_no(), appointment.getIs_rescheduled(), appointment.getRej_appt_sr_no(), appointment.getPackage_price_sr_no(), appointment.getDate_condition(), appointment.getDate_preference(), appointment.getTime_preference(), appointment.getFrom_date(), appointment.getTo_date(), appointment.getReschedule_sr_no(), appointment.getRemarks());

        bookAppointmentPTCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.PHYSIOTHERAPY, "onResponse: " + response.body());
                        bookingResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        bookingResponse.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }

                } else {
                    bookingResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went Wrong \nError code: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.PHYSIOTHERAPY, "onFailure: ", t);
                bookingResponse.setValue(null);
                errorState.setValue(true);
                loadingState.setValue(false);

            }
        });

        return bookingResponse;
    }

//
//

    public MutableLiveData<PackageNCResponse> getPackagesNC() {
        Call<PackageNCResponse> packageNCResponseCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getPackagesNC();

        packageNCResponseCall.enqueue(new Callback<PackageNCResponse>() {
            @Override
            public void onResponse(Call<PackageNCResponse> call, Response<PackageNCResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        homeHealthCarePackageNCResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        homeHealthCarePackageNCResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    homeHealthCarePackageNCResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<PackageNCResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onResponse: ", t);
                homeHealthCarePackageNCResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return homeHealthCarePackageNCResponse;
    }


    public MutableLiveData<PackageNC> getPostNatalSelectedPackage() {
        return postNatalSelectedPackage;
    }

    public void setPostNatalSelectedPackage(PackageNC selectedPackageNC) {
        postNatalSelectedPackage.setValue(selectedPackageNC);
    }

    public LiveData<MessageResponse> scheduleAppointmentNC(Appointment appointment) {
        //we can add switch case statements here! to keep only one function!
        loadingState.setValue(true);
        Call<MessageResponse> bookAppointmentNCCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().scheduleAppointmentNC(appointment.getPerson_sr_no(), appointment.getFamily_sr_no(), appointment.getIs_rescheduled(), appointment.getRej_appt_sr_no(), appointment.getPackage_price_sr_no(), appointment.getDate_condition(), appointment.getDate_preference(), appointment.getFrom_date(), appointment.getTo_date(), appointment.getReschedule_sr_no(), appointment.getRemarks());

        bookAppointmentNCCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.POST_NATAL, "onResponse: " + response.body());
                        bookingResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        bookingResponse.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }

                } else {
                    bookingResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went Wrong \nError code: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.POST_NATAL, "onFailure: ", t);
                bookingResponse.setValue(null);
                errorState.setValue(true);
                loadingState.setValue(false);

            }
        });

        return bookingResponse;
    }

    //
    //

    public MutableLiveData<PackageDSResponse> getPackagesDS() {
        Call<PackageDSResponse> packageDSResponseCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getPackagesDS();

        packageDSResponseCall.enqueue(new Callback<PackageDSResponse>() {
            @Override
            public void onResponse(Call<PackageDSResponse> call, Response<PackageDSResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        homeHealthCarePackageDSResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        homeHealthCarePackageDSResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    homeHealthCarePackageDSResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<PackageDSResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onResponse: ", t);
                homeHealthCarePackageDSResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return homeHealthCarePackageDSResponse;
    }


    public MutableLiveData<PackageDS> getDoctorServicesSelectedPackage() {
        return doctorServicesSelectedPackage;
    }

    public void setDoctorServicesSelectedPackage(PackageDS selectedPackageDS) {
        doctorServicesSelectedPackage.setValue(selectedPackageDS);
    }

    public LiveData<MessageResponse> scheduleAppointmentDS(Appointment appointment) {
        //we can add switch case statements here! to keep only one function!
        loadingState.setValue(true);
        Call<MessageResponse> bookAppointmentDSCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().scheduleAppointmentDS(
                appointment.getPerson_sr_no(),
                appointment.getFamily_sr_no(),
                appointment.getIs_rescheduled(),
                appointment.getRej_appt_sr_no(),
                appointment.getPackage_price_sr_no(),
                appointment.getDate_preference(),
                appointment.getTime_preference(),
                appointment.getReschedule_sr_no(),
                appointment.getRemarks());

        bookAppointmentDSCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.DOCTOR_SERVICES, "onResponse: " + response.body());
                        bookingResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        bookingResponse.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }

                } else {
                    bookingResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went Wrong \nError code: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.DOCTOR_SERVICES, "onFailure: ", t);
                bookingResponse.setValue(null);
                errorState.setValue(true);
                loadingState.setValue(false);

            }
        });

        return bookingResponse;
    }

    //
    //

    public MutableLiveData<PackageECResponse> getPackagesEC() {
        Call<PackageECResponse> packageECResponseCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getPackagesEC();

        packageECResponseCall.enqueue(new Callback<PackageECResponse>() {
            @Override
            public void onResponse(Call<PackageECResponse> call, Response<PackageECResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        homeHealthCarePackageECResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        homeHealthCarePackageECResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    homeHealthCarePackageECResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<PackageECResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onResponse: ", t);
                homeHealthCarePackageECResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return homeHealthCarePackageECResponse;
    }


    public MutableLiveData<PackageEC> getElderCareSelectedPackage() {
        return elderCareSelectedPackage;
    }

    public void setElderCareSelectedPackage(PackageEC selectedPackageEC) {
        elderCareSelectedPackage.setValue(selectedPackageEC);
    }

    public LiveData<MessageResponse> scheduleAppointmentEC(Appointment appointment) {
        //we can add switch case statements here! to keep only one function!
        loadingState.setValue(true);
        Call<MessageResponse> bookAppointmentECCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().scheduleAppointmentEC(appointment.getPerson_sr_no(), appointment.getFamily_sr_no(), appointment.getPackage_price_sr_no(), appointment.getReschedule_sr_no(), appointment.getRemarks());

        bookAppointmentECCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.ELDER_CARE, "onResponse: " + response.body());
                        bookingResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        bookingResponse.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }

                } else {
                    bookingResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went Wrong \nError code: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.ELDER_CARE, "onFailure: ", t);
                bookingResponse.setValue(null);
                errorState.setValue(true);
                loadingState.setValue(false);

            }
        });

        return bookingResponse;
    }


    //
    //

    public MutableLiveData<PackageDMResponse> getPackagesDM() {
        Call<PackageDMResponse> packageDMResponseCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().getPackagesDM();

        packageDMResponseCall.enqueue(new Callback<PackageDMResponse>() {
            @Override
            public void onResponse(Call<PackageDMResponse> call, Response<PackageDMResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        homeHealthCarePackageDMResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        homeHealthCarePackageDMResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    homeHealthCarePackageDMResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<PackageDMResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onResponse: ", t);
                homeHealthCarePackageECResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return homeHealthCarePackageDMResponse;
    }


    public MutableLiveData<PackageDM> getDiabetesManagementSelectedPackage() {
        return diabetesManagementSelectedPackage;
    }

    public void setDiabetesManagementSelectedPackage(PackageDM selectedPackageDM) {
        diabetesManagementSelectedPackage.setValue(selectedPackageDM);
    }

    public LiveData<MessageResponse> scheduleAppointmentDM(Appointment appointment) {
        //we can add switch case statements here! to keep only one function!
        loadingState.setValue(true);
        Call<MessageResponse> bookAppointmentDMCall = HomeHealthCareRetrofitClient.getInstance().getHomeHealthCareApi().scheduleAppointmentDM(appointment.getPerson_sr_no(), appointment.getFamily_sr_no(), appointment.getPackage_price_sr_no(), appointment.getReschedule_sr_no(), appointment.getRemarks());

        bookAppointmentDMCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.DIABETES_MANAGEMENT_ACTIVITY, "onResponse: " + response.body());
                        bookingResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        bookingResponse.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }

                } else {
                    bookingResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went Wrong \nError code: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.DIABETES_MANAGEMENT_ACTIVITY, "onFailure: ", t);
                bookingResponse.setValue(null);
                errorState.setValue(true);
                loadingState.setValue(false);

            }
        });

        return bookingResponse;
    }
//
//

    public void setSummaryResponse(SummaryResponse summaryResponse) {
        this.summaryResponse.setValue(summaryResponse);
    }


    //short term summary
    public MutableLiveData<SummaryResponseST> getSTSummaryFromServer(String familySrNo) {
        MutableLiveData<SummaryResponseST> shortTermOngoing = new MutableLiveData();
        Call<SummaryResponseST> summaryCall;


        summaryCall = HomeHealthCareRetrofitClient.getInstance().getSummaryApi().getShortTermSummary(familySrNo);
        summaryCall.enqueue(new Callback<SummaryResponseST>() {
            @Override
            public void onResponse(Call<SummaryResponseST> call, Response<SummaryResponseST> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        shortTermOngoing.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        shortTermOngoing.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    shortTermOngoing.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<SummaryResponseST> call, Throwable t) {
                shortTermOngoing.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });


        return shortTermOngoing;
    }

    public MutableLiveData<SummaryResponseNA> getSummaryFromServerNA(String familySrNo) {
        MutableLiveData<SummaryResponseNA> summaryResponseNaMutableLiveData = new MutableLiveData<>(null);


        Call<SummaryResponseNA> summaryCall = HomeHealthCareRetrofitClient.getInstance().getSummaryApi().getTrainedAttendantSummary(familySrNo);
        summaryCall.enqueue(new Callback<SummaryResponseNA>() {
            @Override
            public void onResponse(Call<SummaryResponseNA> call, Response<SummaryResponseNA> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        summaryResponseNaMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        summaryResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    summaryResponseNaMutableLiveData.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<SummaryResponseNA> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                summaryResponseNaMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });


        return summaryResponseNaMutableLiveData;
    }

    public MutableLiveData<SummaryResponseLT> getLTSummaryFromServer(String familySrNo) {
        MutableLiveData<SummaryResponseLT> summaryResponseNaMutableLiveData = new MutableLiveData<>(null);


        Call<SummaryResponseLT> summaryCall = HomeHealthCareRetrofitClient.getInstance().getSummaryApi().getLongTermSummary(familySrNo);
        summaryCall.enqueue(new Callback<SummaryResponseLT>() {
            @Override
            public void onResponse(Call<SummaryResponseLT> call, Response<SummaryResponseLT> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        summaryResponseNaMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        summaryResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    summaryResponseNaMutableLiveData.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<SummaryResponseLT> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                summaryResponseNaMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });


        return summaryResponseNaMutableLiveData;
    }


    public MutableLiveData<SummaryResponseDS> getDSSummaryFromServer(String familySrNo) {

        MutableLiveData<SummaryResponseDS> dsSummaryResponse = new MutableLiveData<>(null);

        Call<SummaryResponseDS> summaryCall = HomeHealthCareRetrofitClient.getInstance().getSummaryApi().getDoctorServicesSummary(familySrNo);
        summaryCall.enqueue(new Callback<SummaryResponseDS>() {
            @Override
            public void onResponse(Call<SummaryResponseDS> call, Response<SummaryResponseDS> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        dsSummaryResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        dsSummaryResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    dsSummaryResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<SummaryResponseDS> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                dsSummaryResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return dsSummaryResponse;
    }

    public MutableLiveData<SummaryResponsePT> getPTSummaryFromServer(String familySrNo) {

        MutableLiveData<SummaryResponsePT> ptSummaryResponse = new MutableLiveData<>(null);

        Call<SummaryResponsePT> summaryCall = HomeHealthCareRetrofitClient.getInstance().getSummaryApi().getPhysioTherapySummary(familySrNo);
        summaryCall.enqueue(new Callback<SummaryResponsePT>() {
            @Override
            public void onResponse(Call<SummaryResponsePT> call, Response<SummaryResponsePT> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        ptSummaryResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ptSummaryResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    ptSummaryResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<SummaryResponsePT> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                ptSummaryResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return ptSummaryResponse;
    }

    public MutableLiveData<SummaryResponseDM> getDMSummaryFromServer(String familySrNo) {

        MutableLiveData<SummaryResponseDM> dmSummaryResponse = new MutableLiveData<>(null);

        Call<SummaryResponseDM> summaryCall = HomeHealthCareRetrofitClient.getInstance().getSummaryApi().getDiabetesManagmentSummary(familySrNo);
        summaryCall.enqueue(new Callback<SummaryResponseDM>() {
            @Override
            public void onResponse(Call<SummaryResponseDM> call, Response<SummaryResponseDM> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        dmSummaryResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        dmSummaryResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    dmSummaryResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<SummaryResponseDM> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                dmSummaryResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return dmSummaryResponse;
    }



    public MutableLiveData<SummaryResponseNC> getNCSummaryFromServer(String familySrNo) {

        MutableLiveData<SummaryResponseNC> dmSummaryResponse = new MutableLiveData<>(null);

        Call<SummaryResponseNC> summaryCall = HomeHealthCareRetrofitClient.getInstance().getSummaryApi().getPostNatalSummary(familySrNo);
        summaryCall.enqueue(new Callback<SummaryResponseNC>() {
            @Override
            public void onResponse(Call<SummaryResponseNC> call, Response<SummaryResponseNC> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                        dmSummaryResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        dmSummaryResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    LogMyBenefits.d(LogTags.GET_PACKAGES, "onResponse: " + response.body());
                    dmSummaryResponse.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<SummaryResponseNC> call, Throwable t) {
                LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                dmSummaryResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return dmSummaryResponse;
    }


    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return errorState;
    }

    public MutableLiveData<CancelResponse> cancelAppointment(String ApptInfoSrNo) {
        Call<CancelResponse> cancelCall;
        if (getServiceName().getValue() != null) {

            switch (getServiceName().getValue()) {
                case "TRAINED ATTENDANT":
                    cancelCall = HomeHealthCareRetrofitClient.getInstance().getCancellationApi().funcCancelNAAppointment(ApptInfoSrNo);
                    cancelCall.enqueue(new Callback<CancelResponse>() {
                        @Override
                        public void onResponse(Call<CancelResponse> call, Response<CancelResponse> response) {
                            if (response.code() == 200) {
                                try {
                                    LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                    cancelResponse.setValue(response.body());
                                    loadingState.setValue(false);
                                    errorState.setValue(false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    summaryResponse.setValue(null);
                                    loadingState.setValue(false);
                                    errorState.setValue(true);
                                }
                            } else {
                                LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                cancelResponse.setValue(response.body());
                                loadingState.setValue(false);
                                errorState.setValue(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelResponse> call, Throwable t) {
                            LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                            cancelResponse.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    });
                    break;
                case "LONG TERM NURSING":
                    cancelCall = HomeHealthCareRetrofitClient.getInstance().getCancellationApi().funcCancelLTAppointment(ApptInfoSrNo);
                    cancelCall.enqueue(new Callback<CancelResponse>() {
                        @Override
                        public void onResponse(Call<CancelResponse> call, Response<CancelResponse> response) {
                            if (response.code() == 200) {
                                try {
                                    LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                    cancelResponse.setValue(response.body());
                                    loadingState.setValue(false);
                                    errorState.setValue(false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    summaryResponse.setValue(null);
                                    loadingState.setValue(false);
                                    errorState.setValue(true);
                                }
                            } else {
                                LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                cancelResponse.setValue(response.body());
                                loadingState.setValue(false);
                                errorState.setValue(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelResponse> call, Throwable t) {
                            LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                            cancelResponse.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    });
                    break;
                case "SHORT TERM NURSING":
                    cancelCall = HomeHealthCareRetrofitClient.getInstance().getCancellationApi().funcCancelSTAppointment(ApptInfoSrNo);
                    cancelCall.enqueue(new Callback<CancelResponse>() {
                        @Override
                        public void onResponse(Call<CancelResponse> call, Response<CancelResponse> response) {
                            if (response.code() == 200) {
                                try {
                                    LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                    cancelResponse.setValue(response.body());
                                    loadingState.setValue(false);
                                    errorState.setValue(false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    summaryResponse.setValue(null);
                                    loadingState.setValue(false);
                                    errorState.setValue(true);
                                }
                            } else {
                                LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                cancelResponse.setValue(response.body());
                                loadingState.setValue(false);
                                errorState.setValue(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelResponse> call, Throwable t) {
                            LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                            cancelResponse.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    });
                    break;
                case "DOCTOR SERVICES":
                    cancelCall = HomeHealthCareRetrofitClient.getInstance().getCancellationApi().funcCancelDSAppointment(ApptInfoSrNo);
                    cancelCall.enqueue(new Callback<CancelResponse>() {
                        @Override
                        public void onResponse(Call<CancelResponse> call, Response<CancelResponse> response) {
                            if (response.code() == 200) {
                                try {
                                    LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                    cancelResponse.setValue(response.body());
                                    loadingState.setValue(false);
                                    errorState.setValue(false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    summaryResponse.setValue(null);
                                    loadingState.setValue(false);
                                    errorState.setValue(true);
                                }
                            } else {
                                LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                cancelResponse.setValue(response.body());
                                loadingState.setValue(false);
                                errorState.setValue(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelResponse> call, Throwable t) {
                            LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                            cancelResponse.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    });
                    break;
                case "PHYSIOTHERAPY":
                    cancelCall = HomeHealthCareRetrofitClient.getInstance().getCancellationApi().funcCancelPTAppointment(ApptInfoSrNo);
                    cancelCall.enqueue(new Callback<CancelResponse>() {
                        @Override
                        public void onResponse(Call<CancelResponse> call, Response<CancelResponse> response) {
                            if (response.code() == 200) {
                                try {
                                    LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                    cancelResponse.setValue(response.body());
                                    loadingState.setValue(false);
                                    errorState.setValue(false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    summaryResponse.setValue(null);
                                    loadingState.setValue(false);
                                    errorState.setValue(true);
                                }
                            } else {
                                LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                cancelResponse.setValue(response.body());
                                loadingState.setValue(false);
                                errorState.setValue(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelResponse> call, Throwable t) {
                            LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                            cancelResponse.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    });
                    break;
                case "POST NATAL CARE":
                    cancelCall = HomeHealthCareRetrofitClient.getInstance().getCancellationApi().funcCancelNCAppointment(ApptInfoSrNo);
                    cancelCall.enqueue(new Callback<CancelResponse>() {
                        @Override
                        public void onResponse(Call<CancelResponse> call, Response<CancelResponse> response) {
                            if (response.code() == 200) {
                                try {
                                    LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                    cancelResponse.setValue(response.body());
                                    loadingState.setValue(false);
                                    errorState.setValue(false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    summaryResponse.setValue(null);
                                    loadingState.setValue(false);
                                    errorState.setValue(true);
                                }
                            } else {
                                LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                cancelResponse.setValue(response.body());
                                loadingState.setValue(false);
                                errorState.setValue(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelResponse> call, Throwable t) {
                            LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                            cancelResponse.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    });
                    break;
                case "ELDER CARE":
                    cancelCall = HomeHealthCareRetrofitClient.getInstance().getCancellationApi().funcCancelECAppointment(ApptInfoSrNo);
                    cancelCall.enqueue(new Callback<CancelResponse>() {
                        @Override
                        public void onResponse(Call<CancelResponse> call, Response<CancelResponse> response) {
                            if (response.code() == 200) {
                                try {
                                    LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                    cancelResponse.setValue(response.body());
                                    loadingState.setValue(false);
                                    errorState.setValue(false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    summaryResponse.setValue(null);
                                    loadingState.setValue(false);
                                    errorState.setValue(true);
                                }
                            } else {
                                LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                cancelResponse.setValue(response.body());
                                loadingState.setValue(false);
                                errorState.setValue(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelResponse> call, Throwable t) {
                            LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                            cancelResponse.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    });
                    break;
                case "DIABETES MANAGEMENT":
                    cancelCall = HomeHealthCareRetrofitClient.getInstance().getCancellationApi().funcCancelDMAppointment(ApptInfoSrNo);
                    cancelCall.enqueue(new Callback<CancelResponse>() {
                        @Override
                        public void onResponse(Call<CancelResponse> call, Response<CancelResponse> response) {
                            if (response.code() == 200) {
                                try {
                                    LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                    cancelResponse.setValue(response.body());
                                    loadingState.setValue(false);
                                    errorState.setValue(false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    summaryResponse.setValue(null);
                                    loadingState.setValue(false);
                                    errorState.setValue(true);
                                }
                            } else {
                                LogMyBenefits.d(LogTags.TRAINED_ATTENDANT, "onResponse: " + response.body());
                                cancelResponse.setValue(response.body());
                                loadingState.setValue(false);
                                errorState.setValue(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelResponse> call, Throwable t) {
                            LogMyBenefits.e(LogTags.GET_PACKAGES, "onFailure: ", t);
                            cancelResponse.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    });
                    break;


            }
        }
        return cancelResponse;

    }

    public void resetMessageResponse() {

        bookingResponse.setValue(null);
    }

    public MutableLiveData<City> getCitiesLiveDataHHC() {
        return citiesLiveDataHHC;
    }

    public void setCitiesLiveDataHHC(City city) {
        citiesLiveDataHHC.setValue(city);
    }


    //summaryData
    public MutableLiveData<SummaryResponseEc> getECSummary(String strFamilySrNo) {

        Call<SummaryResponseEc> elderCareSummaryCall = HomeHealthCareRetrofitClient.getInstance().getSummaryApi().getElderCareSummary(strFamilySrNo);

        elderCareSummaryCall.enqueue(new Callback<SummaryResponseEc>() {
            @Override
            public void onResponse(Call<SummaryResponseEc> call, Response<SummaryResponseEc> response) {
                try {
                    if (response.code() == 200) {
                        LogMyBenefits.d(LogTags.ELDER_CARE, "onResponse: " + response.body().toString());
                        //successful
                        loadingState.setValue(false);
                        errorState.setValue(false);
                        summaryResponseEcLiveData.setValue(response.body());
                    } else {
                        //something went wrong
                        loadingState.setValue(false);
                        errorState.setValue(true);
                        summaryResponseEcLiveData.setValue(null);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<SummaryResponseEc> call, Throwable t) {
                //something went wrong
                loadingState.setValue(false);
                errorState.setValue(true);
                summaryResponseEcLiveData.setValue(null);
            }
        });

        return summaryResponseEcLiveData;
    }

    public MutableLiveData<SummaryResponseEc> getEcSummaryData() {
        return summaryResponseEcLiveData;
    }


    public MutableLiveData<MessageResponse> bookingResponseData() {
        return bookingResponse;
    }

}

