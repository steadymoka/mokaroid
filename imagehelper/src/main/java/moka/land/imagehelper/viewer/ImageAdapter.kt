package moka.land.imagehelper.viewer

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import moka.land.imagehelper.R
import kotlinx.android.synthetic.main.mk_layout_viewer_item.view.*

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.BaseItemView>() {

    var onClickItem: ((position: Int) -> Unit)? = null

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

    inner class BaseItemView(var view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.photoView.setOnPhotoTapListener { view, x, y -> onClickItem?.invoke(adapterPosition) }
        }

        fun refresh(data: Data?) {
            Glide
                .with(view.context)
                .load(data!!.uri)
                .into(view.photoView)
        }
    }
}

data class Data(var uri: Uri)
