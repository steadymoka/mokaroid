package moka.land.imagehelper.picker.builder

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import moka.land.imagehelper.picker.layout.ImagePickerLayout
import moka.land.imagehelper.picker.type.SelectType
import moka.land.permissionmanager.PermissionManager
import java.lang.ref.WeakReference

interface Runnable {

    fun setOnCancel(onCancel: () -> Unit): Runnable

    fun showSingle(onSingleSelected: ((uri: Uri) -> Unit))

    fun showMulti(onMultiSelected: ((uriList: List<Uri>) -> Unit))

    fun showCamera(onCamera: ((uri: Uri) -> Unit))
}

/**
 * Builder(Runner) of ImagePicker
 */
class ImagePicker private constructor(
    private var context: WeakReference<Context>,
    private var config: ImagePickerConfig) : Runnable {

    fun setConfig(option: ImagePickerConfig.() -> Unit): Runnable {
        this.config.option()
        return this
    }

    override fun setOnCancel(onCancel: () -> Unit): Runnable {
        ImagePicker.onCancel = onCancel
        return this
    }

    override fun showSingle(onSingleSelected: ((uri: Uri) -> Unit)) {
        if (null == context.get()) {
            return
        }
        this.config.selectType = SelectType.SINGLE
        ImagePicker.onSingleSelected = onSingleSelected
        show()
    }

    override fun showMulti(onMultiSelected: ((uriList: List<Uri>) -> Unit)) {
        if (null == context.get()) {
            return
        }
        this.config.selectType = SelectType.MULTI
        ImagePicker.onMultiSelected = onMultiSelected
        show()
    }

    override fun showCamera(onCamera: (uri: Uri) -> Unit) {
        if (null == context.get()) {
            return
        }
        this.config.showCamera = true
        ImagePicker.onCamera = onCamera
        show()
    }

    private fun show() {
        checkPermission { isGranted ->
            if (isGranted) {
                val intent = ImagePickerLayout.getIntent(context.get()!!, this@ImagePicker.config)
                context.get()!!.startActivity(intent)
            } else {
                Toast.makeText(context.get(), "권한을 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(callback: (isGranted: Boolean) -> Unit) {
        if (null == context.get()) {
            return
        }
        PermissionManager
            .with(context.get()!!)
            .setPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check { isGranted, _ ->
                callback(isGranted)
            }
    }

    /**
     * get instance functions
     */

    companion object {
        var onSingleSelected: ((uri: Uri) -> Unit)? = null
        var onMultiSelected: ((uriList: List<Uri>) -> Unit)? = null
        var onCancel: (() -> Unit)? = null
        var onCamera: ((uri: Uri) -> Unit)? = null

        fun with(activity: FragmentActivity): ImagePicker {
            return ImagePicker(WeakReference(activity), ImagePickerConfig())
        }

        fun with(fragment: Fragment): ImagePicker {
            fragment.activity ?: throw Error("cannot get activity")
            return ImagePicker(WeakReference(fragment.activity!!), ImagePickerConfig())
        }

        fun with(context: Context): ImagePicker {
            return ImagePicker(WeakReference(context), ImagePickerConfig())
        }
    }

}
