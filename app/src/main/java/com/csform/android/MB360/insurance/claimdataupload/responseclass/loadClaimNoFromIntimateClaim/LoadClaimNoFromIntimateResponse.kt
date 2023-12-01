package com.csform.android.MB360.insurance.claimdataupload.responseclass.loadClaimNoFromIntimateClaim

import com.google.gson.annotations.SerializedName
import com.csform.android.MB360.insurance.claimdataupload.responseclass.Detail

data class LoadClaimNoFromIntimateResponse(@SerializedName("\$id"                  ) var id                  : String?                     = null,
                                           @SerializedName("EmployeeClaimsValues" ) var EmployeeClaimsValues : EmployeeClaimsValues?       = EmployeeClaimsValues(),
                                           @SerializedName("ClaimInformation"     ) var ClaimInformation     : ArrayList<ClaimInformation> = arrayListOf(),
                                           @SerializedName("message"              ) var message              : Message?                    = Message()
/*@SerializedName("\$id"      ) var id      : String?             = null,
                                           @SerializedName("claimNum" ) var claimNumList : ArrayList<ClaimNumList> = arrayListOf(),
                                           @SerializedName("Details"  ) var Detail  : ArrayList<Detail>  = arrayListOf(),
                                           @SerializedName("Res"      ) var ResultData      : ResultData?                = ResultData()*/)
