package com.csform.android.MB360.insurance.claimdataupload.responseclass.CDUAllSelecetedValue

data class CDUAllSelecetedValueRequest (var DOC_REQ_BY:String,
                                        var IS_CLM_PRE_HOSP:String,
                                        var IS_CLM_MAIN_HOSP:String,
                                        var IS_CLM_POST_HOSP:String,
                                        var CLAIM_INTIMATED:String,
                                        var CLAIM_INTIMATED_DEST:String,
                                        var CLM_INT_SR_NO:String,
                                        var CLAIM_INTIMATION_NO:String,
                                        var PERSON_SR_NO:String,

) {
    constructor() : this("","","","","","","","","")
}