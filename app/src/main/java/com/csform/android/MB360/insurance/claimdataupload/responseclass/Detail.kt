package com.csform.android.MB360.insurance.claimdataupload.responseclass

import com.google.gson.annotations.SerializedName

data class Detail(@SerializedName("\$id") var id: String? = null,
                  @SerializedName("PERSON_SR_NO") var PERSONSRNO: String? = null,
                  @SerializedName("PERSON_NAME") var PERSONNAME: String? = null,
                  @SerializedName("DATE_OF_BIRTH") var DATEOFBIRTH: String? = null,
                  @SerializedName("RELATION_NAME") var RELATIONNAME: String? = null,
                  @SerializedName("RELATION_ID") var RELATIONID: String? = null,
                  @SerializedName("CELLPHONE_NUMBER") var CELLPHONENUMBER: String? = null,
                  @SerializedName("AGE") var AGE: String? = null,
                  @SerializedName("GENDER") var GENDER: String? = null,
                  var isSelected: Boolean,
                  var claimNo: String="",
var claimIntSrNo:String="") {
    constructor() : this("","","","","","","","","",false,"","")
};
