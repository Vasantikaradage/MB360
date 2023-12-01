package com.csform.android.MB360.insurance.claimdataupload.base

sealed interface UiState<out T> {

    data class Success<T>(val data: T) : UiState<T>

    data class Error(val message: String) : UiState<Nothing>

    object Loading : UiState<Nothing>

}