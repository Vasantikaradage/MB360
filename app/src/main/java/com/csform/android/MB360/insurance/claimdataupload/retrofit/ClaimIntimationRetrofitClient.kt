package com.csform.android.MB360.insurance.claimdataupload.retrofit

import com.csform.android.MB360.BuildConfig
import com.csform.android.MB360.utilities.LogMyBenefits
import com.csform.android.MB360.utilities.LogTags
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ClaimIntimationRetrofitClient {

    private val urlLoggingInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
            val url = request.url
            // Log the URL here as needed
            // Example: Log.d("Retrofit", "Request URL: $url")
            LogMyBenefits.d(LogTags.CDU, url.toString())
            return chain.proceed(request)
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(urlLoggingInterceptor)
            .build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

    val apiService: ClaimDocumentUploadApi = getRetrofit().create(ClaimDocumentUploadApi::class.java)
}
