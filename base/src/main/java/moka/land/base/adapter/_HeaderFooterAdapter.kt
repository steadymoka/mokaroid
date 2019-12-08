package moka.land.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class _HeaderFooterAdapter<DATA : _ItemData, VIEW : _RecyclerItemView<DATA>> : _BaseAdapter<DATA, VIEW>() {

    var onClickHeader: ((_ItemData) -> Unit)? = null

    var headerItems = mutableListOf<_ItemData>()

    abstract fun getViewToCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): _RecyclerItemView<_ItemData>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (Type.get(viewType)) {
            Type.HEADER -> {
                getViewToCreateHeaderViewHolder(parent, viewType).apply {
                    itemView.setOnClickListener {
                        onClickItem()
                        onClickHeader?.invoke(headerItems[adapterPosition])
                    }
                }
            }
            Type.ITEM -> {
                super.onCreateViewHolder(parent, viewType)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (Type.get(getItemViewType(position))) {
            Type.HEADER -> Unit
            Type.ITEM -> super.onBindViewHolder(holder, position - headerItems.size)
        }
    }

    override fun getItemCount(): Int {
        return items.size + headerItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < headerItems.size) {
            Type.HEADER.ordinal
        }
        else {
            Type.ITEM.ordinal
        }
    }

    fun notifyContentItemChanged(contentPosition: Int) {
        notifyItemChanged(contentPosition + headerItems.size)
    }

    fun notifyContentItemChanged(data: DATA) {
        notifyItemChanged(items.indexOf(data) + headerItems.size)
    }

    fun notifyHeaderItemChanged(headerPosition: Int) {
        notifyItemChanged(headerPosition)
    }

    fun notifyHeaderItemChanged(data: _ItemData) {
        notifyItemChanged(headerItems.indexOf(data))
    }

    enum class Type {
        HEADER, ITEM;

        companion object {
            fun get(ordinal: Int) = values().filter { it.ordinal == ordinal }[0]
        }
    }

}
