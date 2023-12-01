package com.csform.android.MB360.wellness.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentHistoryBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.cancellationHC.OnCancellationHealthCheckup;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.rescheduleHC.OnRescheduleHealthCheckup;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.AppointmentHealthCheckup;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupCancelAppointmentRequest;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.ui.SummaryHealthCheckupAdapter;
import com.csform.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.csform.android.MB360.wellness.homehealthcare.cancellation.OnCancellation;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ScheduledSummary;
import com.csform.android.MB360.wellness.homehealthcare.retrofit.RescheduleInterface;
import com.csform.android.MB360.wellness.homehealthcare.ui.SummaryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class HistoryFragment extends Fragment{

    FragmentHistoryBinding binding;

    //change adapter regarding the page
    //adapters

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


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // View Models
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();




        homeHealthCareViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });


        return view;
    }


}