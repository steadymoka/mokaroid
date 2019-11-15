package io.haruharu.imagehelper.viewer

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.FrameLayout

class ImageViewer {

    companion object {
        var finish: (() -> Unit)? = null
        var addHeader: ((parent: FrameLayout) -> Unit)? = null
        var addFooter: ((parent: FrameLayout) -> Unit)? = null
        var onPageSelected: ((position: Int) -> Unit)? = null
    }

    init {
        finish = null
        addHeader = null
        addFooter = null
        onPageSelected = null
    }

    /*- -*/

    fun show(context: Context, paths: ArrayList<String>, selectedPosition: Int = 0) {
        context.startActivity(Intent(context, ImageViewerLayout::class.java)
            .apply {
                putStringArrayListExtra(ImageViewerLayout.KEY_DATAS, paths)
                putExtra(ImageViewerLayout.KEY_SELECTED_POSITION, selectedPosition)
            })
    }

    fun addHeader(header: View) {
        addHeader = {
            it.removeAllViews()
            it.addView(header)
        }
    }

    fun addFooter(footer: View) {
        addFooter = {
            it.removeAllViews()
            it.addView(footer)
        }
    }

    fun finish() {
        finish?.invoke()
    }

}
