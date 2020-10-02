package moka.land.imagehelper.picker.builder

import moka.land.imagehelper.picker.type.MediaType
import moka.land.imagehelper.picker.type.SelectType
import java.io.Serializable

class ImagePickerConfig : Serializable {

    internal var selectType: SelectType = SelectType.SINGLE

    var mediaType: MediaType = MediaType.IMAGE_ONLY

    var indicatorColorRes: Int? = null

    var camera: Boolean = true

    var showFullscreenButton: Boolean = true

    var maxCount: Int = 10

    var showCamera: Boolean = false

}
