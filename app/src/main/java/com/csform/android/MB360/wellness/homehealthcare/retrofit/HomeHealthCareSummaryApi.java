package com.csform.android.MB360.wellness.homehealthcare.retrofit;

import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseDM;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseDS;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseNA;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseNC;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponsePT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseST;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.SummaryResponse;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseEc;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface HomeHealthCareSummaryApi {

    @GET("HomeHealthCare/GetSummary")
    Call<SummaryResponseNA> getTrainedAttendantSummary(@Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryLT")
    Call<SummaryResponseLT> getLongTermSummary(@Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryDS")
    Call<SummaryResponseDS> getDoctorServicesSummary(@Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryPT")
    Call<SummaryResponsePT> getPhysioTherapySummary(@Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryST")
    Call<SummaryResponseST> getShortTermSummary(@Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryNC")
    Call<SummaryResponseNC> getPostNatalSummary(@Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryDM")
    Call<SummaryResponseDM> getDiabetesManagmentSummary(@Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryEC")
    Call<SummaryResponseEc> getElderCareSummary(@Query("FamilySrNo") String strFamilySrNo);


}
