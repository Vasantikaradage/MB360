package com.csform.android.MB360.insurance.claimdataupload.responseclass

import android.net.Uri
import java.io.File

data class ClaimFileUpload(
        var items: String,
        var int: Int,
        var status: Boolean,
        var fileSize: String,
        var fileName: String,
        var mandatory: Boolean,
        var data: Uri?=null,
        var isExpanded: Boolean = false,
        var remark: String="",
        var typedtext:String="",
        var CLM_REQ_DOCS_SR_NO:Int=0,
        var filePathInfo:String="",
        var fromEdit:Boolean=false

) /*{
    constructor() : this("",0,false,"","", false,Uri?=null,false,"","")
}
*/

