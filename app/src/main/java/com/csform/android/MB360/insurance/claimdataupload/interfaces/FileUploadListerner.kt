package com.csform.android.MB360.insurance.claimdataupload.interfaces

import android.os.Parcelable
import java.io.Serializable

interface FileUploadListerner : Serializable {
    fun fileOnClickListerner(param: Int)
}