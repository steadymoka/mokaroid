package moka.land.imagehelper.picker.builder

import moka.land.imagehelper.picker.conf.SelectType
import java.io.Serializable

class ImagePickerBuilder : Serializable {

    internal var selectType: SelectType = SelectType.SINGLE

    var camera: Boolean = true

}
