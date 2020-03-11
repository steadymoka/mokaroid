package moka.land.imagehelper.picker.util

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import moka.land.imagehelper.picker.type.MediaType
import moka.land.imagehelper.picker.model.Album
import moka.land.imagehelper.picker.model.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moka.land.base.log
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object MediaLoader {

    private const val INDEX_MEDIA_ID = MediaStore.MediaColumns._ID
    private const val INDEX_DATE_ADDED_SECOND = MediaStore.MediaColumns.DATE_ADDED

    private const val INDEX_IMAGE_ALBUM_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    private const val INDEX_VIDEO_ALBUM_NAME = MediaStore.Video.Media.BUCKET_DISPLAY_NAME

    private const val INDEX_IMAGE_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE
    private const val INDEX_VIDEO_MIME_TYPE = MediaStore.Video.Media.MIME_TYPE

    private const val INDEX_VIDEO_DURATION = MediaStore.Video.Media.DURATION

    @SuppressLint("Recycle")
    internal suspend fun load(context: Context, mediaType: MediaType): List<Album> {
        return withContext<List<Album>>(Dispatchers.IO) {
            val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            val imageProjection = arrayOf(INDEX_MEDIA_ID, INDEX_IMAGE_ALBUM_NAME, INDEX_DATE_ADDED_SECOND, INDEX_IMAGE_MIME_TYPE)
            val videoProjection = arrayOf(INDEX_MEDIA_ID, INDEX_VIDEO_ALBUM_NAME, INDEX_DATE_ADDED_SECOND, INDEX_VIDEO_MIME_TYPE, INDEX_VIDEO_DURATION)

            val imageSelection = MediaStore.Images.Media.SIZE + " > 0"
            val videoSelection = MediaStore.Video.Media.SIZE + " > 0"
            val order = "$INDEX_DATE_ADDED_SECOND DESC"

            val imageCursor = context.contentResolver.query(imageUri, imageProjection, imageSelection, null, order)
            val videoCursor = context.contentResolver.query(videoUri, videoProjection, videoSelection, null, order)

            val imageList = generateSequence { if (imageCursor?.moveToNext() == true) imageCursor else null }
                .map { getImage(it) }
                .filterNotNull()
                .toList()

            val videoList = generateSequence { if (videoCursor?.moveToNext() == true) videoCursor else null }
                .map { getVideo(it) }
                .filterNotNull()
                .toList()

            val mediaList = when (mediaType) {
                MediaType.IMAGE_ONLY -> {
                    imageList
                }
                MediaType.VIDEO_ONLY -> {
                    videoList
                }
                MediaType.IMAGE_VIDEO -> {
                    imageList + videoList
                }
            }

            val albumList = mediaList
                .asSequence()
                .groupBy {
                    it.album
                }
                .map { getAlbum(it) }
                .toList()

            imageCursor?.close()
            videoCursor?.close()

            return@withContext albumList.toMutableList().apply {
                add(index = 0,
                    element = Album(
                        name = "전체사진",
                        thumbnailUri = Media(Uri.EMPTY, "전체사진", 0, "").uri,
                        mediaUris = mediaList.sortedByDescending { it.datedAddedSecond }))
            }
        }
    }

    fun getMedia(context: Context, uri: Uri): Media {
        val type = context.contentResolver.getType(uri)
            ?: return if (uri.toString().contains(Regex(".mp4|.mp3|.avi|.mpeg|.mov"))) {
                Media(uri = uri, type = "video/mov")
            }
            else {
                Media(uri = uri, type = "image/jpg")
            }

        if (type.contains(Regex("video"))) {
            val projection = arrayOf(INDEX_MEDIA_ID, INDEX_VIDEO_ALBUM_NAME, INDEX_DATE_ADDED_SECOND, INDEX_VIDEO_MIME_TYPE, INDEX_VIDEO_DURATION)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)

            val media = generateSequence { if (cursor?.moveToNext() == true) cursor else null }
                .map { getVideo(it) }
                .filterNotNull()
                .first()

            cursor?.close()
            return media
        }
        else {
            val projection = arrayOf(INDEX_MEDIA_ID, INDEX_IMAGE_ALBUM_NAME, INDEX_DATE_ADDED_SECOND, INDEX_IMAGE_MIME_TYPE)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)

            val media = generateSequence { if (cursor?.moveToNext() == true) cursor else null }
                .map { getImage(it) }
                .filterNotNull()
                .first()

            cursor?.close()
            return media
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
            album = getString(getColumnIndex(INDEX_IMAGE_ALBUM_NAME)),
            datedAddedSecond = getLong(getColumnIndex(INDEX_DATE_ADDED_SECOND)),
            type = getString(getColumnIndex(INDEX_IMAGE_MIME_TYPE))
        )
    }

    private fun getVideo(cursor: Cursor): Media? = cursor.run {
        val indexId = getColumnIndex(INDEX_MEDIA_ID)
        val mediaId = getLong(indexId)
        return Media(
            uri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "" + mediaId),
            album = getString(getColumnIndex(INDEX_VIDEO_ALBUM_NAME)),
            datedAddedSecond = getLong(getColumnIndex(INDEX_DATE_ADDED_SECOND)),
            type = getString(getColumnIndex(INDEX_IMAGE_MIME_TYPE)),
            duration = getLong(getColumnIndex(INDEX_VIDEO_DURATION))
        )
    }

    /**
     * Public util functions
     */

    suspend fun Context.getFile(uri: Uri): File {
        return MediaLoader.getFile(this, uri)
    }

    @JvmName("getFileFromUri")
    suspend fun getFile(context: Context, uri: Uri): File {
        return suspendCoroutine { continuation ->
            Glide.with(context)
                .asFile()
                .load(uri)
                .into(object : CustomTarget<File>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                        continuation.resume(resource)
                    }
                })
        }
    }

}
