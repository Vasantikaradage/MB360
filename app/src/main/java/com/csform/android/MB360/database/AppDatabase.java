package com.csform.android.MB360.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.csform.android.MB360.BuildConfig;
import com.csform.android.MB360.database.Dao.ClaimProcedureDao;
import com.csform.android.MB360.database.Dao.ClaimsDao;
import com.csform.android.MB360.database.Dao.CoverageDao;
import com.csform.android.MB360.database.Dao.EnrollmentWindowCountDao;
import com.csform.android.MB360.database.Dao.LoadSessionDao;
import com.csform.android.MB360.database.Dao.MaxMinAgeDao;
import com.csform.android.MB360.database.Dao.ProviderNetworkDao;
import com.csform.android.MB360.database.Dao.MyClaimsDao;
import com.csform.android.MB360.database.Dao.EscalationsDao;
import com.csform.android.MB360.database.Dao.FaqDao;
import com.csform.android.MB360.database.Dao.MyQueryDao;
import com.csform.android.MB360.database.Dao.PolicyFeatureDao;
import com.csform.android.MB360.database.Dao.ProfileDao;
import com.csform.android.MB360.database.Dao.ServiceNamesDao;
import com.csform.android.MB360.database.Dao.UtilitiesDao;
import com.csform.android.MB360.insurance.FAQ.repository.responseclass.FaqResponse;
import com.csform.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;
import com.csform.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;
import com.csform.android.MB360.insurance.claims.repository.responseclass.LoadPersonsIntimationResponse;
import com.csform.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureEmergencyContactResponse;
import com.csform.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureImageResponse;
import com.csform.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureLayoutInstructionInfo;
import com.csform.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureTextResponse;
import com.csform.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimsProcedureLayoutInfoResponse;
import com.csform.android.MB360.insurance.coverages.repository.responseclass.CoverageDetailsResponse;
import com.csform.android.MB360.insurance.coverages.repository.responseclass.CoverageResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MaxMinAgeMainResponse;
import com.csform.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;
import com.csform.android.MB360.insurance.hospitalnetwork.reponseclass.DocumentElementCount;
import com.csform.android.MB360.insurance.hospitalnetwork.reponseclass.Hospitals;
import com.csform.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation;
import com.csform.android.MB360.insurance.myclaims.responseclass.DocumentElement;
import com.csform.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimsDetails;
import com.csform.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponseOffline;
import com.csform.android.MB360.insurance.profile.response.ProfileResponse;
import com.csform.android.MB360.insurance.queries.responseclass.QueryDetails;
import com.csform.android.MB360.insurance.queries.responseclass.QueryResponse;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.insurance.servicenames.responseclass.ServiceNamesResponse;
import com.csform.android.MB360.insurance.utilities.repository.responseclass.UtilitiesResponse;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

@Database(entities = {FaqResponse.class,
        EscalationsResponse.class,
        UtilitiesResponse.class,
        PolicyFeaturesResponseOffline.class,
        ProfileResponse.class,
        QueryResponse.class,
        QueryDetails.class,
        ClaimsResponse.class,
        ClaimsProcedureLayoutInfoResponse.class,
        ClaimProcedureTextResponse.class,
        LoadPersonsIntimationResponse.class,
        ClaimProcedureLayoutInstructionInfo.class,
        ClaimProcedureEmergencyContactResponse.class,
        ClaimProcedureImageResponse.class,
        CoverageResponse.class,
        CoverageDetailsResponse.class,
        DocumentElement.class,
        ClaimsDetails.class,
        Hospitals.class,
        HospitalInformation.class,
        DocumentElementCount.class,
        LoadSessionResponse.class,
        ServiceNamesResponse.class,
        AdminSettingResponse.class,
        MaxMinAgeMainResponse.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "MB360";

    public abstract FaqDao faqDao();

    public abstract EscalationsDao escalationsDao();

    public abstract UtilitiesDao utilitiesDao();

    public abstract PolicyFeatureDao policyFeatureDao();

    public abstract ProfileDao profileDao();

    public abstract MyQueryDao myQueryDao();

    public abstract ClaimsDao claimsDao();

    public abstract ClaimProcedureDao claimProcedureLayoutDao();

    public  abstract CoverageDao coverageDao();

    public  abstract MyClaimsDao documentElementDao();

    public  abstract ProviderNetworkDao documentElementCountDao();

    public  abstract LoadSessionDao loadSessionDao();

    public  abstract ServiceNamesDao serviceNameDao();

    public  abstract EnrollmentWindowCountDao enrollmentWindowCountDao();
    public  abstract MaxMinAgeDao maxMinAgeDao();

    private static volatile AppDatabase INSTANCE;


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    //encryption
                    SupportFactory supportFactory = new SupportFactory(SQLiteDatabase.getBytes(BuildConfig.DATABASE_PASS_PHRASE.toCharArray()));

                    INSTANCE = Room.databaseBuilder(context, AppDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            /*.openHelperFactory(supportFactory)*/
                            .build();


                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void clearAllTables() {

    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(@NonNull DatabaseConfiguration databaseConfiguration) {
        return null;
    }
}
