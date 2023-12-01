package com.csform.android.MB360.insurance.claimdataupload.base

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

import androidx.core.content.FileProvider
import com.csform.android.MB360.BuildConfig


import com.csform.android.MB360.utilities.AppLocalConstant
import com.csform.android.MB360.utilities.LogTags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object ClaimFileDownloader {
    private const val MEGABYTE = 1024 * 1024

    suspend fun downloadFile(
            fileUrl: String,
            directory: File,
            activity: Activity,
            context: Context
    ): Intent? = withContext(Dispatchers.IO) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): invoked")
                Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): file-URL $fileUrl")
                Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): directory $directory")

                val url = URL(fileUrl)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connect()

                val inputStream = urlConnection.inputStream
                val fileOutputStream = context.openFileOutput(directory.name, Context.MODE_PRIVATE)

                val totalSize = urlConnection.contentLength
                Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): urlConnectionContentLength: $totalSize")

                val buffer = ByteArray(MEGABYTE)
                var bufferLength: Int
                while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength)
                }
                fileOutputStream.close()

                val path = FileProvider.getUriForFile(
                        context,
                        "${BuildConfig.APPLICATION_ID}.provider",
                        directory
                )
                val pdfIntent = Intent(Intent.ACTION_VIEW)

                val contentResolver = context.contentResolver
                val mimeType = contentResolver.getType(path)

                Log.d("CONTENT-RESOLVER", "mime type: $mimeType")
                pdfIntent.setDataAndType(path, mimeType)
                pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                pdfIntent
            } catch (e: Exception) {
                activity.runOnUiThread {
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
                null
            }
        } else {
            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): Permission not granted asking for permission")
            AppLocalConstant.verifyStoragePermissions(activity)
            null
        }
    }

    suspend fun downloadFileWithoutPermission(
            fileUrl: String,
            directory: File,
            activity: Activity,
            context: Context
    ): Intent? = withContext(Dispatchers.IO) {
        try {
            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): invoked")
            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): file-URL $fileUrl")
            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): directory $directory")

            val url = URL(fileUrl)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()

            val inputStream = urlConnection.inputStream
            val fileOutputStream = context.openFileOutput(directory.name, Context.MODE_PRIVATE)

            val totalSize = urlConnection.contentLength
            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): urlConnectionContentLength: $totalSize")

            val buffer = ByteArray(MEGABYTE)
            var bufferLength: Int
            while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                fileOutputStream.write(buffer, 0, bufferLength)
            }
            fileOutputStream.close()

            val path = FileProvider.getUriForFile(
                    context,
                    "${BuildConfig.APPLICATION_ID}.provider",
                    directory
            )
            val pdfIntent = Intent(Intent.ACTION_VIEW)

            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(path)

            Log.d("CONTENT-RESOLVER", "mime type: $mimeType")
            pdfIntent.setDataAndType(path, mimeType)
            pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pdfIntent
        } catch (e: Exception) {
            activity.runOnUiThread {
                when (e) {
                    is FileNotFoundException -> {
                        Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show()
                        directory.delete()
                    }
                    is IOException -> {
                        directory.delete()
                    }
                    is ActivityNotFoundException -> {
                        Toast.makeText(
                                context,
                                "No Application found to open this file.",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            e.printStackTrace()
            null
        }
    }
}