package com.csform.android.MB360.insurance.myclaims.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.ItemMyClaimsBinding;
import com.csform.android.MB360.insurance.myclaims.responseclass.Claims;
import com.csform.android.MB360.insurance.myclaims.responseclass.DocumentElement;
import com.csform.android.MB360.insurance.myclaims.responseclassV1.ClaimInformation;
import com.csform.android.MB360.utilities.UtilMethods;

import java.text.MessageFormat;
import java.util.List;

public class MyClaimsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<ClaimInformation> claimsList;
    Context context;
    OnClaimSelected onClaimSelected;

    public MyClaimsAdapter(List<ClaimInformation> claimsList, Context context, OnClaimSelected onClaimSelected) {
        this.claimsList = claimsList;
        this.context = context;
        this.onClaimSelected = onClaimSelected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyClaimsBinding binding = ItemMyClaimsBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new MyClaimsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        final ClaimInformation claims = claimsList.get(position);
        ((MyClaimsViewHolder) holder).binding.lblName.setText(claims.getBeneficiary());


        ((MyClaimsViewHolder) holder).binding.lblClaimAmtValue.setText(MessageFormat.format("{0}", claims.getClaimAmt()));
        ((MyClaimsViewHolder) holder).binding.lblStatusValue.setText(claims.getClaimStatus());
        ((MyClaimsViewHolder) holder).binding.lblDateValue.setText(claims.getClaimDate());
        ((MyClaimsViewHolder) holder).binding.lblClaimValue.setText(claims.getClaimType());
        if (claims.getClaimType().equals("Reimbursement")) {
            ((MyClaimsViewHolder) holder).binding.lblClaimValue.setTextColor(ContextCompat.getColor(context, R.color.tc));
        }
        if (claims.getClaimType().equals("Cashless")) {
            ((MyClaimsViewHolder) holder).binding.lblClaimValue.setTextColor(ContextCompat.getColor(context, R.color.gradient_end));

        }
        ((MyClaimsViewHolder) holder).binding.lblNoValue.setText(claims.getClaimNo());

        ((MyClaimsViewHolder) holder).binding.const1.setOnClickListener(v -> {
            onClaimSelected.onClaimSelected(claimsList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (claimsList != null) {
            return claimsList.size();
        } else {
            return 0;
        }
    }

    public static class MyClaimsViewHolder extends RecyclerView.ViewHolder {
        ItemMyClaimsBinding binding;

        public MyClaimsViewHolder(@NonNull ItemMyClaimsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
