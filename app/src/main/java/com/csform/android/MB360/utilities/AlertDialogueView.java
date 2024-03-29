package com.csform.android.MB360.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.AlertDialogBinding;
import com.csform.android.MB360.databinding.PolicyChangeDialogueBinding;

import java.util.Objects;

public class AlertDialogueView {

    Context context;
    Activity activity;
    AlertDialogBinding binding;
    private AlertDialog view;
    AlertDialogueLocationHelper alertDialogueLocationHelper;

    public AlertDialogueView(Context context, Activity activity, AlertDialogueLocationHelper alertDialogueLocationHelper) {
        this.context = context;
        this.activity = activity;
        this.alertDialogueLocationHelper = alertDialogueLocationHelper;
    }

    public void showAlert(String alert) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        binding = AlertDialogBinding.inflate(inflater);

        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        view = builder.create();
        view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams wmlp = view.getWindow().getAttributes();
        wmlp.windowAnimations = com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        wmlp.width = 150;
        view.show();


        binding.alertIcon.setImageResource(R.drawable.ic_popupal);
        binding.btnDismissText.setText("Select city");
        binding.tvAlertMessage.setText(alert);

        binding.btnDismiss.setOnClickListener(view1 -> {
            alertDialogueLocationHelper.openCityFragment();
            view.dismiss();
        });

    }


}
