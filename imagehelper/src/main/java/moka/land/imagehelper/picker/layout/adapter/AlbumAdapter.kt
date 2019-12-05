package moka.land.imagehelper.picker.layout.adapter

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import moka.land.base.adapter._BaseAdapter
import moka.land.base.adapter._RecyclerItemView
import moka.land.base.dip
import moka.land.base.visibleOrGone
import moka.land.imagehelper.R
import moka.land.imagehelper.databinding.MkLayoutAlbumItemBinding
import moka.land.imagehelper.picker.model.Album

class AlbumAdapter : _BaseAdapter<AlbumAdapter.Data, _RecyclerItemView<AlbumAdapter.Data>>() {

    var selectedData: Data? = null

    override fun getViewToCreateItemViewHolder(parent: ViewGroup, viewType: Int): _RecyclerItemView<Data> {
        return ItemView(parent)
    }

    /**
     * ItemView & Data
     */

    inner class ItemView(parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.mk_layout_album_item) {

        private val binding = MkLayoutAlbumItemBinding.bind(itemView)

        override fun refreshView() {
            binding.textViewAlbumName.text = data.album.name
            binding.textViewSub.text = String.format("%,d", data.album.mediaUris.size)
            Glide.with(itemView)
                .load(data.album.thumbnailUri)
                .error(R.drawable.mk_rc_black_black_ro3)
                .thumbnail(0.66f)
                .transform(CenterCrop(), RoundedCorners(dip(3)))
                .override(dip(50), dip(50))
                .into(binding.imageViewThumbnail)

            binding.viewClicked.visibleOrGone(data == selectedData)
        }

        override fun onRecycled() {
            Glide.with(itemView).clear(binding.imageViewThumbnail)
        }

        override fun onClickItem() {
            if (this@AlbumAdapter.selectedData != data) {
                notifyItemChanged(items.indexOf(this@AlbumAdapter.selectedData))
                notifyItemChanged(items.indexOf(data))
                this@AlbumAdapter.selectedData = data
            }
        }

    }

    data class Data(var album: Album)

}
