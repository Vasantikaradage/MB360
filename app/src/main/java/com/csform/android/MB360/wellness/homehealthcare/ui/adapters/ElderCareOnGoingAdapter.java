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
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryEC;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.summary.SummaryResponseEc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ElderCareOnGoingAdapter extends RecyclerView.Adapter<ElderCareOnGoingAdapter.SummaryViewHolderEC> {

    List<OnGoingSummaryEC> summaryList = new ArrayList<>();
    Context context;
    OnCancellation onCancellation;

    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);


    public ElderCareOnGoingAdapter(List<OnGoingSummaryEC> elderList, Context context, OnCancellation onCancellation) {
        this.summaryList = elderList;
        this.context = context;
        this.onCancellation = onCancellation;
    }

    @NonNull
    @Override
    public SummaryViewHolderEC onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentBinding binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SummaryViewHolderEC(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolderEC holder, int position) {


        try {
            String strApptSrNo = "";
            holder.binding.txtDependantName.setText(summaryList.get(position).getAppntPerson());
            holder.binding.txtDependantAge.setText(summaryList.get(position).getAppntPersonAge() + " years");
            holder.binding.txtAppointReference.setText(summaryList.get(position).getHhcApptEcRefNo());

            Date strDate = simpleDateFormat3.parse(summaryList.get(position).getScheduledOn());
            String date = simpleDateFormat3.format(strDate);
            holder.binding.txtAppointmentScheduled.setText(date);


            holder.binding.tvDuration.setVisibility(View.GONE);
            holder.binding.tvDurationlabel.setVisibility(View.GONE);

            holder.binding.tvappointmentdoneonlabel.setText("Selected Package: ");
            holder.binding.tvappointmentdoneon.setText(summaryList.get(position).getEcCategory());
            holder.binding.tvappointmentdoneon.setGravity(Gravity.START);
            if (summaryList.get(position).getHhcEcRemarks() != null && !summaryList.get(position).getHhcEcRemarks().equals(""))
                holder.binding.tvRemarks.setText(summaryList.get(position).getHhcEcRemarks());
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
            onCancellation.cancelAppointment(summaryList.get(position).getHhcEcApptInfoSrNo(), position);
        });

        holder.binding.llReschedule.setVisibility(View.GONE);
        holder.binding.btnreschedule.setOnClickListener(view -> {
            //rescheduling the appointment

        });

    }

    @Override
    public int getItemCount() {
        return (summaryList != null ? summaryList.size() : 0);
    }


    public static class SummaryViewHolderEC extends RecyclerView.ViewHolder {
        ItemAppointmentBinding binding;

        public SummaryViewHolderEC(@NonNull ItemAppointmentBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
