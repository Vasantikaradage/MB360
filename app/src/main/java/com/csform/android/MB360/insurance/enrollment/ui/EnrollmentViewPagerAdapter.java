/*
package com.csform.android.MB360.insurance.enrollment.ui;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.csform.android.MB360.insurance.enrollment.EnrollmentHomeFragment;
import com.csform.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentViewPagerAdapter extends FragmentStateAdapter {
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
*/
/*public class EnrollmentViewPagerAdapter extends FragmentStateAdapter {
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;

    EnrollmentWelcomeFragment enrollmentWelcomeFragment;
    InstructionFragment instructionFragment;
    MyCoveragesFragment myCoveragesFragment;
    EmployeeDetailsFragment employeeDetailsFragment;
    DependantDetailsFragment dependantDetailsFragment;
    ParentalDetailsFragment parentalDetailsFragment;
    TopUpFragment topUpFragment_GMC;
    TopUpFragment topUpFragment_GPA;
    TopUpFragment topUpFragment_GTL;
    EnrollmentSummaryFragment enrollmentSummaryFragment;

    public EnrollmentViewPagerAdapter(FragmentActivity activity) {
        super(activity);

    }

    public EnrollmentViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        super(fragmentManager, lifecycle);
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;

        enrollmentWelcomeFragment = new EnrollmentWelcomeFragment(viewPagerNavigationMenuHelper);
        instructionFragment = new InstructionFragment(viewPagerNavigationMenuHelper);
        myCoveragesFragment = new MyCoveragesFragment(viewPagerNavigationMenuHelper);
        employeeDetailsFragment = new EmployeeDetailsFragment(viewPagerNavigationMenuHelper);
        dependantDetailsFragment = new DependantDetailsFragment(viewPagerNavigationMenuHelper);
        parentalDetailsFragment = new ParentalDetailsFragment(viewPagerNavigationMenuHelper);
        topUpFragment_GMC = new TopUpFragment(viewPagerNavigationMenuHelper, "GMC");
        topUpFragment_GPA = new TopUpFragment(viewPagerNavigationMenuHelper, "GPA");
        topUpFragment_GTL = new TopUpFragment(viewPagerNavigationMenuHelper, "GTL");
        enrollmentSummaryFragment = new EnrollmentSummaryFragment(viewPagerNavigationMenuHelper);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return enrollmentWelcomeFragment;
            case 1:
                return instructionFragment;
            case 2:
                return myCoveragesFragment;
            case 3:
                return employeeDetailsFragment;
            case 4:
                return dependantDetailsFragment;
            case 5:
                return parentalDetailsFragment;
            case 6:
                return topUpFragment_GMC;
            case 7:
                return topUpFragment_GPA;
            case 8:
                return topUpFragment_GTL;
            case 9:
                return enrollmentSummaryFragment;
            default:
                //error due to the viewpager position
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }*//*


    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public EnrollmentViewPagerAdapter(FragmentManager supportFragmentManager, Lifecycle lifecycle, ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        super(supportFragmentManager,lifecycle);
        this.viewPagerNavigationMenuHelper=viewPagerNavigationMenuHelper;
    }

    */
/*public EnrollmentViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }*//*


  */
/*  @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }*//*


    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // return null;
        return mFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }


}
*/


package com.csform.android.MB360.insurance.enrollment.ui;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.csform.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;

public class EnrollmentViewPagerAdapter extends FragmentStateAdapter {
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;

    EnrollmentWelcomeFragment enrollmentWelcomeFragment;
    InstructionFragment instructionFragment;
    MyCoveragesFragment myCoveragesFragment;
    EmployeeDetailsFragment employeeDetailsFragment;
    DependantDetailsFragment dependantDetailsFragment;
    ParentalDetailsFragment parentalDetailsFragment;
    TopUpFragment topUpFragment_GMC;
    TopUpFragment topUpFragment_GPA;
    TopUpFragment topUpFragment_GTL;
    EnrollmentSummaryFragment enrollmentSummaryFragment;

    public EnrollmentViewPagerAdapter(FragmentActivity activity) {
        super(activity);

    }

    public EnrollmentViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        super(fragmentManager, lifecycle);
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;

        enrollmentWelcomeFragment = new EnrollmentWelcomeFragment(viewPagerNavigationMenuHelper);
        instructionFragment = new InstructionFragment(viewPagerNavigationMenuHelper);
        myCoveragesFragment = new MyCoveragesFragment(viewPagerNavigationMenuHelper);
        employeeDetailsFragment = new EmployeeDetailsFragment(viewPagerNavigationMenuHelper);
        dependantDetailsFragment = new DependantDetailsFragment(viewPagerNavigationMenuHelper);
        parentalDetailsFragment = new ParentalDetailsFragment(viewPagerNavigationMenuHelper);
        topUpFragment_GMC = new TopUpFragment(viewPagerNavigationMenuHelper, "GMC");
        topUpFragment_GPA = new TopUpFragment(viewPagerNavigationMenuHelper, "GPA");
        topUpFragment_GTL = new TopUpFragment(viewPagerNavigationMenuHelper, "GTL");
        enrollmentSummaryFragment = new EnrollmentSummaryFragment(viewPagerNavigationMenuHelper);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return enrollmentWelcomeFragment;
            case 1:
                return instructionFragment;
            case 2:
                return myCoveragesFragment;
            case 3:
                return employeeDetailsFragment;
            case 4:
                return dependantDetailsFragment;
            case 5:
                return parentalDetailsFragment;
            case 6:
                return topUpFragment_GMC;
            case 7:
                return topUpFragment_GPA;
            case 8:
                return topUpFragment_GTL;
            case 9:
                return enrollmentSummaryFragment;
            default:
                //error due to the viewpager position
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}

