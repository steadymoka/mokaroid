package moka.land.imagehelper.viewer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import java.lang.ref.WeakReference

interface Runnable {

    fun show(uris: ArrayList<Uri>, selectedPosition: Int = 0)
}

class ImageViewer(
    private var context: WeakReference<Context>) : Runnable {
    private var block: (ImageViewer.() -> Unit)? = null

    init {
        finish = null
        addHeader = null
        addFooter = null
        onPageSelected = null
    }

    /*- -*/

    fun setConfig(block: (ImageViewer.() -> Unit)? = null): Runnable {
        this.block = block
        return this
    }

    override fun show(uris: ArrayList<Uri>, selectedPosition: Int) {
        block?.invoke(this)

        context.get()?.startActivity(Intent(context.get(), ImageViewerLayout::class.java)
            .apply {
                putParcelableArrayListExtra(ImageViewerLayout.KEY_DATAS, uris)
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

    fun addOnPageSelected(onPageSelected: ((position: Int) -> Unit)): ImageViewer {
        Companion.onPageSelected = onPageSelected
        return this
    }

    companion object {
        fun with(context: Context): ImageViewer {
            return ImageViewer(WeakReference(context))
        }

        internal var finish: (() -> Unit)? = null
        internal var addHeader: ((parent: FrameLayout) -> Unit)? = null
        internal var addFooter: ((parent: FrameLayout) -> Unit)? = null
        internal var onPageSelected: ((position: Int) -> Unit)? = null
    }

}
