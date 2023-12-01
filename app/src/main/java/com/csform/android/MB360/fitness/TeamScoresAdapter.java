package com.csform.android.MB360.fitness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.ItemClaimProcedureHelplineNumberBinding;
import com.csform.android.MB360.databinding.TeamScoresLayoutBinding;
import com.csform.android.MB360.fitness.AktivoData.Team_Score_VH;

import java.util.List;

public class TeamScoresAdapter extends RecyclerView.Adapter<TeamScoresAdapter.TeamViewHolder> {

    private List<Team_Score_VH> teamScoreVhList;

    private Context context;

    public TeamScoresAdapter(List<Team_Score_VH> lstTeamScore1, Context mContext) {
        this.teamScoreVhList = lstTeamScore1;
        this.context = mContext;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TeamScoresLayoutBinding teamScoresLayoutBinding = TeamScoresLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TeamViewHolder(teamScoresLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        try {
            Team_Score_VH teamScoreVh = teamScoreVhList.get(position);
            if (position == 0) {
                holder.binding.topTeam.setVisibility(View.VISIBLE);
                holder.binding.usrCount.setVisibility(View.GONE);
            }
            holder.binding.usrName.setText(teamScoreVh.getUsrName());

            holder.binding.usrCount.setText((teamScoreVh.getUsrId() < 10) ?
                    "0" + teamScoreVh.getUsrId() :
                    "" + teamScoreVh.getUsrId());

            holder.binding.usrScore.setText(teamScoreVh.getUsrScore() == null ? "-" :
                    teamScoreVh.getUsrScore());


            holder.binding.llTeamView.setBackground(teamScoreVh.getUsrBoolean() ?
                    context.getResources().getDrawable(position == teamScoreVhList.size() - 1 ?
                            R.drawable.leaderboard : R.color.selected_grey) :
                    context.getResources().getDrawable(R.color.white));

            if (teamScoreVh.getImpact() > 0) {
                 holder.binding.scoreProg.setText(context.getString(R.string.up_arrow));
            } else if (teamScoreVh.getImpact() < 0) {
                holder.binding.scoreProg.setText(context.getString(R.string.down_arrow));
            } else {
                holder.binding.scoreProg.setText("-");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return (teamScoreVhList == null ? 0 : teamScoreVhList.size());
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {

        TeamScoresLayoutBinding binding;

        public TeamViewHolder(@NonNull TeamScoresLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

