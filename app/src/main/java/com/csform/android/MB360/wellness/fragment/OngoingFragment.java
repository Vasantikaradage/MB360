package com.csform.android.MB360.wellness.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentOngoingBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.dashboardwellness.repository.responseclass.EmployeeCheckResponse;
import com.csform.android.MB360.wellness.healthcheckup.DiagnosticFragmentArgs;
import com.csform.android.MB360.wellness.healthcheckup.DiagnosticFragmentDirections;
import com.csform.android.MB360.wellness.healthcheckup.PrefDateFragmentArgs;
import com.csform.android.MB360.wellness.healthcheckup.PrefDateFragmentDirections;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.cancellationHC.OnCancellationHealthCheckup;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.rescheduleHC.OnRescheduleHealthCheckup;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.AppointmentHealthCheckup;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupCancelAppointmentRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupUpdatePaymentRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PackageInterface;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.ui.SummaryHealthCheckupAdapter;
import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.csform.android.MB360.wellness.homehealthcare.cancellation.OnCancellation;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.HomeHealthCarePackage;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ScheduledSummary;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryDS;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryNA;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryPT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryST;
import com.csform.android.MB360.wellness.homehealthcare.retrofit.RescheduleInterface;
import com.csform.android.MB360.wellness.homehealthcare.ui.SummaryAdapter;
import com.csform.android.MB360.wellness.homehealthcare.ui.adapters.DiabetesManagementOngoingAdapter;
import com.csform.android.MB360.wellness.homehealthcare.ui.adapters.DoctorServiceOnGoingAdapter;
import com.csform.android.MB360.wellness.homehealthcare.ui.adapters.ElderCareOnGoingAdapter;
import com.csform.android.MB360.wellness.homehealthcare.ui.adapters.LongTermOnGoingAdapter;
import com.csform.android.MB360.wellness.homehealthcare.ui.adapters.PhysiotherapyOngoingAdapter;
import com.csform.android.MB360.wellness.homehealthcare.ui.adapters.PostNatalCareOngoingAdapter;
import com.csform.android.MB360.wellness.homehealthcare.ui.adapters.ShortTermOngoingAdapter;
import com.csform.android.MB360.wellness.homehealthcare.ui.adapters.TrainedAttendantAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OngoingFragment extends Fragment implements OnCancellation, RescheduleInterface, OnCancellationHealthCheckup, OnRescheduleHealthCheckup {

    FragmentOngoingBinding binding;
    View view;

    ArrayList<AppointmentHealthCheckup> appointmentHealthCheckupList;

    //    View Models
    LoadSessionViewModel loadSessionViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;
    PackagesViewModel packagesViewModel;

    SummaryAdapter adapter;
    SummaryHealthCheckupAdapter adapter1;
    String extFamilySrNo = "";
    int canceled_appointment = 0;
    int total_summary = 0;

    NavController navController;

    Packages packages;
    Person person;

    HealthCheckupCancelAppointmentRequest healthCheckupCancelAppointmentRequest = new HealthCheckupCancelAppointmentRequest();

    String empIdNo;
    String groupCode;
    String familySrNo;
    String groupSrNo;

    ScheduledSummary scheduledSummary;

    public OngoingFragment() {
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
        binding = FragmentOngoingBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // View Models
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);


        homeHealthCareViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
           if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
           } else {
                binding.progressBar.setVisibility(View.GONE);
            }


        });
        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();


        getSummary();




        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

            empIdNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
            groupCode = loadSessionResponse.getGroupInfoData().getGroupcode();

            healthCheckupCancelAppointmentRequest.setEmpIdNo(empIdNo);
            healthCheckupCancelAppointmentRequest.setRemark("");

            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                familySrNo = employeeCheckResponse.getExtFamilySrNo();
                groupSrNo = employeeCheckResponse.getExtGroupSrNo();

                healthCheckupCancelAppointmentRequest.setFamilySrNo(familySrNo);
                healthCheckupCancelAppointmentRequest.setGroupSrNo(groupSrNo);


            });
        });

        return view;
    }

    private void
    getSummary() {

        //essentials String for summary parameters
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        EmployeeCheckResponse employeeCheckResponse = dashboardWellnessViewModel.getEmployeeWellnessDetailsData().getValue();


        if (loadSessionResponse != null) {
            empIdNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
            groupCode = loadSessionResponse.getGroupInfoData().getGroupcode();

            //setting emp id no in request body
            healthCheckupCancelAppointmentRequest.setEmpIdNo(empIdNo);

            if (employeeCheckResponse != null) {
                familySrNo = employeeCheckResponse.getExtFamilySrNo();
                groupSrNo = employeeCheckResponse.getExtGroupSrNo();

                //setting parameters in request body
                healthCheckupCancelAppointmentRequest.setFamilySrNo(familySrNo);
                healthCheckupCancelAppointmentRequest.setGroupSrNo(groupSrNo);
            } else {
                //something went wrong
            }
        } else {
            //something went wrong
        }


        switch (homeHealthCareViewModel.getService().toUpperCase()) {
            case "HEALTH CHECKUP":

                packagesViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
                    if (loading) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                    } else {
                        binding.progressBar.setVisibility(View.GONE);

                    }


                });


                //health check up ongoing appointments
                packagesViewModel.getOngoingAppointment(familySrNo, groupSrNo, empIdNo, groupCode).observe(getViewLifecycleOwner(), healthCheckupOngoingResponse -> {
                    if (healthCheckupOngoingResponse != null) {
                        if (healthCheckupOngoingResponse.getMessage() != null) {
                            if (healthCheckupOngoingResponse.getMessage().getStatus()) {
                                total_summary = healthCheckupOngoingResponse.getAppointmentList().size();
                                if (total_summary > 0) {

                                    binding.ongoingCycle.setVisibility(View.VISIBLE);
                                    binding.noHistoryImage.setVisibility(View.GONE);
                                    adapter1 = new SummaryHealthCheckupAdapter(healthCheckupOngoingResponse.getAppointmentList(), requireContext(), this, this);
                                    binding.ongoingCycle.setAdapter(adapter1);
                                } else {
                                    binding.noHistoryImage.setVisibility(View.VISIBLE);
                                    binding.ongoingCycle.setVisibility(View.GONE);
                                }
                            } else {
                                LogMyBenefits.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "getSummary: " + healthCheckupOngoingResponse.getMessage().getMessage());
                                binding.noHistoryImage.setVisibility(View.VISIBLE);
                                binding.ongoingCycle.setVisibility(View.GONE);
                            }
                        }
                    }else
                    {
                        binding.ongoingCycle.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.noHistoryImage.setVisibility(View.VISIBLE);
                    }
                });

                break;
            case "TRAINED ATTENDANT":
                homeHealthCareViewModel.getNASummaryFromServer(familySrNo).observe(getViewLifecycleOwner(), summaryResponse -> {
                    if (summaryResponse != null) {
                        if (summaryResponse.getMessage() != null) {
                            if (summaryResponse.getMessage().getStatus()) {
                                if (summaryResponse.getScheduledSummary().size() > 0) {
                                    //bind the recyclerview
                                    binding.ongoingCycle.setVisibility(View.VISIBLE);
                                    binding.noHistoryImage.setVisibility(View.GONE);
                                    TrainedAttendantAdapter adapter = new TrainedAttendantAdapter(summaryResponse.getScheduledSummary(), requireContext(), homeHealthCareViewModel.getService(), this, this);
                                    binding.ongoingCycle.setAdapter(adapter);
                                } else {
                                    binding.noHistoryImage.setVisibility(View.VISIBLE);
                                    binding.ongoingCycle.setVisibility(View.GONE);
                                }

                            }
                        }
                    }
                });
                break;
            case "LONG TERM NURSING":
                homeHealthCareViewModel.getLTSummaryFromServer(familySrNo).observe(getViewLifecycleOwner(), summaryResponse -> {
                    if (summaryResponse != null) {
                        if (summaryResponse.getMessage() != null) {
                            if (summaryResponse.getMessage().getStatus()) {
                                //bind the recyclerview
                                if (summaryResponse.getScheduledSummary().size() > 0) {
                                    binding.ongoingCycle.setVisibility(View.VISIBLE);
                                    binding.noHistoryImage.setVisibility(View.GONE);
                                    LongTermOnGoingAdapter adapter = new LongTermOnGoingAdapter(summaryResponse.getScheduledSummary(), requireContext(), homeHealthCareViewModel.getService(), this, this);
                                    binding.ongoingCycle.setAdapter(adapter);
                                } else {
                                    binding.noHistoryImage.setVisibility(View.VISIBLE);
                                    binding.ongoingCycle.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });
                break;
            case "SHORT TERM NURSING":
                homeHealthCareViewModel.getSTSummaryFromServer(familySrNo).observe(getViewLifecycleOwner(), summaryResponse -> {
                    total_summary = summaryResponse.getScheduledSummary().size();
                    if (total_summary > 0) {
                        binding.ongoingCycle.setVisibility(View.VISIBLE);
                        binding.noHistoryImage.setVisibility(View.GONE);
                        ShortTermOngoingAdapter adapter = new ShortTermOngoingAdapter(summaryResponse.getScheduledSummary(), requireContext(), homeHealthCareViewModel.getService(), this, this);
                        binding.ongoingCycle.setAdapter(adapter);
                    } else {
                        binding.noHistoryImage.setVisibility(View.VISIBLE);
                        binding.ongoingCycle.setVisibility(View.GONE);
                    }
                });

                break;
            case "DOCTOR SERVICES":
                homeHealthCareViewModel.getDSSummaryFromServer(familySrNo).observe(getViewLifecycleOwner(), summaryResponse -> {
                    if (summaryResponse != null) {
                        if (summaryResponse.getScheduledSummary().size() > 0) {
                            binding.ongoingCycle.setVisibility(View.VISIBLE);
                            binding.noHistoryImage.setVisibility(View.GONE);
                            DoctorServiceOnGoingAdapter adapter = new DoctorServiceOnGoingAdapter(summaryResponse.getScheduledSummary(), requireContext(), homeHealthCareViewModel.getService(), this, this);
                            binding.ongoingCycle.setAdapter(adapter);

                        } else {
                            binding.noHistoryImage.setVisibility(View.VISIBLE);
                            binding.ongoingCycle.setVisibility(View.GONE);
                        }
                    } else {
                        binding.noHistoryImage.setVisibility(View.VISIBLE);
                        binding.ongoingCycle.setVisibility(View.GONE);
                    }
                });

                break;
            case "PHYSIOTHERAPY":

                homeHealthCareViewModel.getPTSummaryFromServer(familySrNo).observe(getViewLifecycleOwner(), summaryResponse -> {
                    if (summaryResponse != null) {
                        if (summaryResponse.getScheduledSummary().size() > 0) {

                            binding.ongoingCycle.setVisibility(View.VISIBLE);
                            binding.noHistoryImage.setVisibility(View.GONE);
                            PhysiotherapyOngoingAdapter adapter = new PhysiotherapyOngoingAdapter(summaryResponse.getScheduledSummary(), requireContext(), homeHealthCareViewModel.getService(), this, this);
                            binding.ongoingCycle.setAdapter(adapter);

                        } else {
                            binding.noHistoryImage.setVisibility(View.VISIBLE);
                            binding.ongoingCycle.setVisibility(View.GONE);
                        }
                    } else {
                        binding.noHistoryImage.setVisibility(View.VISIBLE);
                        binding.ongoingCycle.setVisibility(View.GONE);
                    }
                });

                break;
            case "DIABETES MANAGEMENT":
                homeHealthCareViewModel.getDMSummaryFromServer(familySrNo).observe(getViewLifecycleOwner(), summaryResponse -> {
                    if (summaryResponse != null) {
                        if (summaryResponse.getScheduledSummary().size() > 0) {
                            binding.ongoingCycle.setVisibility(View.VISIBLE);
                            binding.noHistoryImage.setVisibility(View.GONE);
                            DiabetesManagementOngoingAdapter adapter = new DiabetesManagementOngoingAdapter(summaryResponse.getScheduledSummary(), requireContext(), homeHealthCareViewModel.getService(), this, this);
                            binding.ongoingCycle.setAdapter(adapter);
                        } else {
                            binding.noHistoryImage.setVisibility(View.VISIBLE);
                            binding.ongoingCycle.setVisibility(View.GONE);
                        }
                    } else {
                        binding.noHistoryImage.setVisibility(View.VISIBLE);
                        binding.ongoingCycle.setVisibility(View.GONE);
                    }
                });

                break;
            case "POST NATAL CARE":
                homeHealthCareViewModel.getNCSummaryFromServer(familySrNo).observe(getViewLifecycleOwner(), summaryResponse -> {
                    if (summaryResponse != null) {
                        if (summaryResponse.getScheduledSummary().size() > 0) {
                            binding.ongoingCycle.setVisibility(View.VISIBLE);
                            binding.noHistoryImage.setVisibility(View.GONE);
                            PostNatalCareOngoingAdapter adapter = new PostNatalCareOngoingAdapter(summaryResponse.getScheduledSummary(), requireContext(), this);
                            binding.ongoingCycle.setAdapter(adapter);
                        } else {
                            binding.noHistoryImage.setVisibility(View.VISIBLE);
                            binding.ongoingCycle.setVisibility(View.GONE);
                        }
                    } else {
                        binding.noHistoryImage.setVisibility(View.VISIBLE);
                        binding.ongoingCycle.setVisibility(View.GONE);
                    }
                });
                break;
            case "ELDER CARE":
                homeHealthCareViewModel.getECSummary(familySrNo);
                homeHealthCareViewModel.getEcSummaryData().observe(getViewLifecycleOwner(), summaryResponseEc -> {
                    if (summaryResponseEc != null) {
                        total_summary = summaryResponseEc.getScheduledSummary().size();
                        if (total_summary > 0) {
                            binding.ongoingCycle.setVisibility(View.VISIBLE);
                            binding.noHistoryImage.setVisibility(View.GONE);
                            ElderCareOnGoingAdapter adapter = new ElderCareOnGoingAdapter(summaryResponseEc.getScheduledSummary(), requireContext(), this);
                            binding.ongoingCycle.setAdapter(adapter);
                        } else {
                            binding.noHistoryImage.setVisibility(View.VISIBLE);
                            binding.ongoingCycle.setVisibility(View.GONE);
                        }
                    } else {
                        binding.noHistoryImage.setVisibility(View.VISIBLE);
                        binding.ongoingCycle.setVisibility(View.GONE);
                    }
                });
                break;
            default:
                break;
        }


    }

    @Override
    public void cancelAppointment(String ApptInfoSrNo, int position) {
        //this is for home health care appointment cancellation
        canceled_appointment++;

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_appt_cancellation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView tvcancel = dialog.findViewById(R.id.tvcancel);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        CardView btnschedule = dialog.findViewById(R.id.btnreschedule);

        tvcancel.setOnClickListener(v -> {
            homeHealthCareViewModel.cancelAppointment(ApptInfoSrNo).observe(getViewLifecycleOwner(), cancelResponse -> {
                if (cancelResponse.getApiStatus().getBoolStatus()) {
                    getSummary();
                    dialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "Cannot Cancel the Appointment!", Toast.LENGTH_SHORT).show();
                }
            });

        });

        btnschedule.setOnClickListener(v -> {
            dialog.dismiss();
            //reschedule here


        });

        iv_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    @Override
    public void cancelAppointmentHC(int position, AppointmentHealthCheckup healthCheckupPackage) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_appt_cancellation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView tvcancel = dialog.findViewById(R.id.tvcancel);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        CardView btnschedule = dialog.findViewById(R.id.btnreschedule);

        tvcancel.setOnClickListener(v -> {

            healthCheckupCancelAppointmentRequest.setPersonSrNo(healthCheckupPackage.getPersonSrNo());

            packagesViewModel.putCancelAppointment(healthCheckupCancelAppointmentRequest).observe(getViewLifecycleOwner(), cancel -> {

                if (cancel.getMessage().getStatus() != null && cancel.getMessage().getStatus()) {
                    dialog.dismiss();
                    // to update the recyclerview (health checkup adapter) adapter after cancellation
                    getSummary();
                    // packagesViewModel.resetCancelHC();
                } else if (!cancel.getMessage().getStatus()) {
                    dialog.dismiss();
                    Toast.makeText(requireContext(), "Something went wrong, Please try again later.", Toast.LENGTH_LONG).show();
                }
            });
        });

        btnschedule.setOnClickListener(v -> {
            dialog.dismiss();
            //reschedule here

        });

        iv_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    @Override
    public void resscheduleAppointmentHC(AppointmentHealthCheckup appointmentHealthCheckup) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_appt_reschedule);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        Button btnreschedule = dialog.findViewById(R.id.btnreschedule);

        btnreschedule.setOnClickListener(v -> {
            dialog.dismiss();
            //reschedule here
            rescheduleHealthCheckUP(appointmentHealthCheckup);
        });

        iv_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    private void rescheduleHealthCheckUP(AppointmentHealthCheckup appointmentHealthCheckup /** from ongoing fragment (health check-Up) **/) {
        //here we reschedule the health check-up appointment
        packagesViewModel.getPackagesData().observe(getViewLifecycleOwner(), packageResponse -> {

            person = packageResponse.getPackagesList().get(0).getPersons().get(0);
            person.setPersonSRNo(64258);
            packages = packageResponse.getPackagesList().get(0);

            appointmentHealthCheckup.setReschedule(true);

            NavDirections action = OngoingFragmentDirections.actionOngoingFragmentToDiagnosticFragment().setAppointmentHealthCheckupPackage(appointmentHealthCheckup);
            navController.navigate(action);

        });

    }

    @Override
    public void rescheduleAppointmentNA(OnGoingSummaryNA appointment) {
        //navigate
        LogMyBenefits.d("ON-GOING FRAGMENT", "rescheduleAppointmentNA: " + appointment);
        NavDirections actions = OngoingFragmentDirections.actionOngoingFragmentToRescheduleFragment().setOnGoingSummaryNA(appointment);
        navController.navigate(actions);
    }

    @Override
    public void rescheduleAppointmentST(OnGoingSummaryST appointment) {
//navigate
        NavDirections actions = OngoingFragmentDirections.actionOngoingFragmentToRescheduleFragment().setOnGoingSummaryST(appointment);
        navController.navigate(actions);
    }

    @Override
    public void rescheduleAppointmentLT(OnGoingSummaryLT appointment) {
//navigate
        NavDirections actions = OngoingFragmentDirections.actionOngoingFragmentToRescheduleFragment().setOnGoingSummaryLT(appointment);
        navController.navigate(actions);
    }

    @Override
    public void rescheduleAppointmentDS(OnGoingSummaryDS appointment) {
//navigate
        NavDirections actions = OngoingFragmentDirections.actionOngoingFragmentToRescheduleFragment().setOnGoingSummaryDS(appointment);
        navController.navigate(actions);
    }

    @Override
    public void rescheduleAppointmentPT(OnGoingSummaryPT appointment) {
//navigate
        NavDirections actions = OngoingFragmentDirections.actionOngoingFragmentToRescheduleFragment().setOnGoingSummaryPT(appointment);
        navController.navigate(actions);
    }
}