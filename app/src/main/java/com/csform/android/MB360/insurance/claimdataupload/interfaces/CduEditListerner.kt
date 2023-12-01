package com.csform.android.MB360.insurance.claimdataupload.interfaces

import java.io.Serializable

interface CduEditListerner : Serializable {
    fun onEditListerner(param: Int)
}