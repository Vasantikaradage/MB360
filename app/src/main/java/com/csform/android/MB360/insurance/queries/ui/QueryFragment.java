package com.csform.android.MB360.insurance.queries.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentQueryBinding;
import com.csform.android.MB360.insurance.queries.repository.QueryViewModel;
import com.csform.android.MB360.insurance.queries.responseclass.AllQuery;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.onboarding.authentication.LoginActivity;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.utilities.error.ErrorActivity;


public class QueryFragment extends Fragment implements OnQuerySelected {

    FragmentQueryBinding binding;
    View view;
    QueryViewModel queryViewModel;
    QueryAdapter adapter;
    LoadSessionViewModel loadSessionViewModel;
    String empSrNo = null;

    NavController navController;

    public QueryFragment() {
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
        binding = FragmentQueryBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        queryViewModel = new ViewModelProvider(requireActivity()).get(QueryViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);


        queryViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.refreshQuery.setRefreshing(true);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.refreshQuery.setRefreshing(false);
            }
        });


        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), response -> {
            empSrNo = response.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData()
                    .get(0).getEmployeeSrNo();
            getQueries();
        });


        binding.refreshQuery.setOnRefreshListener(() -> {
            if (empSrNo != null) {
                queryViewModel.getQueries(empSrNo);
            } else {
                loadSessionViewModel.getLoadSessionData();
            }
        });

        return view;
    }

    private void getQueries() {
        queryViewModel.getQueries(empSrNo);
        queryViewModel.getQueriesData().observe(getViewLifecycleOwner(), queries -> {
            if (queries != null) {
                if (queries.getAllQueries() == null || queries.getAllQueries().isEmpty()) {
                    //empty layout
                    binding.errorLayout.setVisibility(View.VISIBLE);
                    binding.queryCycle.setVisibility(View.GONE);
                } else {
                    binding.errorLayout.setVisibility(View.GONE);
                    binding.queryCycle.setVisibility(View.VISIBLE);
                    adapter = new QueryAdapter(queries.getAllQueries(), requireContext(), this);
                    binding.queryCycle.setAdapter(adapter);
                }

            }else{
                //empty layout
                binding.errorLayout.setVisibility(View.VISIBLE);
                binding.queryCycle.setVisibility(View.GONE);
            }

        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queryViewModel.getReloginState().observe(getActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });


    }

    @Override
    public void selectedQuery(AllQuery query) {
        NavDirections actions = MyQueriesFragmentDirections.actionMyQueriesFragmentToQueryDetailsFragment(query);
        navController.navigate(actions);
    }
}