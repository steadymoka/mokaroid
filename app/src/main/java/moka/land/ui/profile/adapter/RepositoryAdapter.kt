package moka.land.ui.profile.adapter

import android.view.ViewGroup
import moka.land.R
import moka.land.base.adapter._HeaderFooterAdapter
import moka.land.base.adapter._ItemData
import moka.land.base.adapter._RecyclerItemView
import moka.land.databinding.ViewRepositoryItemBinding
import moka.land.ui.profile.Repository

class RepositoryAdapter : _HeaderFooterAdapter<RepositoryAdapter.Data, _RecyclerItemView<RepositoryAdapter.Data>>() {

    override fun hasHeader(): Boolean = false

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): _RecyclerItemView<Data> {
        return ItemView(parent)
    }

    /**
     * ItemView & Data
     */

    inner class ItemView(parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.view_repository_item) {

        private val _view = ViewRepositoryItemBinding.bind(itemView)

        override fun refreshView() {
            _view.textViewTitle.text = "\uD83D\uDCD3 ${data.repository.name()}"
            _view.textViewDescription.text = data.repository.description()
        }

    }

    data class Data(var repository: Repository) : _ItemData

}
