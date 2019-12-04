package moka.land.imagehelper.picker.layout

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import moka.land.base.*
import moka.land.base.adapter.GridSpacingItemDecoration
import moka.land.imagehelper.databinding.MkLayoutImagePickerBinding
import moka.land.imagehelper.picker.builder.ImagePickerBuilder
import moka.land.imagehelper.picker.layout.adapter.AlbumAdapter
import moka.land.imagehelper.picker.layout.adapter.MediaAdapter

internal class ImagePickerLayout : AppCompatActivity() {

    private lateinit var binding: MkLayoutImagePickerBinding
    private lateinit var builder: ImagePickerBuilder

    private val viewModel by lazy { ImagePickerViewModel() }
    private val albumAdapter by lazy { AlbumAdapter() }
    private val mediaAdapter by lazy { MediaAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MkLayoutImagePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        bindEvents()
        bindViewModel()

        builder = intent.getSerializableExtra(KEY_BUILDER_EXTRA) as ImagePickerBuilder

        lifecycleScope.launch {
            viewModel.loadAlbumList(this@ImagePickerLayout)
        }
    }

    private fun initViews() {
        binding.viewToolbar.topMargin(statusBarSize)
        binding.recyclerViewAlbum.adapter = albumAdapter
        binding.recyclerViewMedia.adapter = mediaAdapter
        binding.recyclerViewMedia.addItemDecoration(GridSpacingItemDecoration(3, 8))
    }

    private fun bindEvents() {
        binding.imageViewClose.setOnClickListener { finish() }

        binding.textViewDirectory.setOnClickListener {
            viewModel.openAlbumList.value = !(viewModel.openAlbumList.value ?: false)
        }

        binding.textViewDone.setOnClickListener {
        }

        binding.viewDim.setOnClickListener {
            viewModel.openAlbumList.value = false
        }

        albumAdapter.onClickItem = { data ->
            binding.textViewDirectory.text = data.album.name
            binding.recyclerViewMedia.scrollToPosition(0)
            viewModel.openAlbumList.value = false
            viewModel.mediaList.value = data.album.mediaUris.map { MediaAdapter.Data(media = it) }
        }

        mediaAdapter.onClickItem = { data ->

        }
    }

    private fun bindViewModel() {
        viewModel.albumList.observe(this, Observer {
            if (albumAdapter.items.isEmpty()) {
                val initData = it.first()
                albumAdapter.selectedData = initData
                viewModel.mediaList.value = initData.album.mediaUris.map { MediaAdapter.Data(media = it) }
            }
            albumAdapter.replaceItems(it)
        })

        viewModel.mediaList.observe(this, Observer {
            mediaAdapter.replaceItems(it)
        })

        viewModel.openAlbumList.observe(this, Observer {
            when (it) {
                true -> {
                    binding.viewDim.visibleFadeIn(200)
                    expand(binding.recyclerViewAlbum, 300, 0f, 0.6f)
                }
                false -> {
                    binding.viewDim.goneFadeOut(200)
                    collapse(binding.recyclerViewAlbum, 300, 0.6f, 0f)
                }
            }
        })
    }

    private var collapseAnimator: ValueAnimator? = null
    private var expandAnimator: ValueAnimator? = null

    private fun collapse(view: View, duration: Int, start: Float, target: Float) {
        val startValue = if (null != expandAnimator) {
            expandAnimator!!.cancel()
            expandAnimator!!.animatedValue as Float
        }
        else {
            start
        }
        collapseAnimator = ValueAnimator.ofFloat(startValue, target)
        collapseAnimator!!.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            view.updateLayoutParams<ConstraintLayout.LayoutParams> {
                matchConstraintPercentHeight = value
            }
            view.requestLayout()
        }
        collapseAnimator!!.interpolator = DecelerateInterpolator()
        collapseAnimator!!.duration = duration.toLong()
        collapseAnimator!!.start()
    }

    private fun expand(view: View, duration: Int, start: Float, target: Float) {
        val startValue = if (null != collapseAnimator) {
            collapseAnimator!!.cancel()
            collapseAnimator!!.animatedValue as Float
        }
        else {
            start
        }
        expandAnimator = ValueAnimator.ofFloat(startValue, target)
        expandAnimator!!.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            view.updateLayoutParams<ConstraintLayout.LayoutParams> {
                matchConstraintPercentHeight = value
            }
            view.requestLayout()
        }
        expandAnimator!!.interpolator = DecelerateInterpolator()
        expandAnimator!!.duration = duration.toLong()
        expandAnimator!!.start()
    }

    /* */

    companion object {

        private const val KEY_BUILDER_EXTRA = "ImagePickerLayout.KEY_BUILDER_EXTRA"

        fun getIntent(context: Context, builder: ImagePickerBuilder): Intent {
            return Intent(context, ImagePickerLayout::class.java).apply {
                putExtra(KEY_BUILDER_EXTRA, builder)
            }
        }

    }

}
