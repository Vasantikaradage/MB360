package com.csform.android.MB360.insurance.claimdataupload.responseclass

import com.google.gson.annotations.SerializedName

data class ClaimDepenedentResponse (@SerializedName("\$id")
                                    val id: String,
                                    @SerializedName("Detail")
                                    val detail: List<DepenedentClaimsDetail>,
                                    @SerializedName("Result")
                                    val result: Result)