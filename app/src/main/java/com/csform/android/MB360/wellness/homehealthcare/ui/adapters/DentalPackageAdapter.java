package com.csform.android.MB360.wellness.homehealthcare.ui.adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.csform.android.MB360.databinding.ItemDentalPackageBinding;
import com.csform.android.MB360.utilities.glidesvghelper.SvgModule;
import com.csform.android.MB360.utilities.glidesvghelper.SvgSoftwareLayerSetter;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.DentalPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DentalPackageAdapter extends RecyclerView.Adapter<DentalPackageAdapter.DentalPackageViewHolder> {

    List<DentalPackage> dentalList = new ArrayList<>();
    Context context;
    RequestBuilder requestBuilder;


    public DentalPackageAdapter(List<DentalPackage> dentalList, Context context) {
        this.dentalList = dentalList;
        this.context = context;
    }

    @NonNull
    @Override
    public DentalPackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDentalPackageBinding binding = ItemDentalPackageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DentalPackageViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull DentalPackageViewHolder holder, int position) {

        Glide.with(context)
                .load(dentalList.get(position).getImg_url())
                .transition(withCrossFade())
                .into(holder.binding.imageTitle);


        holder.binding.textTitle.setText(dentalList.get(position).getName());
        holder.binding.dentalPrice.setText(String.format(Locale.ENGLISH, "Starting from %s", dentalList.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return (dentalList != null ? dentalList.size() : 0);
    }

    public static class DentalPackageViewHolder extends RecyclerView.ViewHolder {
        ItemDentalPackageBinding binding;

        public DentalPackageViewHolder(@NonNull ItemDentalPackageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
