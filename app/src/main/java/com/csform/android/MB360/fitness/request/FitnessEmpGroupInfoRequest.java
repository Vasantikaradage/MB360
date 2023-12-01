package com.csform.android.MB360.fitness.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class FitnessEmpGroupInfoRequest {

    @SerializedName("GROUP_CODE")
    @Expose
    private String groupCode;

    @SerializedName("GROUP_NAME")
    @Expose
    private String groupName;

    @SerializedName("EMPLOYEE_IDENTIFICATION_NO")
    @Expose
    private String employeeIdentificationNo;

    @SerializedName("DATE_OF_JOINING")
    @Expose
    private String dateOfJoining;

    @SerializedName("OFFICIAL_E_MAIL_ID")
    @Expose
    private String officialEmailId;

    @SerializedName("DEPARTMENT")
    @Expose
    private String department;

   @SerializedName("GRADE")
    @Expose
    private String grade;

    @SerializedName("DESIGNATION")
    @Expose
    private String designation;

    @SerializedName("AGE")
    @Expose
    private String age;

    @SerializedName("DATE_OF_BIRTH")
    @Expose
    private String dateofBitrth;

    @SerializedName("OFFICIAL_EMAIL_ID")
    @Expose
    private String emailId;

    @SerializedName("CELLPHONE_NUMBER")
    @Expose
    private String cellPhoneNo;

    @SerializedName("PERSON_NAME")
    @Expose
    private String personName;

    @SerializedName("GENDER")
    @Expose
    private String gender;

    @SerializedName("RELATIONID")
    @Expose
    private String relationId;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEmployeeIdentificationNo() {
        return employeeIdentificationNo;
    }

    public void setEmployeeIdentificationNo(String employeeIdentificationNo) {
        this.employeeIdentificationNo = employeeIdentificationNo;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getOfficialEmailId() {
        return officialEmailId;
    }

    public void setOfficialEmailId(String officialEmailId) {
        this.officialEmailId = officialEmailId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateofBitrth() {
        return dateofBitrth;
    }

    public void setDateofBitrth(String dateofBitrth) {
        this.dateofBitrth = dateofBitrth;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCellPhoneNo() {
        return cellPhoneNo;
    }

    public void setCellPhoneNo(String cellPhoneNo) {
        this.cellPhoneNo = cellPhoneNo;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    @Override
    public String toString() {
        return "FitnessEmpGroupInfoRequest{" +
                "groupCode='" + groupCode + '\'' +
                ", groupName='" + groupName + '\'' +
                ", employeeIdentificationNo='" + employeeIdentificationNo + '\'' +
                ", dateOfJoining='" + dateOfJoining + '\'' +
                ", officialEmailId='" + officialEmailId + '\'' +
                ", department='" + department + '\'' +
                ", grade='" + grade + '\'' +
                ", designation='" + designation + '\'' +
                ", age='" + age + '\'' +
                ", dateofBitrth='" + dateofBitrth + '\'' +
                ", emailId='" + emailId + '\'' +
                ", cellPhoneNo='" + cellPhoneNo + '\'' +
                ", personName='" + personName + '\'' +
                ", gender='" + gender + '\'' +
                ", relationId='" + relationId + '\'' +
                '}';
    }
}

