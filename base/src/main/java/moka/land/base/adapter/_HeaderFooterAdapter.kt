package moka.land.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import moka.land.base.R
import moka.land.base.log

enum class Footer {
    NONE, VIEW, LOADING;
}

abstract class _HeaderFooterAdapter<DATA : _ItemData, VIEW : _RecyclerItemView<DATA>> : _BaseAdapter<DATA, VIEW>() {

    var onClickHeader: (() -> Unit)? = null

    var onClickFooter: (() -> Unit)? = null

    private var hasFooter: Footer = Footer.NONE
        get() {
            return if (hasFooter()) {
                Footer.VIEW
            }
            else {
                field
            }
        }

    fun showFooterLoading() {
        if (!hasFooter()) {
            hasFooter = Footer.LOADING
            notifyItemInserted(itemCount - 1)
        }
    }

    fun hideFooterLoading() {
        if (!hasFooter()) {
            hasFooter = Footer.NONE
            notifyItemRemoved(itemCount)
        }
    }

    open fun hasHeader(): Boolean = true

    open fun hasFooter(): Boolean = false

    open fun onCreateHeaderView(parent: ViewGroup): View? {
        return null
    }

    open fun onCreateFooterView(parent: ViewGroup): View? {
        return null
    }

    open fun onCreateFooterLoadingView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.layout_footer_loading, parent, false)
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
            Type.FOOTER -> {
                when (hasFooter) {
                    Footer.LOADING -> {
                        val footerView = onCreateFooterLoadingView(parent)
                        object : RecyclerView.ViewHolder(footerView) {}.apply {
                            itemView.setOnClickListener { onClickFooter?.invoke() }
                        }
                    }
                    else -> {
                        val footerView = onCreateFooterView(parent)
                            ?: throw Exception("You must override onCreateFooterView(parent: ViewGroup)")
                        object : RecyclerView.ViewHolder(footerView) {}.apply {
                            itemView.setOnClickListener { onClickFooter?.invoke() }
                        }
                    }
                }
            }
            else -> {
                super.onCreateViewHolder(parent, viewType)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (Type.get(getItemViewType(position))) {
            Type.HEADER -> Unit
            Type.FOOTER -> Unit
            else -> {
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
            hasHeader() && hasFooter != Footer.NONE -> {
                items.size + 2
            }
            hasHeader() && hasFooter == Footer.NONE -> {
                items.size + 1
            }
            !hasHeader() && hasFooter != Footer.NONE -> {
                items.size + 1
            }
            else -> {
                items.size
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            hasHeader() && hasFooter != Footer.NONE -> {
                when (position) {
                    0 -> Type.HEADER.index
                    itemCount - 1 -> Type.FOOTER.index
                    else -> getViewType(position - 1)
                }
            }
            hasHeader() && hasFooter == Footer.NONE -> {
                when (position) {
                    0 -> Type.HEADER.index
                    else -> getViewType(position - 1)
                }
            }
            !hasHeader() && hasFooter != Footer.NONE -> {
                when (position) {
                    itemCount - 1 -> Type.FOOTER.index
                    else -> getViewType(position)
                }
            }
            else -> {
                getViewType(position)
            }
        }
    }

    open fun getViewType(position: Int): Int {
        return Type.ITEM.index
    }

    fun notifyContentItemInserted(contentPosition: Int) {
        if (hasHeader()) {
            notifyItemInserted(contentPosition + 1)
        }
        else {
            notifyItemInserted(contentPosition)
        }
    }

    fun notifyContentItemInserted(data: DATA) {
        if (hasHeader()) {
            notifyItemInserted(items.indexOf(data) + 1)
        }
        else {
            notifyItemInserted(items.indexOf(data))
        }
    }

    fun notifyContentItemRemoved(contentPosition: Int) {
        if (hasHeader()) {
            notifyItemRemoved(contentPosition + 1)
        }
        else {
            notifyItemRemoved(contentPosition)
        }
    }

    fun notifyContentItemRemoved(data: DATA) {
        if (hasHeader()) {
            notifyItemRemoved(items.indexOf(data) + 1)
        }
        else {
            notifyItemRemoved(items.indexOf(data))
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

    enum class Type(var index: Int) {
        HEADER(100401), FOOTER(100402), ITEM(100403);

        companion object {
            fun get(index: Int) = values().filter { it.index == index }.getOrNull(0)
        }
    }

}
