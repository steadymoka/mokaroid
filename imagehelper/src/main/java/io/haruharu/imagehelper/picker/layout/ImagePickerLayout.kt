package io.haruharu.imagehelper.picker.layout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.haruharu.imagehelper.R
import io.haruharu.imagehelper.databinding.LayoutImagePickerBinding
import io.haruharu.imagehelper.picker.builder.ImagePickerBuilder
import land.moka.base.statusBarSize
import land.moka.base.topMargin

internal class ImagePickerLayout : AppCompatActivity() {

    private lateinit var binding: LayoutImagePickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutImagePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        bindEvents()
    }

    private fun initViews() {
        binding.viewToolbar.topMargin(statusBarSize)
    }

    private fun bindEvents() {
        binding.imageViewClose.setOnClickListener { finish() }
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
