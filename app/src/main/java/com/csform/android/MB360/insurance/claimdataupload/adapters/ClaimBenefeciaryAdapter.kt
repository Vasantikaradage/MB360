package com.csform.android.MB360.insurance.claimdataupload.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.RecyclerView
import com.csform.android.MB360.R
import com.csform.android.MB360.databinding.ItemCoverageDepandantBinding
import com.csform.android.MB360.insurance.claimdataupload.interfaces.BeneficiarySelectListener
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AaData
import com.csform.android.MB360.insurance.claimdataupload.responseclass.Detail
import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum
import com.csform.android.MB360.utilities.LogMyBenefits


class ClaimBenefeciaryAdapter(private var items: List<Detail>, var context: Context, var beneficiarySelected: BeneficiarySelectListener, var tpaData: String) : RecyclerView.Adapter<ClaimsBeneficiaryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimsBeneficiaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCoverageDepandantBinding.inflate(inflater, parent, false)
        return ClaimsBeneficiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClaimsBeneficiaryViewHolder, position: Int) {

        //set the item here
        val item = items[position]
        holder.binding.itemDependantName.text = item.PERSONNAME

        if ((item.RELATIONNAME.isNullOrEmpty())) {
            when (item.RELATIONID) {
                "4" -> {
                    holder.binding.itemDependantRelation.text = "Daughter"
                }

                "3" -> {
                    holder.binding.itemDependantRelation.text = "Son"
                }

                "21" -> {
                    holder.binding.itemDependantRelation.text = "Partner"
                }

                "1" -> {
                    holder.binding.itemDependantRelation.text = "FATHER"

                }

                "5" -> {

                    holder.binding.itemDependantRelation.text = "FATHER-IN_LAW"
                }

                "6" -> {

                    holder.binding.itemDependantRelation.text = "MOTHER-IN_LAW"
                }

                "2" -> {
                    // itemsdata[position].relation = "TWINS"
                    holder.binding.itemDependantRelation.text = "MOTHER"
                }

                "17" -> {
                    holder.binding.itemDependantRelation.text = "Employee"
                }

                "11" -> {
                    holder.binding.itemDependantRelation.text = "SPOUSE"

                }

                else -> {

                    holder.binding.itemDependantRelation.text = item.RELATIONNAME
                }
            }

        }




        holder.binding.userImage.setImageDrawable(
                if (item.GENDER.equals("MALE", ignoreCase = true)) {
                    if (item.RELATIONNAME.equals("SON", false)) {
                        ContextCompat.getDrawable(context, R.drawable.ic_by_son)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.ic_by_male)
                    }

                } else if (item.GENDER.equals("FEMALE", ignoreCase = true)) {
                    if (item.RELATIONNAME.equals("DAUGHTER", false)) {
                        ContextCompat.getDrawable(context, R.drawable.ic_by_daughter)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.ic_by_woman)
                    }

                } else {
                    ContextCompat.getDrawable(context, R.drawable.ic_by_user)
                }
        )

        holder.binding.itemDob.text = "DOB : ${item.DATEOFBIRTH}"
        holder.binding.itemAge.text = "Age : ${item.AGE + " Years"}"
        holder.binding.itemGender.text = "Gender : ${item.GENDER}"
        holder.binding.itemMobile.text = "Mobile : ${item.CELLPHONENUMBER}"

        if (tpaData.equals("TPAYes")) {
            holder.binding.cliamNoLayout.visibility = View.GONE
        } else {
            holder.binding.cliamNoLayout.visibility = View.VISIBLE
            if (!item.claimNo.isNullOrEmpty()) {
                var claimIntSrNo: String = item.claimIntSrNo
                holder.binding.tvIntimationNo.text = "Claim No. $claimIntSrNo"
            } else {
                holder.binding.tvIntimationNo.text = "Claim No. 00000000"
            }
        }




        holder.binding.dependantRadioBtn.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                beneficiarySelected.onBeneficiarySelect(position, item, true)

            } else {

            }
        }

        holder.binding.itemCard.setOnClickListener {
            beneficiarySelected.onBeneficiarySelect(position, item, true)
        }


        holder.binding.dependantRadioBtn.isChecked = item.isSelected

        if (item.isSelected) {
            holder.binding.itemSecondaryLayout.visibility = View.VISIBLE
        } else {
            holder.binding.itemSecondaryLayout.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return items.size ?: 0
    }

    fun updateData(newData: List<GroupGMCPolicyEmpDependantsDatum>) {
        // items = newData
        notifyDataSetChanged()
    }

}

class ClaimsBeneficiaryViewHolder(binding: ItemCoverageDepandantBinding) : RecyclerView.ViewHolder(binding.root) {

    var binding: ItemCoverageDepandantBinding

    init {
        this.binding = binding
    }


}