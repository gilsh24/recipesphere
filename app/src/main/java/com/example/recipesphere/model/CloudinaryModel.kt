package com.example.recipesphere.model

import android.graphics.Bitmap
import com.cloudinary.android.MediaManager
import com.cloudinary.android.policy.GlobalUploadPolicy
import com.cloudinary.android.callback.UploadCallback
import com.example.recipesphere.base.MyApplication
import java.io.File
import com.cloudinary.android.callback.ErrorInfo
import com.example.recipesphere.extensions.toFile
import com.example.recipesphere.BuildConfig


class CloudinaryModel {

    private var mediaManager: MediaManager? = null

    private fun ensureMediaManagerInitialized() {
        if (mediaManager == null) {
            val config = mapOf(
                "cloud_name" to BuildConfig.CLOUD_NAME,
                "api_key" to BuildConfig.API_KEY,
                "api_secret" to BuildConfig.API_SECRET
            )
            MyApplication.Globals.context?.let {
                MediaManager.init(it, config)
                mediaManager = MediaManager.get()
                mediaManager?.globalUploadPolicy = GlobalUploadPolicy.defaultPolicy()
            } ?: throw IllegalStateException("Application context is null")
        }
    }

    fun getMediaManager(): MediaManager {
        ensureMediaManagerInitialized()
        return mediaManager!!
    }


    fun uploadImage(bitmap: Bitmap,
                    name: String,
                    onSuccess: (String?) -> Unit,
                    onError: (String?) -> Unit)
    {
        val context = MyApplication.Globals.context ?: return
        val file: File = bitmap.toFile(context, name)

        getMediaManager().upload(file.path)
            .option("folder", "images")
            // The callback makes us write all the different states - they cannot be removed
            .callback(object  : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String ?: ""
                    onSuccess(url)
                }
                override fun onError(requestId: String?, error: ErrorInfo?) {
                    onError(error?.description ?: "Unknown error")
                }
                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch()

    }
}