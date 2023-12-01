package com.csform.android.MB360.wellness;

/**
 * this activity is the dashboard activity which will hold the wellness view models
 * and all the navigation components across the app for wellness.
 * this file holds the main activity for wellness
 * <p>- -</p>
 * ps: if we ever add some native functionality for the app we must check if user is logged in or no
 **/

import static com.csform.android.MB360.BuildConfig.AUTH_EMAIL;
import static com.csform.android.MB360.BuildConfig.AUTH_LOGIN_ID;
import static com.csform.android.MB360.BuildConfig.AUTH_LOGIN_TYPE;
import static com.csform.android.MB360.BuildConfig.AUTH_PHONE_NUMBER;
import static com.csform.android.MB360.utilities.AppLocalConstant.verifyLocationPermissions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.csform.android.MB360.utilities.EncryptionPreference;
import com.csform.android.MB360.utilities.LogMyBenefits;

import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.widget.RelativeLayout;

import androidx.lifecycle.Observer;
import androidx.navigation.NavDirections;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.ActivityWellnessDashBoardBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.insurance.servicenames.ServiceNamesViewModel;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.healthcheckup.HealthCheckupFragmentDirections;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.City;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.SummaryResponse;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class WellnessDashBoardActivity extends AppCompatActivity implements LocationListener {

    ActivityWellnessDashBoardBinding binding;
    View view;

    BottomNavigationView bottomNavigationView;
    NavController navController;

    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;
    PackagesViewModel packagesViewModel;
    MaterialToolbar toolbar;
    ServiceNamesViewModel serviceNamesViewModel;
    EncryptionPreference encryptionPreference;


    Boolean LOCATION_SET = false;


    //locations
    private Location currentLocation = null;
    LocationManager locationManager = null;
    Observer<SummaryResponse> observer;

    LayerDrawable icon;


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWellnessDashBoardBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        //encryption
        encryptionPreference = new EncryptionPreference(this);


        toolbar = binding.toolbar.wellnessToolbar;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        verifyLocationPermissions(this);


        if (hasGps) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, this);
        }

        loadSessionViewModel = new ViewModelProvider(this).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(this).get(DashboardWellnessViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(this).get(HomeHealthCareViewModel.class);
        packagesViewModel = new ViewModelProvider(this).get(PackagesViewModel.class);
        serviceNamesViewModel = new ViewModelProvider(this).get(ServiceNamesViewModel.class);

        loadSessions();

        homeHealthCareViewModel.getCitiesAvailable();

        //to set the city for diagnostic center in health checkup
        packagesViewModel.getCitiesLiveDataHC().observe(this, city -> {
            if (city != null) {
                binding.toolbarCity.tvcity.setText(city);
            } else {
                LogMyBenefits.e(LogTags.WELLNESS_DASHBOARD_ACTIVITY, "onDiagnostic: city not found");
            }

        });

        //todo check the user is logged in or no!
        //apparently we don't need it now.
        //setting  up the bottom navigation bar
        bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_wellness);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //to navigate
        /** to say bottom navigation that we are on home page or-else it will select the first option in menu in this case (Support) **/
        navController.navigate(R.id.wellnessHomeFragment);
        //we need to remove a page as we are navigating from something in this case (same page is getting stacked)
        navController.popBackStack();
        
        //Disable bottom navigation click
        enableBottomBar(false);


        binding.fab.setOnClickListener(view -> {
            //cause our home page is the first page
            while (navController.getBackQueue().size() > 2) {
                navController.navigateUp();
            }
        });

        binding.toolbarCity.locationLL.setOnClickListener(view1 -> {
            navController.navigate(R.id.citiesFragment);
        });

        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {

            binding.toolbar.titleTV.setText(navDestination.getLabel());
            binding.toolbar.backToolbar.setVisibility(View.VISIBLE);
            binding.toolbarCity.backToolbar.setVisibility(View.VISIBLE);

            if (navDestination.getId() == R.id.wellnessHomeFragment) {
                LogMyBenefits.d("RESET-SERVICE-NAME", "HOME-FRAGMENT ");
                homeHealthCareViewModel.setService("");
                homeHealthCareViewModel.resetMessageResponse();
                homeHealthCareViewModel.setAppointmentRequest(null);

                enableBottomBar(false);


                resetSelection();
                toolbar = binding.toolbar.wellnessToolbar;
                setSupportActionBar(toolbar);
                binding.toolbar.logo.setVisibility(View.VISIBLE);
                binding.toolbarCity.getRoot().setVisibility(View.GONE);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                binding.toolbar.titleTV.setVisibility(View.GONE);

                binding.toolbar.backToolbar.setVisibility(View.GONE);
                binding.toolbarCity.backToolbar.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                //todo disable the clicking of the Ongoing and history
                //todo disable the clicking of the addMember bottom sheet
                //showBottomBar();

            } else if (navDestination.getId() == R.id.diagnosticFragment) {
                resetSelection();
                enableBottomBar(true);

                toolbar = binding.toolbar.wellnessToolbar;

                binding.toolbarCity.getRoot().setVisibility(View.VISIBLE);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                toolbar = binding.toolbarCity.wellnessToolbar;
                setSupportActionBar(toolbar);
                binding.toolbar.titleTV.setVisibility(View.VISIBLE);

                getSupportActionBar().setDisplayShowTitleEnabled(false);

            } else if (navDestination.getId() == R.id.membersFragment) {
                resetSelection();
                enableBottomBar(true);


                toolbar = binding.toolbar.wellnessToolbar;
                setSupportActionBar(toolbar);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                binding.toolbarCity.getRoot().setVisibility(View.GONE);
                binding.toolbar.logo.setVisibility(View.GONE);
                binding.toolbar.backToolbar.setVisibility(View.VISIBLE);
                binding.toolbar.titleTV.setVisibility(View.VISIBLE);

                binding.toolbar.titleTV.setText("" + homeHealthCareViewModel.getService().substring(0, 1).toUpperCase() + homeHealthCareViewModel.getService().substring(1).toLowerCase());
                getSupportActionBar().setDisplayShowTitleEnabled(false);

            } else {
                resetSelection();

                toolbar = binding.toolbar.wellnessToolbar;
                setSupportActionBar(toolbar);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                binding.toolbarCity.getRoot().setVisibility(View.GONE);
                binding.toolbar.logo.setVisibility(View.GONE);
                binding.toolbar.titleTV.setVisibility(View.VISIBLE);
                binding.toolbar.backToolbar.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayShowTitleEnabled(false);

            }


            if (navDestination.getId() == R.id.ongoingFragment || navDestination.getId() == R.id.historyFragment) {
                enableBottomBar(true);

                for (NavBackStackEntry navBackStackEntry : navController.getBackQueue()) {
                    LogMyBenefits.d("NAVIGATION", "onCreate: " + navBackStackEntry.getDestination().getLabel());

                    if (navController.getCurrentBackStackEntry().getDestination().getLabel() != null) {
                        if (navController.getCurrentBackStackEntry().getDestination().getLabel().toString().equalsIgnoreCase("Ongoing Appointments")) {
                            break;
                        } else {
                            navController.navigateUp();
                            resetSelection();
                        }
                    } else {


                    }
                }


            }


            if (navDestination.getId() == R.id.healthCheckupFragment) {
                enableBottomBar(true);

                addObserverSummary();

            } else {
                removeObserverSummary();
            }

        });

        binding.toolbar.logo.setVisibility(View.VISIBLE);

        //NavigationUI.setupActionBarWithNavController(this, navController);


        //booking response to handle the navigation
        homeHealthCareViewModel.bookingResponseData().observe(this, messageResponse -> {
            if (messageResponse != null) {
                if (messageResponse.getMessage() != null) {
                    if (messageResponse.getMessage().getStatus()) {
                        bottomNavigationView.setSelectedItemId(R.id.ongoingFragment);

                    }
                }
            }
        });


        //summary listener
        observer = summaryResponse -> {
            if (summaryResponse != null) {

                binding.tvitemcount.setText(String.format("%s Items", UtilMethods.PriceFormat(String.valueOf(summaryResponse.getSummary().getSelfSponseredPerson().size()))));
                binding.tvtotalamount.setText("\u20B9 " + UtilMethods.PriceFormat(summaryResponse.getSummary().getYoupay()));


                if (summaryResponse.getSummary() != null) {
                    if (summaryResponse.getSummary().getSelfSponseredPerson().size() == 0 && summaryResponse.getSummary().getCompanySponseredPerson().size() == 0) {

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.coordinator.getLayoutParams();
                        params.setMargins(0, 0, 0, 0);
                        binding.coordinator.setLayoutParams(params);

                        binding.rlcartlayout.setVisibility(View.GONE);

                    } else {

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.coordinator.getLayoutParams();
                        params.setMargins(0, 61, 0, 0);
                        binding.coordinator.setLayoutParams(params);

                        binding.rlcartlayout.setVisibility(View.VISIBLE);
                    }
                } else {

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.coordinator.getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                    binding.coordinator.setLayoutParams(params);

                    binding.rlcartlayout.setVisibility(View.GONE);
                }
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.coordinator.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                binding.coordinator.setLayoutParams(params);

                binding.rlcartlayout.setVisibility(View.GONE);
            }
        };
        packagesViewModel.getSummaryData().observe(this, observer);

        //back navigation
        binding.toolbar.backToolbar.setOnClickListener(v -> {
            navController.navigateUp();
        });

        binding.toolbarCity.backToolbar.setOnClickListener(v -> {
            navController.navigateUp();
        });

        //view cart from bottom
        binding.rlcartlayout.setOnClickListener(view -> {
            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToSummaryFragment();
            navController.navigate(actions);
        });

    }

    private void enableBottomBar(boolean b) {
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            bottomNavigationView.getMenu().getItem(i).setEnabled(b);
            bottomNavigationView.getMenu().getItem(2).setEnabled(true);
        }
    }

    private void addObserverSummary() {
        packagesViewModel.getSummaryData().observe(this, observer);
    }

    private void removeObserverSummary() {
        packagesViewModel.getSummaryData().removeObserver(observer);
        binding.rlcartlayout.setVisibility(View.GONE);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.coordinator.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        binding.coordinator.setLayoutParams(params);


    }


    private void showBottomBar() {
        bottomNavigationView.setVisibility(View.VISIBLE);
        binding.bottomAppBar.setVisibility(View.VISIBLE);
        binding.fab.setVisibility(View.VISIBLE);

    }

    private void hideBottomBar() {
        bottomNavigationView.setVisibility(View.GONE);
        binding.bottomAppBar.setVisibility(View.GONE);
        binding.fab.setVisibility(View.GONE);
    }

    private void loadSessions() {

        LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, "loadSessions: " + getLoginType());
        switch (getLoginType()) {

            case "PHONE_NUMBER":
                //this is the load session with number block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());

                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                }
                break;
            case "EMAIL_ID":
                //this is the load session with email block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithEmail(getEmail());

                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                }
                break;
            case "AUTH_LOGIN_ID":
                //this is the load session with loginId block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithID(getLoginID());

                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithID(getLoginID());
                }

                break;
        }

        loadSessionViewModel.getLoadSessionData().observe(this, loadSessionResponse -> {

            if (loadSessionResponse != null) {


                String EmpIdNo = "";
                String GroupCode = "";
                EmpIdNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
                GroupCode = loadSessionResponse.getGroupInfoData().getGroupcode();
                dashboardWellnessViewModel.getEmployeeWellnessDetails(EmpIdNo, GroupCode);
               // dashboardWellnessViewModel.getEmployeeWellnessDetails("THYNK018", "MAT");
                //call Service Names
                serviceNamesViewModel.getServiceNameData(loadSessionResponse.getGroupInfoData().getGroupchildsrno());
            } else {
                //loadsession is null
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

    private String getPhoneNumber() {


        String phone_number = encryptionPreference.getEncryptedDataString(AUTH_PHONE_NUMBER);

        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    private String getLoginID() {

        String loginId = encryptionPreference.getEncryptedDataString(AUTH_LOGIN_ID);

        if (loginId != null) {
            return loginId;
        } else {
            return null;
        }
    }

    private String getLoginType() {

        String login_type = encryptionPreference.getEncryptedDataString(AUTH_LOGIN_TYPE);
        LogMyBenefits.d("DEBUG", "getLoginType: " + login_type);
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


    public void PaymentNow(String youpay) {

//        final Activity activity = this;

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_P85Qujlx0rs3IX");
        checkout.setImage(R.mipmap.ic_launcher_round);

//        double finalamount = Float.parseFloat(amount)*100;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "MyBenefits");
            options.put("description", "Payment");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://wellness.mybenefits360.com/mybenefits/assets/img/logo-master-small.png");
            options.put("currency", "INR");
            options.put("amount", "" + youpay);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", "");

            options.put("prefill", preFill);


            checkout.open(this, options);

        } catch (Exception e) {
            LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "Error in starting Razorpay Checkout : " + e);
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        if (!LOCATION_SET) {

            final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            currentLocation = location;
            String city = "";
            if (location != null) {

                LogMyBenefits.d("city", "getCity: " + location.toString());

                try {

                    List<Address> latlng = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    city = latlng.get(0).getLocality();
                    LogMyBenefits.d("city", "onSuccess: " + city);


                    String finalCity = city;
                    //we set the city for health checkup and home health care here
                    //if not available then we have to get the city from user (dialogue box)

                    packagesViewModel.setCitiesLiveDataHC(city);
                    homeHealthCareViewModel.getCitiesData().observe(WellnessDashBoardActivity.this, cities -> {
                        List<City> citiesAvailable = cities.getCities();

                        for (City c : citiesAvailable) {
                            if (c.getCity().toLowerCase().equals(finalCity.toLowerCase())) {
                                homeHealthCareViewModel.setCity(c);
                            }
                        }

                    });

                } catch (Exception e) {
                    LOCATION_SET = false;
                    LogMyBenefits.e("city", "ERROR: ", e);
                    e.printStackTrace();


                }
            } else {

            }
            LOCATION_SET = true;

        } else {

        }
    }
}