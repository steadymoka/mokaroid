package io.haruharu.imagehelper.viewer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

class OverlayView(context: Context, resId: Int) : FrameLayout(context) {

    private val view: View = LayoutInflater.from(context).inflate(resId, null, false)

    init {
        addView(view)
    }

    fun getView(): View = view

    fun getView(viewId: Int): View? = view.findViewById(viewId)

    fun getTextView(viewId: Int): TextView? = view.findViewById(viewId) as? TextView

    fun getImageView(viewId: Int): ImageView? = view.findViewById(viewId) as? ImageView

}
