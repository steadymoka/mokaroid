package moka.land.base.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

abstract class _HeaderFooterAdapter<DATA : _ItemData, VIEW : _RecyclerItemView<DATA>> : _BaseAdapter<DATA, VIEW>() {

    var onClickHeader: (() -> Unit)? = null

    var onClickFooter: (() -> Unit)? = null

    open fun hasHeader(): Boolean = true

    open fun hasFooter(): Boolean = false

    open fun onCreateHeaderView(parent: ViewGroup): View? {
        return null
    }

    open fun onCreateFooterView(parent: ViewGroup): View? {
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (Type.get(viewType)) {
            Type.HEADER -> {
                val headerView = onCreateHeaderView(parent)
                    ?: throw Exception("You must override onCreateHeaderView(parent: ViewGroup)")
                object : RecyclerView.ViewHolder(headerView) {}.apply {
                    itemView.setOnClickListener { onClickHeader?.invoke() }
                }
            }
            Type.ITEM -> {
                super.onCreateViewHolder(parent, viewType)
            }
            Type.FOOTER -> {
                val headerView = onCreateFooterView(parent)
                    ?: throw Exception("You must override onCreateFooterView(parent: ViewGroup)")
                object : RecyclerView.ViewHolder(headerView) {}.apply {
                    itemView.setOnClickListener { onClickFooter?.invoke() }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (Type.get(getItemViewType(position))) {
            Type.HEADER -> Unit
            Type.FOOTER -> Unit
            Type.ITEM -> {
                if (hasHeader()) {
                    super.onBindViewHolder(holder, position - 1)
                }
                else {
                    super.onBindViewHolder(holder, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return when {
            hasHeader() && hasFooter() -> {
                items.size + 2
            }
            hasHeader() && !hasFooter() -> {
                items.size + 1
            }
            !hasHeader() && hasFooter() -> {
                items.size + 1
            }
            else -> {
                items.size
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            hasHeader() && hasFooter() -> {
                when (position) {
                    0 -> Type.HEADER.ordinal
                    itemCount - 1 -> Type.FOOTER.ordinal
                    else -> Type.ITEM.ordinal
                }
            }
            hasHeader() && !hasFooter() -> {
                when (position) {
                    0 -> Type.HEADER.ordinal
                    else -> Type.ITEM.ordinal
                }
            }
            !hasHeader() && hasFooter() -> {
                when (position) {
                    itemCount - 1 -> Type.FOOTER.ordinal
                    else -> Type.ITEM.ordinal
                }
            }
            else -> {
                Type.ITEM.ordinal
            }
        }
    }

    fun notifyContentItemChanged(contentPosition: Int) {
        if (hasHeader()) {
            notifyItemChanged(contentPosition + 1)
        }
        else {
            notifyItemChanged(contentPosition)
        }
    }

    fun notifyContentItemChanged(data: DATA) {
        if (hasHeader()) {
            notifyItemChanged(items.indexOf(data) + 1)
        }
        else {
            notifyItemChanged(items.indexOf(data))
        }
    }

    enum class Type {
        HEADER, FOOTER, ITEM;

        companion object {
            fun get(ordinal: Int) = values().filter { it.ordinal == ordinal }[0]
        }
    }

}
