package com.csform.android.MB360.insurance.claimdataupload.responseclass

import com.google.gson.annotations.SerializedName

data class DepenedentClaimsDetail(
        @SerializedName("\$id")
        val id: String,
        @SerializedName("GENDER")
        val gender: String,
        @SerializedName("AGE")
        val age: String,
        @SerializedName("CELLPHONE_NUMBER")
        val cellno: String,
        @SerializedName("RELATION_NAME")
        val relationName: String,

        @SerializedName("DATE_OF_BIRTH")
        val dob: String,

        @SerializedName("PERSON_NAME")
        val personame: String,
        @SerializedName("PERSON_SR_NO")
        val personalSrNo: String,
        @SerializedName("RELATION_ID")
        val relationId: String
)