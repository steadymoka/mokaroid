package moka.land.ui.samples

import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import moka.land.R
import moka.land.base.adapter.GridSpacingItemDecoration
import moka.land.base.adapter._BaseAdapter
import moka.land.base.adapter._ItemData
import moka.land.base.adapter._RecyclerItemView
import moka.land.base.dip
import moka.land.databinding.LayoutImageViewerSampleBinding
import moka.land.databinding.ViewImageItemBinding
import moka.land.imagehelper.picker.builder.ImagePicker
import moka.land.imagehelper.viewer.ImageViewer
import moka.land.util.load

class ImageViewerSampleLayout : Fragment() {

    private val _view by lazy { LayoutImageViewerSampleBinding.inflate(layoutInflater) }
    private val adapter by lazy { ImageAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        bindEvent()
        return _view.root
    }

    private fun initView() {
        _view.recyclerView.adapter = adapter
    }

    private fun bindEvent() {
        _view.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }

        _view.textViewTestButton.setOnClickListener {
            ImagePicker
                .with(this)
                .setConfig {}
                .showMulti { uris ->
                    adapter.setItems(uris.map { ImageAdapter.Data(it) }.toMutableList())
                }
        }

        adapter.onClickItem = {
            val header = TextView(activity!!).apply {
                layoutParams = ViewGroup.LayoutParams(-1, dip(100))

                setTextColor(Color.WHITE)
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
            }

            ImageViewer
                .with(activity!!)
                .setConfig {
                    addHeader(header)
                    addOnPageSelected {
                        header.text = "INDEX : ${it + 1}"
                    }
                }
                .show(adapter.items.map { it.uri } as ArrayList<Uri>, adapter.items.indexOf(it))
        }
    }

    class ImageAdapter : _BaseAdapter<ImageAdapter.Data, _RecyclerItemView<ImageAdapter.Data>>() {

        override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): _RecyclerItemView<Data> {
            return object : _RecyclerItemView<Data>(parent, R.layout.view_image_item) {
                private val _view = ViewImageItemBinding.bind(itemView)

                override fun refreshView() {
                    _view.imageView.load(parent.context, data.uri)
                    _view.textViewLabel.text = "${index + 1}"
                }
            }
        }


        data class Data(var uri: Uri) : _ItemData
    }

}
