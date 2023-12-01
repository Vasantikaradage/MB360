package com.csform.android.MB360.insurance.claimdataupload.responseclass.LoadRequiredDocumentDetails

import com.google.gson.annotations.SerializedName

data class Detail(@SerializedName("\$id"                         ) var id                   : String? = null,
                  @SerializedName("CLM_DOC_NAME"                ) var CLMDOCNAME            : String? = null,
                  @SerializedName("CLM_DOC_DESCRIPTION"         ) var CLMDOCDESCRIPTION     : String? = null,
                  @SerializedName("CLM_REQ_DOCS_SR_NO"          ) var CLMREQDOCSSRNO        : Int?    = null,
                  @SerializedName("IS_DOC_MANDATORY"            ) var ISDOCMANDATORY        : Int?    = null,
                  @SerializedName("IS_DOC_TO_INCL_IN_COMM_MAIL" ) var ISDOCTOINCLINCOMMMAIL : Int?    = null)
