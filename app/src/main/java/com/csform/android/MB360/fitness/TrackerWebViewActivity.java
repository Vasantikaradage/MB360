package com.csform.android.MB360.fitness;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTracker;
import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTrackerAuthUrlInfo;
import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTrackerConnectionState;
import com.aktivolabs.aktivocore.managers.AktivoManager;
import com.csform.android.MB360.databinding.ActivityTrackerWebViewBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TrackerWebViewActivity extends AppCompatActivity {
    ActivityTrackerWebViewBinding activityTrackerWebViewBinding;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTrackerWebViewBinding = ActivityTrackerWebViewBinding.inflate(getLayoutInflater());
        view = activityTrackerWebViewBinding.getRoot();
        setContentView(view);

        loadData();


    }

    private void loadData() {
        activityTrackerWebViewBinding.webView.setWebViewClient(new WebViewClient());
        //  Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        activityTrackerWebViewBinding.webView.getSettings().setJavaScriptEnabled(true);
        FitnessTracker fitnessTracker = new FitnessTracker(FitnessTracker.Platform.FITBIT,
                FitnessTrackerConnectionState.DISCONNECTED);
        String tracker_url = getIntent().getStringExtra("tracker_URL");
        //String tracker_url ="garmin";

        assert tracker_url != null;
        switch (tracker_url) {
            case "fitbit":
                fitnessTracker = new FitnessTracker(FitnessTracker.Platform.FITBIT,
                        FitnessTrackerConnectionState.DISCONNECTED);
                break;
            case "garmin":
                fitnessTracker = new FitnessTracker(FitnessTracker.Platform.GARMIN,
                        FitnessTrackerConnectionState.DISCONNECTED);
                break;
        }

        AktivoManager mAktivoManager = AktivoManager.getInstance(this);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(mAktivoManager
                .getFitnessPlatformAuthUrl(fitnessTracker.getPlatform())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FitnessTrackerAuthUrlInfo>() {

                    @Override
                    public void onSuccess(FitnessTrackerAuthUrlInfo fitnessTrackerAuthUrlInfo) {
                        activityTrackerWebViewBinding.webView.loadUrl(fitnessTrackerAuthUrlInfo.getAuthUrl());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }
}
