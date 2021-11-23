package moka.land.imagehelper.picker.layout

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import moka.land.base.log
import moka.land.imagehelper.picker.builder.ImagePicker
import moka.land.imagehelper.picker.builder.ImagePickerConfig
import moka.land.imagehelper.picker.type.MediaType
import moka.land.imagehelper.picker.type.SelectType
import java.io.File


internal class ImplicitPickerLayout : AppCompatActivity() {

    private lateinit var config: ImagePickerConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        config = intent.getSerializableExtra(KEY_BUILDER_EXTRA) as ImagePickerConfig

        startPickerIntent()
    }

    private fun startPickerIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        when (config.mediaType) {
            MediaType.IMAGE_ONLY -> {
                intent.type = "image/*"
            }
            MediaType.VIDEO_ONLY -> {
                intent.type = "image/*"
            }
            else -> {
                intent.type = "image/*,video/*"
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

        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        when (config.selectType) {
            SelectType.SINGLE -> {
                log("data.data!!: ${data.data!!}")
                ImagePicker.onSingleSelected?.invoke(data.data!!)
                finish()
            }
            SelectType.MULTI -> {
                if (data.clipData != null) {
                    val uris = arrayListOf<Uri>()
                    val mClipData = data.clipData!!
                    for (i in 0 until mClipData.itemCount) {
                        val item = mClipData.getItemAt(i)
                        val uri = item.uri
                        uris.add(uri)

//                        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
//                        cursor?.moveToFirst()
//                        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
//                        val imageEncoded = cursor?.getString(columnIndex ?: 0)
//                        log("imageEncoded: ${imageEncoded}")
//                        uris.add(Uri.parse(imageEncoded))
//                        cursor?.close()

//                        val path = getImageFilePath(uri)
//                        uris.add(Uri.parse(path))
                    }
                    ImagePicker.onMultiSelected?.invoke(uris)
                } else if (data.data != null) {
                    ImagePicker.onMultiSelected?.invoke(listOf(data.data!!))
                }
                finish()
            }
        }
    }

    fun getImageFilePath(uri: Uri): String {
        val file = File(uri.path)
        val filePath = file.path.split(":")
        val imageId = filePath[filePath.size - 1]
        val cursor =
            contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", arrayOf(imageId), null)
        val path: String
        if (cursor != null) {
            cursor.moveToFirst()
            val imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            path = imagePath
            cursor.close()
        } else {
            path = ""
        }
        return path
    }

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
