package moka.land.base.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import moka.land.base.goneFadeOut
import moka.land.base.visibleFadeIn

class _RecyclerView : FrameLayout {

    var recyclerView: RecyclerView

    var placeHolderView: View? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.recyclerView = RecyclerView(context, attrs)
        addView(recyclerView)
    }

    var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
        set(value) {
            field = value
            recyclerView.adapter = value
        }

    fun showPlaceHolder(resId: Int) {
        showPlaceHolder(LayoutInflater.from(context).inflate(resId, null, false))
    }

    fun showPlaceHolder(view: View) {
        this.placeHolderView = view
        addView(this.placeHolderView)
    }

    fun hidePlaceHolder(duration: Long = 100L) {
        if (null != this.placeHolderView && this.placeHolderView?.visibility != View.GONE) {
            this.placeHolderView!!.goneFadeOut(duration)
        }
        if (this.recyclerView.visibility != View.VISIBLE) {
            this.recyclerView.visibleFadeIn(duration)
        }
    }

}
