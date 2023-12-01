package com.csform.android.MB360.insurance.enrollment.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentDependantDetailsBinding;
import com.csform.android.MB360.insurance.enrollment.adapters.DependantDetailsAdapter;
import com.csform.android.MB360.insurance.enrollment.interfaces.DependantHelper;
import com.csform.android.MB360.insurance.enrollment.interfaces.DependantListener;
import com.csform.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.csform.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.Dependent;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.ParentalModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.Relation;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.Dependant;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.RelationMainResponse;
import com.csform.android.MB360.insurance.enrollment.ui.bottomSheets.AddDependantBottomSheet;
import com.csform.android.MB360.insurance.enrollment.ui.bottomSheets.AddTwinsBottomSheet;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.SwipeToDeleteCallback;
import com.csform.android.MB360.utilities.WindowPeriodCounter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependantDetailsFragment extends Fragment implements DependantHelper, DependantListener, SwipeToDeleteCallback.RecyclerItemTouchHelperListener, ConfirmationDialogs.DialogActions {

    //TODO : SOLVE THE ISSUES FOR DELETING THE TWIN.
    //TODO : SOLVE THE ISSUES FOR THE EDITING THE PEOPLE.......................✅
    //TODO : REFINE THE
    //TODO : UNIT TEST ()


    FragmentDependantDetailsBinding binding;
    View view;
    //view-model for enrollment
    EnrollmentViewModel enrollmentViewModel;
    LoadSessionViewModel loadSessionViewModel;
    //recyclerview adapter
    DependantDetailsAdapter adapter;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    boolean isWindowPeriodActive = false;

    //data list for adding deleting and  editing of the dependant.
    List<DependantHelperModel> dependantList = new ArrayList<>();

    DependantHelperModel lgbtqModel = new DependantHelperModel("PARTNER", 1, "1", false, "", "", false, null, "", true, true, "", "22", "");

    boolean SPOUSE = true;
    boolean LGBTQ = true; //done (need special handling of the bottom sheet)
    boolean daughter_not_married = false; //will be done we get the max age.
    boolean parents_allowed = false; //done
    boolean cross_combination = false;//need info
    boolean twins_allowed = true; //twins needed


    boolean animations = true;
    int animation_count = 0;

    String groupChilSrNo = "";
    String employeeSrNo = "";
    String oeGrpBasInfoSrNo = "";

    public DependantDetailsFragment() {
        // Required empty public constructor
    }

    public DependantDetailsFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;

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
        binding = FragmentDependantDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        //to show summary option
        viewPagerNavigationMenuHelper.showSummaryOption();

        //sharing the view model scope same as the activity (insurance activity)
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);


        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        try {

            if (loadSessionResponse != null) {
                groupChilSrNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                employeeSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                List<GroupPolicyData> policyList = loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData();

                for (GroupPolicyData policy : policyList) {
                    if (policy.getPolicyType().equalsIgnoreCase("base")) {
                        oeGrpBasInfoSrNo = policy.getOeGrpBasInfSrNo();
                    } else {
                        //nothing
                    }
                }
            } else {
                //nothing
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        binding.instCard1.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            try {
                viewPagerNavigationMenuHelper.onHideMenu();
                if (scrollY < 40) {
                    viewPagerNavigationMenuHelper.onShowMenu();
                } else {
                    viewPagerNavigationMenuHelper.onHideMenu();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //first get the window period,
        //then get the relationship group to show
        //then get the dependents
        //merge the relationship group and dependants with {@DependantHelperModel} class

        getWindowPeriodTimer();

        if (employeeSrNo != null && groupChilSrNo != null && oeGrpBasInfoSrNo != null) {
            enrollmentViewModel.getRelationshipGroup(employeeSrNo, groupChilSrNo, oeGrpBasInfoSrNo, "1");
        } else {
            //something went wrong
        }
        return view;
    }

    private void getWindowPeriodTimer() {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriod.getWindowPeriod().getWindowEndDateGmc(), requireContext(), requireActivity());
            //timer comment

            try {
                CountDownTimer timer = windowPeriodCounter.getTimer(binding.timerDependantDetails);
                if (timer != null) {
                    isWindowPeriodActive = true;
                    timer.start();
                    //swipe to delete
                    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new SwipeToDeleteCallback(requireContext(), 0, ItemTouchHelper.LEFT, this, dependantList);
                    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.dependantCycle);

                } else {
                    isWindowPeriodActive = false;
                    binding.timerDependantDetails.setText("Window period has expired");
                }
            } catch (ParseException e) {
                //unable to parse date
                isWindowPeriodActive = false;
                Toast.makeText(requireContext(), "Unable to retrieve window period date.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            //no window period available
        }
    }

    private void showLoading() {
        binding.swipetoRefresh.setRefreshing(true);
    }

    private void hideLoading() {
        binding.swipetoRefresh.setRefreshing(false);
    }

    //on dependant clicked from recyclerview.
    @Override
    public void onAddDependant(DependantHelperModel dependant, int position) {
        animations = false;
        //here we need to add the interface to get the details of the dependant
        //so that we can get the details of the dependant added and update the recyclerview.

        switch (dependant.getRelationName().toLowerCase()) {
            case "twins":
                AddTwinsBottomSheet twinsDialog = new AddTwinsBottomSheet(this, position);
                twinsDialog.setCancelable(true);
                twinsDialog.show(getChildFragmentManager(), twinsDialog.getTag());

                break;
            default:
                AddDependantBottomSheet dialog = new AddDependantBottomSheet(this, dependant, position);
                dialog.show(getChildFragmentManager(), dialog.getTag());
                dialog.setCancelable(true);
        }


    }


    @Override
    public void onEditDependant(DependantHelperModel dependant, int position) {
        animations = false;
        //here we edit the dependant the details
        //tip :- here we follow same procedure as adding the data
        //as the data in dependant details is never added but retained the state from empty
        //to filled dependant state.

        switch (dependant.getRelationName().toLowerCase()) {
            //we don't need this case
            case "twins":
               /* AddTwinsBottomSheet twinsDialog = new AddTwinsBottomSheet(this, position);
                twinsDialog.show(getChildFragmentManager(), twinsDialog.getTag());
                twinsDialog.setCancelable(true);*/

                break;
            default:
                AddDependantBottomSheet dialog = new AddDependantBottomSheet(this, dependant, position, true);
                dialog.show(getChildFragmentManager(), dialog.getTag());
                dialog.setCancelable(true);
        }


    }

    @Override
    public void onDeleteDependant() {
        animations = false;
    }

    @Override
    public void onEditTwin(int position) {
        animations = false;
        AddTwinsBottomSheet twinsDialog = new AddTwinsBottomSheet(this, position, true);
        twinsDialog.show(getChildFragmentManager(), twinsDialog.getTag());
        twinsDialog.setCancelable(true);

    }

    //on dependant changed in bottom sheets
    @Override
    public void onDependantSavedListener(DependantHelperModel dependant, int position) {
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        for (DependantHelperModel dependantCycle : dependantList) {
            if (dependantCycle.getRelationName().equalsIgnoreCase(dependant.getRelationName())) {
                dependantCycle.setDateOfBirth(dependant.getDateOfBirth());
                dependantCycle.setName(dependant.getName());
                dependantCycle.setIsAdded(true);
                dependantCycle.setIsDifferentlyAble(dependant.isDifferentlyAble());
                dependantCycle.setDocument(dependant.getDocument());
                dependantCycle.setCanEdit(true);
                dependantCycle.setCanDelete(true);
                dependantCycle.setAge(dependant.getAge());
            }


        }

        if (LGBTQ) {
            if (dependant.getRelationName().equalsIgnoreCase("spouse")) {
                dependantList.removeIf(dependantHelperModel -> (dependantHelperModel.getRelationName().equalsIgnoreCase("partner")));
            } else if (dependant.getRelationName().equalsIgnoreCase("partner")) {
                dependantList.removeIf(dependantHelperModel -> (dependantHelperModel.getRelationName().equalsIgnoreCase("spouse")));
            }
        }
        //api call of adding the dependant
        if (loadSessionResponse != null) {
            Map<String, String> dependantQueryMap = new HashMap<>();
            try {
                dependantQueryMap.put("EmployeeSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
                dependantQueryMap.put("RelationID", dependant.getRelationID());
                dependantQueryMap.put("DateOfMarriage", /* TODO */ "");
                dependantQueryMap.put("WindowPeriodActive", "1");
                dependantQueryMap.put("GroupChildSrNo", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
                dependantQueryMap.put("OeGrpBasInfSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo());
                dependantQueryMap.put("Gender", dependant.getGender());
                dependantQueryMap.put("IsTwins", "0");
                dependantQueryMap.put("Age", dependant.getAge());
                dependantQueryMap.put("DateOfBirth", dependant.getDateOfBirth());
                dependantQueryMap.put("DependentName", dependant.getName());
                dependantQueryMap.put("NicNumber", "");
                dependantQueryMap.put("AadharNumber", "");
                dependantQueryMap.put("CoveredInPolicyType", "1");
                dependantQueryMap.put("personSrNo", "");
                dependantQueryMap.put("ParentalPremiuimSeparate", "0");
                dependantQueryMap.put("BaseSI", "");

                LogMyBenefits.d(LogTags.ENROLLMENT, "Add dependant: " + dependantQueryMap.toString());


                //call for adding dependant
                enrollmentViewModel.addDependantData(dependantQueryMap).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        if (!message.isError() && !message.isLoading()) {

                            if (message.getAddDependantResponse().getMessage() != null && message.getAddDependantResponse().getStatus()) {
                                Toast.makeText(requireContext(), message.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                refreshDependant();
                                adapter.notifyDataSetChanged();
                            } else {
                                refreshDependant();
                                adapter.notifyDataSetChanged();

                                Toast.makeText(requireContext(), message.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (message.isLoading()) {
                        binding.swipetoRefresh.setRefreshing(true);
                        //show loading
                    } else if (message.isError()) {
                        //show error
                        binding.swipetoRefresh.setRefreshing(false);
                    } else {
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogMyBenefits.e(LogTags.ENROLLMENT, "Dependant Details: load-session is null");
        }


    }

    @Override
    public void onTwinsAdded(DependantHelperModel twin1, DependantHelperModel twin2, int position, boolean edit) {
        //here we add twins with 2 dependant classes.
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
      /*  if (edit) {
            enrollmentViewModel.setTwin1(twin1);
            enrollmentViewModel.setTwin2(twin2);
            dependantList.removeIf(dependantHelperModel -> (dependantHelperModel.getRelationName().equalsIgnoreCase("twins")));
            int twins_position = 3;
            if (dependantList.contains(lgbtqModel)) {
                if (dependantList.get(1).getIsAdded()) {
                    twins_position = 3;
                }
                twins_position = 4;
            } else {
                twins_position = 3;
            }

            dependantList.add(twins_position, twin1);
            dependantList.add(twins_position + 1, twin2);

            adapter.notifyDataSetChanged();

        } else {

            for (DependantHelperModel dependantCycle : dependantList) {
                if (dependantCycle.getRelationName().equalsIgnoreCase(twin1.getRelationName())) {
                    dependantCycle.setDateOfBirth(twin1.getDateOfBirth());
                    dependantCycle.setName(twin1.getName());
                    dependantCycle.setIsAdded(true);
                    dependantCycle.setAge(twin1.getAge());
                    dependantCycle.setIsDifferentlyAble(twin1.isDifferentlyAble());
                    dependantCycle.setDocument(twin1.getDocument());
                    dependantCycle.setCanDelete(true);
                    enrollmentViewModel.setTwin1(dependantCycle);
                }
                adapter.notifyItemChanged(position);
            }

            enrollmentViewModel.setTwin2(twin2);
            dependantList.add(position + 1, twin2);
            adapter.notifyItemInserted(position + 1);
        }*/

        if (loadSessionResponse != null) {
            Map<String, String> dependantQueryMap = new HashMap<>();
            try {
                dependantQueryMap.put("EmployeeSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
                dependantQueryMap.put("RelationID", twin1.getRelationID());
                dependantQueryMap.put("DateOfMarriage", /* TODO */ "");
                dependantQueryMap.put("WindowPeriodActive", "1");
                dependantQueryMap.put("GroupChildSrNo", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
                dependantQueryMap.put("OeGrpBasInfSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo());
                dependantQueryMap.put("Gender", twin1.getGender());
                dependantQueryMap.put("IsTwins", "0");
                dependantQueryMap.put("Age", twin1.getAge());
                dependantQueryMap.put("DateOfBirth", twin1.getDateOfBirth());
                dependantQueryMap.put("DependentName", twin2.getName());
                dependantQueryMap.put("NicNumber", "");
                dependantQueryMap.put("AadharNumber", "");
                dependantQueryMap.put("CoveredInPolicyType", "1");
                dependantQueryMap.put("personSrNo", "");
                dependantQueryMap.put("ParentalPremiuimSeparate", "0");
                dependantQueryMap.put("BaseSI", "");

                LogMyBenefits.d(LogTags.ENROLLMENT, "Add dependant: " + dependantQueryMap.toString());


                //call for adding dependant
                enrollmentViewModel.addDependantData(dependantQueryMap).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        if (!message.isError() && !message.isLoading()) {

                            if (message.getAddDependantResponse().getMessage() != null && message.getAddDependantResponse().getStatus()) {
                                Toast.makeText(requireContext(), message.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                refreshDependant();
                            } else {
                                refreshDependant();
                                Toast.makeText(requireContext(), message.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (message.isLoading()) {
                        binding.swipetoRefresh.setRefreshing(true);
                        //show loading
                    } else if (message.isError()) {
                        //show error
                        binding.swipetoRefresh.setRefreshing(false);
                    } else {
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                dependantQueryMap.put("EmployeeSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
                dependantQueryMap.put("RelationID", twin2.getRelationID());
                dependantQueryMap.put("DateOfMarriage", /* TODO */ "");
                dependantQueryMap.put("WindowPeriodActive", "1");
                dependantQueryMap.put("GroupChildSrNo", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
                dependantQueryMap.put("OeGrpBasInfSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo());
                dependantQueryMap.put("Gender", twin2.getGender());
                dependantQueryMap.put("IsTwins", "0");
                dependantQueryMap.put("Age", twin2.getAge());
                dependantQueryMap.put("DateOfBirth", twin2.getDateOfBirth());
                dependantQueryMap.put("DependentName", twin2.getName());
                dependantQueryMap.put("NicNumber", "");
                dependantQueryMap.put("AadharNumber", "");
                dependantQueryMap.put("CoveredInPolicyType", "1");
                dependantQueryMap.put("personSrNo", "");
                dependantQueryMap.put("ParentalPremiuimSeparate", "0");
                dependantQueryMap.put("BaseSI", "");

                LogMyBenefits.d(LogTags.ENROLLMENT, "Add dependant: " + dependantQueryMap.toString());


                //call for adding dependant
                enrollmentViewModel.addDependantData(dependantQueryMap).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        if (!message.isError() && !message.isLoading()) {

                            if (message.getAddDependantResponse().getMessage() != null && message.getAddDependantResponse().getStatus()) {
                                Toast.makeText(requireContext(), message.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                refreshDependant();
                            } else {
                                refreshDependant();
                                Toast.makeText(requireContext(), message.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (message.isLoading()) {
                        binding.swipetoRefresh.setRefreshing(true);
                        //show loading
                    } else if (message.isError()) {
                        //show error
                        binding.swipetoRefresh.setRefreshing(false);
                    } else {
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogMyBenefits.e(LogTags.ENROLLMENT, "Dependant Details: load-session is null");
        }


    }

    @Override
    public void onDependantEditedListener(DependantHelperModel dependant, int position) {
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {
            //here we update the dependant
            Map<String, String> dependantQueryMap = new HashMap<>();
            dependantQueryMap.put("PersonSrNo", dependant.getPersonSrno());
            dependantQueryMap.put("Age", dependant.getAge());
            dependantQueryMap.put("DateOfBirth", dependant.getDateOfBirth());
            dependantQueryMap.put("RelationID", dependant.getRelationID());
            dependantQueryMap.put("Relation", dependant.getRelationName());
            dependantQueryMap.put("EmployeeSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
            dependantQueryMap.put("IsSelected", "1");
            dependantQueryMap.put("IsApplicable", "1");
            dependantQueryMap.put("CanDelete", "0");
            dependantQueryMap.put("Gender", dependant.getGender());
            dependantQueryMap.put("ReasonForNotAbleToDelete", "NA");
            dependantQueryMap.put("CanUpdate", "1");
            dependantQueryMap.put("IsTwins", "1");
            dependantQueryMap.put("ReasonForNotAbleToUpdate", "");
            dependantQueryMap.put("CanUpdate", "1");
            dependantQueryMap.put("IsDisabled", "0");
            dependantQueryMap.put("PairNo", "0");
            dependantQueryMap.put("ExtraChildPremium", "0");
            dependantQueryMap.put("IsParentOpted", "0");
            dependantQueryMap.put("Premium", "0");
            dependantQueryMap.put("SumInsured", "0");
            dependantQueryMap.put("AadharNo", "0");
            dependantQueryMap.put("GroupChildSrNo", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
            dependantQueryMap.put("ParentalPremiuimSeparate", "0");
            dependantQueryMap.put("BaseSI", "0");
            dependantQueryMap.put("OeGrpBasInfSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo());
            dependantQueryMap.put("Name", dependant.getName());

            enrollmentViewModel.updateDependantData(dependantQueryMap).observe(getViewLifecycleOwner(), message -> {
                if (message != null) {
                    if (!message.isError() && !message.isLoading()) {

                        if (message.getAddDependantResponse().getStatus()) {
                            Toast.makeText(requireContext(), message.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            refreshDependant();
                        } else {
                            Toast.makeText(requireContext(), message.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (message.isLoading()) {
                        binding.swipetoRefresh.setRefreshing(true);
                        //show loading
                    } else if (message.isError()) {
                        //show error
                        binding.swipetoRefresh.setRefreshing(false);
                    }
                }
            });

            LogMyBenefits.d(LogTags.ENROLLMENT, "onDependantEditedListener: " + dependantQueryMap.toString());

        } else {
            LogMyBenefits.d(LogTags.ENROLLMENT, "onDependantEditedListener: Load-session is null");
        }

    }

    @Override
    public void onDependantDeletedListener(DependantHelperModel dependant) {
        animations = false;
    }


    @Override
    public void onConfirmed(int position, DependantHelperModel deleteDependant, ParentalModel parentalModel) {
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        //here we get the confirmation for deleting a dependant
        //and we delete
        if (loadSessionResponse != null) {
            try {
                //deletion of the dependant api call
                Map<String, String> dependantOption = new HashMap<>();
                dependantOption.put("EmpSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
                dependantOption.put("GroupChildSrNo", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
                dependantOption.put("Persrno", deleteDependant.getPersonSrno());


                enrollmentViewModel.deleteDependantData(dependantOption).observe(getViewLifecycleOwner(), enrollmentMessage -> {
                    if (enrollmentMessage != null) {

                        if (!enrollmentMessage.isError() && !enrollmentMessage.isLoading()) {
                            Toast.makeText(getActivity(), "" + enrollmentMessage.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            refreshDependant();


                            //todo add the partner if spouse is deleted
                            if (deleteDependant.getRelationName().equalsIgnoreCase("spouse")) {
                                SPOUSE = true;
                                try {
                                    List<Relation> relationList = enrollmentViewModel.getRelationshipGroupData().getValue().getRelations();

                                    for (Relation relation : relationList) {
                                        if (relation.getRelation().equalsIgnoreCase("partner")) {
                                            LGBTQ = true;
                                            dependantList.add(1, lgbtqModel);
                                        } else {
                                            LGBTQ = false;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LGBTQ = false;
                                }

                            }
                            if (deleteDependant.getRelationName().equalsIgnoreCase("partner")) {
                                LGBTQ = true;
                                SPOUSE = true;
                            }

                        } else if (enrollmentMessage.isLoading()) {
                            binding.swipetoRefresh.setRefreshing(true);
                            //show loading
                        } else if (enrollmentMessage.isError()) {
                            //show error
                            binding.swipetoRefresh.setRefreshing(false);
                        }
                    } else {
                        //something went wrong
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRejected(int position, DependantHelperModel dependantHelperModel, ParentalModel parentalModel) {
        //user does not want to delete the dependant.
        animations = false;
        dependantList.get(position).setCanEdit(true);
        dependantList.get(position).setCanDelete(true);
        adapter.notifyItemChanged(position);
    }


    @Override
    public void onDependantSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position, DependantHelperModel dependantHelperModel) {
        //here we can listen that it is swiped
        //logic of resetting the data
        //ask for confirmation
        animations = false;
        ConfirmationDialogs confirmationDialogs = new ConfirmationDialogs(requireContext(), this, position, dependantHelperModel);
        confirmationDialogs.setCancelable(false);
        confirmationDialogs.show(getChildFragmentManager(), confirmationDialogs.getTag());
    }


    @Override
    public void onResume() {
        super.onResume();

        if (animation_count > 0) {
            animations = false;
        } else {
            animations = true;


            Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_down);
            binding.headerDependantImage.startAnimation(animation);
            binding.headerDependantText.startAnimation(animation);
            binding.timerHolder.startAnimation(animation);
            binding.inst1.startAnimation(animation);

            binding.headerDependantImage.setVisibility(View.VISIBLE);
            binding.headerDependantText.setVisibility(View.VISIBLE);
            binding.timerHolder.setVisibility(View.VISIBLE);
            binding.inst1.setVisibility(View.VISIBLE);

        }

        animation_count++;


        refreshDependant();
        binding.swipetoRefresh.setOnRefreshListener(this::refreshDependant);
    }


    //refresh the dependant data
    private void refreshDependant() {
        dependantList.clear();

        enrollmentViewModel.getRelationshipGroup(employeeSrNo, groupChilSrNo, oeGrpBasInfoSrNo, "1")
                .observe(requireActivity(), relationMainResponse -> {
                    //RelationMainResponse relationMainResponse = enrollmentViewModel.getRelationshipGroup(employeeSrNo, groupChilSrNo, oeGrpBasInfoSrNo, "1").getValue();
                    enrollmentViewModel.getDependants(employeeSrNo, groupChilSrNo, oeGrpBasInfoSrNo, "1")
                            .observe(requireActivity(), dependantMainResponse -> {

                                if (dependantMainResponse != null && relationMainResponse != null) {
                                    if (!dependantMainResponse.isError() && !dependantMainResponse.isLoading()) {

                                        try {
                                            //refresh the recyclerview here

                                            List<Dependant> exDependants = dependantMainResponse.getDependants() == null ? new ArrayList<>() : dependantMainResponse.getDependants();

                                            binding.errorLayout.setVisibility(View.GONE);
                                            dependantList.clear();
                                            for (Relation relation : relationMainResponse.getRelations()) {
                                                dependantList.add(new DependantHelperModel(relation.getRelation(), 2,
                                                        "1", false, "", "", false,
                                                        null, "", false, false, "", "", "", relation.getRelationID(), ""));
                                            }
                                            for (Dependant familyMember : exDependants) {
                                                int index_update = 0;

                                                if (!familyMember.getRelationID().equalsIgnoreCase("17")) {


                                                    dependantList.add(new DependantHelperModel(familyMember.getRelation(),
                                                            2, "1", true,
                                                            familyMember.getName(), familyMember.getDateOfBirth(),
                                                            familyMember.getIsDisabled(), null,
                                                            familyMember.getAge(), familyMember.getCanUpdate(),
                                                            familyMember.getCanDelete(), familyMember.getGender(),
                                                            familyMember.getPersonSrNo(), familyMember.getPersonSrNo(),
                                                            familyMember.getRelationID(),
                                                            familyMember.getIsTwins()));
                                                }


                                            }


                                            if (!parents_allowed) {
                                                dependantList.removeIf(dependantParent -> dependantParent.getRelationName().equalsIgnoreCase("mother") || dependantParent.getRelationName().equalsIgnoreCase("father") || dependantParent.getRelationName().equalsIgnoreCase("mother-in-law") || dependantParent.getRelationName().equalsIgnoreCase("mother in law") || dependantParent.getRelationName().equalsIgnoreCase("father-in-law") || dependantParent.getRelationName().equalsIgnoreCase("father in law"));
                                            }

                                            if (!LGBTQ) {
                                                dependantList.removeIf(dependantParent -> dependantParent.getRelationName().equalsIgnoreCase("partner"));
                                            }

                                            if (!SPOUSE) {
                                                dependantList.removeIf(dependantParent -> dependantParent.getRelationName().equalsIgnoreCase("spouse"));
                                            }

                                            if (!twins_allowed) {
                                                dependantList.removeIf(dependantParent -> dependantParent.getRelationName().equalsIgnoreCase("twins"));
                                            }

                                            if (dependantList.size() != 0) {

                                                Collections.reverse(dependantList);
                                                //adapter
                                                adapter = new DependantDetailsAdapter((dependantList), requireActivity(), this, isWindowPeriodActive, animations);
                                                binding.dependantCycle.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                binding.errorLayout.setVisibility(View.VISIBLE);
                                            }


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            //show error
                                        }
                                        binding.swipetoRefresh.setRefreshing(false);

                                    } else if (dependantMainResponse.isLoading()) {
                                        binding.swipetoRefresh.setRefreshing(true);
                                        //show loading
                                    } else if (dependantMainResponse.isError()) {
                                        //show error
                                        binding.swipetoRefresh.setRefreshing(false);
                                        binding.errorLayout.setVisibility(View.VISIBLE);
                                    }
                                }


                            });
                });
    }


}