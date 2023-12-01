package com.csform.android.MB360.insurance.enrollment.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.ItemEnrollmentMyCoverageBinding;
import com.csform.android.MB360.insurance.enrollment.interfaces.ToolTipListener;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.WindowPeriodEnrollmentResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.coverage.CoverageMainResponse;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.coverage.Si;
import com.csform.android.MB360.utilities.UtilMethods;
import com.csform.android.MB360.utilities.WindowPeriodCounter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentCoveragesAdapterNew extends RecyclerView.Adapter<EnrollmentCoveragesAdapterNew.EnrollmentCoveragesViewHolder> {

    Context context;
    ToolTipListener toolTipListener;
    List<Si> coveragesList;
    String tooltipText = "";
    String atleastOneAlpha = ".*[a-zA-Z]+.*";
    WindowPeriodEnrollmentResponse windowResponse;
    boolean TO_SHOW_TIMER;
    Activity activity;
    boolean animations = true;
    int delayAnimate = 400;


    public EnrollmentCoveragesAdapterNew(Context context, Activity activity, ArrayList<Si> coveragesList, ToolTipListener toolTipListener, WindowPeriodEnrollmentResponse windowResponse, boolean TO_SHOW_TIMER, boolean animations) {
        this.context = context;
        this.coveragesList = coveragesList;
        this.toolTipListener = toolTipListener;
        this.windowResponse = windowResponse;
        this.TO_SHOW_TIMER = TO_SHOW_TIMER;
        this.activity = activity;
        this.animations = animations;
    }

    @NonNull
    @Override
    public EnrollmentCoveragesAdapterNew.EnrollmentCoveragesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEnrollmentMyCoverageBinding binding = ItemEnrollmentMyCoverageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EnrollmentCoveragesAdapterNew.EnrollmentCoveragesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EnrollmentCoveragesAdapterNew.EnrollmentCoveragesViewHolder holder, int position) {
        holder.itemView.setVisibility(View.GONE);
        if (!animations) {
            holder.itemView.setVisibility(View.VISIBLE);
        }
        Si coverage = coveragesList.get(position);


        holder.binding.myCoverageTitle.setText(coveragesList.get(position).getSelect_policy());

        if (TO_SHOW_TIMER) {
            getWindowPeriodTimer(windowResponse, holder.binding.timerCoverageDetails, "gmc", context, activity);
        }

        //how to show sum insured

        try {
            if (coverage.getSumInsuredWoComma().equalsIgnoreCase("0")) {
                holder.binding.sumInsured.setVisibility(View.VISIBLE);
                holder.binding.sumInsured.setText(String.format("%s", coverage.getSumInsured()));
            } else {
                holder.binding.sumInsured.setVisibility(View.VISIBLE);
                holder.binding.sumInsured.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getSumInsured()));
                if (coverage.getSiType() != null) {
                    if (coverage.getSiType().equals("1") && coverage.getSelect_policy().equals("Group Health Insurance"))
                        holder.binding.sumInsuredType.setText("(Fixed)");
                    else {
                        holder.binding.sumInsuredType.setText("(Floater)");
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        //to show premium

        try {
            if (coverage.getPremiumValue().equals("0")) {
                holder.binding.premiumAmountTitle.setVisibility(View.VISIBLE);
                holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                holder.binding.premiumAmountType.setVisibility(View.VISIBLE);
                holder.binding.premiumLayout.setVisibility(View.VISIBLE);
                holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);

            } else {
                holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                holder.binding.premiumAmount.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getPremiumValue()));

                holder.binding.premiumAmountTitle.setVisibility(View.VISIBLE);
                holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                holder.binding.premiumAmountType.setVisibility(View.VISIBLE);
                holder.binding.premiumLayout.setVisibility(View.VISIBLE);
                holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);

            }
            if (coverage.getPiType() != null) {
                if (coverage.getPiType().equals("1") && coverage.getSelect_policy().equals("Group Health Insurance")) {

                    holder.binding.premiumAmountType.setText("(Individual)");
                } else {
                    holder.binding.premiumAmountType.setText("(Family)");
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }



        //tool tip
        if (coverage.getEmployeeContri().equals("0") && coverage.getEmployerContri().equals("0")) {
            //hide tool tip
            holder.binding.coverageTooltipImage.setVisibility(View.GONE);
            tooltipText = "";

        } else if (coverage.getEmployeeContri().equals("0") && !coverage.getEmployerContri().equals("0")) {
            holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);
            holder.binding.coverageTooltipImage.setOnClickListener(v -> {
                tooltipText = "  Premium Contribution" + "\n• Employer Contribution: " + UtilMethods.PriceFormat(coverage.getEmployerContri().toString());
                toolTipListener.OnToolTipListener(tooltipText, holder.binding.coverageTooltipImage, holder.binding.coverageItem);
            });

            //to show the Employee Contribution
        } else if (!coverage.getEmployeeContri().equals("") && coverage.getEmployerContri().equals("0")) {
            holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);
            holder.binding.coverageTooltipImage.setOnClickListener(v -> {
                tooltipText = "  Premium Contribution" + "\n• Employee Contribution: " + UtilMethods.PriceFormat(coverage.getEmployeeContri().toString());
                toolTipListener.OnToolTipListener(tooltipText, holder.binding.coverageTooltipImage, holder.binding.coverageItem);
            });

        } else if (!coverage.getEmployeeContri().equals("0") && !coverage.getEmployeeContri().equals("0")) {

            holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);
            holder.binding.coverageTooltipImage.setOnClickListener(v -> {
                tooltipText = "  Premium Contribution" + "\n• Employee Contribution: " + UtilMethods.PriceFormat(coverage.getEmployeeContri().toString()) + "\n• Employer Contribution: " + UtilMethods.PriceFormat(coverage.getEmployerContri().toString());
                toolTipListener.OnToolTipListener(tooltipText, holder.binding.coverageTooltipImage, holder.binding.coverageItem);
            });
        } else {
            holder.binding.coverageTooltipImage.setVisibility(View.GONE);
        }

      /*  if (TO_SHOW_TIMER) {
            holder.binding.timerCoverageDetails.setVisibility(View.VISIBLE);
        } else {
            holder.binding.timerCoverageDetails.setVisibility(View.GONE);
        }*/


        if (animations) {

            if (position % 2 == 0) {
                setAnimationLeft(holder.itemView);
            } else {
                setAnimationRight(holder.itemView);
            }
            // holder.itemView.startAnimation(animation);

        }

    }

    private void getWindowPeriodTimer(WindowPeriodEnrollmentResponse windowPeriod, TextView timerText, String page, Context context, Activity activity) {

        String windowPeriodEndDate = "";
        if (page.equalsIgnoreCase("gmc")) {
            windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGmc();
        } else if (page.equalsIgnoreCase("gpa")) {
            windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGpa();
        } else if (page.equalsIgnoreCase("gtl")) {
            windowPeriodEndDate = windowPeriod.getWindowPeriod().getWindowEndDateGtl();

        }


        if (windowPeriod != null) {
            WindowPeriodCounter windowPeriodCounter = new WindowPeriodCounter(windowPeriodEndDate, context, activity);
            try {
                CountDownTimer timer = windowPeriodCounter.getTimer(timerText);
                if (timer != null) {

                    timer.start();
                } else {
                    timerText.setText("Window period has expired");
                }
            } catch (ParseException e) {

                Toast.makeText(context, "Unable to retrieve window period date.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {
            //no window period available
        }


    }


    @Override
    public int getItemCount() {
        return (coveragesList != null ? coveragesList.size() : 0);
    }


    public static class EnrollmentCoveragesViewHolder extends RecyclerView.ViewHolder {
        ItemEnrollmentMyCoverageBinding binding;

        public EnrollmentCoveragesViewHolder(@NonNull ItemEnrollmentMyCoverageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void setAnimationLeft(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                if (view != null) {
                    view.startAnimation(animation);
                    view.setVisibility(View.VISIBLE);
                }
            }
        }, delayAnimate);
        delayAnimate += 300;
    }

    private void setAnimationRight(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);
                if (view != null) {
                    view.startAnimation(animation);
                    view.setVisibility(View.VISIBLE);
                }
            }
        }, delayAnimate);
        delayAnimate += 300;
    }
}