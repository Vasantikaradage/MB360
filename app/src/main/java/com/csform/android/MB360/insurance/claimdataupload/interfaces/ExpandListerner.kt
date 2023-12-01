package com.csform.android.MB360.insurance.claimdataupload.interfaces

import com.csform.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum

interface ExpandListerner {
    fun onExpandListerner(position: Int, selectedPos:Int)
}