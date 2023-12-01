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
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryLT;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryPT;
import com.csform.android.MB360.wellness.homehealthcare.retrofit.RescheduleInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhysiotherapyOngoingAdapter extends RecyclerView.Adapter<PhysiotherapyOngoingAdapter.PhysiotherapyOngoingViewHolder> {
    List<OnGoingSummaryPT> summaryList;
    Context context;
    String serviceName;
    OnCancellation onCancellation;
    RescheduleInterface rescheduleInterface;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public PhysiotherapyOngoingAdapter(List<OnGoingSummaryPT> summaryList, Context context, String serviceName, OnCancellation onCancellation, RescheduleInterface rescheduleInterface) {
        this.summaryList = summaryList;
        this.context = context;
        this.serviceName = serviceName;
        this.onCancellation = onCancellation;
        this.rescheduleInterface = rescheduleInterface;
    }

    @NonNull
    @Override
    public PhysiotherapyOngoingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentBinding binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PhysiotherapyOngoingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhysiotherapyOngoingViewHolder holder, int position) {
        try {
            String strApptSrNo = "";
            holder.binding.txtDependantName.setText(summaryList.get(position).getAppntPerson());
            holder.binding.txtDependantAge.setText(summaryList.get(position).getAppntPersonAge() + " years");
            holder.binding.txtAppointReference.setText(summaryList.get(position).getHhcApptPtRefNo());

            Date strDate = simpleDateFormat3.parse(summaryList.get(position).getScheduledOn());
            String date = simpleDateFormat3.format(strDate);
            holder.binding.txtAppointmentScheduled.setText(date);


            holder.binding.tvDurationlabel.setText("Selected Package - ");
            holder.binding.tvDuration.setGravity(Gravity.START);
            holder.binding.tvDuration.setText(summaryList.get(position).getPtCategory());

            //if day wise
            if (summaryList.get(position).getPtCategory().equalsIgnoreCase("Per Day")) {

                holder.binding.tvappointmentdoneon.setText(summaryList.get(position).getDatePreference() + " " + summaryList.get(position).getTimePreference());
                holder.binding.tvDuration.setText(summaryList.get(position).getPtCategory());

            } else {
                //if 10 days
                holder.binding.tvappointmentdoneon.setGravity(Gravity.START);
                holder.binding.tvappointmentdoneon.setText(summaryList.get(position).getFromDate() + " to " + summaryList.get(position).getToDate());

            }


            if (summaryList.get(position).getHhcPtRemarks() != null && !summaryList.get(position).getHhcPtRemarks().equals(""))
                holder.binding.tvRemarks.setText(summaryList.get(position).getHhcPtRemarks());
            else {
                holder.binding.tvRemarks.setVisibility(View.GONE);
                holder.binding.tvRemarkslabel.setVisibility(View.GONE);
            }
            strApptSrNo = summaryList.get(position).getAppntPersonSrNo();
            String strPrice = UtilMethods.PriceFormat(summaryList.get(position).getPkgPrice());
            holder.binding.tvTotalPrice.setText("\u20B9 " + strPrice);

            String strPackagePrice = UtilMethods.PriceFormat(summaryList.get(position).getPkgPrice());
            holder.binding.tvPackagePrice.setText("\u20B9 " + strPackagePrice);


        } catch (Exception e) {
            e.printStackTrace();

        }

        holder.binding.llCancelOrder.setOnClickListener(v -> {
            onCancellation.cancelAppointment(summaryList.get(position).getHhcPtApptInfoSrNo(), position);
        });

        holder.binding.btnreschedule.setOnClickListener(view -> {
            rescheduleInterface.rescheduleAppointmentPT(summaryList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return (summaryList != null ? summaryList.size() : 0);
    }

    public static class PhysiotherapyOngoingViewHolder extends RecyclerView.ViewHolder {
        ItemAppointmentBinding binding;

        public PhysiotherapyOngoingViewHolder(@NonNull ItemAppointmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
