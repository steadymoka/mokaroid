package moka.land.imagehelper.picker.model

import android.net.Uri

data class Media(
    var uri: Uri,
    val album: String,
    var datedAddedSecond: Long
)
