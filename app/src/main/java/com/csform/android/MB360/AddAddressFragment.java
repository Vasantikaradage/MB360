package com.csform.android.MB360;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.csform.android.MB360.databinding.FragmentAddAddressBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.fragment.MembersFragmentDirections;
import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.Address;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;
import com.csform.android.MB360.wellness.homehealthcare.ui.AddAddressListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AddAddressFragment extends BottomSheetDialogFragment {

    FragmentAddAddressBinding binding;
    View view;
    Address address = new Address();
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;

    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<String> stateList = new ArrayList<>();
    NavController navController;
    FamilyMember member;
    BottomSheetBehavior bottomSheetBehavior;

    public AddAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddAddressBinding.inflate(inflater, container, false);
        view = binding.getRoot();




        member = AddAddressFragmentArgs.fromBundle(getArguments()).getGetMember();
        address.setStrPersonSrno(AddAddressFragmentArgs.fromBundle(getArguments()).getGetMember().getExtPersonSrNo());


        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        bottomSheetBehavior.setDraggable(true);

        ViewGroup.LayoutParams layoutParams = binding.bottomSheet.getLayoutParams();


        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        binding.bottomSheet.setLayoutParams(layoutParams);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);

        //set wellness serial number
        address.setWellSrno(homeHealthCareViewModel.getWellnessSrNo());

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        setupStatesAndCities();


        ArrayAdapter adState = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.tvspinneritem, stateList);
        ArrayAdapter adCity = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.tvspinneritem, cityList);

        adState.setDropDownViewResource(R.layout.spinner_item);
        adCity.setDropDownViewResource(R.layout.spinner_item);

        binding.spinnerCity.setAdapter(adCity);
        binding.spinnerState.setAdapter(adState);

        Window window = getActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(view.getContext(), R.color.greenlightbg2));

        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCity = adapterView.getItemAtPosition(i).toString();
                address.setStrCity(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                address.setStrCity(null);
            }
        });

        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                Context context = adapterView.getContext();
                CharSequence text = selected;
                address.setStrState(selected);
                addPreferedCities();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                address.setStrState(null);
            }
        });
        try {
            String html = " I agree to <a href=\"https://portal.mybenefits360.com/termsOfUse.html\">Terms of Use</a>";
            Spanned result;
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);

            Spannable s = (Spannable) result;
            for (URLSpan u : s.getSpans(0, s.length(), URLSpan.class)) {
                s.setSpan(new UnderlineSpan() {
                    public void updateDrawState(TextPaint tp) {
                        tp.setUnderlineText(false);
                    }
                }, s.getSpanStart(u), s.getSpanEnd(u), 0);
            }

            binding.chkAgreeTerms.setText(s);
            binding.chkAgreeTerms.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.ivClose.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        binding.btnsaveupdateaddress.setOnClickListener(v -> {

            try {
                if (binding.chkAgreeTerms.isChecked()) {
                    address.setStrMobileNumber(binding.etmobileno.getText().toString().trim());
                    address.setStrLine1(binding.etflatno.getText().toString().trim());
                    address.setStrLine2(binding.etlocation.getText().toString().trim());
                    address.setStrLandmark(binding.etlandmark.getText().toString().trim());
                    address.setStrPincode(binding.etpincode.getText().toString().trim());
                    address.setStrEmail(binding.etemailid.getText().toString());

                    saveAddress();
                } else {
                    Toast.makeText(requireActivity(), "Please Accept Terms and conditions.", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

            }

        });


        binding.etmobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    binding.etmobilenoError.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etemailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    binding.etemailidError.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etflatno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    binding.etflatnoError.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etlocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    binding.etArea.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etlandmark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    binding.etlandmark.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etpincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    binding.etpincodeError.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;

    }

    private void saveAddress() {
        if (!addressIsNotValid()) {
            //post call here for address
            homeHealthCareViewModel.addAddress(address).observe(getViewLifecycleOwner(),messageResponse -> {
                if(messageResponse!=null)
                {
                    navController.navigateUp();
                }
            });

           /* homeHealthCareViewModel.addAddressData().observe(getViewLifecycleOwner(), addressResponse -> {

                Toast.makeText(getContext(), addressResponse.getMessage().getMessage(), Toast.LENGTH_SHORT).show();

                NavDirections actions = AddAddressFragmentDirections.actionAddAddressFragmentToTrainedAttendedFragment(member);
                navController.navigate(actions);
            });
*/
        } else {
            Toast.makeText(getContext(), address.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean addressIsNotValid() {
        boolean isError = false;
        try {
            if (TextUtils.isEmpty(address.getStrMobileNumber()) ||
                    address.getStrMobileNumber().length() <= 9) {
                isError = true;
                binding.etmobilenoError.setError("Invalid Mobile Number");
            } else if (TextUtils.isEmpty(address.getStrEmail()) ||
                    !Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(address.getStrEmail())).matches()) {
                isError = true;
                binding.etemailidError.setError("Invalid E-Mail ID");
            } else if (TextUtils.isEmpty(address.getStrLine1())) {
                isError = true;
                binding.etflatnoError.setError("Enter Flat No.");
            } else if (TextUtils.isEmpty(address.getStrLine2())) {
                isError = true;
                binding.etArea.setError("Enter Area/Locality");
            } else if (TextUtils.isEmpty(address.getStrLandmark())) {
                isError = true;
                binding.etlandmarkError.setError("Enter Landmark");
            } else if (TextUtils.isEmpty(address.getStrPincode()) ||
                    address.getStrPincode().length() < 5) {
                isError = true;
                binding.etpincodeError.setError("Enter Pin-code");
            } else if (TextUtils.isEmpty(address.getStrState())) {
                isError = true;
                Toast.makeText(getContext(), "Enter state", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(address.getStrCity())) {
                isError = true;
                Toast.makeText(getContext(), "Enter city", Toast.LENGTH_SHORT).show();
            } else if (!binding.chkAgreeTerms.isChecked()) {
                isError = true;
                binding.chkAgreeTerms.setError("Please accept the terms and conditions");
            } else {
                binding.etmobileno.setError(null);
                binding.etemailid.setError(null);
                binding.etflatno.setError(null);
                binding.etArea.setError(null);
                binding.etlandmarkError.setError(null);
                binding.etpincode.setError(null);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return isError;
    }

    public void setupStatesAndCities() {
        try {
            JSONArray json = new JSONArray(loadJSONFromAsset());


            for (int i = 0; i < json.length(); i++) {
                JSONObject e = json.getJSONObject(i);
                //cityList.add(e.getString("city"));
                stateList.add(e.getString("state").trim());

            }


            Set<String> set = new HashSet<>(stateList);
            stateList.clear();
            stateList.addAll(set);
            Collections.sort(stateList);
            ArrayAdapter adState = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.tvspinneritem, stateList);
            binding.spinnerState.setAdapter(adState);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addPreferedCities() {
        try {
            cityList.clear();
            JSONArray json = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < json.length(); i++) {
                JSONObject e = json.getJSONObject(i);
                if (e.getString("state").equals(address.getStrState())) {
                    cityList.add(e.getString("city"));
                }
            }
            Collections.sort(cityList);
            ArrayAdapter adCity = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.tvspinneritem, cityList);
            adCity.setDropDownViewResource(R.layout.spinner_item);
            binding.spinnerCity.setAdapter(adCity);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("StateCity.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;

    }


    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }


}