package moka.land.imagehelper.picker.builder

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import moka.land.imagehelper.picker.conf.SelectType
import moka.land.imagehelper.picker.layout.ImagePickerLayout
import moka.land.permissionmanager.PermissionManager
import java.lang.ref.WeakReference

class ImagePicker private constructor(
    private var context: WeakReference<Context>,
    private var builder: ImagePickerBuilder) {

    fun setOption(option: ImagePickerBuilder.() -> Unit): ImagePicker {
        this.builder.option()
        return this
    }

    fun showSingle(onSingleSelected: ((uri: Uri) -> Unit)) {
        if (null == context.get()) {
            return
        }
        this.builder.selectType = SelectType.SINGLE
        ImagePicker.onSingleSelected = onSingleSelected
        show()
    }

    fun showMulti(onMultiSelected: ((uriList: List<Uri>) -> Unit)) {
        if (null == context.get()) {
            return
        }
        this.builder.selectType = SelectType.MULTI
        ImagePicker.onMultiSelected = onMultiSelected
        show()
    }

    private fun show() {
        checkPermission { isGranted ->
            if (isGranted) {
                val intent = ImagePickerLayout.getIntent(context.get()!!, this@ImagePicker.builder)
                context.get()!!.startActivity(intent)
            }
            else {
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

        fun with(activity: FragmentActivity): ImagePicker {
            return ImagePicker(WeakReference(activity), ImagePickerBuilder())
        }

        fun with(fragment: Fragment): ImagePicker {
            fragment.activity ?: throw Error("cannot get activity")
            return ImagePicker(WeakReference(fragment.activity!!), ImagePickerBuilder())
        }

        fun with(context: Context): ImagePicker {
            return ImagePicker(WeakReference(context), ImagePickerBuilder())
        }
    }

}
