package moka.land.imagehelper.picker.layout

import android.content.Context
import android.util.Log
import moka.land.imagehelper.picker.conf.MediaType
import moka.land.imagehelper.picker.util.MediaLoader

class ImagePickerViewModel {

    suspend fun loadAlbumList(context: Context) {
        val list = MediaLoader.get(context, MediaType.IMAGE_ONLY)
        Log.wtf("aaaa", "list: ${list}")
    }


}
