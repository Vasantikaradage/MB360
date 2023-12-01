package com.csform.android.MB360.insurance.claimdataupload.responseclass.loadClaimNoFromIntimateClaim

import com.google.gson.annotations.SerializedName

data class EmployeeClaimsValues(@SerializedName("\$id"                  ) var id             : String? = null,
                                @SerializedName("EMPLOYEE_SR_NO"       ) var EMPLOYEESRNO    : String? = null,
                                @SerializedName("OE_GRP_BAS_INF_SR_NO" ) var OEGRPBASINFSRNO : String? = null)
