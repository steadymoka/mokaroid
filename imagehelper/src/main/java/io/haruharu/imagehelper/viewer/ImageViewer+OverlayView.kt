package io.haruharu.imagehelper.viewer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import io.haruharu.imagehelper.R

class OverlayView(context: Context, resId: Int) : FrameLayout(context) {

    val root: View = LayoutInflater.from(context).inflate(resId, null, false)

    init {
        addView(root)
    }

    fun getView(viewId: Int): View? {
        return root.findViewById(viewId)
    }

    fun getTextView(viewId: Int): TextView? {
        return root.findViewById(viewId) as? TextView
    }

    fun getImageView(viewId: Int): ImageView? {
        return root.findViewById(viewId) as? ImageView
    }

}

object ImageViewer {

    var finish: (() -> Unit)? = null
    var addHeader: ((parent: FrameLayout) -> Unit)? = null
    var addFooter: ((parent: FrameLayout) -> Unit)? = null
    var onPageSelected: ((position: Int) -> Unit)? = null

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
