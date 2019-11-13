package io.haruharu.imagehelper.viewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import io.haruharu.imagehelper.R
import kotlinx.android.synthetic.main.layout_image_viewer.*
import land.moka.base.goneFadeOut
import land.moka.base.visibleFadeIn

class ImageViewerLayout : AppCompatActivity() {

    companion object {
        const val KEY_DATAS = "ImageViewerLayout.KEY_DATAS"
        const val KEY_SELECTED_POSITION = "ImageViewerLayout.KEY_SELECTED_POSITION"
    }

    private val imageAdapter by lazy { ImageAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_image_viewer)

        ImageViewer.finish = { finish() }
        ImageViewer.addHeader?.invoke(header)
        ImageViewer.addFooter?.invoke(footer)

        viewPager.adapter = imageAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                ImageViewer.onPageSelected?.invoke(position)
            }
        })

        val items = intent.getStringArrayListExtra(KEY_DATAS)
        imageAdapter.items = items.map { Data(it) } as ArrayList<Data>

        val selectedPosition = intent.getIntExtra(KEY_SELECTED_POSITION, 0)
        viewPager.setCurrentItem(selectedPosition, false)

        imageAdapter.onClickItem = {
            if (header.isVisible) {
                header.goneFadeOut(100)
                footer.goneFadeOut(100)
            }
            else {
                header.visibleFadeIn(100)
                footer.visibleFadeIn(100)
            }
        }
    }

    override fun onDestroy() {
        ImageViewer.addHeader = null
        ImageViewer.addFooter = null
        super.onDestroy()
    }

}
