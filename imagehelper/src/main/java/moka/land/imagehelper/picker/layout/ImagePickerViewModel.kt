package moka.land.imagehelper.picker.layout

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moka.land.imagehelper.picker.type.MediaType
import moka.land.imagehelper.picker.layout.adapter.AlbumAdapter
import moka.land.imagehelper.picker.layout.adapter.MediaAdapter
import moka.land.imagehelper.picker.util.MediaLoader

class ImagePickerViewModel {

    var currentDirectory = MutableLiveData<String>()
    var albumList = MutableLiveData<List<AlbumAdapter.Data>>()
    var mediaList = MutableLiveData<List<MediaAdapter.Data>>()
    var openAlbumList = MutableLiveData<Boolean>()

    suspend fun loadAlbumList(context: Context, mediaType: MediaType) {
        val albumList = MediaLoader
            .load(context, mediaType)
            .map { AlbumAdapter.Data(it) }
        this.albumList.value = albumList
    }

}
