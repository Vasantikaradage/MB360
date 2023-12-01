package com.csform.android.MB360.fitness.ProfileScreenData;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktivolabs.aktivocore.data.models.userprofile.UserProfile;
import com.aktivolabs.aktivocore.managers.AktivoManager;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentFitnessProfileBinding;

import com.csform.android.MB360.insurance.profile.repository.ProfileViewModel;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;

import org.threeten.bp.LocalTime;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FitnessProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitnessProfileFragment extends Fragment implements View.OnClickListener {

    FragmentFitnessProfileBinding fragmentFitnessProfileBinding;
    public String Gender = "MALE", HeightMeasuredIN = "cm", WeightMeasuredIN = "kg";
    private CompositeDisposable compositeDisposable;
    String strEmpSrno;
    private List<String> str_Ciggarettes;



    private OnBoardRequest onBoardRequest = new OnBoardRequest();
    ProfileViewModel profileViewModel;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    private Calendar mcurrentTime = Calendar.getInstance();
    private TimePickerDialog mTimePicker;
    private double inch = 0, foot = 0;
    private int heightCm = 0;
    private final String HeightinInchDisplay = "{0} FT   {1} IN";
    private OnBoardRepo onBoardRepo;
    private Context mContext;
    private AktivoManager mAktivoManager;
    LoadSessionViewModel loadSessionViewModel;
    private ArrayAdapter<String> adapter;


    public FitnessProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FitnessProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FitnessProfileFragment newInstance(String param1, String param2) {
        FitnessProfileFragment fragment = new FitnessProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAktivoManager = AktivoManager.getInstance(context);
        onBoardRepo = OnBoardRepo.getInstance(context);

        // sharedPreferences = context.getSharedPreferences(FinalVariables.adminsettings, Context.MODE_PRIVATE);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFitnessProfileBinding = FragmentFitnessProfileBinding.inflate(inflater, container, false);
        view = fragmentFitnessProfileBinding.getRoot();

        initData();
        return view;
    }

    private void initData() {
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        loadSessiondata();

        setCiggaretteSpinner();
        setDrinksSpinner();

        fragmentFitnessProfileBinding.swtchHeight.setOnClickListener(this);
        fragmentFitnessProfileBinding.swtchweight.setOnClickListener(this);
        // fragmentFitnessProfileBinding.image.setOnClickListener(this);
        fragmentFitnessProfileBinding.txtHeight.setOnClickListener(this);
        fragmentFitnessProfileBinding.txtWeight.setOnClickListener(this);
        fragmentFitnessProfileBinding.txtBedTime.setOnClickListener(this);
        fragmentFitnessProfileBinding.waketime.setOnClickListener(this);
    }

    private void loadSessiondata() {
        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
            String groupChild = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
            GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);
            String oeGrpBasInfoSrNo = groupGMCPolicyEmployeeDatum.getOeGrpBasInfSrNo();
            String employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();

            profileViewModel.getProfile(groupChild, oeGrpBasInfoSrNo, employeeSrNo);

            profileViewModel.getProfileData().observe(getActivity(), profileResponse -> {
                // FitnessEmpGroupInfoRequest fitnessEmpGroupInfoRequest = new FitnessEmpGroupInfoRequest();


                if (profileResponse != null) {

                    String names = profileResponse.getUserPersonalDetails().getEmployeeName();
                    String[] splitName = names.split("\\s+");
                    onBoardRequest.getMember().setStrFirstName(splitName[0]);

                    int count = splitName.length;
                    if (count == 1) {
                        fragmentFitnessProfileBinding.txtLastName.setText(" ");

                    } else {
                        onBoardRequest.getMember().setStrLastName(splitName[1]);
                        fragmentFitnessProfileBinding.txtLastName.setText(splitName[1]);
                    }
                    onBoardRequest.getMember().setStrGender(profileResponse.getUserPersonalDetails().getGender());


                    fragmentFitnessProfileBinding.txtFirstName.setText(splitName[0]);
                    fragmentFitnessProfileBinding.txtLastName.setText(splitName[1]);
                    fragmentFitnessProfileBinding.txtGender.setText(profileResponse.getUserPersonalDetails().getGender());
                    fragmentFitnessProfileBinding.txtCellphone.setText(profileResponse.getUserPersonalDetails().getCellphoneNumber());
                    fragmentFitnessProfileBinding.txtBirth.setText(profileResponse.getUserPersonalDetails().getDateOfBirth());
                    onBoardRequest.getMember().setStrDateOfBirth(profileResponse.getUserPersonalDetails().getDateOfBirth());


                    strEmpSrno = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();

                    //  cur = dbHelper.fetch_emp_details("OE_GROUP_CHILD_MASTER");

                    onBoardRequest.getCompany().set_id(loadSessionResponse.getGroupInfoData().getGroupcode());
                    onBoardRequest.getCompany().setTitle(loadSessionResponse.getGroupInfoData().getGroupcode());

                    compositeDisposable = new CompositeDisposable();

                    Single<UserProfile> helper = mAktivoManager.getUserProfile();

                    compositeDisposable.add(helper.subscribeWith(new DisposableSingleObserver<UserProfile>() {
                        @Override
                        public void onSuccess(UserProfile userProfile) {

                            UserProfile.Gender gender = userProfile.getGender();
                            LocalTime wakeTime = userProfile.getWakeTime();
                            LocalTime bedTime = userProfile.getBedTime();
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    try {
                                        Gender = gender.toString();
                                        fragmentFitnessProfileBinding.txtGender.setText(Gender.toLowerCase().equals("male") ? "MALE" : "FEMALE");

                                        fragmentFitnessProfileBinding.txtMale.setBackground(getResources().getDrawable(Gender.toUpperCase().equals("MALE") ?
                                                R.drawable.blank_rounded_background : R.drawable.switch_border_black));
                                        fragmentFitnessProfileBinding.txtFemale.setBackground(getResources().getDrawable(Gender.toUpperCase().equals("FEMALE") ?
                                                R.drawable.blank_rounded_background : R.drawable.switch_border_black));

                                        int hour = wakeTime.getHour();
                                        int minute = wakeTime.getMinute();
                                        fragmentFitnessProfileBinding.waketime.setText(String.format("%s", (hour <= 9 ? "0" + hour : hour) + ":" +
                                                (minute <= 9 ? "0" + minute : minute)));
                                        hour = bedTime.getHour();
                                        minute = bedTime.getMinute();

                                        fragmentFitnessProfileBinding.txtBedTime.setText(MessageFormat.format("{0}",
                                                (hour < 9 ? "0" + hour : hour) + ":" +
                                                        (minute <= 9 ? "0" + minute : minute)));

                                        String myString = Gender;

                                        //01-Jan-1981
                                        onBoardRequest.getMember().setStrGender(myString.substring(0, 1).toUpperCase() +
                                                myString.substring(1).toLowerCase());
                                        onBoardRequest.getMember().setStrEmpSrNo(strEmpSrno);
                                        onBoardRequest.getMember().setStrEmpSrNo("-1");


                                        onBoardRequest.getMember().setStrWakeUp(fragmentFitnessProfileBinding.waketime.getText().toString());
                                        onBoardRequest.getMember().setStrBedTime(fragmentFitnessProfileBinding.txtBedTime.getText().toString());

                                        if (Gender.toUpperCase().equals("MALE")) {
                                            //   userImage.setImageResource(R.drawable.user_male);
                                        } else {
                                            // userImage.setImageResource(R.drawable.user_female);
                                        }

                                        //  heightCm = Math.toIntExact(Math.round(Double.parseDouble(sharedPreferences.getString("height_cm", ""))));
                                        clickListeners();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        // todo hide loading
                                        clickListeners();
                                    }

                                });
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("Message", e.toString());
                        }
                    }));
                }
            });
        });
    }

    private void setCiggaretteSpinner() {
        try {
            str_Ciggarettes = new ArrayList<>();
            str_Ciggarettes.add("NA");
            str_Ciggarettes.add("0 Cigarettes");
            str_Ciggarettes.add("1 Cigarettes");
            str_Ciggarettes.add("2 Cigarettes");
            str_Ciggarettes.add("3 Cigarettes");
            str_Ciggarettes.add("4 Cigarettes");
            str_Ciggarettes.add("5 Cigarettes");
            str_Ciggarettes.add("6 Cigarettes");
            str_Ciggarettes.add("7 Cigarettes");
            str_Ciggarettes.add("8 Cigarettes");
            str_Ciggarettes.add("9 Cigarettes");
            str_Ciggarettes.add("10+ Cigarettes");

            adapter = new ArrayAdapter(getContext(),
                    R.layout.activity_first_walkthrough2,
                    R.id.spnText, this.str_Ciggarettes);

            adapter.setDropDownViewResource(R.layout.spin_item_layout);

            fragmentFitnessProfileBinding.spnCiggarettes.setPopupBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.nhborder));
            fragmentFitnessProfileBinding.spnCiggarettes.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDrinksSpinner() {
        try {
            str_Ciggarettes = new ArrayList<>();
            str_Ciggarettes.add("NA");
            str_Ciggarettes.add("0 Drink");
            str_Ciggarettes.add("1 Drink");
            str_Ciggarettes.add("2 Drinks");
            str_Ciggarettes.add("3 Drinks");
            str_Ciggarettes.add("4 Drinks");
            str_Ciggarettes.add("5 Drinks");
            str_Ciggarettes.add("6 Drinks");
            str_Ciggarettes.add("7 Drinks");
            str_Ciggarettes.add("8 Drinks");
            str_Ciggarettes.add("9 Drinks");
            str_Ciggarettes.add("10+ Drinks");

            adapter = new ArrayAdapter(getContext(),
                    R.layout.activity_first_walkthrough2,
                    R.id.spnText, this.str_Ciggarettes);

            adapter.setDropDownViewResource(R.layout.spin_item_layout);

            fragmentFitnessProfileBinding.spnDrinks.setPopupBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.nhborder));

            fragmentFitnessProfileBinding.spnDrinks.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clickListeners() {
        fragmentFitnessProfileBinding.txtBedTime.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) ->
                    fragmentFitnessProfileBinding.txtBedTime.setText(String.format("%s:%s",
                            selectedHour <= 9 ? "0" + selectedHour : selectedHour,
                            selectedMinute <= 9 ? "0" + selectedMinute : selectedMinute)),
                    hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });

        fragmentFitnessProfileBinding.txtBedTime.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) ->
                    fragmentFitnessProfileBinding.txtBedTime.setText(String.format("%s:%s",
                            selectedHour <= 9 ? "0" + selectedHour : selectedHour,
                            selectedMinute <= 9 ? "0" + selectedMinute : selectedMinute)),
                    hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });

        fragmentFitnessProfileBinding.txtBedTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onBoardRequest.getMember().setStrBedTime(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fragmentFitnessProfileBinding.waketime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onBoardRequest.getMember().setStrWakeUp(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fragmentFitnessProfileBinding.txtHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    double hgt = Double.parseDouble(charSequence.toString());
                    String finalWeight = HeightMeasuredIN.equals("inch") ? MessageFormat.format("{0}",
                            hgt / 0.0328) : hgt + "";

                    onBoardRequest.getMember().setStrHeightCM(finalWeight);


                    onBoardRepo.HeightMeasuredIN = "cm";


                } catch (Exception e) {
                    e.printStackTrace();
                }
//                onBoardRequest.getMember().setStrHeightCM(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fragmentFitnessProfileBinding.txtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    double hgt = Double.parseDouble(charSequence.toString());
                    String finalWeight = WeightMeasuredIN.equals("lbs") ?
                            MessageFormat.format("{0}", hgt / 2.20) : hgt + "";
                    onBoardRequest.getMember().setStrWeightKG(finalWeight);
                    onBoardRepo.WeightMeasuredIN = "kg";

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                onBoardRequest.getMember().setStrWeightKG(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fragmentFitnessProfileBinding.btnSave.setOnClickListener(view -> {
            onBoardRequest.getMember().setStrHeightCM(fragmentFitnessProfileBinding.txtHeight.getText().toString());
            onBoardRequest.getMember().setStrWeightKG(fragmentFitnessProfileBinding.txtWeight.getText().toString());


            onBoardRepo.getOnBoardedEmp().postValue(false);
            onBoardRepo.onBoardMember(onBoardRequest, "-1");
            setObserver();
        });

    }

    private void setObserver() {
        try {
            onBoardRepo.getOnBoardedEmp().observe(getViewLifecycleOwner(), value -> {
                if (value) {

                    Toast.makeText(mContext, "Profile details updated successfully", Toast.LENGTH_SHORT).show();
                    final Dialog alertDialog = new Dialog(mContext);
                    Objects.requireNonNull(alertDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
                    LayoutInflater mLayoutInflater = getLayoutInflater();

                    View alertLayout = mLayoutInflater.inflate(R.layout.dialog_internet_error, null);
                    Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(getResources().getDrawable(R.drawable.nhborder));

                    alertDialog.setContentView(alertLayout);
                    ImageView alertIcon = alertLayout.findViewById(R.id.alertIcon);
                    alertIcon.setImageResource(R.drawable.right_arrow);
                    Button btnDismiss = alertDialog.findViewById(R.id.btnDismiss);
                    btnDismiss.setText("Submit");

                    btnDismiss.setOnClickListener(view -> alertDialog.cancel());

                    TextView alertMessage = alertDialog.findViewById(R.id.tvAlertMessage);
                    alertMessage.setText("Profile details updated successfully");

                    // alertDialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        Intent intDialog;

        switch (v.getId()) {

            case R.id.txtBedTime:

                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(getContext(),
                        (timePicker, selectedHour, selectedMinute) -> {

                            String time = String.format("%s:%s",
                                    selectedHour <= 9 ? "0" + selectedHour : "" + selectedHour,
                                    selectedMinute <= 9 ? "0" + selectedMinute : selectedMinute + "");

                            onBoardRequest.getMember().setStrBedTime(time);
                            fragmentFitnessProfileBinding.txtBedTime.setText(time);

                        }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

                break;

            case R.id.waketime:

                int hour2 = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute2 = mcurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) -> {

                    String time = String.format("%s:%s",
                            selectedHour <= 9 ? "0" + selectedHour : selectedHour + "",
                            selectedMinute <= 9 ? "0" + selectedMinute : selectedMinute + "");

                    fragmentFitnessProfileBinding.waketime.setText(time);
                    onBoardRequest.getMember().setStrWakeUp(time);

                }, hour2, minute2, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

                break;

            case R.id.txtWeight:

                intDialog = new Intent(getActivity(), DialogPicker.class);
                intDialog.putExtra("type", "weight");
                if (TextUtils.isEmpty(fragmentFitnessProfileBinding.txtWeight.getText()))
                    intDialog.putExtra("value", "120");
                else
                    intDialog.putExtra("value", fragmentFitnessProfileBinding.txtWeight.getText().toString());
                startActivityForResult(intDialog, 3);

                break;

            case R.id.txtHeight:
                intDialog = new Intent(getActivity(), DialogPicker.class);
                intDialog.putExtra("type", "height");
                if (TextUtils.isEmpty(fragmentFitnessProfileBinding.txtHeight.getText()))
                    intDialog.putExtra("value", "90");
                else
                    intDialog.putExtra("value", fragmentFitnessProfileBinding.txtHeight.getText().toString());
                startActivityForResult(intDialog, 2);
                break;

            case R.id.swtchGender:

                fragmentFitnessProfileBinding.txtMale.setBackground(getResources().getDrawable(Gender.equals("FEMALE") ?
                        R.drawable.switch_border_black : R.drawable.blank_rounded_background));
                fragmentFitnessProfileBinding.txtFemale.setBackground(getResources().getDrawable(Gender.equals("FEMALE") ?
                        R.drawable.blank_rounded_background : R.drawable.switch_border_black));
                Gender = Gender.equals("MALE") ? "FEMALE" : "MALE";

                break;


            case R.id.swtchHeight:

                try {

                    fragmentFitnessProfileBinding.txtCm.setBackgroundDrawable(getResources().getDrawable(HeightMeasuredIN.equals("inch") ?
                            R.drawable.blank_rounded_background :
                            R.drawable.switch_border_black));

                    fragmentFitnessProfileBinding.txtFT.setBackgroundDrawable(getResources().getDrawable(HeightMeasuredIN.equals("inch") ?
                            R.drawable.switch_border_black :
                            R.drawable.blank_rounded_background));

                    HeightMeasuredIN = HeightMeasuredIN.equals("inch") ? "cm" : "inch";
                    onBoardRepo.HeightMeasuredIN = HeightMeasuredIN;

                    if (!TextUtils.isEmpty(fragmentFitnessProfileBinding.txtHeight.getText().toString())) {
                        switch (HeightMeasuredIN) {

                            case "inch":

                                double weight = Double.parseDouble((fragmentFitnessProfileBinding.txtHeight.getText().toString()));

                                if (String.valueOf(weight) != null && String.valueOf(weight).trim().length() != 0) {
                                    foot = Math.round((weight / 2.54) / 12);
                                    inch = Math.ceil((weight / 2.54) - (foot * 12));
                                }

                                fragmentFitnessProfileBinding.txtHeight.setText(MessageFormat.format(HeightinInchDisplay,
                                        foot, Math.max(0, inch)));

                                break;

                            case "cm":

                                if (foot == 0) {
                                    fragmentFitnessProfileBinding.txtHeight.setText(MessageFormat.format("{0}", heightCm));
                                } else {
//                                    double heightFT = Double.parseDouble(foot + "." + inch);

                                    double cm = 0.0;

                                    inch += foot * 12;
//                                    cm += foot * 30.48;
                                    cm += inch * 2.54;

                                    heightCm = (int) Math.round(cm);
                                    fragmentFitnessProfileBinding.txtHeight.setText(MessageFormat.format("{0}", heightCm));

                                }
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.swtchweight:

                try {

                    fragmentFitnessProfileBinding.txtKg.setBackground(getResources().getDrawable(WeightMeasuredIN.equals("lbs") ?
                            R.drawable.blank_rounded_background : R.drawable.switch_border_black));

                    fragmentFitnessProfileBinding.txtLbs.setBackground(getResources().getDrawable(WeightMeasuredIN.equals("lbs") ?
                            R.drawable.switch_border_black : R.drawable.blank_rounded_background));

                    WeightMeasuredIN = WeightMeasuredIN.equals("kg") ? "lbs" : "kg";
                    onBoardRepo.WeightMeasuredIN = WeightMeasuredIN;

                    if (!TextUtils.isEmpty(fragmentFitnessProfileBinding.txtWeight.getText().toString())) {
                        double weight = Double.parseDouble((fragmentFitnessProfileBinding.txtWeight.getText().toString()));
                        switch (WeightMeasuredIN) {
                            case "kg":
                                fragmentFitnessProfileBinding.txtWeight.setText(MessageFormat.format("{0}",
                                        Math.round(weight / 2.20462262)));
                                break;
                            case "lbs":
                                fragmentFitnessProfileBinding.txtWeight.setText(MessageFormat.format("{0}",
                                        Math.round(weight * 2.20462262)));
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

          /*  case R.id.userImage:
                Intent intent = new Intent(Intent.ACTION_PICK);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                // Launching the Intent
                startActivityForResult(intent, 3);
                break;*/
        }
    }
}