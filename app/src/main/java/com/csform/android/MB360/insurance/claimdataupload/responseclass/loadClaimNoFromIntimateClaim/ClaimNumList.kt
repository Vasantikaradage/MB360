package com.csform.android.MB360.insurance.claimdataupload.responseclass.loadClaimNoFromIntimateClaim

import com.google.gson.annotations.SerializedName

data class ClaimNumList(@SerializedName("\$id"               ) var id               : String? = null,
                        @SerializedName("ClaimNo"           ) var ClaimNo           : String? = null,
                        @SerializedName("IntimationClaimNo" ) var IntimationClaimNo : String? = null)
