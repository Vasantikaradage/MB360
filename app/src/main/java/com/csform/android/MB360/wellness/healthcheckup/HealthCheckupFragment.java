package com.csform.android.MB360.wellness.healthcheckup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentHealthCheckupBinding;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.utilities.BadgesDrawable;
import com.csform.android.MB360.utilities.LoadingWellnessDialogue;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.wellness.WellnessDashBoardActivity;
import com.csform.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.AppointmentHealthCheckup;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.DiagnosticCenter;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PackageInterface;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PersonDelete;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.ui.PackageAdapter;

import java.util.ArrayList;
import java.util.List;


public class HealthCheckupFragment extends Fragment implements PackageInterface, PersonDelete {

    FragmentHealthCheckupBinding binding;
    View view;
    NavController navController;
    List<Packages> packageList = new ArrayList<>();

    //PackageHEALTHCHEKUP ViewModel
    PackagesViewModel packagesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    PackageAdapter adapter;

    LayerDrawable icon;

    LoadingWellnessDialogue loadingWellnessDialogue;

    public HealthCheckupFragment() {
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
        binding = FragmentHealthCheckupBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        ((WellnessDashBoardActivity) getActivity()).getSupportActionBar().setLogo(null);
        ((WellnessDashBoardActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);
        ((WellnessDashBoardActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);


        //menu
        setHasOptionsMenu(true);
        //loader
        loadingWellnessDialogue = new LoadingWellnessDialogue(requireContext(), requireActivity());

        //To Navigate

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);

        navController = navHostFragment.getNavController();


        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);

        // Overview
        binding.btnOverview.setOnClickListener(view -> {

            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToOverviewFragment();
            navController.navigate(actions);
        });

        // Pre-Test-Request
        binding.btnPreTest.setOnClickListener(view -> {

            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToPretestreqFragment();
            navController.navigate(actions);
        });

        // Terms&Condition
        binding.btnTandC.setOnClickListener(view -> {

            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToTcFragment();
            navController.navigate(actions);
        });


       /* binding.rlcartlayout.setOnClickListener(view -> {

            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToSummaryFragment();
            navController.navigate(actions);
        });*/


        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                String familySrNo = employeeCheckResponse.getExtFamilySrNo();
                String groupCode = loadSessionResponse.getGroupInfoData().getGroupcode();

                packagesViewModel.getSummary(familySrNo, groupCode);

            });
        });


        getPackage();

        //summary listener
        packagesViewModel.getSummaryData().observe(getViewLifecycleOwner(), summaryResponse -> {


            if (summaryResponse != null) {
                // Badge Count
                setBadgeCount(requireContext(), icon, String.valueOf(summaryResponse.getSummary().getSelfSponseredPerson().size()));
            } else {

            }


        });

        //loader listener
        packagesViewModel.getPackageLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_wellness_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.cartIT);
        icon = (LayerDrawable) item.getIcon();


    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        try {
            BadgesDrawable badge;

            // Reuse drawable if possible
            Drawable reuse = icon.findDrawableByLayerId(R.id.ic_cart);
            if (reuse instanceof BadgesDrawable) {
                badge = (BadgesDrawable) reuse;
            } else {
                badge = new BadgesDrawable(context);
            }

            badge.setCount(count);
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_cart, badge);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cartIT) {

            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToSummaryFragment();
            navController.navigate(actions);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPackage() {
        //to get the  policyFeatures data we need some parameters from load session values

        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {

            String groupCode = "" + loadSessionResponse.getGroupInfoData().getGroupcode();
            String empIdNo = "" + loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();

            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(requireActivity(), employeeCheckResponse -> {
                if (employeeCheckResponse.getExtGroupSrNo() != null) {
                    String extGroupSrno = employeeCheckResponse.getExtGroupSrNo();
                    packagesViewModel.getPackages(extGroupSrno, groupCode, empIdNo);
                } else {
                    Toast.makeText(getContext(), "package not available", Toast.LENGTH_SHORT).show();
                }
            });
        });

        packagesViewModel.getPackagesData().observe(getViewLifecycleOwner(), packageResponse -> {

            if (packageResponse != null) {
                packageList = packageResponse.getPackagesList();
                adapter = new PackageAdapter(requireContext(), packageList, this, this);
                binding.packageRecyclerView.setAdapter(adapter);
                /*adapter.notifyItemRangeChanged(0, packageResponse.getPackagesList().size());*/

                //handled loading here
                hideLoading();

                if (!packageResponse.getMessage().getStatus()) {
                    Toast.makeText(requireContext(), "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(requireContext(), "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void showLoading() {
        loadingWellnessDialogue.showLoading("Loading, Please wait...");
    }

    private void hideLoading() {
        loadingWellnessDialogue.hideLoading();
    }

    @Override
    public void getPackageDetails(Packages packages) {
        NavDirections action = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToPackageDetailsFragment(packages);
        navController.navigate(action);
    }

    @Override
    public void scheduleAppointment(Packages packages, Person person) {

        AppointmentHealthCheckup appointmentHealthCheckup = new AppointmentHealthCheckup();
        appointmentHealthCheckup.setRejApptSrNo("-1");
        appointmentHealthCheckup.setApptSrNo("0");
        appointmentHealthCheckup.setPersonSrNo(String.valueOf(person.getPersonSRNo()));
        appointmentHealthCheckup.setPaymentFlag(0);
        appointmentHealthCheckup.setPackageSrNo(packages.getPackageSrNo());
        appointmentHealthCheckup.setPackageName(packages.getPackageName());
        appointmentHealthCheckup.setPaymentFlag(0);
        appointmentHealthCheckup.setRelationId(person.getRelationID());
        appointmentHealthCheckup.setReschedule(false);


        NavDirections action = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToDiagnosticFragment().setAppointmentHealthCheckupPackage(appointmentHealthCheckup);
        navController.navigate(action);

    }

    @Override
    public void personDelete(String packagePosition, String personSrNo, int position, Person person) {
        //removing person from the list
        //adapter.personRemoved(packagePosition, position, person);


        packagesViewModel.dependentDelete(personSrNo).observe(getViewLifecycleOwner(), messageResponseDependentDelete -> {
            if (messageResponseDependentDelete.getStatus()) {
                Toast.makeText(requireContext(), "" + messageResponseDependentDelete.getMessage(), Toast.LENGTH_SHORT).show();

                switch (packagePosition) {
                    case "13":
                        packageList.get(0).getPersons().remove(position);
                        adapter.notifyItemChanged(0);
                        hideLoading();
                        break;
                    case "14":
                        packageList.get(1).getPersons().remove(position);
                        adapter.notifyItemChanged(1);
                        break;
                    case "15":
                        packageList.get(2).getPersons().remove(position);
                        adapter.notifyItemChanged(2);
                        break;
                    case "16":
                        packageList.get(3).getPersons().remove(position);
                        adapter.notifyItemChanged(3);
                        break;
                    case "17":
                        packageList.get(4).getPersons().remove(position);
                        adapter.notifyItemChanged(4);
                        break;
                    case "18":
                        packageList.get(5).getPersons().remove(position);
                        adapter.notifyItemChanged(5);
                        break;

                    default:
                        hideLoading();

                }
                hideLoading();


            } else {
                Toast.makeText(requireContext(), "" + messageResponseDependentDelete.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}