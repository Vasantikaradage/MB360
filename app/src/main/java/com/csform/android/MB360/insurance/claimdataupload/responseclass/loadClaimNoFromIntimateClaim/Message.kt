package com.csform.android.MB360.insurance.claimdataupload.responseclass.loadClaimNoFromIntimateClaim

import com.google.gson.annotations.SerializedName

data class Message(@SerializedName("\$id"          ) var id          : String?  = null,
                   @SerializedName("Message"      ) var Message      : String?  = null,
                   @SerializedName("Status"       ) var Status       : Boolean? = null,
                   @SerializedName("ResponseData" ) var ResponseData : String?  = null)
