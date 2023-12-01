package com.csform.android.MB360.fitness;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTracker;
import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTrackerConnectionState;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentConnectedAppsBinding;
import com.csform.android.MB360.fitness.utils.RecyclerViewItemDecoration;
import com.csform.android.MB360.fitness.viewmodel.DeviceConnectionViewModel;

import java.text.MessageFormat;


public class ConnectedDeviceFragment extends Fragment {
    private FragmentConnectedAppsBinding fragmentConnectedAppsBinding;
    private View view;
    private DeviceConnectionAdapter deviceConnectionAdapter;
    private DeviceConnectionViewModel deviceConnectionViewModel;
    private Context mContext;
    String myDeviceModel = Build.MODEL;
    String myDeviceName = Build.MANUFACTURER;
    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentConnectedAppsBinding = FragmentConnectedAppsBinding.inflate(inflater, container, false);
        view = fragmentConnectedAppsBinding.getRoot();

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_fitness);
        navController = navHostFragment.getNavController();

        loadData();
        return view;
    }



    private void loadData() {
        fragmentConnectedAppsBinding.deviceLayout.deviceType.setText(MessageFormat.format("{0}  {1}", myDeviceName.toUpperCase(), myDeviceModel));
        fragmentConnectedAppsBinding.deviceLayout.deviceStatus.setBackgroundDrawable(getActivity().getDrawable(R.drawable.roundedwhite));
        fragmentConnectedAppsBinding.deviceLayout.deviceStatus.setTextColor(Color.parseColor("#04B360"));

        deviceConnectionViewModel = new ViewModelProvider(this).get(DeviceConnectionViewModel.class);

        deviceConnectionViewModel.getMutableData().observe(getViewLifecycleOwner(), deviceList -> {

            FitnessTracker fitnessTracker;

            if (deviceList.size() == 0) {
                fitnessTracker = new FitnessTracker(
                        FitnessTracker.Platform.FITBIT, FitnessTrackerConnectionState.DISCONNECTED
                );
                deviceList.add(fitnessTracker);
                fitnessTracker = new FitnessTracker(
                        FitnessTracker.Platform.GARMIN, FitnessTrackerConnectionState.DISCONNECTED
                );
                deviceList.add(fitnessTracker);

            }

            deviceConnectionAdapter = new DeviceConnectionAdapter(mContext, deviceList);
            fragmentConnectedAppsBinding.rvDevicesConnection.setLayoutManager(new LinearLayoutManager(mContext));
            RecyclerViewItemDecoration recyclerViewItemDecoration =
                    new RecyclerViewItemDecoration(mContext, LinearLayoutManager.VERTICAL, 10);
            fragmentConnectedAppsBinding.rvDevicesConnection.addItemDecoration(recyclerViewItemDecoration);
            fragmentConnectedAppsBinding.rvDevicesConnection.setAdapter(deviceConnectionAdapter);
            deviceConnectionAdapter.notifyDataSetChanged();

        });
    }


}