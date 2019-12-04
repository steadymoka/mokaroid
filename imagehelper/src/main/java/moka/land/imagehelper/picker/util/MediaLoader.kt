package moka.land.imagehelper.picker.util

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import moka.land.imagehelper.picker.conf.MediaType
import moka.land.imagehelper.picker.model.Album
import moka.land.imagehelper.picker.model.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object MediaLoader {

    private const val INDEX_MEDIA_ID = MediaStore.MediaColumns._ID
    private const val INDEX_DATE_ADDED_SECOND = MediaStore.MediaColumns.DATE_ADDED
    private const val INDEX_ALBUM_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME

    @SuppressLint("Recycle")
    internal suspend fun get(context: Context, mediaType: MediaType): List<Album> {
        return withContext<List<Album>>(Dispatchers.IO) {
            val uri = when (mediaType) {
                MediaType.IMAGE_ONLY -> {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                MediaType.VIDEO_ONLY -> {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                else -> {
                    Uri.EMPTY
                }
            }
            val projection = arrayOf(INDEX_MEDIA_ID, INDEX_ALBUM_NAME, INDEX_DATE_ADDED_SECOND)
            val selection = MediaStore.Images.Media.SIZE + " > 0"
            val order = "$INDEX_DATE_ADDED_SECOND DESC"

            val cursor = context.contentResolver.query(uri, projection, selection, null, order)
                ?: return@withContext emptyList()

            val mediaList = generateSequence { if (cursor.moveToNext()) cursor else null }
                .map { getImage(it) }
                .filterNotNull()
                .toList()

//                .toSortedMap(Comparator { albumName1: String, albumName2: String ->
//                    if (albumName2 == "Camera")
//                        1
//                    else
//                        albumName1.compareTo(albumName2, true)
//                })

            val albumList = mediaList
                .groupBy { it.album }
                .map { getAlbum(it) }
                .toList()

            cursor.close()
            return@withContext albumList.toMutableList().apply {
                add(index = 0,
                    element = Album(
                        name = "전체사진",
                        thumbnailUri = Media(Uri.EMPTY, "전체사진", 0).uri,
                        mediaUris = mediaList))
            }
        }
    }

    private fun getAlbum(data: Map.Entry<String, List<Media>>): Album {
        return Album(data.key, data.value[0].uri, data.value)
    }

    private fun getImage(cursor: Cursor): Media? = cursor.run {
        val indexId = getColumnIndex(INDEX_MEDIA_ID)
        val mediaId = getLong(indexId)
        return Media(
            uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + mediaId),
            album = getString(getColumnIndex(INDEX_ALBUM_NAME)),
            datedAddedSecond = getLong(getColumnIndex(INDEX_DATE_ADDED_SECOND))
        )
    }

}
