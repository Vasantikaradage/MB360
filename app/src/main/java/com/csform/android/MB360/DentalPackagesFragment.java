package com.csform.android.MB360;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csform.android.MB360.databinding.FragmentDentalPackagesBinding;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.DentalPackage;
import com.csform.android.MB360.wellness.homehealthcare.ui.adapters.DentalPackageAdapter;

import java.util.ArrayList;
import java.util.List;


public class DentalPackagesFragment extends Fragment {

    FragmentDentalPackagesBinding binding;
    View view;
    List<DentalPackage> dentalList = new ArrayList<>();

    public DentalPackagesFragment() {
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
        binding = FragmentDentalPackagesBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        binding.dentalPackageCycle.showShimmer();
        setUpRecyclerView();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void setUpRecyclerView() {
        //offline response example for dental package
        dentalList.add(new DentalPackage("CLEANING AND POLISHING WITH DENTAL CHECKUP", "₹ 499", "https://wellness.mybenefits360.com/images/dentalPackages/cleaning.svg"));
        dentalList.add(new DentalPackage("CLEANING AND FILLING WITH DENTAL CHECKUP", "₹ 1999", "https://wellness.mybenefits360.com/images/dentalPackages/cavityfilling.svg"));
        dentalList.add(new DentalPackage("CLEANING AND WHITENING WITH DENTAL CHECKUP", "₹ 3,999", "https://wellness.mybenefits360.com/images/dentalPackages/teethwhitening.svg"));
        dentalList.add(new DentalPackage("ROOT CANAL AND CROWN WITH DENTAL CHECKUP", "₹ 4,999", "https://wellness.mybenefits360.com/images/dentalPackages/rootcanal.svg"));
        dentalList.add(new DentalPackage("WISDOM TOOTH EXTRACTION", "₹ 4,499", "https://wellness.mybenefits360.com/images/dentalPackages/toothextraction.svg"));
        dentalList.add(new DentalPackage("DENTAL IMPLANT WITH CERAMIC CROWN(KOREAN)", "₹ 17,999", "https://wellness.mybenefits360.com/images/dentalPackages/korean.svg"));
        dentalList.add(new DentalPackage("DENTAL IMPLANT WITH CERAMIC CROWN(SWISS)", "₹ 27,999", "https://wellness.mybenefits360.com/images/dentalPackages/swiss.svg"));
        dentalList.add(new DentalPackage("DENTAL IMPLANT WITH CERAMIC CROWN(GERMAN)", "₹ 37,999", "https://wellness.mybenefits360.com/images/dentalPackages/german.svg"));
        dentalList.add(new DentalPackage("SMILE DESIGNING", "₹ 76,999", "https://wellness.mybenefits360.com/images/dentalPackages/smiledesign.svg"));
        dentalList.add(new DentalPackage("SINGLE ARCH REHAB", "₹ 1,09,999", "https://wellness.mybenefits360.com/images/dentalPackages/singlearch.svg"));
        dentalList.add(new DentalPackage("DOUBLE ARCH REHAB", "₹ 2,09,999", "https://wellness.mybenefits360.com/images/dentalPackages/doublearch.svg"));

        DentalPackageAdapter adapter = new DentalPackageAdapter(dentalList, requireContext());
        binding.dentalPackageCycle.setAdapter(adapter);
        binding.dentalPackageCycle.hideShimmer();
    }
}