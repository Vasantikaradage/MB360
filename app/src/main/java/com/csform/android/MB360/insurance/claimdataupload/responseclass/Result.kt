package com.csform.android.MB360.insurance.claimdataupload.responseclass

import com.google.gson.annotations.SerializedName

data class Result(
        @SerializedName("\$id")
        val id: String,
        @SerializedName("Message")
        val message: String,
        @SerializedName("Status")
        val status: Boolean
) {
    constructor() : this("","",false)
}
