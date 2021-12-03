package moka.land.imagehelper.picker.layout

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moka.land.base.log
import moka.land.dialog.LoadingDialog
import moka.land.imagehelper.picker.builder.ImagePicker
import moka.land.imagehelper.picker.builder.ImagePickerConfig
import moka.land.imagehelper.picker.type.MediaType
import moka.land.imagehelper.picker.type.SelectType
import java.io.*
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


internal class ImplicitPickerLayout : AppCompatActivity() {

    private lateinit var config: ImagePickerConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        config = intent.getSerializableExtra(KEY_BUILDER_EXTRA) as ImagePickerConfig

        startPickerIntent()
    }

    /*
     * https://developer.android.com/training/secure-file-sharing/request-file
     */
    private fun startPickerIntent() {
        val intent = when (config.mediaType) {
            MediaType.IMAGE_ONLY -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                intent
            }
            MediaType.VIDEO_ONLY -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "video/*"
                intent
            }
            else -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "*/*"
                intent
            }
        }
        if (config.selectType == SelectType.MULTI) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(intent, KEY_PICK_IMAGES_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            finish()
            return
        }
        if (requestCode != KEY_PICK_IMAGES_REQUEST_CODE) {
            finish()
            return
        }
        if (null == data) {
            finish()
            return
        }

        lifecycleScope.launch {
            when (config.selectType) {
                SelectType.SINGLE -> {
                    val data = data.data
                    if (null != data) {
                        ImagePicker.onSingleSelected?.invoke(convertUri(data)!!)
                    }

                    dismissLoading()
                    finish()
                }
                SelectType.MULTI -> {
                    if (data.clipData != null) {
                        val uris = arrayListOf<Uri>()
                        val mClipData = data.clipData!!
                        for (i in 0 until mClipData.itemCount) {
                            val item = mClipData.getItemAt(i)
                            uris.add(convertUri(item.uri)!!)
                        }
                        ImagePicker.onMultiSelected?.invoke(uris)
                    } else if (data.data != null) {
                        val uris = arrayListOf(convertUri(data.data!!)!!)
                        ImagePicker.onMultiSelected?.invoke(uris)
                    }

                    dismissLoading()
                    finish()
                }
            }
        }
    }

    private fun checkUri(uri: Uri): Boolean {
        val resolver = contentResolver
        //1. Check Uri
        var cursor: Cursor? = null
        val isUriExist: Boolean = try {
            cursor = resolver.query(uri, null, null, null, null)
            //cursor null: content Uri was invalid or some other error occurred
            //cursor.moveToFirst() false: Uri was ok but no entry found.
            (cursor != null && cursor.moveToFirst())
        } catch (t: Throwable) {
            log("1.Check Uri Error: ${t.message}")
            false
        } finally {
            try {
                cursor?.close()
            } catch (t: Throwable) {
            }
        }
        //2. Check File Exist
        var ins: InputStream? = null
        val isFileExist: Boolean = try {
            ins = resolver.openInputStream(uri)
            // file exists
            true
        } catch (t: Throwable) {
            // File was not found eg: open failed: ENOENT (No such file or directory)
            log("2. Check File Exist Error: ${t.message}")
            false
        } finally {
            try {
                ins?.close()
            } catch (t: Throwable) {
            }
        }
        return isUriExist && isFileExist
    }

    private suspend fun convertUri(uri: Uri): Uri? {
        val cursor = contentResolver.query(
            uri,
            arrayOf(MediaStore.MediaColumns._ID, MediaStore.Images.Media.MIME_TYPE, MediaStore.Video.Media.MIME_TYPE),
            null, null, null
        )
        val mimeType = contentResolver.getType(uri)

        var convertedUri: Uri? = null
        if (null != cursor) {
            cursor.moveToFirst()
            val indexId = cursor.getColumnIndex(MediaStore.MediaColumns._ID)
            val mediaId = cursor.getLong(indexId)

            // -- 외부 저장소가 아닌, 구글 포토에 있는 미디어
            if (mediaId == 0L) {
                showLoading()

                val file = getTempImagePathFromInputStreamUri(uri, mimeType ?: "image/*")
                cursor.close()
                return Uri.fromFile(file)
            }
            // --

            convertedUri = when {
                true == mimeType?.startsWith("image") -> {
                    Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + mediaId)
                }
                true == mimeType?.startsWith("video") -> {
                    Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "" + mediaId)
                }
                else -> {
                    null
                }
            }
            cursor.close()
        }
        return convertedUri
    }

    private suspend fun getTempImagePathFromInputStreamUri(uri: Uri, mimeType: String): File? {
        return suspendCoroutine {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    var inputStream: InputStream? = null
                    var photoFile: File? = null
                    if (uri.authority != null) {
                        try {
                            inputStream = contentResolver.openInputStream(uri) // context needed
                            photoFile = createTemporalFileFrom(inputStream, mimeType)
                        } catch (e: FileNotFoundException) {
                            it.resumeWithException(e)
                            // log
                        } catch (e: IOException) {
                            it.resumeWithException(e)
                            // log
                        } finally {
                            try {
                                inputStream?.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                                it.resumeWithException(e)
                            }
                        }
                    }

                    it.resume(photoFile)
                }
            }
        }
    }

    private fun createTemporalFileFrom(inputStream: InputStream?, mimeType: String): File? {
        var targetFile: File? = null
        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)
            targetFile = if (mimeType.startsWith("video/")) {
                createTemporalVideoFile()
            } else {
                createTemporalImageFile()
            }

            val outputStream = FileOutputStream(targetFile)
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return targetFile
    }

    private fun createTemporalImageFile(): File {
        return File(externalCacheDir, "tempFile_${Date().time}.jpg") // context needed
    }

    private fun createTemporalVideoFile(): File {
        return File(externalCacheDir, "tempFile_${Date().time}.mp4") // context needed
    }

    // -- LoadingDialog

    private var loadingDialog: LoadingDialog? = null

    private fun showLoading() {
        if (null == loadingDialog) {
            loadingDialog = LoadingDialog(false)
        }
        if (loadingDialog!!.isAdded) {
            return
        }
        loadingDialog!!.show(supportFragmentManager)
    }

    private fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    // --

    companion object {

        private const val KEY_BUILDER_EXTRA = "ImplicitIntentLayout.KEY_BUILDER_EXTRA"
        private const val KEY_PICK_IMAGES_REQUEST_CODE = 1004

        fun getIntent(context: Context, config: ImagePickerConfig): Intent {
            return Intent(context, ImplicitPickerLayout::class.java).apply {
                putExtra(KEY_BUILDER_EXTRA, config)
            }
        }
    }
}
