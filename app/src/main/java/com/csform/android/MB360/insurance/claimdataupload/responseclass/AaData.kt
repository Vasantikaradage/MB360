package com.csform.android.MB360.insurance.claimdataupload.responseclass

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AaData(@SerializedName("\$id"                       ) var id                  : String? = null,
                  @SerializedName("CLM_DOCS_UPLOAD_REQ_NO"    ) var CLMDOCSUPLOADREQNO   : String? = null,
                  @SerializedName("CLM_DOCS_UPLOAD_REQ_SR_NO" ) var CLMDOCSUPLOADREQSRNO : String? = null,
                  @SerializedName("CLM_DOCS_ULOADED_ON"       ) var CLMDOCSULOADEDON     : String? = null,
                  @SerializedName("LAST_MODIFIED_BY"          ) var LASTMODIFIEDBY       : String? = null,
                  @SerializedName("TYPE_OF_CLAIM"             ) var TYPEOFCLAIM          : String? = null,
                  @SerializedName("CLAIM_INTIMATED"           ) var CLAIMINTIMATED       : String? = null,
                  @SerializedName("CLAIM_INTIMATED_DEST"      ) var CLAIMINTIMATEDDEST   : String? = null,
                  @SerializedName("CLM_INT_SR_NO"             ) var CLMINTSRNO           : String? = null,
                  @SerializedName("CLAIM_INTIMATION_NO"       ) var CLAIMINTIMATIONNO    : String? = null,
                  @SerializedName("PERSON_SR_NO"              ) var PERSONSRNO           : String? = null,
                  @SerializedName("PERSON_NAME"               ) var PERSONNAME           : String? = null,
                  @SerializedName("FILES"                     ) var FILES                : String? = null,
                  @SerializedName("CELLPHONE_NUMBER"          ) var CELLPHONENUMBER      : String? = null,
                  @SerializedName("DATE_OF_BIRTH"             ) var DATEOFBIRTH          : String? = null,
                  @SerializedName("AGE"                       ) var AGE                  : String? = null,
                  @SerializedName("E_MAIL_ID"                 ) var EMAILID              : String? = null,
                  @SerializedName("STATUS"                    ) var STATUS               : String? = null,
                  @SerializedName("GENDER"                    ) var GENDER               : String? = null,
                  @SerializedName("RELATIONID"                ) var RELATIONID           : String? = null,
                  @SerializedName("UPLOAD_DOCUMENT"           ) var UPLOADDOCUMENT       : String? = null,
                  @SerializedName("MANDATORY_DOC"             ) var MANDATORYDOC         : String? = null,
                  @SerializedName("STATUSDATE"                ) var STATUSDATE           : String? = null): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(id)
        parcel.writeString(CLMDOCSUPLOADREQNO)
        parcel.writeString(CLMDOCSUPLOADREQSRNO)
        parcel.writeString(CLMDOCSULOADEDON)
        parcel.writeString(LASTMODIFIEDBY)
        parcel.writeString(TYPEOFCLAIM)
        parcel.writeString(CLAIMINTIMATED)
        parcel.writeString(CLAIMINTIMATEDDEST)
        parcel.writeString(CLMINTSRNO)
        parcel.writeString(CLAIMINTIMATIONNO)
        parcel.writeString(PERSONSRNO)
        parcel.writeString(PERSONNAME)
        parcel.writeString(FILES)
        parcel.writeString(CELLPHONENUMBER)
        parcel.writeString(DATEOFBIRTH)
        parcel.writeString(AGE)
        parcel.writeString(EMAILID)
        parcel.writeString(STATUS)
        parcel.writeString(GENDER)
        parcel.writeString(RELATIONID)
        parcel.writeString(UPLOADDOCUMENT)
        parcel.writeString(MANDATORYDOC)
        parcel.writeString(STATUSDATE)




    }

    companion object CREATOR : Parcelable.Creator<AaData> {
        override fun createFromParcel(parcel: Parcel): AaData {
            return AaData(parcel)
        }

        override fun newArray(size: Int): Array<AaData?> {
            return arrayOfNulls(size)
        }
    }
}