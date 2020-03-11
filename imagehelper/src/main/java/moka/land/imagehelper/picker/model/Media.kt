package moka.land.imagehelper.picker.model

import android.net.Uri

data class Media(
    var uri: Uri,
    val album: String = "",
    var datedAddedSecond: Long = 0,
    var type: String,
    var duration: Long = 0
) {

    fun getDurationString(): String {
        val totalSecond = duration / 1000
        val second = totalSecond % 60
        val minute = (totalSecond / 60) % 60
        val hour = (totalSecond / 60 / 60) % 60

        return if (hour > 0) {
            String.format("%02d:%02d:%02d", hour, minute, second)
        }
        else {
            String.format("%02d:%02d", minute, second)
        }
    }

}
