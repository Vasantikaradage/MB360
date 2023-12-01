package com.csform.android.MB360.insurance.claimdataupload.responseclass.inserClaimDocResponse

import com.google.gson.annotations.SerializedName

data class InsertClaimDocResponse(@SerializedName("\$id"                        ) var id                  : String? = null,
                                  @SerializedName("CLM_DOC_UPD_REQ_DOCS_SR_NO" ) var CLMDOCUPDREQDOCSSRNO : Int?    = null,
                                  @SerializedName("CLM_DOC_FILE_UPD_SR_NO"     ) var CLMDOCFILEUPDSRNO    : Int?    = null,
                                  @SerializedName("IsMapped"                   ) var IsMapped             : Int?    = null,
                                  @SerializedName("IsStatusUpdated"            ) var IsStatusUpdated      : Int?    = null,
                                  @SerializedName("IsFileSavedToServer"        ) var IsFileSavedToServer  : Int?    = null,
                                  @SerializedName("res"                        ) var res                  : ResInfo?    = ResInfo())
