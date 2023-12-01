package com.csform.android.MB360.fitness;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTracker;
import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.DeviceLayoutBinding;
import com.csform.android.MB360.fitness.request.DeviceList;

import java.util.List;
import java.util.Objects;

public class DeviceConnectionAdapter extends RecyclerView.Adapter<DeviceConnectionAdapter.ViewHolder> {
    private List<FitnessTracker> devicePlatforms;
    private Context context;

    public DeviceConnectionAdapter(Context mContext, List<FitnessTracker> deviceList) {
        this.context = mContext;
        this.devicePlatforms = deviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeviceLayoutBinding deviceLayoutBinding = DeviceLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(deviceLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FitnessTracker fitnessTracker = devicePlatforms.get(position);
        holder.binding.deviceType.setText(fitnessTracker.getPlatform().toString());
        holder.binding.dataVia.setVisibility(View.GONE);
        Intent intent = new Intent(context, TrackerWebViewActivity.class);


        switch (Objects.requireNonNull(fitnessTracker.getPlatform())) {
            case FITBIT:
                holder.binding.deviceImage.setImageResource(R.drawable.ic_fitbit);
                intent.putExtra("tracker_URL", "fitbit");
                break;
            case GARMIN:
                holder.binding.deviceImage.setImageResource(R.drawable.ic_garmin);
                intent.putExtra("tracker_URL", "garmin");
                break;
        }

        switch (Objects.requireNonNull(fitnessTracker.getFitnessTrackerConnectionState())) {
            case CONNECTED:
                holder.binding.deviceStatus.setText(R.string.disconnect);
                break;
            case DISCONNECTED:
            case REVOKED:
                holder.binding.deviceStatus.setText(R.string.connect);
                break;
        }

        holder.binding.deviceStatus.setOnClickListener(v -> {
            context.startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return (devicePlatforms == null ? 0 : devicePlatforms.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        DeviceLayoutBinding binding;

        public ViewHolder(@NonNull DeviceLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}