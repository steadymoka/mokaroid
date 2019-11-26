package moka.land.app.main

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.haruharu.imagehelper.picker.builder.ImagePicker
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
            ImagePicker
                .with(this)
                .showSingle {
                    Log.wtf("moka", "it: $it")
                }
        }

        findViewById<TextView>(R.id.textView02).setOnClickListener {
            LoadingDialog().show(supportFragmentManager)
        }

        findViewById<TextView>(R.id.textView03).setOnClickListener {
            WebViewActivity.goWebView(this, "https://naver.com", "WEB")
        }

    }

}
