package com.csform.android.MB360.wellness.homehealthcare.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.csform.android.MB360.R;
import com.csform.android.MB360.databinding.ItemHomeHealthCareMemberBinding;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;
import com.csform.android.MB360.wellness.healthcheckuppackages.repository.ui.PersonAdapter;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;
import com.csform.android.MB360.wellness.homehealthcare.responseclass.MembersResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<FamilyMember> membersList;
    AddAddressListener addAddressListener;


    public MembersAdapter(Context context, List<FamilyMember> membersList, AddAddressListener addAddressListener) {
        this.context = context;
        this.membersList = membersList;
        this.addAddressListener = addAddressListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeHealthCareMemberBinding binding = ItemHomeHealthCareMemberBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MembersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final FamilyMember lstMember = membersList.get(holder.getAdapterPosition());

        ((MembersViewHolder) holder).binding.txtName.setText(membersList.get(position).getPersonName());
        ((MembersViewHolder) holder).binding.txtRelations.setText(membersList.get(position).getRelationName());
        String age = "(" + membersList.get(position).getAge() + " years)";
        ((MembersViewHolder) holder).binding.txtdepandantAge.setText(age);
        ((MembersViewHolder) holder).binding.actionText.setText("Select Member");

        ((MembersViewHolder) holder).binding.member.setOnClickListener(v -> {
            if (membersList.get(holder.getAdapterPosition()).getIsAddressSaved().equals("0")) {
                addAddressListener.getMember(membersList.get(holder.getAdapterPosition()));
            } else {
                if (lstMember.isSelected()) {
                    addAddressListener.selectPackage(lstMember);
                } else {
                    addAddressListener.selectMember(lstMember, position);
                }

            }

        });


        ((MembersViewHolder) holder).binding.checkedRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                addAddressListener.selectMember(lstMember, position);

            }
        });


        if (lstMember.isSelected()) {
            if (lstMember.getIsAddressSaved().equalsIgnoreCase("0")) {
                ((MembersViewHolder) holder).binding.actionText.setText("Add Address");
                ((MembersViewHolder) holder).binding.checkedRadio.setChecked(true);
            } else {
                ((MembersViewHolder) holder).binding.actionText.setText("Select Package");
                ((MembersViewHolder) holder).binding.checkedRadio.setChecked(true);
            }

        } else {
            ((MembersViewHolder) holder).binding.actionText.setText("Select Member");
            ((MembersViewHolder) holder).binding.checkedRadio.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        if (membersList == null) {
            return 0;
        } else {
            return membersList.size();
        }
    }

    static class MembersViewHolder extends RecyclerView.ViewHolder {
        ItemHomeHealthCareMemberBinding binding;

        public MembersViewHolder(@NonNull ItemHomeHealthCareMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    private void setSelected(int pos, boolean check) {
        membersList.get(pos).setSelected(check);
    }
}
