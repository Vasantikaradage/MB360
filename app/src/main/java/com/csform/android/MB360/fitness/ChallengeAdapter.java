package com.csform.android.MB360.fitness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csform.android.MB360.databinding.ChallengeListItemBinding;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {
    private List<ChallengeLayoutItems> lstChallenge, lstChallengeFiltered;
    private Context cnt;
    private SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH);

    public ChallengeAdapter(Context cnt, List<ChallengeLayoutItems> lstChallenge) {
        this.lstChallengeFiltered = this.lstChallenge;
        this.cnt = cnt;
        this.lstChallenge = lstChallenge;
    }


    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChallengeListItemBinding challengeListItemBinding = ChallengeListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChallengeViewHolder(challengeListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstChallengeFiltered.size();
    }

    public class ChallengeViewHolder extends RecyclerView.ViewHolder {
        ChallengeListItemBinding binding;

        public ChallengeViewHolder(@NonNull ChallengeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
