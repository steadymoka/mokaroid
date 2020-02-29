package moka.land.imagehelper.picker.layout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import moka.land.base.adapter._HeaderFooterAdapter
import moka.land.base.adapter._ItemData
import moka.land.base.adapter._RecyclerItemView
import moka.land.base.gone
import moka.land.base.visible
import moka.land.base.visibleOrGone
import moka.land.imagehelper.R
import moka.land.imagehelper.databinding.MkLayoutMediaItemBinding
import moka.land.imagehelper.picker.builder.ImagePickerConfig
import moka.land.imagehelper.picker.model.Media
import moka.land.imagehelper.picker.type.SelectType

class MediaAdapter : _HeaderFooterAdapter<MediaAdapter.Data, _RecyclerItemView<MediaAdapter.Data>>() {

    /**
     */

    var onClickToShowImage: ((Data) -> Unit)? = null
    var onClickToPlayVideo: ((Data) -> Unit)? = null

    private lateinit var config: ImagePickerConfig
    internal val selectedDataList = mutableListOf<Data>()

    fun setConfig(config: ImagePickerConfig) {
        this.config = config
    }

    override fun hasHeader(): Boolean = config.camera

    override fun onCreateHeaderView(parent: ViewGroup): View? {
        return LayoutInflater.from(parent.context).inflate(R.layout.mk_layout_media_header, parent, false)
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): _RecyclerItemView<Data> {
        return ItemView(parent)
    }

    /**
     * ItemView & Data & Type
     */

    inner class ItemView(var parent: ViewGroup) : _RecyclerItemView<Data>(parent, R.layout.mk_layout_media_item) {

        private val _view = MkLayoutMediaItemBinding.bind(itemView)

        init {
            _view.frameLayoutFullScreen.setOnClickListener { onClickToShowImage?.invoke(data) }
            _view.textViewPlayVideo.setOnClickListener { onClickToPlayVideo?.invoke(data) }
        }

        override fun refreshView() {
            if (data.media.type.contains("video")) {
                _view.textViewPlayVideo.visible()
                _view.frameLayoutFullScreen.gone()

                _view.textViewPlayVideo.text = data.media.getDurationString()
            }
            else {
                _view.textViewPlayVideo.gone()
                _view.frameLayoutFullScreen.visibleOrGone(config.showFullscreenButton)

                _view.textViewPlayVideo.text = ""
            }

            Glide.with(itemView)
                .load(data.media.uri)
                .thumbnail(0.66f)
                .transform(CenterCrop())
                .error(R.drawable.mk_vc_error)
                .into(_view.imageViewThumbnail)

            if (selectedDataList.contains(data)) {
                _view.viewClicked.visible()
                _view.textViewCheck.visible()
                _view.textViewCheck.setBackgroundResource(R.drawable.mk_cc_red_red)
                if (null != config.indicatorColorRes) {
                    _view.textViewCheck.backgroundTintList = ContextCompat.getColorStateList(itemView.context, config.indicatorColorRes!!)
                }

                when (config.selectType) {
                    SelectType.SINGLE -> {
                        _view.textViewCheck.text = ""
                        _view.imageViewCheck.visible()
                    }
                    SelectType.MULTI -> {
                        _view.textViewCheck.text = "${selectedDataList.indexOf(data) + 1}"
                        _view.imageViewCheck.gone()
                    }
                }
            }
            else {
                _view.imageViewCheck.gone()
                _view.viewClicked.gone()
                _view.textViewCheck.gone() // todo, config 에 따라서 GONE / VISIBLE
                _view.textViewCheck.setBackgroundResource(R.drawable.mk_cc_white_tr)
                _view.textViewCheck.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.white_01)
                _view.textViewCheck.text = ""
            }
        }

        override fun onRecycled() {
            Glide.with(itemView).clear(_view.imageViewThumbnail)
        }

        override fun onClickItem() {
            when (config.selectType) {
                SelectType.SINGLE -> {
                    if (selectedDataList.isNotEmpty()) {
                        notifyContentItemChanged(items.indexOf(selectedDataList[0]))
                    }

                    if (selectedDataList.getOrNull(0) == data) {
                        selectedDataList.clear()
                        notifyContentItemChanged(items.indexOf(data))
                    }
                    else {
                        selectedDataList.clear()
                        selectedDataList.add(data)
                        notifyContentItemChanged(items.indexOf(data))
                    }
                }
                SelectType.MULTI -> {
                    if (selectedDataList.contains(data)) {
                        selectedDataList.remove(data)
                        notifyContentItemChanged(items.indexOf(data))
                    }
                    else {
                        selectedDataList.add(data)
                    }
                    selectedDataList.forEach {
                        notifyContentItemChanged(items.indexOf(it))
                    }
                }
            }
        }

    }

    data class Data(
        var media: Media
    ) : _ItemData

}
