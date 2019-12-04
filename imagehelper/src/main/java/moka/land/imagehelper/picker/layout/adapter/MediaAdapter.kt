package moka.land.imagehelper.picker.layout.adapter

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import moka.land.base.adapter._BaseAdapter
import moka.land.base.adapter._RecyclerItemView
import moka.land.base.dip
import moka.land.imagehelper.R
import moka.land.imagehelper.databinding.MkLayoutMediaItemBinding
import moka.land.imagehelper.picker.model.Media

class MediaAdapter : _BaseAdapter<MediaAdapter.Data, _RecyclerItemView<MediaAdapter.Data>>() {

    internal val selectedDataList = mutableListOf<Data>()

    override fun getItemViewType(position: Int) = items[position].type.ordinal

    override fun getViewToCreateViewHolder(parent: ViewGroup, viewType: Int): _RecyclerItemView<Data> {
        return when (Type.get(viewType)) {
            Type.HEADER -> {
                HeaderView(parent)
            }
            Type.ITEM -> {
                ItemView(parent)
            }
        }
    }

    /**
     * ItemView & Data & Type
     */

    inner class HeaderView(parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.mk_layout_media_header)

    inner class ItemView(parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.mk_layout_media_item) {

        private val binding = MkLayoutMediaItemBinding.bind(itemView)

        override fun refreshView() {
            Glide.with(itemView)
                .load(data.media.uri)
                .thumbnail(0.66f)
                .transform(CenterCrop(), RoundedCorners(dip(1)))
                .into(binding.imageViewThumbnail)
        }

        override fun onRecycled() {
            Glide.with(itemView).clear(binding.imageViewThumbnail)
        }

    }

    enum class Type {
        HEADER, ITEM;

        companion object {
            fun get(ordinal: Int) = values().filter { it.ordinal == ordinal }[0]
        }
    }

    data class Data(var type: Type = Type.ITEM, var media: Media)

}
