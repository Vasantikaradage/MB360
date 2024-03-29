package com.csform.android.MB360.wellness.homehealthcare;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

/**
 * This file will have the details regarding the home health service
 * which will hold the data for the services for all the services
 * example : services name,
 * family lists,
 * packages of services,
 * location biased search
 **/

public class HomeHealthCareViewModel extends AndroidViewModel {

    HomeHealthCareRepository homeHealthCareRepository;


    public HomeHealthCareViewModel(@NonNull Application application) {
        super(application);
        homeHealthCareRepository = new HomeHealthCareRepository(application);
    }


    public void setService(String serviceName) {
        homeHealthCareRepository.setServiceName(serviceName);
    }

    public String getService() {
        return homeHealthCareRepository.getServiceName().getValue();
    }

    public LiveData<String> getSelectedService() {
        return homeHealthCareRepository.getServiceName();
    }

    public String getWellnessSrNo() {
        return homeHealthCareRepository.getWellSrNo().getValue();
    }

    public MutableLiveData<HealthcareOverviewResponse> getHealthcareOverview(String wellSrNo) {
        return homeHealthCareRepository.getHealthcareOverview(wellSrNo);
    }

    public MutableLiveData<HealthcareOverviewResponse> getHealthcareOverviewData() {
        return homeHealthCareRepository.getHealthcareOverviewData();
    }

    public MutableLiveData<MembersResponse> getMembers(String empId, String groupCode, String wellNessSrNo) {
        return homeHealthCareRepository.getMembers(empId, groupCode, wellNessSrNo);
    }

    public MutableLiveData<MembersResponse> getMembersData() {
        return homeHealthCareRepository.getMembersData();
    }

    public MutableLiveData<MessageResponse> addAddress(Address address) {

        return homeHealthCareRepository.addAddress(address);

    }

    public MutableLiveData<MessageResponse> addAddressData() {

        return homeHealthCareRepository.getAddressData();

    }

    public void setSelectedPerson(FamilyMember member) {
        homeHealthCareRepository.setSelectedPerson(member);
    }

    public LiveData<FamilyMember> getSelectedPerson() {
        return homeHealthCareRepository.getSelectedPerson();
    }

    public void setCity(City city) {

        homeHealthCareRepository.setSelectedCity(city);
    }

    public LiveData<City> getSelectedCity() {
        return homeHealthCareRepository.getSelectedCity();
    }

    public LiveData<PackageResponse> getPackages() {
        return homeHealthCareRepository.getPackages();
    }


    public LiveData<CityResponse> getCitiesAvailable() {
        return homeHealthCareRepository.getCities();
    }

    public LiveData<CityResponse> getCitiesData() {
        return homeHealthCareRepository.getCityData();
    }

    public void setSelectedTrainedAttendantPackage(HomeHealthCarePackage selectedTrainedAttendant) {
        homeHealthCareRepository.setTrainedAttendantSelectedPackage(selectedTrainedAttendant);
    }

    public LiveData<HomeHealthCarePackage> getTrainedAttendantPackage() {
        return homeHealthCareRepository.getTrainedAttendantSelectedPackage();
    }

    public void setAppointmentRequest(Appointment appointment) {
        homeHealthCareRepository.setAppointmentRequest(appointment);
    }

    public LiveData<Appointment> getAppointmentRequest() {
        return homeHealthCareRepository.getAppointmentRequest();
    }

    public LiveData<MessageResponse> scheduleAppointment(Appointment appointment) {
        return homeHealthCareRepository.scheduleAppointment(appointment);
    }

    public LiveData<PackageLTResponse> getPackagesLT() {
        return homeHealthCareRepository.getPackagesLT();
    }

    public void setSelectedLongtermNursing(PackageLT selectedLongtermNursing) {
        homeHealthCareRepository.setLongtermNursingSelectedPackage(selectedLongtermNursing);
    }

    public LiveData<PackageLT> getLongtermNursingPackage() {
        return homeHealthCareRepository.getLongtermNursingSelectedPackage();
    }

    public LiveData<MessageResponse> scheduleAppointmentLT(Appointment appointment) {
        return homeHealthCareRepository.scheduleAppointmentLT(appointment);
    }

    public LiveData<PackageSTResponse> getPackagesST() {
        return homeHealthCareRepository.getPackagesST();
    }

    public void setSelectedShorttermNursing(PackageST selectedShorttermNursing) {
        homeHealthCareRepository.setShorttermNursingSelectedPackage(selectedShorttermNursing);
    }

    public LiveData<PackageST> getShorttermNursingPackage() {
        return homeHealthCareRepository.getShorttermNursingSelectedPackage();
    }

    public LiveData<MessageResponse> scheduleAppointmentST(Appointment appointment) {
        return homeHealthCareRepository.scheduleAppointmentST(appointment);
    }

    public LiveData<PackagePTResponse> getPackagesPT() {
        return homeHealthCareRepository.getPackagesPT();
    }

    public void setSelectedPhysiotherapy(PackagePT selectedPhysiotherapy) {
        homeHealthCareRepository.setPhysiotherapySelectedPackage(selectedPhysiotherapy);
    }

    public LiveData<PackagePT> getPhysiotherapyPackage() {
        return homeHealthCareRepository.getPhysiotherapySelectedPackage();
    }

    public LiveData<MessageResponse> scheduleAppointmentPT(Appointment appointment) {
        return homeHealthCareRepository.scheduleAppointmentPT(appointment);
    }

    public LiveData<PackageNCResponse> getPackagesNC() {
        return homeHealthCareRepository.getPackagesNC();
    }

    public void setSelectedPostNatal(PackageNC selectedPostNatal) {
        homeHealthCareRepository.setPostNatalSelectedPackage(selectedPostNatal);
    }

    public LiveData<PackageNC> getPostNatalPackage() {
        return homeHealthCareRepository.getPostNatalSelectedPackage();
    }

    public LiveData<MessageResponse> scheduleAppointmentNC(Appointment appointment) {
        return homeHealthCareRepository.scheduleAppointmentNC(appointment);
    }

    public LiveData<PackageDSResponse> getPackagesDS() {
        return homeHealthCareRepository.getPackagesDS();
    }

    public void setSelectedDoctorServices(PackageDS selectedDoctorServices) {
        homeHealthCareRepository.setDoctorServicesSelectedPackage(selectedDoctorServices);
    }

    public LiveData<PackageDS> getDoctorServicesPackage() {
        return homeHealthCareRepository.getDoctorServicesSelectedPackage();
    }

    public LiveData<MessageResponse> scheduleAppointmentDS(Appointment appointment) {
        return homeHealthCareRepository.scheduleAppointmentDS(appointment);
    }

    public LiveData<PackageECResponse> getPackagesEC() {
        return homeHealthCareRepository.getPackagesEC();
    }

    public void setSelectedEldercare(PackageEC selectedEldercare) {
        homeHealthCareRepository.setElderCareSelectedPackage(selectedEldercare);
    }

    public LiveData<PackageEC> getElderCarePackage() {
        return homeHealthCareRepository.getElderCareSelectedPackage();
    }

    public LiveData<MessageResponse> scheduleAppointmentEC(Appointment appointment) {
        return homeHealthCareRepository.scheduleAppointmentEC(appointment);
    }

    public LiveData<PackageDMResponse> getPackagesDM() {
        return homeHealthCareRepository.getPackagesDM();
    }

    public void setSelectedDiabetesManagement(PackageDM selectedDiabetesManagement) {
        homeHealthCareRepository.setDiabetesManagementSelectedPackage(selectedDiabetesManagement);
    }

    public LiveData<PackageDM> getDiabetesManagementPackage() {
        return homeHealthCareRepository.getDiabetesManagementSelectedPackage();
    }

    public LiveData<MessageResponse> scheduleAppointmentDM(Appointment appointment) {
        return homeHealthCareRepository.scheduleAppointmentDM(appointment);
    }

    public LiveData<SummaryResponseST> getSTSummaryFromServer(String extFamilySrNo) {
        return homeHealthCareRepository.getSTSummaryFromServer(extFamilySrNo);
    }

    public LiveData<SummaryResponseNA> getNASummaryFromServer(String extFamilySrNo) {
        return homeHealthCareRepository.getSummaryFromServerNA(extFamilySrNo);
    }

    public LiveData<SummaryResponseLT> getLTSummaryFromServer(String extFamilySrNo) {
        return homeHealthCareRepository.getLTSummaryFromServer(extFamilySrNo);
    }

    public LiveData<SummaryResponseDS> getDSSummaryFromServer(String extFamilySrNo) {
        return homeHealthCareRepository.getDSSummaryFromServer(extFamilySrNo);
    }

    public LiveData<SummaryResponsePT> getPTSummaryFromServer(String extFamilySrNo) {
        return homeHealthCareRepository.getPTSummaryFromServer(extFamilySrNo);
    }

    public LiveData<SummaryResponseDM> getDMSummaryFromServer(String extFamilySrNo) {
        return homeHealthCareRepository.getDMSummaryFromServer(extFamilySrNo);
    }

    public LiveData<SummaryResponseNC> getNCSummaryFromServer(String extFamilySrNo) {
        return homeHealthCareRepository.getNCSummaryFromServer(extFamilySrNo);
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return homeHealthCareRepository.getLoadingState();
    }

    public MutableLiveData<Boolean> getErrorState() {
        return homeHealthCareRepository.getErrorState();
    }


    public MutableLiveData<CancelResponse> cancelAppointment(String ApptSrInfoNo) {
        return homeHealthCareRepository.cancelAppointment(ApptSrInfoNo);
    }

    public void resetMessageResponse() {

        homeHealthCareRepository.resetMessageResponse();
    }

    public MutableLiveData<City> getCitiesLiveDataHHC() {
        return homeHealthCareRepository.getCitiesLiveDataHHC();
    }

    public void setCitiesLiveDataHHC(City city) {
        homeHealthCareRepository.setCitiesLiveDataHHC(city);
    }


    //summaryData

    public MutableLiveData<SummaryResponseEc> getECSummary(String strFamilySrNo) {
        return homeHealthCareRepository.getECSummary(strFamilySrNo);
    }

    public MutableLiveData<SummaryResponseEc> getEcSummaryData() {
        return homeHealthCareRepository.getEcSummaryData();
    }

    public MutableLiveData<MessageResponse> bookingResponseData() {
        return homeHealthCareRepository.bookingResponseData();
    }
}
