package moka.land.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moka.land.R
import moka.land.base.adapter._HeaderFooterAdapter
import moka.land.base.adapter._ItemData
import moka.land.base.adapter._RecyclerItemView
import moka.land.base.dip
import moka.land.databinding.ViewOrganizerItemBinding
import moka.land.databinding.ViewRepositoryItemBinding
import moka.land.ui.profile.Organizer
import moka.land.ui.profile.Pinned
import moka.land.util.load

class OverviewAdapter : _HeaderFooterAdapter<OverviewAdapter.Data, _RecyclerItemView<OverviewAdapter.Data>>() {

    override fun hasFooter(): Boolean = true

    override fun getViewType(position: Int): Int {
        return items[position].type.ordinal
    }

    override fun onCreateHeaderView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.view_pinned_header, parent, false)
    }

    override fun onCreateFooterView(parent: ViewGroup): View? {
        return View(parent.context).apply { layoutParams = ViewGroup.LayoutParams(-1, dip(30)) }
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): _RecyclerItemView<Data> {
        return when (Type.get(viewType)) {
            Type.PINNED -> PinnedItemView(parent)
            Type.ORGANIZER -> OrganizerItemView(parent)
            Type.ORGANIZER_SECTION -> OrganizerSectionView(parent)
        }
    }

    /**
     * ItemView & Data
     */

    inner class PinnedItemView(parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.view_repository_item) {

        private val _view = ViewRepositoryItemBinding.bind(itemView)

        override fun refreshView() {
            _view.textViewTitle.text = "\uD83D\uDCD3 ${data.repository?.name() ?: ""}"
            _view.textViewDescription.text = data.repository?.description()
        }

    }

    inner class OrganizerItemView(var parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.view_organizer_item) {

        private val _view = ViewOrganizerItemBinding.bind(itemView)

        override fun refreshView() {
            _view.imageViewThumb.load(parent.context, data.organizer!!.avatarUrl() as String)
            _view.textViewTitle.text = data.organizer?.name()
            _view.textViewDescription.text = data.organizer?.description()
        }

    }

    inner class OrganizerSectionView(parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.view_organizer_header)

    enum class Type {
        PINNED, ORGANIZER, ORGANIZER_SECTION;

        companion object {
            fun get(index: Int): Type = values().filter { it.ordinal == index }[0]
        }
    }

    data class Data(
        var type: Type,
        var repository: Pinned? = null,
        var organizer: Organizer? = null) : _ItemData

}
