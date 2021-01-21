package io.moka.mediautil

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moka.land.base.deviceHeightPixel
import moka.land.base.deviceWidthPixel
import java.io.*
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object MediaUtil {

    fun getCameraIntent(context: Context, mediaType: MediaType, fileToSave: File): Intent? {
        val intent = when (mediaType) {
            MediaType.IMAGE_ONLY -> Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            MediaType.VIDEO_ONLY -> Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            else -> Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        }
        if (intent.resolveActivity(context.packageManager) == null) {
            return null
        }
        val uriToSave = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", fileToSave)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriToSave)
        return intent
    }

    suspend fun getFileToSave(context: Context, mediaType: MediaType): File {
        return withContext(Dispatchers.IO) {
            val fileName = when (mediaType) {
                MediaType.IMAGE_ONLY -> "IMG_${Date().time}"
                MediaType.VIDEO_ONLY -> "VOD_${Date().time}"
                else -> "IMG_${Date().time}"
            }
            val directoryName = when (mediaType) {
                MediaType.IMAGE_ONLY -> Environment.DIRECTORY_PICTURES
                MediaType.VIDEO_ONLY -> Environment.DIRECTORY_MOVIES
                else -> Environment.DIRECTORY_PICTURES
            }
            val directory = context.getExternalFilesDir(directoryName)!!
            if (!directory.exists()) {
                directory.mkdir()
            }
            val fileSuffix = when (mediaType) {
                MediaType.IMAGE_ONLY -> ".jpg"
                MediaType.VIDEO_ONLY -> ".mp4"
                else -> ".jpg"
            }
            File.createTempFile(fileName, fileSuffix, directory)
        }
    }

    fun save(context: Context, file: File, callback: () -> Unit) {
        GlobalScope.launch {
            save(context, file)
            callback()
        }
    }

    suspend fun save(context: Context, file: File) {
        return withContext(Dispatchers.IO) {
            val exif = ExifInterface(file)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val degree = when {
                (orientation == ExifInterface.ORIENTATION_ROTATE_90) -> 90f
                (orientation == ExifInterface.ORIENTATION_ROTATE_180) -> 180f
                (orientation == ExifInterface.ORIENTATION_ROTATE_270) -> 270f
                else -> 0f
            }

            val bitmap = BitmapFactory.Options().run {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(file.path, this)
                inSampleSize = calculateInSampleSize(this, (deviceWidthPixel * 0.8f).toInt(), (deviceHeightPixel * 0.8f).toInt())

                inJustDecodeBounds = false
                BitmapFactory.decodeFile(file.path, this)
            }
            saveBitmap(context, bitmap, degree)
            file.delete()
            Unit
        }
    }

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

    suspend fun saveBitmap(context: Context, bitmap: Bitmap, degree: Float) {
        return withContext(Dispatchers.IO) {
            val fileName = "IMG_${Date().time}"
            val directoryName = Environment.DIRECTORY_PICTURES

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveImageInQ(context, bitmap, fileName, directoryName, degree)
            } else {
                saveImageNotQ(context, bitmap, fileName, directoryName, degree)
            }
        }
    }

    private suspend fun saveImageNotQ(context: Context, bitmap: Bitmap, fileName: String, directoryName: String, degree: Float) {
        val directoryName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator
        val file = File(directoryName)
        if (!file.exists()) {
            file.mkdir()
        }
        val image = File(directoryName, "$fileName.jpg")
        val stream = FileOutputStream(image)

        try {
            val matrix = Matrix()
            matrix.setRotate(degree)
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            if (!rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                throw IOException("Failed to save bitmap.")
            }
            rotatedBitmap.recycle()
        } finally {
            bitmap.recycle()
            stream.close()
            scanMedia(context, Uri.fromFile(image))
        }
    }

    private suspend fun saveImageInQ(context: Context, bitmap: Bitmap, fileName: String, directoryName: String, degree: Float) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName)
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

            val matrix = Matrix()
            matrix.setRotate(degree)
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            if (!rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                throw IOException("Failed to save bitmap.")
            }
            rotatedBitmap.recycle()
        } catch (e: IOException) {
            if (uri != null) {
                context.contentResolver.delete(uri, null, null)
            }
            throw e
        } finally {
            bitmap.recycle()
            stream?.close()
            if (null != uri) {
                scanMedia(context, uri)
            }
        }
    }

    private suspend fun scanMedia(context: Context, uri: Uri) {
        suspendCoroutine<Unit> { continuation ->
            MediaScannerConnection.scanFile(context, arrayOf(uri.path), null) { path, uri ->
                continuation.resume(Unit)
            }
        }
    }

}
