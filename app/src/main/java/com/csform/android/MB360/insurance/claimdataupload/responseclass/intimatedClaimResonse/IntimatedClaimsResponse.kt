package com.csform.android.MB360.insurance.claimdataupload.responseclass.intimatedClaimResonse

import com.google.gson.annotations.SerializedName


data class IntimatedClaimsResponse(@SerializedName("\$id"    ) var id: String?           = null,
                                   @SerializedName("Detail" ) var IntimatedDetailInfo: ArrayList<IntimatedDetailInfo> = arrayListOf(),
                                   @SerializedName("Result" ) var Result: Result = Result())

