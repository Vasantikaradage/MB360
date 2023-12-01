package com.csform.android.MB360.insurance.enrollment.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.ItemEnrollmentEmployeeDetailsBinding;
import com.csform.android.MB360.insurance.enrollment.interfaces.EmployeeHelper;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MaxMinAgeResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.employeedetails.EmployeeFieldDisplaySubResponse;
import com.csform.android.MB360.insurance.profile.response.ProfileResponse;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.monthpicker.DatePickerTheme;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.UtilMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EmployeeDetailsAdapterNew extends RecyclerView.Adapter<EmployeeDetailsAdapterNew.EmployeeDetailViewHolder> {

    FragmentActivity activity;
    Context context;
    List<EmployeeFieldDisplaySubResponse> employeeDetailList;
    WindowPeriodEnrollmentResponse windowPeriod;
    static final int MAX_AGE_EMPLOYEE = 50;
    static final int MIN_AGE_EMPLOYEE = 25;

    LoadSessionResponse loadSessionResponse;

    List<MaxMinAgeResponse> maxMinAgeResponseList;
    boolean isWindowPeriodActive;
    EmployeeHelper employeeHelper;
    private ProfileResponse profileResponse;

    private int mYear, mMonth, mDay, mHour, mMinute;

    public EmployeeDetailsAdapterNew(FragmentActivity activity, Context context, List<EmployeeFieldDisplaySubResponse> employeeDetailList, WindowPeriodEnrollmentResponse windowPeriod, boolean isWindowPeriodActive, LoadSessionResponse loadSessionResponse, ProfileResponse profileResponse, EmployeeHelper employeeHelper, List<MaxMinAgeResponse> maxMinAgeResponse) {
        this.activity = activity;
        this.context = context;
        this.employeeDetailList = employeeDetailList;
        this.windowPeriod = windowPeriod;
        this.isWindowPeriodActive = isWindowPeriodActive;

        this.loadSessionResponse = loadSessionResponse;
        this.employeeHelper = employeeHelper;
        this.profileResponse = profileResponse;
        this.maxMinAgeResponseList = maxMinAgeResponse;
    }


    @NonNull
    @Override
    public EmployeeDetailsAdapterNew.EmployeeDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEnrollmentEmployeeDetailsBinding binding = ItemEnrollmentEmployeeDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EmployeeDetailsAdapterNew.EmployeeDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeDetailsAdapterNew.EmployeeDetailViewHolder holder, int position) {
        EmployeeFieldDisplaySubResponse employeeDetail = employeeDetailList.get(position);

        if (employeeDetail.getPiCanDisplay().equals("0")) {
            //set the item as invisible
            holder.binding.itemEmployeeDetailsLayout.setVisibility(View.GONE);
            holder.binding.itemEmployeeDetailLabel.setVisibility(View.GONE);
            holder.binding.itemEmployeeDetailText.setVisibility(View.GONE);
            holder.binding.itemEditEmployee.setVisibility(View.GONE);

            holder.binding.itemEmployeeDetailTextView.setVisibility(View.GONE);
        } else {
            //set the item as visible
            holder.binding.itemEmployeeDetailLabel.setVisibility(View.VISIBLE);
            holder.binding.itemEmployeeDetailText.setVisibility(View.VISIBLE);
            holder.binding.itemEmployeeDetailTextView.setVisibility(View.GONE);
            holder.binding.itemEditEmployee.setVisibility(View.VISIBLE);
            holder.binding.itemEmployeeDetailLabel.setText(employeeDetail.getPiFieldName());


            List<GroupGMCPolicyEmpDependantsDatum> groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData();

            GroupGMCPolicyEmpDependantsDatum employeeData;
            for (GroupGMCPolicyEmpDependantsDatum listData : groupGMCPolicyEmployeeDatum) {
                if (listData.getRelationid().equals("17")) {
                    employeeData = listData;


                    if (employeeData != null) {
                        switch (employeeDetail.getPiOeGrpFieldSrNo()) {
                            case 1:
                                holder.binding.itemEmployeeDetailText.setText(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo());
                                break;
                            case 2:
                                holder.binding.itemEmployeeDetailText.setText(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getDateOfJoining());
                                break;

                            case 3:
                                holder.binding.itemEmployeeDetailText.setText(employeeData.getDateOfBirth());
                                break;
                            case 4:
                                holder.binding.itemEmployeeDetailText.setText(employeeData.getAge());
                                break;
                            case 7:

                                holder.binding.itemEmployeeDetailText.setText(profileResponse.getUserPersonalDetails().getEMailId());
                                break;
                            case 8:
                                holder.binding.itemEmployeeDetailText.setText(employeeData.getCellphoneNumber());
                                break;
                            case 32:
                                holder.binding.itemEmployeeDetailText.setText(employeeData.getGender());
                                break;

                            case 5:

                                holder.binding.itemEmployeeDetailText.setText(wordFirstCap(employeeData.getPersonName()));
                                break;

                            case 6:
                                holder.binding.itemEmployeeDetailText.setText(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOfficialEMailId());
                              if(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOfficialEMailId().length()>=30 || holder.binding.itemEmployeeDetailText.getText().length()>=30)
                              {
                                  holder.binding.itemEmployeeDetailText.setLines(2);
                              }
                                break;
                            case 41:
                                holder.binding.itemEmployeeDetailText.setText(loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData().get(0).getEmergencyContactNumber());
                                break;

                            case 39:
                                // holder.binding.itemEmployeeDetailText.setText(profileResponse.getUserPersonalDetails().g);
                                break;

                            case 38:
                                holder.binding.itemEmployeeDetailText.setText(profileResponse.getUserAddressDetails().getEmpPerAddrLine1());
                                break;

                            case 36:
                                holder.binding.itemEmployeeDetailText.setText(profileResponse.getUserPersonalDetails().getCellphoneNumber());
                                break;


                        }
                    }
                }
            }


            //if employee-details are present from the api itself
           /* if (!employeeDetail.getFieldValue().equalsIgnoreCase("")) {
                holder.binding.itemEmployeeDetailText.setText(employeeDetail.getFieldValue());
                holder.binding.itemEmployeeDetailTextView.setText(employeeDetail.getFieldValue());
            } else {
                holder.binding.itemEmployeeDetailText.setHint(String.format("Enter the %s", employeeDetail.getPiFieldName()));
            }*/

            // if(employeeDetail.getPiFieldName().equals(""))

            if (employeeDetail.getPiCanEdit() == 0) {
                /*holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_NULL);
                holder.binding.itemEmployeeDetailTextView.setVisibility(View.VISIBLE);
                holder.binding.itemEmployeeDetailText.setVisibility(View.GONE);*/
                holder.binding.itemEditEmployee.setVisibility(View.GONE);
                holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_NULL);
                holder.binding.itemEmployeeDetailText.setEnabled(false);
            } else {
                holder.binding.itemEditEmployee.setVisibility(View.VISIBLE);
                holder.binding.itemEmployeeDetailText.setEnabled(true);
                holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_CLASS_TEXT);
               /* holder.binding.itemEmployeeDetailText.setVisibility(View.VISIBLE);
                holder.binding.itemEmployeeDetailText.setVisibility(View.VISIBLE);
                holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_CLASS_TEXT);
                holder.binding.itemEmployeeDetailTextView.setVisibility(View.GONE);*/

            }
        }


        holder.binding.itemEmployeeDetailText.setOnFocusChangeListener((view, b) -> {

            employeeDetail.setEditState(!employeeDetail.isEditState());
            if (employeeDetail.isEditState()) {

                holder.binding.itemEditEmployee.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tick));
                String[] genderList = {"MALE", "FEMALE"};
                final ArrayAdapter<String> spinner_gender = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, genderList);
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                switch (employeeDetail.getPiOeGrpFieldSrNo()) {
                    case 3:
                        datePickerData(holder);
                        break;

                    case 2:
                        datePickerData(holder);
                        break;
                    case 32:
                        holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_NULL);
                        new AlertDialog.Builder(context)
                                .setTitle("Select Gender")
                                .setAdapter(spinner_gender, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        holder.binding.itemEmployeeDetailText.setText(genderList[which].toString());
                                        dialog.dismiss();
                                    }
                                }).create().show();

                        break;

                    case 8:
                        cellphoneinputType(holder);

                        break;
                    case 36:
                        cellphoneinputType(holder);
                        break;
                }


            } else {
                holder.binding.itemEditEmployee.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pencil_svg));
            }
        });


        holder.binding.itemEditEmployee.setOnClickListener(v -> {
            if (employeeDetail.isEditState()) {

                holder.binding.itemEmployeeDetailText.clearFocus();
                holder.binding.itemEditEmployee.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pencil_svg));
                hideKeyboard(v);
                employeeHelper.onEditEmployee(employeeDetailList.get(position), holder.binding.itemEmployeeDetailText.getText().toString());



            } else {
                holder.binding.itemEmployeeDetailText.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(holder.binding.itemEmployeeDetailTextView, InputMethodManager.SHOW_FORCED);

                holder.binding.itemEmployeeDetailText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0));
                holder.binding.itemEmployeeDetailText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0));
                try {
                    holder.binding.itemEmployeeDetailText.setSelection(holder.binding.itemEmployeeDetailText.getText().length());
                } catch (Exception e) {
                    LogMyBenefits.d("ERROR", "onBindViewHolder:length is null ");
                }
            }
        });


        //validation
        if (employeeDetail.getPiFieldName().toLowerCase().contains("age")) {
            holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.binding.itemEmployeeDetailText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        }
        if (employeeDetail.getPiFieldName().toLowerCase().contains("mobile")) {
            holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.binding.itemEmployeeDetailText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }


        //if window period is expired freeze the interaction
        if (!isWindowPeriodActive) {
            holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_NULL);
            holder.binding.itemEmployeeDetailTextView.setVisibility(View.VISIBLE);
            holder.binding.itemEmployeeDetailText.setVisibility(View.GONE);
            holder.binding.itemEditEmployee.setVisibility(View.GONE);

            if (!employeeDetail.getPiCanDisplay().equals("0")) {
                if (holder.binding.itemEmployeeDetailTextView.getText().toString().trim().isEmpty()) {
                    holder.binding.itemEmployeeDetailTextView.setText("-");
                }
            }
        }

    }

    private String wordFirstCap(String personName) {
        String[] words = personName.trim().split(" ");
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (words[i].trim().length() > 0) {
                Log.e("words[i].trim", "" + words[i].trim().charAt(0));
                ret.append(Character.toUpperCase(words[i].trim().charAt(0)));
                ret.append(words[i].trim().substring(1));
                if (i < words.length - 1) {
                    ret.append(' ');
                }
            }
        }

        return ret.toString();
    }

    private void datePickerData(EmployeeDetailViewHolder holder) {
        PrimeDatePicker datePicker;
        //handling the date of birth
        SingleDayPickCallback callback = dateOfBirth -> {
            SimpleDateFormat dateRangeFormat = UtilMethods.DATE_FORMAT;
            holder.binding.itemEmployeeDetailText.setText(dateRangeFormat.format(dateOfBirth.getTime()));
            //  binding.etAge.setText(getAge(dateOfBirth));
        };
        LightThemeFactory themeFactory = DatePickerTheme.Companion.getEnrollmentTheme();
        //  binding.tilDOB.setError(null);

        // MaxMinAgeResponse maxMinAgeResponse1;
        for (MaxMinAgeResponse listData : maxMinAgeResponseList) {
            if (listData.getRelationId() == 17) {

                CivilCalendar maxAge = new CivilCalendar();
                maxAge.set(2018, 0, 12);
                datePicker = PrimeDatePicker.Companion.dialogWith(getMinAge(listData))
                        .pickSingleDay(callback)
                        .minPossibleDate(getMaxAge(listData))
                        .maxPossibleDate(getMinAge(listData))
                        .applyTheme(themeFactory)
                        .build();
                datePicker.show(activity.getSupportFragmentManager(), "SOME_TAG");

            }
        }


    }

    private PrimeCalendar getMaxAge(MaxMinAgeResponse maxMinAgeResponse) {
        CivilCalendar maxAge = new CivilCalendar();
        maxAge.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - maxMinAgeResponse.getMaxAge());

        return maxAge;

    }

    private PrimeCalendar getMinAge(MaxMinAgeResponse maxMinAgeResponse) {
        CivilCalendar minAge = new CivilCalendar();
        minAge.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - maxMinAgeResponse.getMinAge());

        return minAge;
    }


    private void cellphoneinputType(EmployeeDetailViewHolder holder) {
        holder.binding.itemEmployeeDetailText.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
    }


    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public int getItemCount() {
        return (employeeDetailList != null ? employeeDetailList.size() : 0);
    }

    public static class EmployeeDetailViewHolder extends RecyclerView.ViewHolder {
        ItemEnrollmentEmployeeDetailsBinding binding;

        public EmployeeDetailViewHolder(@NonNull ItemEnrollmentEmployeeDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
