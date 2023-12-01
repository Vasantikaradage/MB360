package com.csform.android.MB360.insurance.claimdataupload.responseclass.intimatedClaimResonse

import com.google.gson.annotations.SerializedName
import com.csform.android.MB360.insurance.claimdataupload.responseclass.Detail
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ResultInfo

data class IntimatedClaimsDetail(
        @SerializedName("\$id"    ) var id: String?           = null,
        @SerializedName("Detail" ) var Detail: ArrayList<Detail> = arrayListOf(),
        @SerializedName("Result" ) var Result: ResultInfo = ResultInfo()

)

       /* @SerializedName("\$id")
        val id: String,
        @SerializedName("CLAIM_NAME")
        var claimName: String,
        @SerializedName("CLM_INT_SR_NO")
        val clmIntSrNo: String,
        @SerializedName("CLAIM_INTIMATION_NO")
        val claimIntimationNo: String,
        var isSelected: Boolean = false,

        var age: String="",
        var dob: String="",
        var mob: String="",
        var gender: String="",
        var relation: String="",
        var relationId: String="",
) {
        constructor() : this("","","","",false,"","","","","","")
}*/


