package com.csform.android.MB360.insurance.claimdataupload.responseclass.CDUAllSelecetedValue

import com.google.gson.annotations.SerializedName

data class CDUAllSelectedResponse(@SerializedName("\$id"                       ) var id                  : String? = null,
                                  @SerializedName("CLM_DOCS_UPLOAD_REQ_SR_NO" ) var CLMDOCSUPLOADREQSRNO : Int?    = null,
                                  @SerializedName("Res"                       ) var statusValue                  : StatusValue?    = StatusValue())
