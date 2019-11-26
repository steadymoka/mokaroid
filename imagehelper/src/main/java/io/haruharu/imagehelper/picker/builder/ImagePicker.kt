package io.haruharu.imagehelper.picker.builder

import android.content.Context
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.haruharu.imagehelper.picker.conf.SelectType
import io.haruharu.imagehelper.picker.layout.ImagePickerLayout
import java.lang.ref.WeakReference

class ImagePicker private constructor(
    var context: WeakReference<Context>,
    var builder: ImagePickerBuilder) {

    /*

    show functions */

    fun showSingle(onSingleSelected: ((uri: Uri) -> Unit)) {
        if (null == context.get()) {
            return
        }
        this.builder.selectType = SelectType.SINGLE
        this.builder.onSingleSelected = onSingleSelected

        val intent = ImagePickerLayout.getIntent(context.get()!!, this.builder)
        context.get()!!.startActivity(intent)
    }

    fun showMulti(onMultiSelected: ((uriList: List<Uri>) -> Unit)) {
        if (null == context.get()) {
            return
        }
        this.builder.selectType = SelectType.MULTI
        this.builder.onMultiSelected = onMultiSelected

        val intent = ImagePickerLayout.getIntent(context.get()!!, this.builder)
        context.get()!!.startActivity(intent)
    }

    /*

    get instance functions */

    companion object {
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
