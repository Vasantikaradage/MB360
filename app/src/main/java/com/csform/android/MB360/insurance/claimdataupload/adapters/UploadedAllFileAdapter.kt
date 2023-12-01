package com.csform.android.MB360.insurance.claimdataupload.adapters

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.csform.android.MB360.R
import com.csform.android.MB360.databinding.ItemAllClaimsUploadedBinding
import com.csform.android.MB360.insurance.claimdataupload.interfaces.FileDownLoadHelper
import com.csform.android.MB360.insurance.claimdataupload.responseclass.FilePathList


class UploadedAllFileAdapter(var context: Context, var filePathList: ArrayList<FilePathList>, var activity: FragmentActivity,  var utilDownloadHelper: FileDownLoadHelper): RecyclerView.Adapter<UploadedFilesViewModel>()  {
    private lateinit var isDownloading: BooleanArray


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadedFilesViewModel {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAllClaimsUploadedBinding.inflate(inflater, parent, false)
        return UploadedFilesViewModel(binding)
    }

    override fun onBindViewHolder(holder: UploadedFilesViewModel, position: Int) {
        isDownloading = kotlin.BooleanArray(filePathList.size)
        val util: FilePathList = filePathList.get(position)

        when (util.fileType) {
            "pdf" -> holder.binding.itemUtilitiesIcon.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_pdf))
            "xlsx", "xls" ->holder.binding.itemUtilitiesIcon.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_xlsx))
            "docx", "doc" ->holder.binding.itemUtilitiesIcon.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_doc))
            "jpeg", "jpg" ->holder.binding.itemUtilitiesIcon.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.filetype_img))
            "png" -> holder.binding.itemUtilitiesIcon.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_png))
        }

        holder.binding.itemUtilitiesName.text=util.fileNameTemp

        holder.binding.itemUtilitiesCard.setOnClickListener { view: View? ->
            try {
                if (Build.VERSION.SDK_INT > 32) {

                   utilDownloadHelper.onUtilitiesClicked(position)
                } else {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        utilDownloadHelper.onUtilitiesClicked(position)
                     } else {
                        utilDownloadHelper!!.requestPermission(position, util)
                    }
                }
            } catch (e: ActivityNotFoundException) {
                utilDownloadHelper!!.onFinishDownload(position)
                Toast.makeText(context, "No Application found to open this file", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                utilDownloadHelper!!.onFinishDownload(position)
            }
        }
    }


    override fun getItemCount(): Int {
        return filePathList.size ?: 0
    }

    fun startDownloading(position: Int) {
        isDownloading[position] = true
    }

    fun finishDownloading(position: Int) {
        isDownloading[position] = false
    }
}



class UploadedFilesViewModel (binding: ItemAllClaimsUploadedBinding) : RecyclerView.ViewHolder(binding.root) {
    var binding: ItemAllClaimsUploadedBinding

    init {
        this.binding = binding
    }
}

