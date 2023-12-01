package com.csform.android.MB360.fitness;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentFitnessCompleteBinding;
import com.csform.android.MB360.fitness.AktivoData.AktivoChallenges;
import com.csform.android.MB360.fitness.ProfileScreenData.AktivoBeanProfile;
import com.csform.android.MB360.fitness.retrofit.FitnessApi;
import com.csform.android.MB360.fitness.retrofit.FitnessRetrofitClient;
import com.csform.android.MB360.fitness.viewmodel.AktivoChallengesViewModel;
import com.csform.android.MB360.insurance.repository.LoadSessionViewModel;
import com.csform.android.MB360.utilities.AppServerConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FitnessCompeteFragment extends Fragment implements View.OnClickListener {

    FragmentFitnessCompleteBinding fragmentFitnessCompleteBinding;
    private View view;
    private AktivoChallengesViewModel aktivoChallengesViewModel;
    private ChallengeAdapter challengeAdapter;
    private List<ChallengeLayoutItems> lstChallenge;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFitnessCompleteBinding = FragmentFitnessCompleteBinding.inflate(inflater, container, false);
        view = fragmentFitnessCompleteBinding.getRoot();


        loadData();
        return view;

    }

    private void loadData() {
        fragmentFitnessCompleteBinding.tabOnGoing.setOnClickListener(this);
        fragmentFitnessCompleteBinding.tabOver.setOnClickListener(this);
        aktivoChallengesViewModel = new ViewModelProvider(this).get(AktivoChallengesViewModel.class);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        String _id = "63afd1d89ea2a604a96123d2";

        String url = "https://api.aktivolabs.com/api/users/" + _id + "?include=leaderboards&" +
                "startDate=" + year + "-01-01&endDate=" + year + "-12-31";
        Log.d("Data", "Member id" + _id);

        String token = getActivity().getSharedPreferences(AppServerConstants.adminsettings,
                Context.MODE_PRIVATE).getString("access_token", null);

        aktivoChallengesViewModel.getAktivoChanllengerInfo(token,_id,"leaderboards",year +"-01-01", year + "-12-31");
        aktivoChallengesViewModel.getAktivoChanllengerInfoData().observe(getViewLifecycleOwner(),aktivoBeanProfile -> {
            if(aktivoBeanProfile!=null)
            {
                String data=aktivoBeanProfile.challengesData.lstChallenges.toString();
                Log.d("@@","@@"+data);
                setRecyclerView();

            }
        });


    }

    private void setRecyclerView() {
        lstChallenge = new ArrayList<>();
        challengeAdapter = new ChallengeAdapter(getContext(),lstChallenge);


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tabOnGoing:
                fragmentFitnessCompleteBinding.tabOnGoing.setBackground(getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fragmentFitnessCompleteBinding.tabOver.setBackground(getResources().getDrawable(R.color.transparent));

                fragmentFitnessCompleteBinding.tabOnGoing.setTextColor(getResources().getColor(R.color.cpb_white));
                fragmentFitnessCompleteBinding.tabOver.setTextColor(getResources().getColor(R.color.cpb_black));

                 //challengeAdapter.getFilter().filter(getActivity().getString(R.string.ongoing));
                break;

            case R.id.tabOver:
                fragmentFitnessCompleteBinding.tabOver.setBackground(getResources().getDrawable(R.drawable.fitness_buttons_inactive));
                fragmentFitnessCompleteBinding.tabOnGoing.setBackground(getResources().getDrawable(R.color.transparent));

                fragmentFitnessCompleteBinding.tabOnGoing.setTextColor(getResources().getColor(R.color.cpb_black));
                fragmentFitnessCompleteBinding.tabOver.setTextColor(getResources().getColor(R.color.cpb_white));

                //challengeAdapter.getFilter().filter(getActivity().getString(R.string.Over));
                break;
        }
    }

}
