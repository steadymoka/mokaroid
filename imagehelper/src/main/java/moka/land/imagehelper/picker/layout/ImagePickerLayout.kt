package moka.land.imagehelper.picker.layout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import moka.land.base.statusBarSize
import moka.land.base.topMargin
import moka.land.imagehelper.databinding.LayoutImagePickerBinding
import moka.land.imagehelper.picker.builder.ImagePickerBuilder

internal class ImagePickerLayout : AppCompatActivity() {

    private lateinit var binding: LayoutImagePickerBinding
    private val viewModel by lazy { ImagePickerViewModel() }
    private lateinit var builder: ImagePickerBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutImagePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        bindEvents()

        builder = intent.getSerializableExtra(KEY_BUILDER_EXTRA) as ImagePickerBuilder
        Log.wtf("aaa", "builder: ${builder}")

        lifecycleScope.launch {
            viewModel.loadAlbumList(this@ImagePickerLayout)
        }
    }

    private fun initViews() {
        binding.viewToolbar.topMargin(statusBarSize)
    }

    private fun bindEvents() {
        binding.imageViewClose.setOnClickListener { finish() }
        binding.textViewDirectory.setOnClickListener { }
        binding.textViewDone.setOnClickListener { }
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
