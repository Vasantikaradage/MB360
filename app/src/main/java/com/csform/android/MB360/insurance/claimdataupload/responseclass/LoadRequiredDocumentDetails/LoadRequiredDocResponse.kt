package com.csform.android.MB360.insurance.claimdataupload.responseclass.LoadRequiredDocumentDetails

import com.google.gson.annotations.SerializedName

data class LoadRequiredDocResponse(@SerializedName("\$id"    ) var id    : String?           = null,
                                   @SerializedName("Detail" ) var Detail : ArrayList<Detail> = arrayListOf(),
                                   @SerializedName("Result" ) var Result : Result?           = Result())
