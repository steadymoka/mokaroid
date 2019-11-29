package moka.land.imagehelper.picker.model

import android.net.Uri

data class Album(
    val name: String,
    val thumbnailUri: Uri,
    val mediaUris: List<Media>
)
