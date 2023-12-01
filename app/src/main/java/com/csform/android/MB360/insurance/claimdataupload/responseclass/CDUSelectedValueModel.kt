package com.csform.android.MB360.insurance.claimdataupload.responseclass

data class CDUSelectedValueModel (var isIntimatedSelecetdYes: String,
                                 // var isIntimatedSelecetdNO: String,
                                  var isTpaSelectedYes: String,
                                  var typeOfClaimPre: String,
                                  var typeOfClaimMain: String,
                                  var typeOfClaimPost: String) {
    constructor() : this("","","","","")
}