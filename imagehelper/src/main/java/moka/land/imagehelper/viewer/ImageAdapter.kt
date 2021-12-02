package moka.land.imagehelper.viewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.mk_layout_viewer_item.view.*
import moka.land.base.visibleOrGone
import moka.land.imagehelper.R
import moka.land.imagehelper.picker.model.Media

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.BaseItemView>() {

    var onClickItem: (() -> Unit)? = null
    var onClickToPlayVideo: ((Data) -> Unit)? = null

    var items = arrayListOf<Data>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemView {
        return BaseItemView(LayoutInflater.from(parent.context).inflate(R.layout.mk_layout_viewer_item, parent, false))
    }

    override fun onBindViewHolder(holder: BaseItemView, position: Int) {
        holder.refresh(items[position])
    }

    /*- -*/

    inner class BaseItemView(var _view: View) : RecyclerView.ViewHolder(_view) {

        lateinit var data: Data

        init {
            _view.photoView.setOnPhotoTapListener { view, x, y -> onClickItem?.invoke() }
            _view.imageViewPlayVideo.setOnClickListener { onClickToPlayVideo?.invoke(data) }
        }

        fun refresh(data: Data?) {
            this.data = data!!

            Glide
                .with(_view.context)
                .load(data.media.uri)
                .into(_view.photoView)

            _view.imageViewPlayVideo.visibleOrGone(data.media.mimetype.contains(Regex("video")))
        }
    }
}

data class Data(var media: Media)
