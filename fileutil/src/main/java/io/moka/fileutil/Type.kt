package io.moka.fileutil

import java.io.Serializable

enum class MediaType : Serializable {
    IMAGE_ONLY,
    VIDEO_ONLY,
    IMAGE_VIDEO
}
