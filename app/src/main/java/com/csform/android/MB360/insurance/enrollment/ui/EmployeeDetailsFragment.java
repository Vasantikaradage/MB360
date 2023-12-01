package com.csform.android.MB360.insurance.enrollment.ui;

import static com.csform.android.MB360.BuildConfig.AUTH_EMAIL;
import static com.csform.android.MB360.BuildConfig.AUTH_LOGIN_ID;
import static com.csform.android.MB360.BuildConfig.AUTH_LOGIN_TYPE;
import static com.csform.android.MB360.BuildConfig.AUTH_PHONE_NUMBER;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.CountDownTimer;

import com.bumptech.glide.Glide;
import com.csform.android.MB360.R;
import com.csform.android.MB360.insurance.enrollment.adapters.EmployeeDetailsAdapterNew;
import com.csform.android.MB360.insurance.enrollment.interfaces.EmployeeHelper;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MaxMinAgeResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails.EmployeeFieldDisplaySubResponse;
import com.csform.android.MB360.insurance.profile.repository.ProfileViewModel;
import com.csform.android.MB360.insurance.profile.response.ProfileResponse;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.utilities.EncryptionPreference;
import com.csform.android.MB360.utilities.LogMyBenefits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.csform.android.MB360.databinding.FragmentEmployeeDetailsBinding;
import com.csform.android.MB360.insurance.enrollment.adapters.EmployeeDetailsAdapter;
import com.csform.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.csform.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.WindowPeriodCounter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmployeeDetailsFragment extends Fragment implements EmployeeHelper {

    FragmentEmployeeDetailsBinding binding;

    EncryptionPreference encryptionPreference;
    View view;
    EnrollmentViewModel enrollmentViewModel;
    LoadSessionViewModel loadSessionViewModel;
    Toast toast;

    ProfileViewModel profileViewModel;
    EmployeeDetailsAdapter adapter;

    EmployeeDetailsAdapterNew employeeDetailsAdapterNew;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    boolean isWindowPeriodActive = false;

    String gpaOeGrpSRno = "";
    String gtlOeGrpSRno = "";
    String gpaEmpSrNo = "";
    String gtlEmpSrno = "";

    LoadSessionResponse loadSessionResponse;
    ProfileResponse profileResponse;

    List<MaxMinAgeResponse> maxMinAgeResponse;

    List<EmployeeFieldDisplaySubResponse> employeeFieldDisplaySubResponseList;
    WindowPeriodEnrollmentResponse windowPeriod;


    public EmployeeDetailsFragment() {
        // Required empty public constructor
    }

    public EmployeeDetailsFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        encryptionPreference = new EncryptionPreference(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        binding = FragmentEmployeeDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        enrollmentViewModel = new ViewModelProvider(getActivity()).get(EnrollmentViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);


        getWindowPeriodTimer();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
        if(toast != null)
            toast.cancel();
        super.onPause();
    }

    private void getWindowPeriodTimer() {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriod.getWindowPeriod().getWindowEndDateGmc(), requireContext(), requireActivity());

            //counter comment
            /*try {
                CountDownTimer timer = windowPeriodCounter.getTimer(binding.timerEmployeeDetails);
                if (timer != null) {
                    timer.start();
                } else {
                    binding.timerEmployeeDetails.setText("Window period has expired");
                }
            } catch (ParseException e) {
                //unable to parse date
                Toast.makeText(requireContext(), "Unable to retrieve window period date.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
*/
        } else {
            //no window period available
        }
    }

    private void getEmployeeDetails() {
        windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {
            // enrollmentViewModel.getEmployee();
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriod.getWindowPeriod().getWindowEndDateGmc(), requireContext(), requireActivity());
            try {
                CountDownTimer timer = windowPeriodCounter.countDownTimer(false);
                if (timer != null) {
                    isWindowPeriodActive = true;
                } else {
                    isWindowPeriodActive = false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

                if (loadSessionResponse != null) {
                    List<GroupGMCPolicyEmpDependantsDatum> groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData();

                    GroupGMCPolicyEmpDependantsDatum employeeData;

                    String gender;
                    for (GroupGMCPolicyEmpDependantsDatum listData : groupGMCPolicyEmployeeDatum) {
                        if (listData.getRelationid().equals("17")) {
                            employeeData = listData;
                            gender = employeeData.getGender();
                            if (gender.equalsIgnoreCase("Male")) {
                                Glide.with(this).load(R.drawable.ic_male_employee).into(binding.imgGender);

                            } else {
                                Glide.with(this).load(R.drawable.ic_female_employee).into(binding.imgGender);

                            }
                        }
                    }
                    if (loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().size() != 0) {
                        enrollmentViewModel.getEmployeeFieldResponse(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo()).observe(getViewLifecycleOwner(), employeeFiledMainResponse -> {
                            if (employeeFiledMainResponse != null) {
                                List<GroupGMCPolicyEmpDependantsDatum> groupGMCPolicyEmployeeDatum1 = loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData();

                                profileViewModel.getProfile(loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                                        loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(),
                                        groupGMCPolicyEmployeeDatum1.get(0).getEmployeeSrNo()).observe(getViewLifecycleOwner(), profileResponse -> {

                                    // maxMinAgeResponse = enrollmentViewModel.getMinMaxData().getValue().getMaxMinAgeResponseList();
                                    enrollmentViewModel.maxMinAge(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo()).observe(getViewLifecycleOwner(), maxMinAgeResponse1 -> {

                                        if (maxMinAgeResponse1 != null) {
                                            maxMinAgeResponse = maxMinAgeResponse1.getMaxMinAgeResponseList();

                                            employeeFieldDisplaySubResponseList = employeeFiledMainResponse.getEmployeeFieldDisplaySubResponses();
                                            employeeDetailsAdapterNew = new EmployeeDetailsAdapterNew(requireActivity(), requireActivity(), employeeFieldDisplaySubResponseList, windowPeriod, isWindowPeriodActive, loadSessionResponse, profileResponse, this, maxMinAgeResponse);

                                            GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false);
                                            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                                @Override
                                                public int getSpanSize(int position) {
                                                    if (employeeFiledMainResponse.getEmployeeFieldDisplaySubResponses().get(position).getPiOeGrpFieldSrNo() == 6 || employeeFiledMainResponse.getEmployeeFieldDisplaySubResponses().get(position).getPiOeGrpFieldSrNo() == 7 || employeeFiledMainResponse.getEmployeeFieldDisplaySubResponses().get(position).getPiOeGrpFieldSrNo() == 5) {

                                                        return (employeeFiledMainResponse.getEmployeeFieldDisplaySubResponses().get(position).getPiFieldName().length() < 8 ? 1 : 2);//span condition here
                                                    }
                                                    return (employeeFiledMainResponse.getEmployeeFieldDisplaySubResponses().get(position).getPiFieldName().length() < 18 ? 1 : 2);//span condition here

                                                }
                                            });
                                            binding.employeeDetailsCycle.setLayoutManager(layoutManager);
                                            binding.employeeDetailsCycle.setAdapter(employeeDetailsAdapterNew);
                                        }

                                    });
                                });

                            } else {
                                LogMyBenefits.e(LogTags.ENROLLMENT, "getEmployeeDetails: Null");
                            }

                            binding.loadingBar.setVisibility(View.GONE);
                        });


                    } else {
                        //here we get window period as null
                        //we must let user not interact with anything..
                        //error state
                        //Toast.makeText(requireContext(), "Failed to get window period.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
       /* viewPagerNavigationMenuHelper.hideSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/
        getEmployeeDetails();


    }


    @Override
    public void onEditEmployee(EmployeeFieldDisplaySubResponse employeeFieldDisplaySubResponse, String value) {
        LogMyBenefits.d(LogTags.ENROLLMENT, "updated msg: " + "times");

        loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        String gmCEmpSRNo = null;
        if (loadSessionResponse != null) {

            if (loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo() != null) {
                gmCEmpSRNo = "1";
            } else {
                gmCEmpSRNo = "0";
            }


            //here we update the dependant
            Map<String, String> employeeData = new HashMap<>();

            try {
                if (!loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().isEmpty() && !loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().isEmpty()) {
                    gpaOeGrpSRno = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getOeGrpBasInfSrNo();
                    gtlOeGrpSRno = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getOeGrpBasInfSrNo();
                    gpaEmpSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getEmployeeSrNo();
                    gtlEmpSrno = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getEmployeeSrNo();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            employeeData.put("GMC_OEGRPINFSRNO", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo());
            employeeData.put("GPA_OEGRPINFSRNO", gpaOeGrpSRno != null ? gpaOeGrpSRno : "");
            employeeData.put("GTL_OEGRPINFSRNO", gtlOeGrpSRno != null ? gtlOeGrpSRno : "");
            employeeData.put("GROUPCHILDSRNO", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
            employeeData.put("GMC_EMPLOYEESRNO", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
            employeeData.put("GPA_EMPLOYEESRNO", gpaEmpSrNo != null ? gpaEmpSrNo : "");
            employeeData.put("GTL_EMPLOYEESRNO", gtlEmpSrno != null ? gtlEmpSrno : "");
            employeeData.put("FIELDSRNO", employeeFieldDisplaySubResponse.getPiOeGrpFieldSrNo().toString());
            employeeData.put("UPDATEVAL", value);
            employeeData.put("GMC_EMPSRNOAVAIL", gmCEmpSRNo);
            employeeData.put("GPA_EMPSRNOAVAIL", gpaEmpSrNo != null ? gpaEmpSrNo : "0");
            employeeData.put("GTL_EMPSRNOAVAIL", gtlEmpSrno != null ? gtlEmpSrno : "0");


            String emailPattern1 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (((employeeFieldDisplaySubResponse.getPiOeGrpFieldSrNo() == 6) || (employeeFieldDisplaySubResponse.getPiOeGrpFieldSrNo() == 7)) && (!value.matches(emailPattern1))) {
                Toast.makeText(getActivity(), "Invalid email address", Toast.LENGTH_SHORT).show();
            } else if (((employeeFieldDisplaySubResponse.getPiOeGrpFieldSrNo() == 6) || (employeeFieldDisplaySubResponse.getPiOeGrpFieldSrNo() == 7)) && (value.matches(emailPattern1)) && (value.contains("@"))) {
                enrollmentViewModel.updateEmployee(employeeData).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        if (message.getStatus()) {
                            //  Toast.makeText(requireContext(), message.getMessage() + "", Toast.LENGTH_SHORT).show();
                            if (employeeFieldDisplaySubResponse.getPiOeGrpFieldSrNo() == 8) {
                                encryptionPreference.setEncryptedDataString(AUTH_PHONE_NUMBER, value);
                            }
                            refreshData();
                            Toast.makeText(getContext(), message.getMessage() + "", Toast.LENGTH_SHORT).show();
                            LogMyBenefits.d(LogTags.ENROLLMENT, "updated msg1: " + message.getStatus());

                        } else {
                            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } else if ((!((employeeFieldDisplaySubResponse.getPiOeGrpFieldSrNo() == 6) || employeeFieldDisplaySubResponse.getPiOeGrpFieldSrNo() == 7)) && (!value.matches(emailPattern1)) ) {
              //  enrollmentViewModel.getEmployeeUpdateData().removeObservers(this);

                enrollmentViewModel.updateEmployee(employeeData);
                LogMyBenefits.d(LogTags.ENROLLMENT, "updated msg: " + "times@@@@@");
                Toast.makeText(getContext(), "updated" + "", Toast.LENGTH_SHORT).show();

                callData(employeeFieldDisplaySubResponse,value);
               /* enrollmentViewModel.updateEmployee(employeeData).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        if (message.getStatus()) {
                            if (employeeFieldDisplaySubResponse.getPiOeGrpFieldSrNo() == 8) {
                                encryptionPreference.setEncryptedDataString(AUTH_PHONE_NUMBER, value);
                            }
                            refreshData();
                            LogMyBenefits.d(LogTags.ENROLLMENT, "updated msg2: " + message.getStatus());

                            Toast.makeText(getContext(), message.getMessage() + "", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/


            }
            LogMyBenefits.d(LogTags.ENROLLMENT, "editListerner: " + employeeData.toString());
        } else {
            LogMyBenefits.d(LogTags.ENROLLMENT, "editListerner: Load-session is null");
        }




    }

    private void callData(EmployeeFieldDisplaySubResponse employeeFieldDisplaySubResponse, String value) {
        enrollmentViewModel.getEmployeeUpdateData().observe(getViewLifecycleOwner(), message -> {
            if (message != null && message.getStatus()) {
                LogMyBenefits.d("updated", "update: " + "@@@@");
                //if (message.getStatus()) {
                    //  Toast.makeText(requireContext(), message.getMessage() + "", Toast.LENGTH_SHORT).show();

                    if (employeeFieldDisplaySubResponse.getPiOeGrpFieldSrNo() == 8) {
                        encryptionPreference.setEncryptedDataString(AUTH_PHONE_NUMBER, value);
                    }
            displaymsg(message.getMessage());

                  //  Toast.makeText(getContext(), message.getMessage() , Toast.LENGTH_SHORT).show();
                refreshData();



                } else {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

        });
    }

    private void displaymsg(String message) {
        LogMyBenefits.d("updated", "update1111: " + "@@@@");
        if(toast != null)
            LogMyBenefits.d("updated", "update2222: " + "@@@@");
            //toast.cancel();
        toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }


    private void refreshData() {
        LogMyBenefits.d("HOME-FRAGMENT", "loadSessions: " + getLoginType());
        switch (getLoginType()) {

            case "PHONE_NUMBER":
                //this is the load session with number block
                loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                break;
            case "EMAIL_ID":
                //this is the load session with email block
                loadSessionViewModel.loadSessionWithEmail(getEmail());
                break;
            case "AUTH_LOGIN_ID":
                //this is the load session with loginId block
                loadSessionViewModel.loadSessionWithID(getLoginID());
                break;
        }
        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse1 -> {
            if (view.getRootView() != null) {
                if (loadSessionResponse1 != null) {
                    LogMyBenefits.d("loadSessionResponse1", "loadSessionResponse1" + loadSessionResponse1.getGroupInfoData().getGroupchildsrno());
                    loadSessionResponse = loadSessionResponse1;


                    profileViewModel.getProfile(loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                            loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(),
                            loadSessionResponse1.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo()).observe(getViewLifecycleOwner(), profileResponse1 -> {

                        if (profileResponse1 != null) {
                            profileResponse = profileResponse1;
                        }
                    });
                    enrollmentViewModel.maxMinAge(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo()).observe(getViewLifecycleOwner(), maxMinAgeResponse1 -> {

                        maxMinAgeResponse = maxMinAgeResponse1.getMaxMinAgeResponseList();


                    });

                    if (profileResponse != null && maxMinAgeResponse!=null) {

                        employeeDetailsAdapterNew = new EmployeeDetailsAdapterNew(requireActivity(), requireActivity(), employeeFieldDisplaySubResponseList, windowPeriod, isWindowPeriodActive, loadSessionResponse, profileResponse, this, maxMinAgeResponse);
                        //employeeDetailsAdapterNew.notifyDataSetChanged();
                    }
                }
            }

        });


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

        if (login_type != null) {
            return login_type;
        } else {
            return "PHONE_NUMBER";
        }
    }

    private String getEmail() {
        String phone_number = encryptionPreference.getEncryptedDataString(AUTH_EMAIL);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }
}