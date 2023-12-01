package com.csform.android.MB360.fitness;



import static com.csform.android.MB360.BuildConfig.AUTH_EMAIL;
import static com.csform.android.MB360.BuildConfig.AUTH_LOGIN_ID;
import static com.csform.android.MB360.BuildConfig.AUTH_LOGIN_TYPE;
import static com.csform.android.MB360.BuildConfig.AUTH_PHONE_NUMBER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.aktivolabs.aktivocore.data.repositories.LocalRepository;
import com.aktivolabs.aktivocore.managers.AktivoManager;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.ActivityFitnessDashboardBinding;
import com.csform.android.MB360.fitness.AktivoData.AktivoAuthPerm;
import com.csform.android.MB360.fitness.AktivoData.AktivoMBProfile;
import com.csform.android.MB360.fitness.AktivoData.AktivoMethods;
import com.csform.android.MB360.fitness.request.FitnessEmpGroupInfoRequest;
import com.csform.android.MB360.fitness.retrofit.FitnessApi;
import com.csform.android.MB360.fitness.retrofit.FitnessRetrofitClient;
import com.csform.android.MB360.fitness.viewmodel.FitnessViewModel;
import com.csform.android.MB360.insurance.profile.repository.ProfileViewModel;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.utilities.AppLocalConstant;
import com.csform.android.MB360.utilities.EncryptionPreference;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class FitnessDashBoardActivity extends AppCompatActivity implements AktivoMethods {
    ActivityFitnessDashboardBinding fitnessDashboardBinding;
    View view;
    BottomNavigationView bottomNavigationView;
    NavController navController;
    LoadSessionViewModel loadSessionViewModel;
    FitnessViewModel fitnessViewModel;
    ProfileViewModel profileViewModel;
    private AktivoManager mAktivoManager;
    private AktivoAuthPerm aktivoAuthPerm;
    private SharedPreferences sharedpreferences;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    EncryptionPreference encryptionPreference;

    FitnessEmpGroupInfoRequest fitnessEmpGroupInfoRequest = new FitnessEmpGroupInfoRequest();
    MaterialToolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fitnessDashboardBinding = ActivityFitnessDashboardBinding.inflate(getLayoutInflater());
        view = fitnessDashboardBinding.getRoot();
        setContentView(view);
        Window window = this.getWindow();

        //toolbar
        toolbar = fitnessDashboardBinding.toolbar.fitnessToolbar;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);


        loadSessionViewModel = new ViewModelProvider(this).get(LoadSessionViewModel.class);
        fitnessViewModel = new ViewModelProvider(this).get(FitnessViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        encryptionPreference = new EncryptionPreference(this);


        // status-bar colour change
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.schedulecolor));

        //has menu options
        MenuProvider menuProvider = new MenuProvider() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
                if (menu instanceof MenuBuilder) {
                    ((MenuBuilder) menu).setOptionalIconsVisible(true);
                }
                inflater.inflate(R.menu.header_menu, menu);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                //item on click
                switch (menuItem.getItemId()) {
                    case R.id.connect_app_fragment:
                        navController.navigate(R.id.connect_app_fragment);
                        break;
                    case R.id.improve_aktivo_score:

                        break;
                    case R.id.help_fragment:
                        navController.navigate(R.id.help_fragment);

                        break;
                    case R.id.privacy_policy_fragment:
                        navController.navigate(R.id.privacy_policy_fragment);
                        break;
                }
                return true;


            }
        };
        addMenuProvider(menuProvider, this);

        //initializing aktivo manager
        initializeAktivoManager();

        //calling sync state is necessary or else your hamburger icon wont show up
        sharedpreferences = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_fitness);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        //to navigate
        /** to say bottom navigation that we are on home page or-else it will select the first option in menu in this case (compete) **/
        navController.navigate(R.id.fitnessHomeFragment);
        //we need to remove a page as we are navigating from something in this case (same page is getting stacked)
        navController.popBackStack();

        NavOptions navOptions = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(NavGraph.findStartDestination(navController.getGraph()).getId(),
                        false, // inclusive
                        true) // saveState
                .build();


        loadSessions();
        generateMemberId();
        fitnessDashboardBinding.fab.setOnClickListener(view -> {
            //cause our home page is the first page
           /* while (navController.getBackQueue().size() > 2) {
                navController.navigateUp();
            }*/

            navController.navigate(R.id.fitnessHomeFragment, null, navOptions);

        });

        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
           /* if (navDestination.getId() == R.id.fitnessHomeFragment) {

                resetSelection();
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                fitnessDashboardBinding.toolbar.logo.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                fitnessDashboardBinding.toolbar.getRoot().setVisibility(View.VISIBLE);
                showBottomBar();


            } else if (navDestination.getId() == R.id.fitnessRewardsFragment || navDestination.getId() == R.id.fitnessStatsFragment || navDestination.getId() == R.id.fitnessProfileFragment) {

                fitnessDashboardBinding.toolbar.logo.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                showBottomBar();
            }
*/

        });
        NavigationUI.setupActionBarWithNavController(this, navController);

        //listen for permission
        fitnessViewModel.requestGoogleSignIn().observe(this, googleSignin -> {
            if (googleSignin) {
                AppLocalConstant.requestGoogleFitness(this, this);
            }
        });

    }

    private void resetSelection() {
        bottomNavigationView.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0, size = bottomNavigationView.getMenu().size(); i < size; i++) {
            bottomNavigationView.getMenu().getItem(i).setChecked(false);
            bottomNavigationView.getMenu().getItem(i).setVisible(true);
        }
        bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
    }

    private void hideBottomBar() {
        fitnessDashboardBinding.bottomNavHolder.setVisibility(View.GONE);
    }


    private void showBottomBar() {
        fitnessDashboardBinding.bottomNavHolder.setVisibility(View.VISIBLE);
    }


    @Override
    public void authenticated() {

        LocalRepository localRepository = new LocalRepository(this);
        localRepository.putNotificationTitle(getString(R.string.app_name));
        localRepository.putNotificationForSmallIcon(R.drawable.add_white);
        fitnessDashboardBinding.fab.performClick();
        updateData();
        hideLoading();
    }

    private void hideLoading() {
        fitnessDashboardBinding.progressLayout.setVisibility(View.GONE);
    }

    private void showLoading() {
        fitnessDashboardBinding.progressLayout.setVisibility(View.VISIBLE);
    }

    public void updateData() {
        mAktivoManager
                .isPermissionGranted()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers
                        .mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull Boolean value) {
                        if (value) {
                            syncData();
                        } else {
                            gotPermissions();

                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void syncData() {
        Log.d("MB360", "Syncing Data from sdk");
        Toast.makeText(this, "Syncing Data", Toast.LENGTH_SHORT).show();
        mAktivoManager.syncFitnessData().subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> Toast.makeText(FitnessDashBoardActivity.this, "Error" + e.toString(),
                        Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void gotPermissions() {
        Log.d(LogTags.FITNESS_ACTIVITY, "Permission request");

        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_ACTIVITY_SEGMENT,
                                FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_HEART_RATE_BPM,
                                FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA,
                                FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA,
                                FitnessOptions.ACCESS_READ)
                        .build();

        GoogleSignIn.requestPermissions(
                this, // your activity
                123,
                GoogleSignIn.getLastSignedInAccount(getApplicationContext()),
                fitnessOptions);

        hideLoading();
    }


    @Override
    public void getToken(String token) {

        //  String _id = sharedpreferences.getString("_id", null);
        String _id = "63afd1d89ea2a604a96123d2";
        //  String Id = sharedpreferences.getString("Employee_SR_NO", null);
        String Id = "-1";
        if (_id == null || _id.equalsIgnoreCase("-1")) {
            getAktivoID(Id);
        } else aktivoAuthPerm.authenticateUser(_id);
    }

    @Override
    public void authenticationFailed() {
        hideLoading();
        Toast.makeText(this, "Authentication Issue", Toast.LENGTH_SHORT).show();

        //todo on authentication failed Layout
    }

    private void generateMemberId() {
        //  String id = sharedpreferences.getString("_id", null);
        String id = "63afd1d89ea2a604a96123d2";
        if (id == null) {
            aktivoAuthPerm.getAccessToken("semantic");
        } else
            aktivoAuthPerm.getAccessToken(id);
    }


    private void loadSessions() {

        Log.d(LogTags.LOGIN_ACTIVITY, "loadSessions: " + getLoginType());
        switch (getLoginType()) {

            case "PHONE_NUMBER":
                //this is the load session with number block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    Log.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                }
                break;
            case "EMAIL_ID":
                //this is the load session with email block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    Log.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithEmail(getEmail());
                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithEmail(getEmail());
                }
                break;
            case "AUTH_LOGIN_ID":
                //this is the load session with loginId block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    Log.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithID(getLoginID());
                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithID(getLoginID());
                }
                break;
        }


        loadSessionViewModel.getLoadSessionData().observe(this, loadSessionResponse -> {
            String groupChild = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
            GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);
            String oeGrpBasInfoSrNo = groupGMCPolicyEmployeeDatum.getOeGrpBasInfSrNo();
            String employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();

            profileViewModel.getProfile(groupChild, oeGrpBasInfoSrNo, employeeSrNo);

            profileViewModel.getProfileData().observe(this, profileResponse -> {



                if (profileResponse != null) {

                    fitnessEmpGroupInfoRequest.setAge(profileResponse.getUserPersonalDetails().getAge());
                    fitnessEmpGroupInfoRequest.setCellPhoneNo(profileResponse.getUserPersonalDetails().getCellphoneNumber());
                    fitnessEmpGroupInfoRequest.setDateofBitrth(profileResponse.getUserPersonalDetails().getDateOfBirth());
                    fitnessEmpGroupInfoRequest.setDesignation(profileResponse.getUserOfficialDetails().getDesignation());
                    fitnessEmpGroupInfoRequest.setDateOfJoining(profileResponse.getUserOfficialDetails().getDateOfJoining());
                    fitnessEmpGroupInfoRequest.setEmailId(profileResponse.getUserPersonalDetails().getEMailId());
                    fitnessEmpGroupInfoRequest.setGrade(profileResponse.getUserOfficialDetails().getGrade());
                    fitnessEmpGroupInfoRequest.setDepartment(profileResponse.getUserOfficialDetails().getDepartment());
                }


                /*{
                    "GROUP_CODE": "MYBFTS",
                        "GROUP_NAME": "Mybenefits360 Technology Platform Demo",
                        "EMPLOYEE_IDENTIFICATION_NO": "12022",
                        "DATE_OF_JOINING": "15-Apr-2020",
                        "OFFICIAL_E_MAIL_ID": "",
                        "DEPARTMENT": "SEMANTIC",
                        "GRADE": "NOT AVAILABLE",
                        "DESIGNATION": "NOT AVAILABLE",
                        "AGE": "31",
                        "DATE_OF_BIRTH": "31",
                        "CELLPHONE_NUMBER": "7977155209",
                        "PERSON_NAME": "Geeta Lambe",
                        "GENDER": "FEMALE",
                        "RELATIONID": "17"
                }*/


                fitnessEmpGroupInfoRequest.setEmployeeIdentificationNo(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo());
                fitnessEmpGroupInfoRequest.setGroupCode(loadSessionResponse.getGroupInfoData().getGroupcode());
                fitnessEmpGroupInfoRequest.setGroupName(loadSessionResponse.getGroupInfoData().getGroupname());
                fitnessEmpGroupInfoRequest.setRelationId(loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData().get(0).getRelationid());

                /*fitnessEmpGroupInfoRequest.setAge("31");
                fitnessEmpGroupInfoRequest.setCellPhoneNo("7977155209");
                fitnessEmpGroupInfoRequest.setDateofBitrth("31");
                fitnessEmpGroupInfoRequest.setDesignation("NOT AVAILABLE");
                fitnessEmpGroupInfoRequest.setDateOfJoining("15-Apr-2020");
                fitnessEmpGroupInfoRequest.setEmailId("");
                fitnessEmpGroupInfoRequest.setGrade("NOT AVAILABLE");
                fitnessEmpGroupInfoRequest.setDepartment("SEMANTIC");
                fitnessEmpGroupInfoRequest.setEmployeeIdentificationNo("12022");
                fitnessEmpGroupInfoRequest.setGroupCode("MYBFTS");
                fitnessEmpGroupInfoRequest.setGroupName("Mybenefits360 Technology Platform Demo");
                fitnessEmpGroupInfoRequest.setRelationId("17");
                fitnessEmpGroupInfoRequest.setEmployeeIdentificationNo("12022");
                fitnessEmpGroupInfoRequest.setGender("FEMALE");*/



                //data from server
                fitnessViewModel.getFitnessEmpGroupInfo(fitnessEmpGroupInfoRequest).observe(this, fitnessEmpGroupInfoResponse -> {
                    if (fitnessEmpGroupInfoResponse != null) {
                        String empSrNo = fitnessEmpGroupInfoResponse.getEXT_EMPLOYEE_SR_NO();
                        SharedPreferences.Editor edit = sharedpreferences.edit();
                        edit.putString("Employee_SR_NO", empSrNo);
                        //  edit.putString("OFFICIAL_E_MAIL_ID",fitnessEmpGroupInfoResponse.getOFFICIAL_E_MAIL_ID());
                        edit.apply();
                        getAktivoID(sharedpreferences.getString("Employee_SR_NO", null));

                    }

                });
            });


        });


    }

    private void getAktivoID(String strEmpID) {

        try {
            FitnessApi aktivoAPI = FitnessRetrofitClient.getAktivoRetrofit().create(FitnessApi.class);

            Single<AktivoMBProfile> aktivoBeanProfileSingle = aktivoAPI.checkOnBoardEmp("-1");
            compositeDisposable.add(aktivoBeanProfileSingle.subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(result -> result)
                    .subscribeWith(new DisposableSingleObserver<AktivoMBProfile>() {
                        @Override
                        public void onSuccess(AktivoMBProfile aktivoBeanProfile) {
                            if (aktivoBeanProfile.getLstUserPhysicalInfo().size() > 0) {
                                String member_id = aktivoBeanProfile.getLstUserMemberIdInfo().get(0).getStrMemberId();
                                Log.e("Aktivo id", member_id);
                                SharedPreferences.Editor edit = sharedpreferences.edit();

                                edit.putString("_id", member_id);
                                edit.apply();
                                aktivoAuthPerm.authenticateUser(member_id);


                                edit.putString("_id", member_id);
                                edit.putString("height_cm", aktivoBeanProfile.getLstUserPhysicalInfo().get(0).getStrHeight());
                                edit.putString("weight_kg", aktivoBeanProfile.getLstUserPhysicalInfo().get(0).getStrWeight());


                                edit.apply();
                            } else {
                                //  showOnBoard();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("Rx Aktivo id Error", "Errorr 2" + e.toString());

                            //showOnBoard();
                            authenticationFailed();
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }

        hideLoading();
    }

    private String getPhoneNumber() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_PHONE_NUMBER, null);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    private String getLoginID() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_LOGIN_ID, null);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    private String getLoginType() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String login_type = sharedPreferences.getString(AUTH_LOGIN_TYPE, null);
        Log.d("DEBUG", "getLoginType: " + login_type);
        if (login_type != null) {
            return login_type;
        } else {
            return "PHONE_NUMBER";
        }
    }

    private String getEmail() {

        String email = encryptionPreference.getEncryptedDataString(AUTH_EMAIL);
        LogMyBenefits.d("DEBUG", "getLoginType: " + email);
        if (email != null) {
            return email;
        } else {
            return null;
        }
    }

    private void initializeAktivoManager() {

        mAktivoManager = AktivoManager.getInstance(this);
        aktivoAuthPerm = new AktivoAuthPerm(this, this);
        aktivoAuthPerm.getPermissions();
        hideLoading();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //   Toast.makeText(this, "Result Code" + resultCode + "Request code" + requestCode + "Data :" + data, Toast.LENGTH_SHORT).show();
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("Data", "Permission Pass");
                // Toast.makeText(this, "Permission Pass", Toast.LENGTH_SHORT).show();
                syncData();
            } else {
                Log.d("MB360", "Permission fail");
                //                         Toast.makeText(this, "Permission Fail", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 0) {
            Toast.makeText(this, "Please provide permissions to app", Toast.LENGTH_SHORT).show();
            /*Log.d("Data", "Permission fail");
            Toast.makeText(this, "Please provide permissions to app", Toast.LENGTH_SHORT).show();
            Intent i;
            i = new Intent(this, AppLandingPage.class);
            i.putExtra("PolicyCheckingStatus", 1);
            startActivity(i);*/
        } else if (requestCode == -1) {
            //        Toast.makeText(this, "Permission Pass", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}
