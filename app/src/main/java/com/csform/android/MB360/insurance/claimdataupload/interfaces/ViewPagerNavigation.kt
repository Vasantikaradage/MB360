package com.csform.android.MB360.insurance.claimdataupload.interfaces

import androidx.fragment.app.FragmentActivity
import com.csform.android.MB360.insurance.claimdataupload.responseclass.ClaimFileUpload
import java.io.Serializable

interface ViewPagerNavigation : Serializable {
    fun nextPage()
    fun previousPage()
    fun disableNextLayout()
    fun enableNextLayout()

    fun enableBottomNavigation()

    fun disableBottomNavigation()

    fun disableProgressBar()

    fun uploadedDocs(requestBody: ArrayList<ClaimFileUpload>)

    fun onTpaSelect(boolean: Boolean)
}