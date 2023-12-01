package com.csform.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.TopuplayooutBinding;
import com.csform.android.MB360.insurance.enrollment.interfaces.TopUpSelected;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.Si;
import com.csform.android.MB360.utilities.UtilMethods;

import java.util.List;

public class TopUpSumInsuredAdapter extends RecyclerView.Adapter<TopUpSumInsuredAdapter.TopUpsViewHolder> {
    Context context;
    List<Si> topUpList;
    TopUpSelected topUpSelected;
    boolean isWindowPeriodActive;
    boolean animation;
    int delayAnimate = 400;

    public TopUpSumInsuredAdapter(Context context, List<Si> topUpList, TopUpSelected topUpSelected, boolean isWindowPeriodActive, boolean animation) {
        this.context = context;
        this.topUpList = topUpList;
        this.topUpSelected = topUpSelected;
        this.isWindowPeriodActive = isWindowPeriodActive;
        this.animation = animation;
    }

    @NonNull
    @Override
    public TopUpSumInsuredAdapter.TopUpsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TopuplayooutBinding binding = TopuplayooutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TopUpSumInsuredAdapter.TopUpsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TopUpSumInsuredAdapter.TopUpsViewHolder holder, int position) {
        //to support the animation
        holder.itemView.setVisibility(View.GONE);

        try {
            if (!animation) {
                holder.itemView.setVisibility(View.VISIBLE);
            }

            if (animation) {
                Animation animation = null;
                if (position % 2 == 0) {
                    setAnimationLeft(holder.itemView);
                } else {
                    setAnimationRight(holder.itemView);
                }
              //  holder.itemView.startAnimation(animation);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Si topUp = topUpList.get(position);
        holder.binding.lblOptions.setText("option " + (position + 1));
        holder.binding.topupAmt.setText(String.format("%s %s", context.getString(R.string.rs), topUp.getSumInsured()));
        try {
            String primiousValue=topUp.getPremiumValue();
            holder.binding.premiumTxt.setText(String.format("Premium %s %s", context.getString(R.string.rs), UtilMethods.PriceFormat(primiousValue.replace(",",""))));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        if (topUp.getSelected()==true) {
            holder.binding.completeView.setBackground(ContextCompat.getDrawable(context, R.drawable.top_up_bg_70));


            holder.binding.checkedRadio.setVisibility(View.VISIBLE);
            holder.binding.chkbox.setChecked(true);


        } else {
            holder.binding.completeView.setBackground(ContextCompat.getDrawable(context, R.drawable.top_up_bg_35));
            holder.binding.checkedRadio.setVisibility(View.INVISIBLE);
            holder.binding.chkbox.setChecked(false);
        }
        holder.binding.completeView.setOnClickListener(v -> {
            if (isWindowPeriodActive) {
                topUpSelected.OnTopUpSelected(topUp, position);
            } else {
                Toast.makeText(context, "Window period has expired", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return (topUpList != null ? topUpList.size() : 0);
    }

    public static class TopUpsViewHolder extends RecyclerView.ViewHolder {
        public TopuplayooutBinding binding;

        public TopUpsViewHolder(@NonNull TopuplayooutBinding binding) {
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

