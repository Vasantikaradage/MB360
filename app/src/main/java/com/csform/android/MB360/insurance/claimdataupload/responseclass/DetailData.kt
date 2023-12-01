package com.csform.android.MB360.insurance.claimdataupload.responseclass

import com.google.gson.annotations.SerializedName

data class DetailData( @SerializedName("\$id"                 ) var id               : String?            = null,
                       @SerializedName("CLAIM_NAME"          ) var CLAIMNAME         : String?            = null,
                       @SerializedName("CLM_INT_SR_NO"       ) var CLMINTSRNO        : String?            = null,
                       @SerializedName("CLAIM_INTIMATION_NO" ) var CLAIMINTIMATIONNO : String?            = null,
                       @SerializedName("IntimationDetails"   ) var IntimationDetails : IntimationDetails? = IntimationDetails()
)
