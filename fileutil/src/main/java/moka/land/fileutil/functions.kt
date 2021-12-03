package moka.land.fileutil

import android.net.Uri
import android.provider.MediaStore
import moka.land.fileutil._FileUtil.Companion.context

fun uri2path(contentUri: Uri): String {
    if (contentUri.host.isNullOrEmpty()) {
        return contentUri.path ?: ""
    }
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(contentUri, proj, null, null, null)
    cursor?.moveToNext()
    val path = cursor?.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
    cursor?.close()
    return path ?: ""
}
