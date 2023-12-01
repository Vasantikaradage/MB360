package com.csform.android.MB360.insurance.claimdataupload.interfaces

import com.csform.android.MB360.insurance.claimdataupload.responseclass.FilePathList

interface FileDownLoadHelper {

    fun onUtilitiesClicked( position: Int)

    fun onStartDownload(position: Int)

    fun onFinishDownload(position: Int)

    fun requestPermission(position: Int, utility: FilePathList?)
}