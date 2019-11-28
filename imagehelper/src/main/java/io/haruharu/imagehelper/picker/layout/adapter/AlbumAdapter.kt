package io.haruharu.imagehelper.picker.layout.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.haruharu.imagehelper.R
import land.moka.base.adapter._BaseAdapter
import land.moka.base.adapter._RecyclerItemView

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
