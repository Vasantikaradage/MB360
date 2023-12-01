package com.csform.android.MB360.insurance.claimdataupload.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.csform.android.MB360.insurance.claimdataupload.interfaces.ClaimDocUploadApiHelper
import com.csform.android.MB360.insurance.claimdataupload.repository.ClaimDocumentUploadViewModel
import com.csform.android.MB360.utilities.DispatcherProvider

class ViewModelFactory(
        private val apiHelper: ClaimDocUploadApiHelper,
        private val dispatcherProvider: DispatcherProvider
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClaimDocumentUploadViewModel::class.java)) {
            return ClaimDocumentUploadViewModel(apiHelper, dispatcherProvider) as T
        }
        throw IllegalAccessException("Unknown Class")
    }
}