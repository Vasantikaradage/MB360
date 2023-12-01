package com.csform.android.MB360.fitness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.csform.android.MB360.databinding.FragmentPrivacyPolicyBinding;

public class HelpFragment extends Fragment {
   FragmentPrivacyPolicyBinding fragmentPrivacyPolicyBinding;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPrivacyPolicyBinding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false);
        view = fragmentPrivacyPolicyBinding.getRoot();
        loadData();
        return view;
    }

    private void loadData() {
        fragmentPrivacyPolicyBinding.webView.loadUrl("https://www.aktivolabs.com/faq");

        WebSettings webSettings = fragmentPrivacyPolicyBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        fragmentPrivacyPolicyBinding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress == 100)
                    fragmentPrivacyPolicyBinding.progress.setVisibility(View.GONE);
            }
        });
    }


}

