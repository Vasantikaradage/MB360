package com.csform.android.MB360.insurance.claimdataupload.responseclass.loadClaimNoFromIntimateClaim

import com.google.gson.annotations.SerializedName

data class ClaimInformation(

        @SerializedName("\$id"                    ) var id                  : String? = null,
        @SerializedName("BENEFICIARY"            ) var BENEFICIARY          : String? = null,
        @SerializedName("PERSON_SR_NO"           ) var PERSONSRNO           : String? = null,
        @SerializedName("CLAIM_NO"               ) var CLAIMNO              : String? = null,
        @SerializedName("CLAIM_DATE"             ) var CLAIMDATE            : String? = null,
        @SerializedName("FIR"                    ) var FIR                  : String? = null,
        @SerializedName("CLAIM_SR_NO"            ) var CLAIMSRNO            : String? = null,
        @SerializedName("RELATION_WITH_EMPLOYEE" ) var RELATIONWITHEMPLOYEE : String? = null,
        @SerializedName("CLAIM_STATUS"           ) var CLAIMSTATUS          : String? = null,
        @SerializedName("AGE"                    ) var AGE                  : String? = null,
        @SerializedName("GENDER"                 ) var GENDER               : String? = null,
        @SerializedName("DATE_OF_BIRTH"          ) var DATEOFBIRTH          : String? = null,
        @SerializedName("CELLPHONE_NUMBER"       ) var CELLPHONENUMBER      : String? = null
)
