package com.csform.android.MB360;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.csform.android.MB360.utilities.LogMyBenefits;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.MB360.databinding.FragmentHomeHealthSummaryBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.wellness.WellnessDashBoardActivity;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;


import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.HomeHealthCarePackage;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageDM;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageDS;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageEC;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageNC;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackagePT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.PackageST;

public class HomeHealthSummaryFragment extends Fragment {

    FragmentHomeHealthSummaryBinding binding;
    View view;

    LoadSessionViewModel loadSessionViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    NavController navController;

    String remark = "";


    public HomeHealthSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeHealthSummaryBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();


        //setCity
        binding.txtCityName.setText((homeHealthCareViewModel.getSelectedCity().getValue().getCity() == null || homeHealthCareViewModel.getSelectedCity().getValue().getCity().equalsIgnoreCase("")) ? "Mumbai" : homeHealthCareViewModel.getSelectedCity().getValue().getCity());


        //set person info
        FamilyMember person = homeHealthCareViewModel.getSelectedPerson().getValue();
        binding.txtDependantName.setText(person.getPersonName());
        String personage = person.getRelationName() + "(" + person.getAge() + " years)";
        binding.txtRelationAge.setText(personage);


        homeHealthCareViewModel.getSelectedService().observe(getViewLifecycleOwner(), service -> {
            if (!service.equalsIgnoreCase("")) {

                switch (service.toUpperCase()) {

                    case "DENTAL":
                        break;
                    case "TRAINED ATTENDANT":
                        HomeHealthCarePackage homeHealthCarePackage = homeHealthCareViewModel.getTrainedAttendantPackage().getValue();
                        //selected package details
                        if (homeHealthCarePackage != null) {

                            binding.catLabel.setVisibility(View.GONE);
                            binding.txtCatValue.setVisibility(View.GONE);

                            binding.hourValue.setText(homeHealthCarePackage.getHhcNaHours());
                            binding.durationValue.setText(homeHealthCarePackage.getHhcNaDurations());
                            binding.nursingCountValue.setText(homeHealthCarePackage.getHhcNaNacount());

                            Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                            if (appointment != null) {

                                if (appointment.getDate_condition() == 1) {
                                    //daily
                                    binding.txtNoMonthLabel.setText("No. of Days");
                                    binding.txtDuration.setText(appointment.getDate_preference());

                                } else if (appointment.getDate_condition() == 2) {
                                    //Monthly
                                    binding.txtNoMonthLabel.setText("No. of Months");
                                    //todo calculate the better dates to show
                                    binding.txtDuration.setText(appointment.getFrom_date() + "\n           To           \n" + appointment.getTo_date());
                                } else if (appointment.getDate_condition() == 3) {
                                    //date range
                                    binding.txtNoMonthLabel.setText("No. of Months");
                                    binding.txtDuration.setText(appointment.getFrom_date() + "\n           To           \n" + appointment.getTo_date());
                                }
                                binding.txtNoMonthValue.setText(appointment.getCount());

                            }

                            //calculation for amount
                            try {
                                //amount calculations
                                binding.textPackageRate.setText("\u20B9 "+UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
                                if (appointment.getCount() != null) {
                                    int count = Integer.parseInt(appointment.getCount());
                                    double total = Integer.parseInt(homeHealthCarePackage.getPkgPriceMb()) * count;
                                    binding.textTotalAmount.setText("\u20B9 "  +UtilMethods.PriceFormat(String.valueOf(total)));
                                } else {
                                    binding.btnSchedule.setEnabled(false);
                                    binding.textTotalAmount.setText("-");
                                    binding.txtNoMonthValue.setText("-");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                        } else {
                            Toast.makeText(requireContext(), "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "LONG TERM NURSING":
                        PackageLT homeHealthCarePackageLT = homeHealthCareViewModel.getLongtermNursingPackage().getValue();

                        //selected package details
                        if (homeHealthCarePackageLT != null) {


                            binding.catLabel.setVisibility(View.VISIBLE);
                            binding.txtCatValue.setVisibility(View.VISIBLE);
                            binding.txtCatValue.setText(homeHealthCarePackageLT.getHhcLtCategory());

                            binding.hourValue.setText(homeHealthCarePackageLT.getHhcLtHours());
                            binding.durationValue.setText(homeHealthCarePackageLT.getHhcLtDurations());
                            binding.nursingCountValue.setText(homeHealthCarePackageLT.getHhcLtNacount());

                            Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                            if (appointment != null) {

                                if (appointment.getDate_condition() == 1) {
                                    //daily
                                    binding.txtNoMonthLabel.setText("No. of Days");
                                    binding.txtDuration.setText(appointment.getDate_preference());

                                } else if (appointment.getDate_condition() == 2) {
                                    //Monthly
                                    binding.txtNoMonthLabel.setText("No. of Months");
                                    //todo calculate the better dates to show
                                    binding.txtDuration.setText(appointment.getFrom_date() + "\n           To           \n" + appointment.getTo_date());
                                } else if (appointment.getDate_condition() == 3) {
                                    //date range
                                    binding.txtNoMonthLabel.setText("No. of Months");
                                    binding.txtDuration.setText(appointment.getFrom_date() + "\n           To           \n" + appointment.getTo_date());
                                }
                                binding.txtNoMonthValue.setText(appointment.getCount());

                            }

                            //calculation for amount
                            try {
                                //amount calculations
                                binding.textPackageRate.setText("\u20B9 "+UtilMethods.PriceFormat(homeHealthCarePackageLT.getPkgPriceMb()));
                                if (appointment.getCount() != null) {

                                    int count = Integer.parseInt(appointment.getCount());
                                    double total = Integer.parseInt(homeHealthCarePackageLT.getPkgPriceMb()) * count;
                                    binding.textTotalAmount.setText("\u20B9 "  +UtilMethods.PriceFormat(String.valueOf(total)));
                                } else {

                                    binding.btnSchedule.setEnabled(false);
                                    binding.textTotalAmount.setText("-");
                                    binding.txtNoMonthValue.setText("-");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(requireContext(), "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case "SHORT TERM NURSING":
                        PackageST homeHealthCarePackageST = homeHealthCareViewModel.getShorttermNursingPackage().getValue();
                        //selected package details
                        if (homeHealthCarePackageST != null) {


                            binding.hourValue.setVisibility(View.GONE);
                            binding.HoursLayout.setVisibility(View.GONE);
                            binding.durationValue.setVisibility(View.GONE);
                            binding.durationLabel.setVisibility(View.GONE);
                            binding.nursingCountValue.setVisibility(View.GONE);
                            binding.nursingCountLabel.setVisibility(View.GONE);
                            binding.txtNoMonthValue.setVisibility(View.GONE);
                            binding.txtNoMonthLabel.setVisibility(View.GONE);
                            binding.viewline.setVisibility(View.GONE);

                            Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                            if (appointment != null) {

                                binding.txtCatValue.setText("" + appointment.getService());
                                binding.txtDuration.setText("" + appointment.getDate_preference());
//
                            }

                            //calculation for amount
                            try {
                                //amount calculations
                                binding.textPackageRate.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackageST.getPkgPriceMb()));
                                binding.textTotalAmount.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackageST.getPkgPriceMb()));
//
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(requireContext(), "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "DOCTOR SERVICES":
                        PackageDS homeHealthCarePackageDS = homeHealthCareViewModel.getDoctorServicesPackage().getValue();
                        if (homeHealthCarePackageDS != null) {


//                        binding.catLabel.setVisibility(View.GONE);
                            binding.txtCatValue.setVisibility(View.VISIBLE);
                            binding.txtCatValue.setText("" + homeHealthCarePackageDS.getCategory());


                            binding.hourValue.setVisibility(View.GONE);
                            binding.HoursLayout.setVisibility(View.GONE);
                            binding.durationValue.setVisibility(View.GONE);
                            binding.durationLabel.setVisibility(View.GONE);
                            binding.nursingCountValue.setVisibility(View.GONE);
                            binding.nursingCountLabel.setVisibility(View.GONE);
                            binding.txtNoMonthValue.setVisibility(View.GONE);
                            binding.txtNoMonthLabel.setVisibility(View.GONE);
                            binding.viewline.setVisibility(View.GONE);


                            Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                            if (appointment != null) {

                                binding.txtDuration.setText(String.format("%s %s", appointment.getDate_preference(), appointment.getTime_preference()));
                            }

                            //calculation for amount
                            try {
                                //amount calculations
                                binding.textPackageRate.setText(String.format("\u20B9 %s", UtilMethods.PriceFormat(homeHealthCarePackageDS.getPkgPriceMb())));
                                binding.textTotalAmount.setText(String.format("\u20B9 %s", UtilMethods.PriceFormat(homeHealthCarePackageDS.getPkgPriceMb())));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(requireContext(), "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case "PHYSIOTHERAPY":
                        PackagePT homeHealthCarePackagePT = homeHealthCareViewModel.getPhysiotherapyPackage().getValue();

                        if (homeHealthCarePackagePT != null) {


                            binding.hourValue.setVisibility(View.GONE);
                            binding.HoursLayout.setVisibility(View.GONE);
                            binding.durationValue.setVisibility(View.GONE);
                            binding.durationLabel.setVisibility(View.GONE);
                            binding.nursingCountValue.setVisibility(View.GONE);
                            binding.nursingCountLabel.setVisibility(View.GONE);
                            binding.txtNoMonthValue.setVisibility(View.GONE);
                            binding.txtNoMonthLabel.setVisibility(View.GONE);
                            binding.viewline.setVisibility(View.GONE);

                            binding.txtCatValue.setVisibility(View.VISIBLE);
                            binding.txtCatValue.setText("" + homeHealthCarePackagePT.getCategory());


                            Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                            if (appointment != null) {

                                if (appointment.getDate_condition() == 1) {
                                    //daily

                                    binding.txtDuration.setText(appointment.getDate_preference());

                                } else if (appointment.getDate_condition() == 2) {
                                    //Monthly

                                    //todo calculate the better dates to show
                                    binding.txtDuration.setText(appointment.getFrom_date() + "\n   To       \n" + appointment.getTo_date());
                                }

                            }

                            //calculation for amount
                            try {
                                //amount calculations
                                binding.textPackageRate.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackagePT.getPkgPriceMb()));
                                binding.textTotalAmount.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackagePT.getPkgPriceMb()));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(requireContext(), "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "DIABETES MANAGEMENT":
                        PackageDM homeHealthCarePackageDM = homeHealthCareViewModel.getDiabetesManagementPackage().getValue();

                        if (homeHealthCarePackageDM != null) {

                            binding.catLabel.setVisibility(View.GONE);
                            binding.txtCatValue.setVisibility(View.GONE);
//                        binding.txtCatValue.setText("" + homeHealthCarePackage.getCategory());

                            binding.hourValue.setVisibility(View.GONE);
                            binding.HoursLayout.setVisibility(View.GONE);
                            binding.durationValue.setVisibility(View.GONE);
                            binding.durationLabel.setVisibility(View.GONE);
                            binding.nursingCountValue.setVisibility(View.GONE);
                            binding.nursingCountLabel.setVisibility(View.GONE);
                            binding.txtNoMonthValue.setVisibility(View.GONE);
                            binding.txtNoMonthLabel.setVisibility(View.GONE);
                            binding.viewline.setVisibility(View.GONE);


                            Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                            if (appointment != null) {

                                binding.txtDuration.setText("" + appointment.getDate_preference());
                                binding.textPackageRate.setText("\u20B9 " + UtilMethods.PriceFormat(appointment.getPrice()));
                                binding.textTotalAmount.setText("\u20B9 " + UtilMethods.PriceFormat(appointment.getPrice()));
                            }

                            //calculation for amount

                        } else {
                            Toast.makeText(requireContext(), "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case "POST NATAL CARE":
                        PackageNC homeHealthCarePackageNC = homeHealthCareViewModel.getPostNatalPackage().getValue();

                        //selected package details
                        if (homeHealthCarePackageNC != null) {


                            binding.txtCatValue.setVisibility(View.VISIBLE);
                            binding.txtCatValue.setText("" + homeHealthCarePackageNC.getCategory());

                            binding.hourValue.setVisibility(View.GONE);
                            binding.HoursLayout.setVisibility(View.GONE);
                            binding.durationValue.setVisibility(View.GONE);
                            binding.durationLabel.setVisibility(View.GONE);
                            binding.nursingCountValue.setVisibility(View.GONE);
                            binding.nursingCountLabel.setVisibility(View.GONE);
                            binding.txtNoMonthValue.setVisibility(View.GONE);
                            binding.txtNoMonthLabel.setVisibility(View.GONE);
                            binding.viewline.setVisibility(View.GONE);


                            Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                            if (appointment != null) {

                                if (appointment.getDate_condition() == 1) {
                                    //daily

                                    binding.txtDuration.setText(appointment.getDate_preference());

                                } else if (appointment.getDate_condition() == 2) {
                                    //Monthly

                                    //todo calculate the better dates to show
                                    binding.txtDuration.setText(appointment.getFrom_date() + "\n   To       \n" + appointment.getTo_date());
                                } else if (appointment.getDate_condition() == 3) {
                                    //date range

                                    binding.txtDuration.setText(appointment.getFrom_date() + "\n   To       \n" + appointment.getTo_date());
                                }


                            }

                            //calculation for amount
                            try {
                                //amount calculations
                                binding.textPackageRate.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackageNC.getPkgPriceMb()));
                                binding.textTotalAmount.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackageNC.getPkgPriceMb()));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(requireContext(), "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case "ELDER CARE":
                        PackageEC homeHealthCarePackageEC = homeHealthCareViewModel.getElderCarePackage().getValue();

                        if (homeHealthCarePackageEC != null) {


                            Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                            if (appointment != null) {

                                binding.txtDuration.setText(appointment.getDate_preference());
                                binding.txtCatValue.setVisibility(View.VISIBLE);
                                binding.txtCatValue.setText("" + homeHealthCarePackageEC.getCategory());
                                binding.textPackageRate.setText("\u20B9 "  + UtilMethods.PriceFormat(homeHealthCarePackageEC.getPkgPriceMb()));


                                binding.nursingCountLayout.setVisibility(View.GONE);
                                binding.HoursLayout.setVisibility(View.GONE);
                                binding.durationLayout.setVisibility(View.GONE);
                                binding.packageDurationLayout.setVisibility(View.GONE);
                                binding.textTotalAmount.setText("" +"\u20B9 "  + UtilMethods.PriceFormat(homeHealthCarePackageEC.getPkgPriceMb()));


                            } else {
                                //we did not fill the appointment request
                            }

                        } else {
                            Toast.makeText(requireContext(), "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    default:


                }

            } else {
                Toast.makeText(requireContext(), "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
            }
        });


        //appointment (queries related)
        binding.btnSchedule.setOnClickListener(v -> {

            remark = binding.remarkEditText.getText().toString();


            switch (homeHealthCareViewModel.getService()) {
                case "TRAINED ATTENDANT":
                    Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                    appointment.setRemarks(remark);
                    homeHealthCareViewModel.scheduleAppointment(appointment).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();

                                });
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                ImageView imageView = dialog.findViewById(R.id.image_confirm);
                                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rejectnew));
                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
                                appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            LogMyBenefits.e(LogTags.TRAINED_ATTENDANT, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;

                case "LONG TERM NURSING":
                    Appointment appointmentLT = homeHealthCareViewModel.getAppointmentRequest().getValue();
                    appointmentLT.setRemarks(remark);
                    homeHealthCareViewModel.scheduleAppointmentLT(appointmentLT).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();

                                });

                                dialog.show();

                            } else {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                ImageView imageView = dialog.findViewById(R.id.image_confirm);
                                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rejectnew));
                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
                                appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            LogMyBenefits.e(LogTags.LONGTERM_NURSING, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;

                case "SHORT TERM NURSING":
                    Appointment appointmentST = homeHealthCareViewModel.getAppointmentRequest().getValue();

                    appointmentST.setRemarks(remark);
                    homeHealthCareViewModel.scheduleAppointmentST(appointmentST).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(dialogInterface -> homeHealthCareViewModel.resetMessageResponse());


                                dialog.show();

                            } else {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                ImageView imageView = dialog.findViewById(R.id.image_confirm);
                                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rejectnew));
                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
                                appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(dialogInterface -> homeHealthCareViewModel.resetMessageResponse());

                                dialog.show();

                            }

                        } else {
                            LogMyBenefits.e(LogTags.SHORTTERM_NURSING, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;

                case "DOCTOR SERVICES":
                    Appointment appointmentDS = homeHealthCareViewModel.getAppointmentRequest().getValue();
                    appointmentDS.setRemarks(remark);
                    homeHealthCareViewModel.scheduleAppointmentDS(appointmentDS).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();

                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                ImageView imageView = dialog.findViewById(R.id.image_confirm);
                                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rejectnew));
                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
                                appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            LogMyBenefits.e(LogTags.DOCTOR_SERVICES, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;


                case "PHYSIOTHERAPY":
                    Appointment appointmentPT = homeHealthCareViewModel.getAppointmentRequest().getValue();
                    appointmentPT.setRemarks(remark);
                    homeHealthCareViewModel.scheduleAppointmentPT(appointmentPT).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                ImageView imageView = dialog.findViewById(R.id.image_confirm);
                                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rejectnew));
                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
                                appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            LogMyBenefits.e(LogTags.PHYSIOTHERAPY, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;

                case "DIABETES MANAGEMENT":
                    Appointment appointmentDM = homeHealthCareViewModel.getAppointmentRequest().getValue();
                    appointmentDM.setRemarks(remark);
                    homeHealthCareViewModel.scheduleAppointmentDM(appointmentDM).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                ImageView imageView = dialog.findViewById(R.id.image_confirm);
                                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rejectnew));
                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
                                appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            LogMyBenefits.e(LogTags.DIABETES_MANAGEMENT_ACTIVITY, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;


                case "POST NATAL CARE":
                    Appointment appointmentNC = homeHealthCareViewModel.getAppointmentRequest().getValue();
                    appointmentNC.setRemarks(remark);
                    homeHealthCareViewModel.scheduleAppointmentNC(appointmentNC).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                ImageView imageView = dialog.findViewById(R.id.image_confirm);
                                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rejectnew));
                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
                                appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            LogMyBenefits.e(LogTags.POST_NATAL, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;

                case "ELDER CARE":

                    Appointment appointmentEC = homeHealthCareViewModel.getAppointmentRequest().getValue();
                    appointmentEC.setRemarks(remark);

                    homeHealthCareViewModel.scheduleAppointmentEC(appointmentEC).observe(getViewLifecycleOwner(), messageResponse -> {
                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                ImageView imageView = dialog.findViewById(R.id.image_confirm);
                                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rejectnew));
                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
                                appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.show();

                            }

                        } else {
                            LogMyBenefits.e(LogTags.ELDER_CARE, "onSchedule: ERROR OCCURRED");

                        }
                    });
            }

        });

        return view;
    }
}