package com.csform.android.MB360.insurance.claimdataupload.responseclass

import com.google.gson.annotations.SerializedName

data class IntimationDetails(@SerializedName("\$id"    ) var id    : String?           = null,
                             @SerializedName("Detail" ) var Detail : ArrayList<Detail> = arrayListOf(),
                             @SerializedName("Result" ) var Result : Result?           = Result())
