package moka.land.imagehelper.picker.layout

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import moka.land.base.*
import moka.land.base.adapter.GridSpacingItemDecoration
import moka.land.dialog.LoadingDialog
import moka.land.imagehelper.databinding.MkLayoutImagePickerBinding
import moka.land.imagehelper.picker.builder.ImagePicker
import moka.land.imagehelper.picker.builder.ImagePickerConfig
import moka.land.imagehelper.picker.type.MediaType
import moka.land.imagehelper.picker.type.SelectType
import moka.land.imagehelper.picker.layout.adapter.AlbumAdapter
import moka.land.imagehelper.picker.layout.adapter.MediaAdapter
import moka.land.imagehelper.picker.util.CameraUtil
import java.io.File

internal class ImagePickerLayout : AppCompatActivity() {

    private lateinit var _view: MkLayoutImagePickerBinding
    private lateinit var config: ImagePickerConfig

    private val viewModel by lazy { ImagePickerViewModel() }
    private val albumAdapter by lazy { AlbumAdapter() }
    private val mediaAdapter by lazy { MediaAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _view = MkLayoutImagePickerBinding.inflate(layoutInflater)
        config = intent.getSerializableExtra(KEY_BUILDER_EXTRA) as ImagePickerConfig
        setContentView(_view.root)

        initViews()
        bindEvents()
        bindViewModel()

        lifecycleScope.launch {
            viewModel.loadAlbumList(this@ImagePickerLayout)
        }
    }

    private var fileToSave: File? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || null == fileToSave) {
            return
        }

        val loadingDialog = LoadingDialog()
        lifecycleScope.launch {
            loadingDialog.show(this@ImagePickerLayout.supportFragmentManager)
            CameraUtil.save(this@ImagePickerLayout, fileToSave!!)
            viewModel.loadAlbumList(this@ImagePickerLayout)
            viewModel.mediaList.value = viewModel.albumList.value?.get(0)?.album?.mediaUris?.map { MediaAdapter.Data(media = it) }
            _view.recyclerViewMedia.scrollToPosition(0)
            loadingDialog.dismiss()
        }
    }

    private fun initViews() {
        _view.viewToolbar.topMargin(statusBarSize)

        _view.recyclerViewAlbum.adapter = albumAdapter
        _view.recyclerViewAlbum.itemAnimator = null

        mediaAdapter.setOption {
            this.selectType = config.selectType
            this.camera = config.camera
            this.indicatorColorRes = config.indicatorColorRes
        }
        _view.recyclerViewMedia.adapter = mediaAdapter
        _view.recyclerViewMedia.itemAnimator = null
        _view.recyclerViewMedia.addItemDecoration(GridSpacingItemDecoration(3, dip(2)))
    }

    private fun bindEvents() {
        _view.imageViewClose.setOnClickListener { finish() }

        _view.textViewDirectory.setOnClickListener {
            viewModel.openAlbumList.value = !(viewModel.openAlbumList.value ?: false)
        }

        _view.textViewDone.setOnClickListener {
            onClickDone()
        }

        _view.viewDim.setOnClickListener {
            viewModel.openAlbumList.value = false
        }

        albumAdapter.onClickItem = { data ->
            _view.textViewDirectory.text = data.album.name
            _view.recyclerViewMedia.scrollToPosition(0)
            viewModel.openAlbumList.value = false
            viewModel.mediaList.value = data.album.mediaUris.map { MediaAdapter.Data(media = it) }
        }

        mediaAdapter.onClickHeader = {
            lifecycleScope.launch {
                fileToSave = CameraUtil.getFileToSave(this@ImagePickerLayout, MediaType.IMAGE_ONLY)
                val intent = CameraUtil.getCameraIntent(this@ImagePickerLayout, MediaType.IMAGE_ONLY, fileToSave!!)
                startActivityForResult(intent, 1004)
            }
        }

        mediaAdapter.onClickItem = { data ->
            // todo 사진 크게보기
        }
    }

    private fun onClickDone() {
        when (config.selectType) {
            SelectType.SINGLE -> {
                if (mediaAdapter.selectedDataList.isNotEmpty()) {
                    ImagePicker.onSingleSelected?.invoke(mediaAdapter.selectedDataList[0].media.uri)
                    finish()
                }
                else {
                    Toast.makeText(this, "선택 해주세요", Toast.LENGTH_SHORT).show()
                }
            }
            SelectType.MULTI -> {
                if (mediaAdapter.selectedDataList.isNotEmpty()) {
                    ImagePicker.onMultiSelected?.invoke(mediaAdapter.selectedDataList.map { it.media.uri })
                    finish()
                }
                else {
                    Toast.makeText(this, "선택 해주세요", Toast.LENGTH_SHORT).show()
                }
            }
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
                    _view.viewDim.visibleFadeIn(200)
                    expand(_view.recyclerViewAlbum, 300, 0f, 0.6f)
                }
                false -> {
                    _view.viewDim.goneFadeOut(200)
                    collapse(_view.recyclerViewAlbum, 300, 0.6f, 0f)
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

        fun getIntent(context: Context, config: ImagePickerConfig): Intent {
            return Intent(context, ImagePickerLayout::class.java).apply {
                putExtra(KEY_BUILDER_EXTRA, config)
            }
        }

    }

}
