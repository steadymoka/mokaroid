package moka.land.imagehelper.picker.builder

import android.net.Uri
import moka.land.imagehelper.picker.conf.SelectType
import java.io.Serializable

class ImagePickerBuilder : Serializable {
    var selectType: SelectType = SelectType.SINGLE

    var onSingleSelected: ((uri: Uri) -> Unit)? = null
    var onMultiSelected: ((uriList: List<Uri>) -> Unit)? = null

}
