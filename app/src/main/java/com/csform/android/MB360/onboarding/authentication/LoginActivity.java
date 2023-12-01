package com.csform.android.MB360.onboarding.authentication;

import static com.csform.android.MB360.BuildConfig.AUTH_EMAIL;
import static com.csform.android.MB360.BuildConfig.AUTH_LOGIN_ID;
import static com.csform.android.MB360.BuildConfig.AUTH_LOGIN_TYPE;
import static com.csform.android.MB360.BuildConfig.AUTH_PHONE_NUMBER;
import static com.csform.android.MB360.BuildConfig.BEARER_TOKEN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.csform.android.MB360.BuildConfig;
import com.csform.android.MB360.IIsolatedService;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.ActivityLoginBinding;
import com.csform.android.MB360.insurance.DashBoardActivity;

import com.csform.android.MB360.magisk.IsolatedService;
import com.csform.android.MB360.networkmanager.NetworkStateManager;
import com.csform.android.MB360.onboarding.authentication.repository.LoginViewModel;
import com.csform.android.MB360.onboarding.authentication.responseclass.LoginEmailRequest;
import com.csform.android.MB360.onboarding.authentication.responseclass.LoginIdRequest;
import com.csform.android.MB360.onboarding.authentication.responseclass.LoginRequest;
import com.csform.android.MB360.onboarding.validation.Otp_Activity;
import com.csform.android.MB360.utilities.AppServerConstants;
import com.csform.android.MB360.utilities.EncryptionPreference;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.Regex;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.system.Os;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    /**
     * we are using view-binding from this activity
     **/
    ActivityLoginBinding binding;
    View view;
    private IIsolatedService serviceBinder;
    private boolean bServiceBound;
    private static final String TAG = "DetectMagisk";


    /**
     * view-model for login
     **/
    LoginViewModel loginViewModel;

    String LOGIN_TYPE = AUTH_PHONE_NUMBER;
    String phone_number = "";
    String email = "";
    private boolean SHOW_PASSWORD = false;
    private boolean LOGIN_ERROR = false;

    Boolean isConnected = false;

    EncryptionPreference encryptionPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //assigning view binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        //encryption preference
        encryptionPreference = new EncryptionPreference(this);


        //to forcefully enable the mobile number layout
        binding.numberRadio.setChecked(true);
        binding.phoneNumberLayout.setVisibility(View.VISIBLE);
        binding.errorTextView.setVisibility(View.GONE);

        binding.errorTextView.setText("");


        NetworkStateManager.getInstance().getNetworkConnectivityStatus().observe(this, isConnected -> {
            this.isConnected = isConnected;
        });

        Intent intent = new Intent(getApplicationContext(), IsolatedService.class);
        /*Binding to an isolated service */
        getApplicationContext().bindService(intent, mIsolatedServiceConnection, BIND_AUTO_CREATE);

/*
        //testing viewpager-credential tabs
        binding.credentialTabs.viewPagerTabs.addTab(binding.credentialTabs.viewPagerTabs.newTab().setText("Mobile Number"));
        binding.credentialTabs.viewPagerTabs.addTab(binding.credentialTabs.viewPagerTabs.newTab().setText("Official E-mail id"));
        binding.credentialTabs.viewPagerTabs.addTab(binding.credentialTabs.viewPagerTabs.newTab().setText("Web Credentials"));*/


        //password type input visibility
        binding.passwordVisible.setOnClickListener(v -> {
            SHOW_PASSWORD = !SHOW_PASSWORD;
            if (SHOW_PASSWORD) {
                //hide password

                binding.webCredPasswordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.passwordVisible.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_off));
                binding.webCredPasswordText.setSelection(binding.webCredPasswordText.getText().length());
            } else {
                //show password

                binding.webCredPasswordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.passwordVisible.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visible));
                binding.webCredPasswordText.setSelection(binding.webCredPasswordText.getText().length());
            }


        });


        //to show the correct layout
        binding.numberRadio.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                LOGIN_TYPE = AUTH_PHONE_NUMBER;
                binding.phoneNumberEditText.requestFocus();
                binding.phoneNumberLayout.setVisibility(View.VISIBLE);
                binding.emailLayout.setVisibility(View.GONE);
                binding.webCredLayout.setVisibility(View.GONE);
                binding.errorTextView.setVisibility(View.GONE);
                binding.errorTextView.setText("");
                binding.emailEditText.setText("");
                binding.webCredGroupNameText.setText("");
                binding.webCredPasswordText.setText("");
                binding.webCredUserNameText.setText("");
            } else {
                binding.phoneNumberLayout.setVisibility(View.GONE);
            }
        });
        binding.emailRadio.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                LOGIN_TYPE = AUTH_EMAIL;
                binding.emailEditText.requestFocus();
                binding.emailLayout.setVisibility(View.VISIBLE);
                binding.phoneNumberLayout.setVisibility(View.GONE);
                binding.webCredLayout.setVisibility(View.GONE);
                binding.errorTextView.setVisibility(View.GONE);
                binding.errorTextView.setText("");


                binding.phoneNumberEditText.setText("");
                binding.webCredGroupNameText.setText("");
                binding.webCredPasswordText.setText("");
                binding.webCredUserNameText.setText("");

            } else {
                binding.emailLayout.setVisibility(View.GONE);
            }
        });
        binding.webCredRadio.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                LOGIN_TYPE = AUTH_LOGIN_ID;
                binding.webCredGroupNameText.requestFocus();
                binding.webCredLayout.setVisibility(View.VISIBLE);
                binding.phoneNumberLayout.setVisibility(View.GONE);
                binding.emailLayout.setVisibility(View.GONE);
                binding.errorTextView.setVisibility(View.GONE);
                binding.errorTextView.setText("");


                binding.emailEditText.setText("");
                binding.phoneNumberEditText.setText("");
            } else {
                binding.webCredLayout.setVisibility(View.GONE);
            }
        });

        //if user comes back from otp activity
        if (binding.emailRadio.isChecked()) {
            LOGIN_TYPE = AUTH_EMAIL;
            binding.emailLayout.setVisibility(View.VISIBLE);
            binding.phoneNumberLayout.setVisibility(View.GONE);
            binding.webCredLayout.setVisibility(View.GONE);
        } else if (binding.numberRadio.isChecked()) {
            LOGIN_TYPE = AUTH_PHONE_NUMBER;
            binding.phoneNumberLayout.setVisibility(View.VISIBLE);
            binding.emailLayout.setVisibility(View.GONE);
            binding.webCredLayout.setVisibility(View.GONE);
        } else if (binding.webCredRadio.isChecked()) {
            LOGIN_TYPE = AUTH_LOGIN_ID;
            binding.phoneNumberLayout.setVisibility(View.GONE);
            binding.emailLayout.setVisibility(View.GONE);
            binding.webCredLayout.setVisibility(View.VISIBLE);
        }


        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //in-case we have to edit the number from otp activity
        //binding.phoneNumberEditText.setText(getPhoneNumber());


        binding.next.setOnClickListener(view -> {
            //check magisk
            checkMagisk();

            hideKeyboard(this.view);
            if (isConnected) {
                showLoading();
                LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, "Login Process Started!");
                login();
            } else {
                loginError("Check your internet connection!");
            }


        });


        loginViewModel.getLoginResponse().observe(this, loginResponse -> {
            if (loginResponse != null && loginResponse.getStatus().equals(BuildConfig.SUCCESS)) {

                switch (loginResponse.getOTPStatusInformation()) {
                    /* -- Case 3 : Valid mobile number -- */
                    case "3":
                        LogMyBenefits.d("LOGIN-ACTIVITY", "OTP SENT");
                        Intent otpIntent = new Intent(LoginActivity.this, Otp_Activity.class);
                        if (LOGIN_TYPE.equals(AUTH_EMAIL)) {
                            otpIntent.putExtra("LOGIN_TYPE", LOGIN_TYPE);
                            otpIntent.putExtra("EMAIL_ID", email);
                        } else {
                            otpIntent.putExtra("LOGIN_TYPE", LOGIN_TYPE);
                            otpIntent.putExtra("PHONE_NUMBER", phone_number);
                            loginViewModel.resetLoginResponse();
                        }
                        startActivity(otpIntent);

                        break;
                    /* -- Case 2 : Mobile number does not exists -- **/
                    case "2":

                        if (LOGIN_TYPE.equals(AUTH_EMAIL)) {
                            LogMyBenefits.d("LOGIN-ACTIVITY", "Official E-mail does not exist");
                            loginError("Official E-mail does not exist");
                        } else {
                            LogMyBenefits.d("LOGIN-ACTIVITY", "Mobile number does not exist");
                            loginError("Mobile number does not exist");
                        }
                        loginViewModel.resetLoginResponse();

                        break;
                    /* -- Case 1 : Multiple Mobile number exists -- **/
                    case "1":

                        if (LOGIN_TYPE.equals(AUTH_EMAIL)) {
                            LogMyBenefits.d("LOGIN-ACTIVITY", "Multiple Official E-mail Id exists");
                            loginError("Multiple Official E-mail Id exists");

                        } else {
                            LogMyBenefits.d("LOGIN-ACTIVITY", "Multiple Mobile number exists");
                            loginError("Multiple Mobile number exists");


                        }
                        loginViewModel.resetLoginResponse();
                        break;
                }
            }
        });

        //Login with login id
        loginViewModel.getLoginIDResponse().observe(this, loginIDResponse -> {

            if (loginIDResponse != null && loginIDResponse.getMessage().equals(BuildConfig.SUCCESS) && loginIDResponse.getStatus().equals("1")) {


                // Store null values to SharedPreferences for logging out a user
                encryptionPreference.setEncryptedDataString(AUTH_LOGIN_ID, loginIDResponse.getUniqueID());
                encryptionPreference.setEncryptedDataString(AUTH_LOGIN_TYPE, AUTH_LOGIN_ID);
                //store auth token
                encryptionPreference.setEncryptedDataString(BEARER_TOKEN, loginIDResponse.getAuthToken());


                //Login with Login Id must be redirected to Dashboard
                Intent otpIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
                otpIntent.putExtra("LOGIN_TYPE", AUTH_LOGIN_ID);
                otpIntent.putExtra(AUTH_LOGIN_ID, loginIDResponse.getUniqueID());
                startActivity(otpIntent);
                finish();
                binding.errorTextView.setVisibility(View.GONE);
            } else {
                if (loginIDResponse != null) {
                    binding.errorTextView.setVisibility(View.VISIBLE);
                    binding.errorTextView.setText(loginIDResponse.getMessage());
                } else {
                    binding.errorTextView.setVisibility(View.VISIBLE);
                    binding.errorTextView.setText("something went wrong");
                }
            }
        });


        //for loading
        loginViewModel.getLoadingState().observe(this, loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });


        //textwatchers
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.errorTextView.getText().length() > 0) {
                    binding.errorTextView.setVisibility(View.GONE);
                    binding.errorTextView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher phoneTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.errorTextView.getText().length() > 0) {
                    binding.errorTextView.setVisibility(View.GONE);
                    binding.errorTextView.setText("");
                }
                if (s.toString().trim().length() == 10) {
                    hideKeyboard(binding.phoneNumberEditText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        binding.phoneNumberEditText.addTextChangedListener(phoneTextWatcher);
        binding.emailEditText.addTextChangedListener(textWatcher);
        binding.webCredGroupNameText.addTextChangedListener(textWatcher);
        binding.webCredUserNameText.addTextChangedListener(textWatcher);
        binding.webCredPasswordText.addTextChangedListener(textWatcher);


    }

    private void checkMagisk() {
        if (bServiceBound) {
            boolean bIsMagisk = false;
            try {
                LogMyBenefits.d("MagiskService", "UID:" + Os.getuid());
                bIsMagisk = serviceBinder.isMagiskPresent();
                if (bIsMagisk) {
                    Toast.makeText(getApplicationContext(), "Magisk Found", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                } else {
                    // Toast.makeText(getApplicationContext(), "Magisk Not Found", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof RuntimeException) {
                    throw new RuntimeException();
                }
            }
        } else {
            //Toast.makeText(getApplicationContext(), "Isolated service not bound", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void login() {
        //todo check a regex for phone number and email if email provided
        //todo the post api call from the next button.
        //todo handle Internet case
        showLoading();
        switch (LOGIN_TYPE) {
            case "AUTH_EMAIL":
                if (!binding.emailEditText.getText().toString().trim().isEmpty() && Regex.EMAIL_PATTERN.matcher(binding.emailEditText.getText().toString().trim()).matches()) {
                    email = binding.emailEditText.getText().toString().trim();
                    //same with Login ID
                    /** this is observer for the login state for email login **/
                    LoginEmailRequest loginEmailRequest = new LoginEmailRequest();
                    loginEmailRequest.setOfficialemailId(email);
                    loginViewModel.loginWithEmail(loginEmailRequest);
                    binding.errorTextView.setVisibility(View.GONE);

                } else {
                    hideLoading();
                    binding.errorTextView.setVisibility(View.VISIBLE);
                    binding.errorTextView.setText("Please enter your valid email-id");

                }
                break;

            case "AUTH_PHONE_NUMBER":
                if (!binding.phoneNumberEditText.getText().toString().trim().equals("") && Regex.MOBILE_NUMBER_PATTERN.matcher(binding.phoneNumberEditText.getText().toString().trim()).matches()) {
                    phone_number = binding.phoneNumberEditText.getText().toString().trim();
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setMobileno(Long.parseLong(phone_number));

                    /** observer for the login state for mobile login **/
                    loginViewModel.loginWithMobileNumber(loginRequest);
                    binding.errorTextView.setVisibility(View.GONE);

                } else {
                    hideLoading();
                    binding.errorTextView.setVisibility(View.VISIBLE);
                    binding.errorTextView.setText("Please enter your valid mobile number");
                }
                break;

            case "AUTH_LOGIN_ID":
                if (!binding.webCredGroupNameText.getText().toString().trim().equals("") && !binding.webCredUserNameText.getText().toString().trim().equals("") && !binding.webCredPasswordText.getText().toString().trim().equals("") /* TODO  && matches REGEX OF EMAIL */) {
                    phone_number = binding.phoneNumberEditText.getText().toString().trim();
                    LoginIdRequest loginRequest = new LoginIdRequest();
                    loginRequest.setGroupCode(binding.webCredGroupNameText.getText().toString().trim().toUpperCase());
                    loginRequest.setPassword(binding.webCredPasswordText.getText().toString().trim());
                    loginRequest.setUserName(binding.webCredUserNameText.getText().toString().trim().toUpperCase());

                    /** observer for the login state for mobile login **/
                    loginViewModel.loginWithId(loginRequest);
                    binding.errorTextView.setVisibility(View.GONE);

                } else {
                    hideLoading();
                    if (binding.webCredGroupNameText.getText().toString().trim().equals("")) {
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        binding.errorTextView.setText("Please enter the group name");
                    } else if (binding.webCredUserNameText.getText().toString().trim().equals("")) {
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        binding.errorTextView.setText("Please enter the username");
                    } else if (binding.webCredPasswordText.getText().toString().trim().equals("")) {
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        binding.errorTextView.setText("Please enter the password");
                    }

                }
                break;
        }

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


    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.next.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.next.setVisibility(View.VISIBLE);
    }

    public void loginError(String error) {
        /*Mobile no does not exists in database or Wrong Format mobile no*/
        final Dialog alertDialog = new Dialog(this);
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nhborder));

        // et_mobno.setError();
        LayoutInflater mLayoutInflater = getLayoutInflater();
        View alertLayout = mLayoutInflater.inflate(R.layout.dialog_internet_error, null);
        alertDialog.setContentView(alertLayout);
        ImageView alertIcon = alertLayout.findViewById(R.id.alertIcon);
        alertIcon.setImageResource(R.drawable.ic_popupal);
        Button btnDismiss = alertDialog.findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.setOnDismissListener(dialogInterface -> {
            LOGIN_ERROR = false;
        });

        TextView alertMessage = alertDialog.findViewById(R.id.tvAlertMessage);
        alertMessage.setText(error);
        alertDialog.show();
    }

    private final ServiceConnection mIsolatedServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceBinder = IIsolatedService.Stub.asInterface(iBinder);
            bServiceBound = true;
            LogMyBenefits.d("Service", "Service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bServiceBound = false;
            LogMyBenefits.d("Service", "Service Unbound");
        }
    };

}