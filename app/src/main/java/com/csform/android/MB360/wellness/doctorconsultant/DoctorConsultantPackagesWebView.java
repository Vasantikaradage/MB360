package com.csform.android.MB360.wellness.doctorconsultant;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.FragmentDoctorConsultantPackagesWebViewBinding;
import com.csform.android.MB360.utilities.LoadingWellnessDialogue;
import com.csform.android.MB360.wellness.doctorconsultant.repository.DCViewModel;

public class DoctorConsultantPackagesWebView extends Fragment {

    DCViewModel dcViewModel;
    FragmentDoctorConsultantPackagesWebViewBinding binding;
    View view;

    LoadingWellnessDialogue loadingWellnessDialogue;

    public DoctorConsultantPackagesWebView() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDoctorConsultantPackagesWebViewBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        loadingWellnessDialogue = new LoadingWellnessDialogue(requireContext(), requireActivity());

        dcViewModel = new ViewModelProvider(requireActivity()).get(DCViewModel.class);

        /*  binding.doctorConsultantWebView.setWebViewClient(new WebViewClient());*/

       /* binding.doctorConsultantWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.doctorConsultantWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.doctorConsultantWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.doctorConsultantWebView.setWebChromeClient(new WebChromeClient());*/

        DCwebClient webViewClient = new DCwebClient(loadingWellnessDialogue);


        binding.doctorConsultantWebView.getSettings().setJavaScriptEnabled(true);
        binding.doctorConsultantWebView.getSettings().setBuiltInZoomControls(false);
        binding.doctorConsultantWebView.getSettings().setSupportZoom(false);
        binding.doctorConsultantWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.doctorConsultantWebView.getSettings().setAllowFileAccess(true);
        binding.doctorConsultantWebView.getSettings().setDomStorageEnabled(true);


        binding.doctorConsultantWebView.setWebViewClient(webViewClient);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dcViewModel.buyPackageData().observe(getViewLifecycleOwner(), dcPackageResponse -> {

            if (dcPackageResponse != null) {
                binding.doctorConsultantWebView.loadUrl(dcPackageResponse.getUrl());
            } else {
                //something is wrong
            }

        });

    }

    class DCwebClient extends WebViewClient {
        LoadingWellnessDialogue loadingWellnessDialogue;

        public DCwebClient(LoadingWellnessDialogue loadingWellnessDialogue) {
            this.loadingWellnessDialogue = loadingWellnessDialogue;
            loadingWellnessDialogue.showLoading("");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadingWellnessDialogue.hideLoading();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            view.loadUrl(request.getUrl().toString());
            return true;
        }


    }
}

