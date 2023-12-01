package com.csform.android.MB360.insurance.escalations.repository.ui;

import static com.csform.android.MB360.BuildConfig.NOT_AVAILABLE;

import android.content.Context;
import android.text.util.Linkify;
import com.csform.android.MB360.utilities.LogMyBenefits;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;



import com.csform.android.MB360.R;


import com.csform.android.MB360.insurance.escalations.repository.responseclass.GroupEscalationInfo;
import com.csform.android.MB360.utilities.AppServerConstants;
import com.csform.android.MB360.utilities.LogMyBenefits;
import com.csform.android.MB360.utilities.LogTags;

import java.text.MessageFormat;
import java.util.List;

public class EscalationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<GroupEscalationInfo> escalationInfoList;


    public EscalationAdapter(Context mContext, List<GroupEscalationInfo> escalationInfoList) {
        this.escalationInfoList = escalationInfoList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.escalation_items, parent, false);

        return new EscalationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {
            final GroupEscalationInfo lstContac = escalationInfoList.get(position);
            int level = position + 1;
            String Escalation = lstContac.getEscalation();

            if (Escalation.equals("NOT APPLICABLE") | Escalation.equals("ESCALATION"))
                ((EscalationsViewHolder) holder).lblExcalation.setVisibility(View.GONE);
            else
                ((EscalationsViewHolder) holder).lblExcalation.setText(String.valueOf(level));


            ((EscalationsViewHolder) holder).lblContactName.setText(lstContac.getContactPerson());
            ((EscalationsViewHolder) holder).lblContactAddress.setText(lstContac.getAddress());
            ((EscalationsViewHolder) holder).lblContactll.setText(String.format("%s %s",
                    lstContac.getMobileNo().replace(NOT_AVAILABLE, ""),
                    lstContac.getLandlineNo().replace(NOT_AVAILABLE, "")));
            Linkify.addLinks(((EscalationsViewHolder) holder).lblContactll, Linkify.PHONE_NUMBERS);


            ((EscalationsViewHolder) holder).lblContactfax.setText(lstContac.getFaxNo().equals(NOT_AVAILABLE) ? "-" : lstContac.getFaxNo());
            Linkify.addLinks(((EscalationsViewHolder) holder).lblContactfax, Linkify.PHONE_NUMBERS);


            ((EscalationsViewHolder) holder).lblEmail.setText(lstContac.getEmail());
            Linkify.addLinks(((EscalationsViewHolder) holder).lblEmail, Linkify.EMAIL_ADDRESSES);

            switch (lstContac.getDescription()) {

                case "TPA":
                    ((EscalationsViewHolder) holder).v1.setBackgroundColor(ContextCompat.getColor(mContext,R.color.tpa));
                    ((EscalationsViewHolder) holder).lblExcalation.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.badge_blue));
                    break;

                case "BROKER":
                    ((EscalationsViewHolder) holder).v1.setBackgroundColor(ContextCompat.getColor(mContext,R.color.lng));
                    ((EscalationsViewHolder) holder).lblExcalation.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.badge_red));
                    break;

                case "HR":
                    ((EscalationsViewHolder) holder).v1.setBackgroundColor(ContextCompat.getColor(mContext,R.color.hr));
                    ((EscalationsViewHolder) holder).lblExcalation.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.badge_yellow));
                    break;

                default:
                    //    default:
                    ((EscalationsViewHolder) holder).v1.setBackgroundColor(ContextCompat.getColor(mContext,R.color.lng));
                    ((EscalationsViewHolder) holder).lblExcalation.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.badge_red));
                    ((EscalationsViewHolder) holder).lblExcalation.setText(MessageFormat.format("{0}", position + 1));
                    ((EscalationsViewHolder) holder).lblExcalation.setVisibility(View.VISIBLE);

                    break;

            }

        } catch (Exception e) {
            LogMyBenefits.e(LogTags.ESCALATION_ACTIVITY, "Error ", e);
        }

    }

    @Override
    public int getItemCount() {
        if (escalationInfoList != null) {
            return escalationInfoList.size();
        } else {
            return 0;
        }
    }

    public static class EscalationsViewHolder extends RecyclerView.ViewHolder {

         AppCompatTextView lblExcalation, lblContactName, lblContactAddress, lblContactll, lblContactfax, lblEmail;
         AppCompatTextView lblMail, lblPhone, lblFax;
         AppCompatTextView lblLocation;
         View v1;

        public EscalationsViewHolder(@NonNull View itemView) {
            super(itemView);

            //
            lblExcalation = itemView.findViewById(R.id.txtLVL);
            lblContactName = itemView.findViewById(R.id.lblContactName);
            lblContactAddress = itemView.findViewById(R.id.lblContactAddress);
            lblContactll = itemView.findViewById(R.id.lblContactll);
            lblContactfax = itemView.findViewById(R.id.lblContactfax);

            lblEmail = itemView.findViewById(R.id.lblContactemail);

            lblLocation = itemView.findViewById(R.id.locIcon);
            lblMail = itemView.findViewById(R.id.mailIcon);
            lblPhone = itemView.findViewById(R.id.callIcon);
            lblFax = itemView.findViewById(R.id.faxIcon);
            v1 = itemView.findViewById(R.id.v1);

        }
    }
}
