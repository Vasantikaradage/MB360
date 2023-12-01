package com.csform.android.MB360.fitness.response;

public class FitnessEmpGroupInfoResponse {
    private  String EXT_EMPLOYEE_SR_NO;
    private  String OFFICIAL_E_MAIL_ID;

    public String getOFFICIAL_E_MAIL_ID() {
        return OFFICIAL_E_MAIL_ID;
    }

    public void setOFFICIAL_E_MAIL_ID(String OFFICIAL_E_MAIL_ID) {
        this.OFFICIAL_E_MAIL_ID = OFFICIAL_E_MAIL_ID;
    }

    public String getEXT_EMPLOYEE_SR_NO() {
        return EXT_EMPLOYEE_SR_NO;
    }

    public void setEXT_EMPLOYEE_SR_NO(String EXT_EMPLOYEE_SR_NO) {
        this.EXT_EMPLOYEE_SR_NO = EXT_EMPLOYEE_SR_NO;
    }

    @Override
    public String toString() {
        return "FitnessEmpGroupInfoResponse{" +
                "EXT_EMPLOYEE_SR_NO='" + EXT_EMPLOYEE_SR_NO + '\'' +
                ", OFFICIAL_E_MAIL_ID='" + OFFICIAL_E_MAIL_ID + '\'' +
                '}';
    }
}
