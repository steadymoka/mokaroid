package moka.land.transcoder.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Base64
import moka.land.fileutil.uri2path
import java.io.ByteArrayOutputStream
import java.io.IOException


object Thumbnail {

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun imageUriToBitmap(uri: Uri, reqWidth: Int = 200, reqHeight: Int = 200): Bitmap {
        var bitmap: Bitmap? = null

        try {
            val path = uri2path(uri)
            bitmap = BitmapFactory.Options().run {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(path, this)
                inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
                inJustDecodeBounds = false

                BitmapFactory.decodeFile(path, this)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap!!
    }

    fun bitmapToBase64(bitmap: Bitmap, quality: Int = 100): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun videoUriToBitmap(uri: Uri): Bitmap {
        val path = uri2path(uri)

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val timeInSeconds = 1
        return retriever.getFrameAtTime(
            timeInSeconds * 1000000L,
            MediaMetadataRetriever.OPTION_CLOSEST_SYNC
        )!!
    }

    fun videoUriToBase64(uri: Uri): String {
        return bitmapToBase64(videoUriToBitmap(uri))
    }

}
