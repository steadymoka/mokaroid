package io.haruharu.imagehelper.picker.builder

import android.net.Uri
import io.haruharu.imagehelper.picker.conf.SelectType
import java.io.Serializable

class ImagePickerBuilder : Serializable {
    var selectType: SelectType = SelectType.SINGLE

    /*

    callback functions */

    var onSingleSelected: ((uri: Uri) -> Unit)? = null
    var onMultiSelected: ((uriList: List<Uri>) -> Unit)? = null

}
