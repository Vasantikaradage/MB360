package com.csform.android.MB360.insurance.claimdataupload.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.csform.android.MB360.R
import com.csform.android.MB360.databinding.ItemClaimsFileUploadBinding
import com.csform.android.MB360.insurance.claimdataupload.interfaces.ExpandListerner
import com.csform.android.MB360.insurance.claimdataupload.interfaces.FileUploadListerner
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ClaimFileUpload


class ClaimFileUploadAdapter(private val items: ArrayList<ClaimFileUpload>, private val fileUploadListerner: FileUploadListerner, private val expandListerner: ExpandListerner,var  context: FragmentActivity) : RecyclerView.Adapter<ClaimsFileUploadViewHolder>() {
    var previousExpandedPosition = -1
    var mExpandedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimsFileUploadViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemClaimsFileUploadBinding.inflate(inflater, parent, false)
        return ClaimsFileUploadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClaimsFileUploadViewHolder, position: Int) {


        val item: ClaimFileUpload = items[position]
        holder.binding.itemFileName.text = item.items
        holder.getRemarkData(item, position)

        // expand view
        val isExpanded = position === mExpandedPosition
        holder.binding.secondLayout.setVisibility(if (isExpanded) View.VISIBLE else View.GONE)
        holder.itemView.isActivated = isExpanded
        if (isExpanded) previousExpandedPosition = position

        if (item.status) {
            holder.binding.fileEdit.visibility = View.VISIBLE
            holder.binding.fileAdd.visibility = (View.INVISIBLE)
            holder.binding.etRemark.editText?.setText(item.remark)


            // "${item.fileSize} Kb".also { holder.binding.itemFileSize.text = it }
            holder.binding.mainLayout.setOnClickListener {
                mExpandedPosition = if (isExpanded) -1 else position
                expandListerner.onExpandListerner(previousExpandedPosition, position)
            }


        } else {
            holder.binding.fileAdd.visibility = (View.VISIBLE)
            holder.binding.fileEdit.visibility = (View.GONE)
            holder.binding.etRemark.editText?.setText("")


            //  holder.binding.itemFileSize.text = ""
            holder.binding.mainLayout.setOnClickListener {
                mExpandedPosition = if (isExpanded) -1 else position

                if(!isExpanded) {
                    fileUploadListerner.fileOnClickListerner(position)
                }
                expandListerner.onExpandListerner(previousExpandedPosition, position)
            }


        }

        if(item.fromEdit)
        {
            holder.binding.mainLayout.isEnabled=false
            holder.binding.secondLayout.visibility=View.GONE
            holder.binding.fileEdit.visibility=View.VISIBLE
            holder.binding.fileAdd.visibility=View.GONE
            holder.binding.itemFileName.setTextColor(ContextCompat.getColor(context, R.color.error_text_color))
        }


        holder.binding.browse.setOnClickListener {
            item.status = false
            fileUploadListerner.fileOnClickListerner(position)
        }


        if (item.fileName.equals("", false)) {
            holder.binding.fileName.text = "No File Choosen"
            holder.binding.cancel.isClickable = false

        } else {
            holder.binding.fileName.text = item.fileName
            holder.binding.cancel.isClickable = true

            holder.binding.cancel.setOnClickListener {
                item.status = true
                fileUploadListerner.fileOnClickListerner(position)
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}



class ClaimsFileUploadViewHolder(binding: ItemClaimsFileUploadBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
    public val binding: ItemClaimsFileUploadBinding
    var textWatcher: TextWatcher? = null

    val remarkEditText: TextInputLayout


    init {
        this.binding = binding
        remarkEditText = this.binding.etRemark
    }

    fun getRemarkData(item: ClaimFileUpload, position: Int) {
        remarkEditText.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (adapterPosition == position) {
                    item.remark = p0.toString()
                }

            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
}


