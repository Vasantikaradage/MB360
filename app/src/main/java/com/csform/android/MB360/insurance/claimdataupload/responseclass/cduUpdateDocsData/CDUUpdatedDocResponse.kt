package com.csform.android.MB360.insurance.claimdataupload.responseclass.cduUpdateDocsData

import com.google.gson.annotations.SerializedName

data class CDUUpdatedDocResponse(
        @SerializedName("\$id"     ) var id     : String?  = null,
        @SerializedName("Message" ) var Message : String?  = null,
        @SerializedName("Status"  ) var Status  : Boolean? = null
)
