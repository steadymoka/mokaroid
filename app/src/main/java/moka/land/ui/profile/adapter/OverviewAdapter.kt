package moka.land.ui.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moka.land.R
import moka.land.base.adapter._HeaderFooterAdapter
import moka.land.base.adapter._ItemData
import moka.land.base.adapter._RecyclerItemView
import moka.land.databinding.LayoutOrganizerItemBinding
import moka.land.databinding.LayoutRepositoryItemBinding
import moka.land.ui.profile.Organizer
import moka.land.ui.profile.Pinned
import moka.land.util.load

class OverviewAdapter : _HeaderFooterAdapter<OverviewAdapter.Data, _RecyclerItemView<OverviewAdapter.Data>>() {

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

    override fun hasFooter(): Boolean = showLoading

    override fun getViewType(position: Int): Int {
        return items[position].type.ordinal
    }

    override fun onCreateHeaderView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.layout_pinned_repository_header, parent, false)
    }

    override fun onCreateFooterView(parent: ViewGroup): View? {
        return LayoutInflater.from(parent.context).inflate(R.layout.layout_repository_footer, parent, false)
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

    inner class PinnedItemView(parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.layout_repository_item) {

        private val _view = LayoutRepositoryItemBinding.bind(itemView)

        override fun refreshView() {
            _view.textViewTitle.text = "\uD83D\uDCD3 ${data.repository?.name() ?: ""}"
            _view.textViewDescription.text = data.repository?.description()
        }

    }

    inner class OrganizerItemView(var parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.layout_organizer_item) {

        private val _view = LayoutOrganizerItemBinding.bind(itemView)

        override fun refreshView() {
            _view.imageViewThumb.load(parent.context, data.organizer!!.avatarUrl() as String)
            _view.textViewTitle.text = data.organizer?.name()
            _view.textViewDescription.text = data.organizer?.description()
        }

    }

    inner class OrganizerSectionView(parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.layout_organizer_section)

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
