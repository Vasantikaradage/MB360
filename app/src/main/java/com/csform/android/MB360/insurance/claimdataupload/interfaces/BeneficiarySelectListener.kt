package com.csform.android.MB360.insurance.claimdataupload.interfaces

import com.csform.android.MB360.insurance.claimdataupload.responseclass.Detail

interface BeneficiarySelectListener {

    fun onBeneficiarySelect(position: Int, person: Detail, b: Boolean)
}