package moka.land.imagehelper.picker.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import moka.land.imagehelper.picker.conf.MediaType
import java.io.IOException
import java.io.OutputStream
import java.util.*

object CameraUtil {

    internal fun getCameraIntent(context: Context, mediaType: MediaType): Intent? {
        val intent = when (mediaType) {
            MediaType.IMAGE_ONLY -> Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            MediaType.VIDEO_ONLY -> Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            else -> Intent()
        }
        if (intent.resolveActivity(context.packageManager) == null) {
            return null
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        return intent
    }

    internal fun saveBitmap(context: Context, bitmap: Bitmap, callback: (() -> Unit)? = null) {
        val fileName = "IMG_${Date().time}"
        val directoryName = Environment.DIRECTORY_PICTURES

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.Images.ImageColumns.RELATIVE_PATH, directoryName)
        }

        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var stream: OutputStream? = null
        var uri: Uri? = null

        try {
            uri = context.contentResolver.insert(contentUri, contentValues)
            if (uri == null) {
                throw IOException("Failed to create new MediaStore record.")
            }

            stream = context.contentResolver.openOutputStream(uri)
            if (stream == null) {
                throw IOException("Failed to get output stream.")
            }

            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                throw IOException("Failed to save bitmap.")
            }
        }
        catch (e: IOException) {
            if (uri != null) {
                context.contentResolver.delete(uri, null, null)
            }
            throw IOException(e)
        }
        finally {
            stream?.close()
            scanMedia(context, uri!!) { callback?.invoke() }
        }
    }

    fun scanMedia(context: Context, uri: Uri, callback: () -> Unit) {
        MediaScannerConnection.scanFile(context, arrayOf(uri.path), null) { _, _ -> callback() }
    }

}
