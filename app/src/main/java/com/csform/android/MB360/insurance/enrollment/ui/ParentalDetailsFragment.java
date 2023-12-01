package com.csform.android.MB360.insurance.enrollment.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentParentalDetailsBinding;
import com.csform.android.MB360.insurance.enrollment.adapters.ParentalDetailAdapter;
import com.csform.android.MB360.insurance.enrollment.interfaces.DependantHelper;
import com.csform.android.MB360.insurance.enrollment.interfaces.DependantListener;
import com.csform.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.csform.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.dependantdetails.Relation;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.Parent;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.ParentalModel;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.AvailPaernt;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.parentalDetails.ParentalRelationResponse;
import com.csform.android.MB360.insurance.enrollment.ui.bottomSheets.AddDependantBottomSheet;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.csform.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;
import com.csform.android.MB360.utilities.SwipeToDeleteCallBackParental;
import com.csform.android.MB360.utilities.WindowPeriodCounter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentalDetailsFragment extends Fragment implements DependantHelper, DependantListener, SwipeToDeleteCallBackParental.RecyclerItemTouchHelperListener, ConfirmationDialogs.DialogActions {
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    EnrollmentViewModel enrollmentViewModel;
    LoadSessionViewModel loadSessionViewModel;

    FragmentParentalDetailsBinding binding;
    View view;
    boolean isWindowPeriodActive = false;

    //parental data
    List<ParentalModel> parentalList = new ArrayList<>();

    ParentalDetailAdapter adapter;

    boolean animations = true;

    int animation_count = 0;

    String groupChilSrNo = "";
    String employeeSrNo = "";
    String oeGrpBasInfoSrNo = "";

    public ParentalDetailsFragment() {
        // Required empty public constructor
    }

    public ParentalDetailsFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
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
        binding = FragmentParentalDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //setting the view-model according to the scope
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


        if (employeeSrNo != null && groupChilSrNo != null && oeGrpBasInfoSrNo != null) {
            enrollmentViewModel.getParentalRelationshipGroup(employeeSrNo, groupChilSrNo, oeGrpBasInfoSrNo, "1");
        } else {
            //something went wrong
        }


        //to show summary option
        viewPagerNavigationMenuHelper.showSummaryOption();

        //getWindowPeriod
        getWindowPeriodTimer();
        binding.swipetoRefresh.setOnRefreshListener(this::getParentalData);

        return view;
    }



    private void getWindowPeriodTimer() {
        WindowPeriodEnrollmentResponse windowPeriod = enrollmentViewModel.getWindowPeriod().getValue();
        if (windowPeriod != null) {
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriod.getWindowPeriod().getWindowEndDateGmc(), requireContext(), requireActivity());

          //time comment
            try {
                CountDownTimer timer = windowPeriodCounter.getTimer(binding.timerParentalDetails);
                if (timer != null) {
                    isWindowPeriodActive = true;
                    timer.start();
                    //swipe to delete
                    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new SwipeToDeleteCallBackParental(requireContext(), 0, ItemTouchHelper.LEFT, this, parentalList);
                    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvFamilyDetails);
                } else {
                    isWindowPeriodActive = false;
                    binding.timerParentalDetails.setText("Window period has expired");
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

    private void getParentalData() {
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {
            ParentalRelationResponse parentalRelationResponse = enrollmentViewModel.getParentalRelationshipGroup(employeeSrNo, groupChilSrNo, oeGrpBasInfoSrNo, "1").getValue();
            enrollmentViewModel.getParentalData("1",
                    groupChilSrNo,
                    oeGrpBasInfoSrNo,
                    employeeSrNo,
                    oeGrpBasInfoSrNo,
                    "1", "1").observe(requireActivity(), dependantMainResponse -> {

                if (parentalRelationResponse != null && dependantMainResponse != null) {
                    List<Parent> parentList = parentalRelationResponse.getAvailPaernts() == null ? new ArrayList<>() : dependantMainResponse.getRelations();
                    List<AvailPaernt> relationList = parentalRelationResponse.getAvailPaernts();
                    parentalList.clear();

                    //remove other relation than parental
                    relationList.removeIf(relation -> relation.getRelation().equalsIgnoreCase("spouse") || relation.getRelation().equalsIgnoreCase("partner") || relation.getRelation().equalsIgnoreCase("son") || relation.getRelation().equalsIgnoreCase("twins") || relation.getRelation().equalsIgnoreCase("daughter"));
                    for (AvailPaernt relation : relationList) {
                        parentalList.add(new ParentalModel(relation.getRelation(), 1, "1", false, "", "", false, null, "", true, false, "", false, "", "", relation.getRelationId(),""));

                    }

                    for (Parent familyMember : parentList) {
                        int index_update = 0;
                        for (ParentalModel dependantHelperModel : parentalList) {
                            if (dependantHelperModel.getRelationID().equalsIgnoreCase(familyMember.getRelationId())) {
                                if (dependantHelperModel.getPersonSrno().equalsIgnoreCase("")) {

                                    if (familyMember.getName().equalsIgnoreCase("")) {
                                        parentalList.set(index_update, new ParentalModel(familyMember.getRelation(), 2, "1", false, "", "", false, null, "", true, false, familyMember.getRelation(), false, "", "0", familyMember.getRelationId(),""));

                                    } else {
                                        parentalList.set(index_update, new ParentalModel(familyMember.getRelation(), 2, "1", true, familyMember.getName(), familyMember.getDateOfBirthToShow(), false, null, familyMember.getAge(), familyMember.getCanUpdate(), familyMember.getCanDelete(), familyMember.getRelation(), false, familyMember.getPersonSrNo(), "0", familyMember.getRelationId(),""));
                                    }

                                } else {
                                    parentalList.add(new ParentalModel(familyMember.getRelation(), 2, "1", true, familyMember.getName(), familyMember.getDateOfBirthToShow(), false, null, familyMember.getAge(), familyMember.getCanUpdate(), familyMember.getCanDelete(), familyMember.getRelation(), false, familyMember.getPersonSrNo(), "0", familyMember.getRelationId(),""));
                                }
                                break;
                            }
                            index_update++;
                        }

                    }
                    parentalList.add(0, new ParentalModel("", 1, "", false, "", "", false, null, "", true, true, "", true, "", "", "",""));
                    if (parentalList.size() > 3) {
                        //that means we get the cross parents data too
                        parentalList.add(3, new ParentalModel("", 1, "", false, "", "", false, null, "", true, true, "", true, "", "", "",""));
                    }

                    if(parentalList.size()!=0) {
                        adapter = new ParentalDetailAdapter(parentalList, requireContext(), this, isWindowPeriodActive, animations);
                        binding.rvFamilyDetails.setAdapter(adapter);
                        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new SwipeToDeleteCallBackParental(requireContext(), 0, ItemTouchHelper.LEFT, this, parentalList);
                        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvFamilyDetails);
                    }else {
                        binding.errorLayout.setVisibility(View.VISIBLE);
                    }
                }

            });
        } else {
            //load session is null
        }

    }


    @Override
    public void onAddDependant(DependantHelperModel dependantHelperModel, int position) {
        animations = false;
        AddDependantBottomSheet parentBottomSheet = new AddDependantBottomSheet(this, dependantHelperModel, position);
        parentBottomSheet.setCancelable(true);
        parentBottomSheet.show(getChildFragmentManager(), parentBottomSheet.getTag());
    }

    @Override
    public void onEditDependant(DependantHelperModel dependant, int position) {
        animations = false;
        AddDependantBottomSheet dialog = new AddDependantBottomSheet(this, dependant, position, true);
        dialog.show(getChildFragmentManager(), dialog.getTag());
        dialog.setCancelable(true);
    }

    @Override
    public void onDeleteDependant() {

    }

    @Override
    public void onEditTwin(int position) {

    }

    @Override
    public void onDependantSavedListener(DependantHelperModel dependant, int position) {
        animations = false;
        //here we get the parent and we need to save the parent in the parent list
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        for (DependantHelperModel dependantCycle : parentalList) {
            if (dependantCycle.getRelationName().equalsIgnoreCase(dependant.getRelationName())) {
                dependantCycle.setDateOfBirth(dependant.getDateOfBirth());
                dependantCycle.setName(dependant.getName());
                dependantCycle.setIsAdded(true);
                dependantCycle.setIsDifferentlyAble(dependant.isDifferentlyAble());
                dependantCycle.setDocument(dependant.getDocument());
                dependantCycle.setCanEdit(true);
                dependantCycle.setCanDelete(true);
                dependantCycle.setAge(dependant.getAge());
                adapter.notifyItemChanged(position);
                adapter.notifyItemChanged(position);
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
                                getParentalData();
                            } else {
                                getParentalData();
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
        //we dont need any twins here because we are currently adding parent
    }

    @Override
    public void onDependantEditedListener(DependantHelperModel dependant, int position) {
        //here we listen to the changes to the edited parent which also can be listen by on add-dependant
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
                            getParentalData();
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
        //here we should call the confirmation dialog with the item touch helper
        //swipe gesture confirmation is being called here
        //and then after confirmation we call delete method which
        //only updates the parent again to empty state!
    }

    // Deleting the dependant
    @Override
    public void onConfirmed(int position, DependantHelperModel dependantHelperModel, ParentalModel parentalModel) {
        // this is on-confirmed of deleting
        animations = false;
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();


        if (loadSessionResponse != null) {
            try {
                //deletion of the dependant api call
                Map<String, String> dependantOption = new HashMap<>();
                dependantOption.put("EmpSrNo", loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());
                dependantOption.put("GroupChildSrNo", loadSessionResponse.getGroupInfoData().getGroupchildsrno());
                dependantOption.put("Persrno", parentalModel.getPersonSrno());


                enrollmentViewModel.deleteDependantData(dependantOption).observe(getViewLifecycleOwner(), enrollmentMessage -> {
                    if (enrollmentMessage != null) {

                        if (!enrollmentMessage.isError() && !enrollmentMessage.isLoading()) {
                            Toast.makeText(getActivity(), "" + enrollmentMessage.getAddDependantResponse().getMessage(), Toast.LENGTH_SHORT).show();
                            getParentalData();

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

    //canceled the delete process
    @Override
    public void onRejected(int position, DependantHelperModel dependantHelperModel, ParentalModel parentalModel) {
        //on canceled deleting
        parentalList.get(position).setCanEdit(true);
        parentalList.get(position).setCanDelete(true);
        adapter.notifyItemChanged(position);
        animations = false;

    }


    @Override
    public void onResume() {
        super.onResume();
      /*  //to show summary option
        viewPagerNavigationMenuHelper.showSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/


        if (animation_count > 0) {
            animations = false;
        } else {
            animations = true;


            Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_down);
            binding.appCompatImageView3.startAnimation(animation);
            binding.headerHolderParent.startAnimation(animation);
            binding.timerHolder.startAnimation(animation);
            binding.inst1.startAnimation(animation);

            binding.appCompatImageView3.setVisibility(View.VISIBLE);
            binding.headerHolderParent.setVisibility(View.VISIBLE);
            binding.timerHolder.setVisibility(View.VISIBLE);
            binding.inst1.setVisibility(View.VISIBLE);


        }


        animation_count++;
        getParentalData();

    }

    @Override
    public void onSwipedParental(RecyclerView.ViewHolder viewHolder, int direction, int position, ParentalModel parentalModel) {
        //start the deleting process
        ConfirmationDialogs confirmationDialogs = new ConfirmationDialogs(requireContext(), this, position, parentalModel);
        confirmationDialogs.setCancelable(false);
        confirmationDialogs.show(getChildFragmentManager(), confirmationDialogs.getTag());
        animations = false;
    }


    private void showLoading() {
        binding.swipetoRefresh.setRefreshing(true);
    }

    private void hideLoading() {
        binding.swipetoRefresh.setRefreshing(false);
    }


}