package moka.land.imagehelper.picker.conf

import java.io.Serializable

enum class MediaType : Serializable {
    IMAGE_ONLY,
    VIDEO_ONLY,
    IMAGE_VIDEO
}

enum class SelectType : Serializable {
    SINGLE,
    MULTI
}
