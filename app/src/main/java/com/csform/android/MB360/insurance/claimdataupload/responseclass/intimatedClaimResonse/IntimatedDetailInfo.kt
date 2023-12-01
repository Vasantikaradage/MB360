package com.csform.android.MB360.insurance.claimdataupload.responseclass.intimatedClaimResonse

import com.google.gson.annotations.SerializedName
import com.csform.android.MB360.insurance.claimdataupload.responseclass.intimatedClaimResonse.IntimatedClaimsDetail

data class IntimatedDetailInfo (
        @SerializedName("\$id"                 ) var id               : String?            = null,
        @SerializedName("CLAIM_NAME"          ) var CLAIMNAME         : String?            = null,
        @SerializedName("CLM_INT_SR_NO"       ) var CLMINTSRNO        : String?            = null,
        @SerializedName("CLAIM_INTIMATION_NO" ) var CLAIMINTIMATIONNO : String?            = null,
        @SerializedName("IntimationDetails"   ) var IntimationDetails : IntimatedClaimsDetail? = IntimatedClaimsDetail()
        )
