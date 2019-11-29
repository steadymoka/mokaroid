package moka.land.imagehelper.picker.layout.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import moka.land.imagehelper.R
import moka.land.base.adapter._BaseAdapter
import moka.land.base.adapter._RecyclerItemView

class AlbumAdapter : _BaseAdapter<AlbumAdapter.Data, AlbumAdapter.ItemView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemView(parent)
    }

    /**
     * ItemView & Data
     */

    inner class ItemView(parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.layout_album_item) {

        override fun refreshView(data: Data) {

        }

    }

    data class Data(var title: String)

}
