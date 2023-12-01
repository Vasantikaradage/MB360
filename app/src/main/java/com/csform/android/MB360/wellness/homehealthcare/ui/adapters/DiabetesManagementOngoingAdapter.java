package com.csform.android.MB360.wellness.homehealthcare.ui.adapters;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csform.android.MB360.databinding.ItemAppointmentBinding;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.wellness.homehealthcare.cancellation.OnCancellation;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryDM;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryNA;
import com.csform.android.MB360.wellness.homehealthcare.retrofit.RescheduleInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiabetesManagementOngoingAdapter extends RecyclerView.Adapter<DiabetesManagementOngoingAdapter.DiabetesManagementViewHolder> {
    List<OnGoingSummaryDM> summaryList;
    Context context;
    String serviceName;
    OnCancellation onCancellation;
    RescheduleInterface rescheduleInterface;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public DiabetesManagementOngoingAdapter(List<OnGoingSummaryDM> summaryList, Context context, String serviceName, OnCancellation onCancellation, RescheduleInterface rescheduleInterface) {
        this.summaryList = summaryList;
        this.context = context;
        this.serviceName = serviceName;
        this.onCancellation = onCancellation;
        this.rescheduleInterface = rescheduleInterface;
    }

    @NonNull
    @Override
    public DiabetesManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentBinding binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DiabetesManagementViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiabetesManagementViewHolder holder, int position) {
        try {
            String strApptSrNo = "";
            holder.binding.txtDependantName.setText(summaryList.get(position).getAppntPerson());
            holder.binding.txtDependantAge.setText(summaryList.get(position).getAppntPersonAge() + " years");
            holder.binding.txtAppointReference.setText(summaryList.get(position).getHhcApptDmRefNo());

            Date strDate = simpleDateFormat3.parse(summaryList.get(position).getScheduledOn());
            String date = simpleDateFormat3.format(strDate);
            holder.binding.txtAppointmentScheduled.setText(date);

            // viewHolder.txtPackageDetails.setText(lstSummary.get(position).getStrPkgPrice());
            holder.binding.tvDurationlabel.setVisibility(View.GONE);
            holder.binding.tvDuration.setVisibility(View.GONE);


            if (summaryList.get(position).getHhcDmRemarks() != null && !summaryList.get(position).getHhcDmRemarks().equals(""))
                holder.binding.tvRemarks.setText(summaryList.get(position).getHhcDmRemarks());
            else {
                holder.binding.tvRemarks.setVisibility(View.GONE);
                holder.binding.tvRemarkslabel.setVisibility(View.GONE);
            }
            strApptSrNo = summaryList.get(position).getAppntPersonSrNo();
            String strPrice = UtilMethods.PriceFormat(summaryList.get(position).getPkgPrice());
            holder.binding.tvTotalPrice.setText("\u20B9 " + strPrice);

            String strPackagePrice = UtilMethods.PriceFormat(summaryList.get(position).getPkgPrice());
            holder.binding.tvPackagePrice.setText("\u20B9 " + strPackagePrice);


            holder.binding.tvappointmentdoneonlabel.setText("Selcted Package - ");
            holder.binding.tvappointmentdoneon.setText(summaryList.get(position).getDmCategory());

            //no need to reschedule
            holder.binding.llReschedule.setVisibility(View.GONE);


        } catch (Exception e) {
            e.printStackTrace();

        }

        holder.binding.llCancelOrder.setOnClickListener(v -> {
            onCancellation.cancelAppointment(summaryList.get(position).getHhcDmApptInfoSrNo(), position);
        });

        holder.binding.btnreschedule.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return (summaryList != null ? summaryList.size() : 0);
    }

    public static class DiabetesManagementViewHolder extends RecyclerView.ViewHolder {
        ItemAppointmentBinding binding;

        public DiabetesManagementViewHolder(@NonNull ItemAppointmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

