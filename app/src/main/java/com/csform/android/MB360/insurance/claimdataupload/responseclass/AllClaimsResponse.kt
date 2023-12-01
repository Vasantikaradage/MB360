package com.csform.android.MB360.insurance.claimdataupload.responseclass

import com.google.gson.annotations.SerializedName

data class AllClaimsResponse(
        @SerializedName("\$id")
        var id: String,
        @SerializedName("iTotalRecords")
        var iTotalRecords: Int,
        @SerializedName("iTotalDisplayRecords")
        var iTotalDisplayRecords: Int,
        @SerializedName("aaData")
        var aaData: ArrayList<AaData> = arrayListOf()

)