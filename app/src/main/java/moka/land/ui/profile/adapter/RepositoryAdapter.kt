package moka.land.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moka.land.R
import moka.land.base.adapter._HeaderFooterAdapter
import moka.land.base.adapter._ItemData
import moka.land.base.adapter._RecyclerItemView
import moka.land.databinding.LayoutRepositoryItemBinding
import moka.land.ui.profile.Repository

class RepositoryAdapter : _HeaderFooterAdapter<RepositoryAdapter.Data, _RecyclerItemView<RepositoryAdapter.Data>>() {

    var showLoading: Boolean = true
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    notifyItemInserted(itemCount - 1)
                }
                else {
                    notifyItemRemoved(itemCount - 1)
                }
            }
        }

    override fun hasHeader(): Boolean = false

    override fun hasFooter(): Boolean = showLoading

    override fun getViewToCreateItemViewHolder(parent: ViewGroup, viewType: Int): _RecyclerItemView<Data> {
        return ItemView(parent)
    }

    override fun onCreateFooterView(parent: ViewGroup): View? {
        return LayoutInflater.from(parent.context).inflate(R.layout.layout_repository_footer, parent, false)
    }

    /**
     * ItemView & Data
     */

    inner class ItemView(parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.layout_repository_item) {

        private val _view = LayoutRepositoryItemBinding.bind(itemView)

        override fun refreshView() {
            _view.textViewTitle.text = "\uD83D\uDCD3 ${data.repository.name()}"
            _view.textViewDescription.text = data.repository.description()
        }

    }

    data class Data(var repository: Repository) : _ItemData

}
