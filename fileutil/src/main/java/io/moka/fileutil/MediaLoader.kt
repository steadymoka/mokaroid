package io.moka.fileutil

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object MediaLoader {

    @JvmName("getFileFromUri")
    suspend fun getFile(context: Context, uri: Uri): File {
        return suspendCoroutine { continuation ->
            Glide.with(context)
                .asFile()
                .load(uri)
                .into(object : CustomTarget<File>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                        continuation.resume(resource)
                    }
                })
        }
    }

}
