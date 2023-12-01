package com.csform.android.MB360.insurance.enrollment.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csform.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.DependantDetailsResponseNew;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.EmployeeResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentMessage;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentSummaryResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.InstructionResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MaxMinAgeMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MaxMinAgeResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MyCoveragesResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.Parent;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.TopupResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.coverage.CoverageMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.DependantMessageMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.DependantMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.RelationMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails.EmployeeFiledMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails.EmployeeUpdateInfo;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.ParentDetailMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.ParentalRelationResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.summary.SummaryMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUpPlanMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.TopUpSiMainResponse;

import java.util.List;
import java.util.Map;

public class EnrollmentViewModel extends AndroidViewModel {

    EnrollmentRepository enrollmentRepository;

    public EnrollmentViewModel(@NonNull Application application) {
        super(application);
        enrollmentRepository = new EnrollmentRepository(application);
    }


    public LiveData<InstructionResponse> getInstructions() {
        return enrollmentRepository.getInstructionResponse();
    }

    public LiveData<InstructionResponse> getInstructionsData() {
        return enrollmentRepository.getInstructionResponseData();
    }

    public LiveData<MyCoveragesResponse> getCoverages() {
        return enrollmentRepository.getCoveragesResponse();
    }

    public LiveData<MyCoveragesResponse> getCoveragesData() {
        return enrollmentRepository.getCoveragesData();
    }

    public LiveData<EmployeeResponse> getEmployee() {
        return enrollmentRepository.getEmployeeResponse();
    }

    public LiveData<EmployeeResponse> getEmployeeData() {
        return enrollmentRepository.getEmployeeData();
    }

    public LiveData<DependantMainResponse> getDependants(String emp_sr_no,
                                                         String GroupChildSrNo,
                                                         String oeGrpBasInfoSrNo,
                                                         String isRelGrpDivided) {
        return enrollmentRepository.getDependants(emp_sr_no, GroupChildSrNo, oeGrpBasInfoSrNo, isRelGrpDivided);
    }

    public LiveData<DependantMainResponse> getDependantsData() {
        return enrollmentRepository.getDependantsData();
    }


    public LiveData<RelationMainResponse> getRelationshipGroup(String empSrNo, String groupChildSrNo, String oeGrpBasInfoSrNo, String windowPeriod) {
        return enrollmentRepository.getRelationshipGroup(empSrNo, groupChildSrNo, oeGrpBasInfoSrNo, windowPeriod);
    }

    public LiveData<RelationMainResponse> getRelationshipGroupData() {
        return enrollmentRepository.getRelationshipGroupData();
    }


    public LiveData<List<Parent>> getParental(String isWindowPeriodActive, String groupChildSrNo, String oeGrpBasInfoSrNo, String employeeSrNo, String parentalPremium) {
        return enrollmentRepository.getParental(isWindowPeriodActive, groupChildSrNo, oeGrpBasInfoSrNo, employeeSrNo, parentalPremium);
    }

    public LiveData<List<Parent>> getParentalData() {
        return enrollmentRepository.getParentalData();
    }

    public LiveData<TopupResponse> getTopups() {
        return enrollmentRepository.getTopUps();
    }

    public LiveData<TopupResponse> getTopUpsData() {
        return enrollmentRepository.getTopUpsData();
    }

    public MutableLiveData<EnrollmentSummaryResponse> getSummary() {
        return enrollmentRepository.getSummary();
    }

    public MutableLiveData<EnrollmentSummaryResponse> getSummaryData() {
        return enrollmentRepository.getSummaryData();
    }


    public LiveData<Boolean> getLoading() {
        return enrollmentRepository.loadingState;
    }

    public LiveData<Boolean> getError() {
        return enrollmentRepository.errorState;
    }

    public LiveData<DependantHelperModel> getTwin1() {
        return enrollmentRepository.getTwin1();
    }

    public LiveData<DependantHelperModel> getTwin2() {
        return enrollmentRepository.getTwin2();
    }

    public void setTwin1(DependantHelperModel dependantHelperModel) {
        enrollmentRepository.setTwin1(dependantHelperModel);
    }

    public void setTwin2(DependantHelperModel dependantHelperModel) {
        enrollmentRepository.setTwin2(dependantHelperModel);
    }


    public MutableLiveData<WindowPeriodEnrollmentResponse> getWindowPeriod() {
        return enrollmentRepository.getWindowPeriod();
    }


    //new dependant details work
    public MutableLiveData<DependantDetailsResponseNew> getDependantDetailsNew() {
        return enrollmentRepository.getDependantDetailsNew();
    }

    public MutableLiveData<EnrollmentMessage> addDependant(Map<String, String> dependantOption) {
        return enrollmentRepository.addDependant(dependantOption);
    }

  /*  public MutableLiveData<EnrollmentMessage> deleteDependant(Map<String, String> dependantOption) {
        return enrollmentRepository.deleteDependant(dependantOption);
    }*/

    public MutableLiveData<EnrollmentMessage> updateDependant(Map<String, String> dependantOption) {
        return enrollmentRepository.updateDependant(dependantOption);
    }


    public MutableLiveData<EnrollmentMessage> addParent(Map<String, String> parentOption) {
        return enrollmentRepository.addParent(parentOption);
    }

    public MutableLiveData<EnrollmentMessage> deleteParent(Map<String, String> dependantOption) {
        return enrollmentRepository.deleteParent(dependantOption);
    }

    public MutableLiveData<EnrollmentMessage> updateParent(Map<String, String> dependantOption) {
        return enrollmentRepository.updateParent(dependantOption);
    }


    //Topup SumInsured data
    public LiveData<TopUpSiMainResponse> getTopUpSumInsured(String EmpSrNo,
                                                            String groupChildSrNo,
                                                            String oeGrpBasInfSrNo,
                                                            String Prdttype) {
        return enrollmentRepository.getTopUpSumInsured(EmpSrNo, groupChildSrNo, oeGrpBasInfSrNo, Prdttype);
    }


    //Topup GMC Plan Response
    public LiveData<TopUpPlanMainResponse> getTopUpGMCPlanResponse(String EmpSrNo,
                                                                   String groupChildSrNo,
                                                                   String oeGrpBasInfSrNo,
                                                                   String amount,
                                                                   String premium) {
        return enrollmentRepository.getTopUpGMCPlanResponse(EmpSrNo, groupChildSrNo, oeGrpBasInfSrNo, amount, premium);
    }

    //Topup GPA Plan Response
    public LiveData<TopUpPlanMainResponse> getTopUpGPAPlanResponse(String EmpSrNo,
                                                                   String groupChildSrNo,
                                                                   String oeGrpBasInfSrNo,
                                                                   String amount,
                                                                   String premium) {
        return enrollmentRepository.getTopUpGPAPlanResponse(EmpSrNo, groupChildSrNo, oeGrpBasInfSrNo, amount, premium);
    }


    //Topup GPA Plan Response
    public LiveData<TopUpPlanMainResponse> getTopUpGTLPlanResponse(String EmpSrNo,
                                                                   String groupChildSrNo,
                                                                   String oeGrpBasInfSrNo,
                                                                   String amount,
                                                                   String premium) {
        return enrollmentRepository.getTopUpGTLPlanResponse(EmpSrNo, groupChildSrNo, oeGrpBasInfSrNo, amount, premium);
    }

    //coverage data for ghi
    public LiveData<CoverageMainResponse> getCoverageGHIResponse(String GroupChildSrNo,
                                                                 String OeGrpBasInfSrNo,
                                                                 String EmpSrNo) {
        return enrollmentRepository.getCoverageGHIResponse(GroupChildSrNo, OeGrpBasInfSrNo, EmpSrNo);
    }

    //coverage data for gpa
    public LiveData<CoverageMainResponse> getCoverageGPAResponse(String GroupChildSrNo,
                                                                 String OeGrpBasInfSrNo,
                                                                 String EmpSrNo) {
        return enrollmentRepository.getCoverageGPAResponse(GroupChildSrNo, OeGrpBasInfSrNo, EmpSrNo);
    }


    //coverage data for gtl
    public LiveData<CoverageMainResponse> getCoverageGTLResponse(String GroupChildSrNo,
                                                                 String OeGrpBasInfSrNo,
                                                                 String EmpSrNo) {
        return enrollmentRepository.getCoverageGTLResponse(GroupChildSrNo, OeGrpBasInfSrNo, EmpSrNo);
    }


    //Employee field Display response
    public LiveData<EmployeeFiledMainResponse> getEmployeeFieldResponse(String OeGrpBasInfSrNo) {
        return enrollmentRepository.getEmployeeFieldResponse(OeGrpBasInfSrNo);
    }

    public MutableLiveData<EmployeeUpdateInfo> updateEmployee(Map<String, String> employeeData) {
        return enrollmentRepository.updateEmployee(employeeData);
    }

    public MutableLiveData<EmployeeUpdateInfo> getEmployeeUpdateData() {
        return enrollmentRepository.getEmployeeUpdateData();
    }

    public LiveData<SummaryMainResponse> getSummaryDetails(String gmcEMpSrNo,
                                                           String gpaEmpSrNo,
                                                           String gtlEmpSrNo,
                                                           String GroupChildSrNo,
                                                           String OeGrpBasInfSrNo
    ) {
        return enrollmentRepository.getSummeryDetails(gmcEMpSrNo, gpaEmpSrNo, gtlEmpSrNo, GroupChildSrNo, OeGrpBasInfSrNo);
    }

    // add depenedent new api call
    public LiveData<DependantMessageMainResponse> addDependantData(Map<String, String> dependantOption) {
        return enrollmentRepository.addDependantData(dependantOption);
    }


// update  depenedent new api call

    public LiveData<DependantMessageMainResponse> updateDependantData(Map<String, String> dependantOption) {
        return enrollmentRepository.updateDependantData(dependantOption);
    }

    public LiveData<DependantMessageMainResponse> deleteDependantData(Map<String, String> dependantsOption) {
        return enrollmentRepository.deleteDependantData(dependantsOption);
    }

    public LiveData<ParentalRelationResponse> getParentalRelationshipGroup(String empSrNo, String groupChildSrNo, String oeGrpBasInfoSrNo, String windowPeriod) {
        return enrollmentRepository.getParentalRelationshipGroup(empSrNo, groupChildSrNo, oeGrpBasInfoSrNo, windowPeriod);
    }

    public LiveData<ParentalRelationResponse> getParentalRelationshipGroupData() {
        return enrollmentRepository.getParentalRelationshipGroupData();
    }

    public LiveData<ParentDetailMainResponse> getParentalData(String windowPeriodActive,
                                                              String groupChildSrNo,
                                                              String oeGrpBasInfoSrNo,
                                                              String employeeSrNo,
                                                              String parentalPremium,
                                                              String typaPolicySrNo,
                                                              String employeeType) {
        return enrollmentRepository.getParentalDataNew(windowPeriodActive, groupChildSrNo, oeGrpBasInfoSrNo, employeeSrNo, parentalPremium, typaPolicySrNo, employeeType);
    }

   //confirm summary new api
    public LiveData<DependantMessageMainResponse> confirmSummary(Map<String, String> summarytOption) {
        return enrollmentRepository.confirmSummary(summarytOption);
    }

    //get min max age
    public LiveData<MaxMinAgeMainResponse>maxMinAge(String oeGrpBasInfoSrNo) {
        return enrollmentRepository.getMaxMinAge(oeGrpBasInfoSrNo);
    }

    public MutableLiveData<MaxMinAgeMainResponse> getMinMaxData() {
        return enrollmentRepository.getMinMaxData();
    }
}
