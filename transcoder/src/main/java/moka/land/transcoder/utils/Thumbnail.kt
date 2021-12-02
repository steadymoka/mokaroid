package moka.land.transcoder.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import moka.land.base.log
import moka.land.transcoder._Transcoder
import java.io.ByteArrayOutputStream

object Thumbnail {

    fun createBitmapThumbnail(uri: Uri): Bitmap {
        val path = uri2path(uri)

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val timeInSeconds = 1
        return retriever.getFrameAtTime(
            timeInSeconds * 1000000L,
            MediaMetadataRetriever.OPTION_CLOSEST_SYNC
        )
    }

    fun createBase64Thumbnail(uri: Uri): String {
        val thumbnail = createBitmapThumbnail(uri)

        val byteArrayOutputStream = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun uri2path(contentUri: Uri): String {
        if (contentUri.host.isNullOrEmpty()) {
            return contentUri.path ?: ""
        }
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = _Transcoder.context.contentResolver.query(contentUri, proj, null, null, null)
        cursor?.moveToNext()
        val path = cursor?.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        cursor?.close()
        return path ?: ""
    }

}
