package moka.land.imagehelper.picker.layout

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import moka.land.base.printCurrentThread
import moka.land.imagehelper.picker.conf.MediaType
import moka.land.imagehelper.picker.layout.adapter.AlbumAdapter
import moka.land.imagehelper.picker.layout.adapter.MediaAdapter
import moka.land.imagehelper.picker.model.Album
import moka.land.imagehelper.picker.util.MediaLoader

class ImagePickerViewModel {

    var albumList = MutableLiveData<List<AlbumAdapter.Data>>()
    var mediaList = MutableLiveData<List<MediaAdapter.Data>>()
    var openAlbumList = MutableLiveData<Boolean>()

    suspend fun loadAlbumList(context: Context) {
        MediaLoader
            .get(context, MediaType.IMAGE_ONLY)
            .map { AlbumAdapter.Data(it) }
            .run {
                albumList.value = this
            }
    }

}
