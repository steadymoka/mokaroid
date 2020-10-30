package moka.land.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moka.land.imagehelper.picker.util.MediaLoader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object TakePictureUtil {

    internal suspend fun save(context: Context, uri: Uri): Uri {
        val file = MediaLoader.getFile(context, uri)
        return save(context, file)
    }

    suspend fun save(context: Context, file: File): Uri {
        return withContext(Dispatchers.IO) {
            val exif = ExifInterface(file)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val degree = when {
                (orientation == ExifInterface.ORIENTATION_ROTATE_90) -> 90f
                (orientation == ExifInterface.ORIENTATION_ROTATE_180) -> 180f
                (orientation == ExifInterface.ORIENTATION_ROTATE_270) -> 270f
                else -> 0f
            }
            val options = BitmapFactory.Options()
            options.inSampleSize = 3
            val bitmap = BitmapFactory.decodeFile(file.path, options)
            val uri = saveBitmap(context, bitmap, degree)
            file.delete()
            uri
        }
    }

    private suspend fun saveBitmap(context: Context, bitmap: Bitmap, degree: Float): Uri {
        return withContext(Dispatchers.IO) {
            val fileName = "IMG_${Date().time}"
            saveReceivedImage(context, bitmap, fileName, degree)
        }
    }

    private fun saveReceivedImage(context: Context, bitmap: Bitmap, imageName: String, degree: Float): Uri {
        try {
            val path = File(context.filesDir, "alarm" + File.separator + "picture")
            if (!path.exists()) {
                path.mkdirs()
            }
            val outFile = File(path, "$imageName.jpeg")
            val outputStream = FileOutputStream(outFile)

            val matrix = Matrix()
            matrix.setRotate(degree)

            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
            return Uri.fromFile(outFile)
        }
        catch (e: FileNotFoundException) {
            Log.e("TAG", "Saving received message failed with", e)
            return Uri.EMPTY
        }
        catch (e: IOException) {
            Log.e("TAG", "Saving received message failed with", e)
            return Uri.EMPTY
        }
    }

}
