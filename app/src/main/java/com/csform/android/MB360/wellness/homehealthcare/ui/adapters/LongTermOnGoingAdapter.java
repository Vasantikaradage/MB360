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
import com.csform.android.MB360.wellness.homehealthcare.responseclass.ongoing.OnGoingSummaryNA;
import com.csform.android.MB360.wellness.homehealthcare.retrofit.RescheduleInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LongTermOnGoingAdapter extends RecyclerView.Adapter<LongTermOnGoingAdapter.LongTermOnGoingViewHolder> {
    List<OnGoingSummaryLT> summaryList;
    Context context;
    String serviceName;
    OnCancellation onCancellation;
    RescheduleInterface rescheduleInterface;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public LongTermOnGoingAdapter(List<OnGoingSummaryLT> summaryList, Context context, String serviceName, OnCancellation onCancellation, RescheduleInterface rescheduleInterface) {
        this.summaryList = summaryList;
        this.context = context;
        this.serviceName = serviceName;
        this.onCancellation = onCancellation;
        this.rescheduleInterface = rescheduleInterface;
    }

    @NonNull
    @Override
    public LongTermOnGoingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentBinding binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LongTermOnGoingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LongTermOnGoingViewHolder holder, int position) {
        try {
            String strApptSrNo = "";
            holder.binding.txtDependantName.setText(summaryList.get(position).getAppntPerson());
            holder.binding.txtDependantAge.setText(summaryList.get(position).getAppntPersonAge() + " years");
            holder.binding.txtAppointReference.setText(summaryList.get(position).getHhcApptLtRefNo());

            Date strDate = simpleDateFormat3.parse(summaryList.get(position).getScheduledOn());
            String date = simpleDateFormat3.format(strDate);
            holder.binding.txtAppointmentScheduled.setText(date);

            // viewHolder.txtPackageDetails.setText(lstSummary.get(position).getStrPkgPrice());
            String str = summaryList.get(position).getDatePreference();
            if (str.contains(",")) {
                String myStr = str.replaceAll(",", "\n");
                holder.binding.tvappointmentdoneon.setText(myStr);
            } else {
                holder.binding.tvappointmentdoneon.setText(str);
            }

            if (summaryList.get(position).getDateCondition().equalsIgnoreCase("Daywise")) {
                holder.binding.tvappointmentdoneonlabel.setText("Date Preference :  ");
            } else if (summaryList.get(position).getDateCondition().equalsIgnoreCase("Monthwise")) {
                holder.binding.tvappointmentdoneonlabel.setText("Month Preference :  ");
                String from_date = summaryList.get(position).getFromDate();
                String to_date = summaryList.get(position).getToDate();
                String dates = from_date + "\nTo\n" + to_date;
                holder.binding.tvappointmentdoneon.setText(dates);
            } else if (summaryList.get(position).getDateCondition().equalsIgnoreCase("Weekwise")) {
                holder.binding.tvappointmentdoneonlabel.setText("Week Preference :  ");
                String from_date = summaryList.get(position).getFromDate();
                String to_date = summaryList.get(position).getToDate();
                String dates = from_date + "\nTo\n" + to_date;
                holder.binding.tvappointmentdoneon.setText(dates);
            }else if (summaryList.get(position).getDateCondition().equalsIgnoreCase("DateRangewise")) {
                holder.binding.tvappointmentdoneonlabel.setText("Date Preference :  ");
                String from_date = summaryList.get(position).getFromDate();
                String to_date = summaryList.get(position).getToDate();
                String dates = from_date + "\nTo\n" + to_date;
                holder.binding.tvappointmentdoneon.setText(dates);
            }



            if (summaryList.get(position).getHhcLtRemarks() != null && !summaryList.get(position).getHhcLtRemarks().equals(""))
                holder.binding.tvRemarks.setText(summaryList.get(position).getHhcLtRemarks());
            else {
                holder.binding.tvRemarks.setVisibility(View.GONE);
                holder.binding.tvRemarkslabel.setVisibility(View.GONE);
            }
            strApptSrNo = summaryList.get(position).getAppntPersonSrNo();
            String strPrice = UtilMethods.PriceFormat(summaryList.get(position).getTotalPrice());
            holder.binding.tvTotalPrice.setText("\u20B9 " + strPrice);

            String strPackagePrice = UtilMethods.PriceFormat(summaryList.get(position).getPkgPrice());
            holder.binding.tvPackagePrice.setText("\u20B9 " + strPackagePrice);

            holder.binding.tvDuration.setText(summaryList.get(position).getLtDurations());


        } catch (Exception e) {
            e.printStackTrace();

        }

        holder.binding.llCancelOrder.setOnClickListener(v -> {
            onCancellation.cancelAppointment(summaryList.get(position).getHhcLtApptInfoSrNo(), position);
        });

        holder.binding.btnreschedule.setOnClickListener(view -> {
            rescheduleInterface.rescheduleAppointmentLT(summaryList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return (summaryList != null ? summaryList.size() : 0);
    }

    public static class LongTermOnGoingViewHolder extends RecyclerView.ViewHolder {
        ItemAppointmentBinding binding;

        public LongTermOnGoingViewHolder(@NonNull ItemAppointmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
