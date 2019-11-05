package moka.land.app.main

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.haruharu.imagehelper.viewer.ImageViewer
import io.haruharu.imagehelper.viewer.OverlayView
import io.haruharu.webview.WebViewActivity
import land.moka.dialog.LoadingDialog
import moka.land.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.textView01).setOnClickListener {
            val header = OverlayView(this, R.layout.layout_header)
            header.getTextView(R.id.textViewTitle)?.setOnClickListener {
                ImageViewer.finish()
            }
            ImageViewer.onPageSelected = { position: Int ->
                Log.wtf("aaaaaa", "position: ${position}")
            }

            ImageViewer.addHeader(header)
            ImageViewer.show(
                this@MainActivity,
                arrayListOf(
                    "https://s3.ap-northeast-2.amazonaws.com/ai.hashup.co/191014/____072728.png",
                    "https://s3.ap-northeast-2.amazonaws.com/ai.hashup.co/191014/room708_official_69452159_402669076973677_6822960396756110049_n_083606.jpg",
                    "https://s3.ap-northeast-2.amazonaws.com/ai.hashup.co/191014/____072728.png"),
                1)
        }

        findViewById<TextView>(R.id.textView02).setOnClickListener {
            LoadingDialog().show(supportFragmentManager)
        }

        findViewById<TextView>(R.id.textView03).setOnClickListener {
            WebViewActivity.goWebView(this, "https://naver.com", "HARU HARU")
        }
    }

}
