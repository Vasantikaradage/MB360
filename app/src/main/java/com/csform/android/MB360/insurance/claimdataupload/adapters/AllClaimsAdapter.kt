package com.csform.android.MB360.insurance.claimdataupload.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csform.android.MB360.databinding.ItemAllClaimsBinding
import com.csform.android.MB360.insurance.claimdataupload.AllClaimsFragment
import com.csform.android.MB360.insurance.claimdataupload.interfaces.CduEditListerner
import com.csform.android.MB360.insurance.claimdataupload.interfaces.FileUploadListerner
import com.csform.android.MB360.insurance.claimdataupload.responseclass.AaData

class AllClaimsAdapter(private var items: List<AaData>, var context: AllClaimsFragment, private val fileUploadListerner: FileUploadListerner, private var cduEditListerner: CduEditListerner) : RecyclerView.Adapter<AllClaimsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllClaimsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAllClaimsBinding.inflate(inflater, parent, false)
        return AllClaimsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size ?: 0
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: AllClaimsViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvRequest.text = item.CLMDOCSUPLOADREQNO
        holder.binding.tvDate.text = item.CLMDOCSULOADEDON
        holder.binding.tvStatus.text = item.STATUS
        holder.binding.tvTypeClaim.text = item.TYPEOFCLAIM
        holder.binding.tvClaimIntimated.text = "On ${item.CLAIMINTIMATEDDEST}"
        holder.binding.tvClaimIntimationNo.text = item.CLAIMINTIMATIONNO
        holder.binding.tvFullName.text = item.PERSONNAME

        holder.binding.tvYears.text = item.DATEOFBIRTH + " (${item.AGE + " years"})"


        when (item.RELATIONID) {
            "4" -> {
                holder.binding.tvRelation.text = "Daughter"
            }

            "3" -> {
                holder.binding.tvRelation.text = "Son"
            }

            "21" -> {
                holder.binding.tvRelation.text = "Partner"
            }

            "1" -> {
                holder.binding.tvRelation.text = "FATHER"

            }

            "5" -> {
                holder.binding.tvRelation.text = "FATHER-IN_LAW"
            }

            "6" -> {
                holder.binding.tvRelation.text = "MOTHER-IN_LAW"
            }

            "2" -> {
                holder.binding.tvRelation.text = "MOTHER"
            }

            "17" -> {
                holder.binding.tvRelation.text = "Employee"
            }

            "11" -> {
                holder.binding.tvRelation.text = "SPOUSE"
            }

            else -> {

                holder.binding.tvRelation.text = ""
            }
        }

        /*if (item.STATUS.equals("INCOMPLETE")) {
            holder.binding.btnView.visibility = View.GONE
        } else {
            holder.binding.btnView.visibility = View.VISIBLE
        }*/

        holder.binding.btnView.setOnClickListener {
            fileUploadListerner.fileOnClickListerner(position)
        }

        holder.binding.btnEdit.setOnClickListener {
            cduEditListerner.onEditListerner(position)
        }


    }
}

class AllClaimsViewHolder(binding: ItemAllClaimsBinding) : RecyclerView.ViewHolder(binding.root) {
    var binding: ItemAllClaimsBinding

    init {
        this.binding = binding
    }
}