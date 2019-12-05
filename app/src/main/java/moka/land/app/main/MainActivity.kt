package moka.land.app.main

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.InternalCoroutinesApi
import moka.land.R
import moka.land.adhelper.BannerAdView
import moka.land.adhelper.NativeAdView
import moka.land.adhelper.Period
import moka.land.base.log
import moka.land.dialog.LoadingDialog
import moka.land.imagehelper.picker.builder.ImagePicker
import moka.land.imagehelper.picker.conf.SelectType
import moka.land.webview.WebViewActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<NativeAdView>(R.id.nativeAdView)
            .setOption {
                media = true
                period = Period.FACEBOOK_ADMOB
                admobKey = "ca-app-pub-3940256099942544/2247696110"
                fbAudienceKey = "YOUR_PLACEMENT_ID"
            }
            .showNative {}

        findViewById<BannerAdView>(R.id.bannerView)
            .setOption {
                period = Period.ADMOB_FACEBOOK
                admobKey = "ca-app-pub-3940256099942544/6300978111"
                fbAudienceKey = "YOUR_PLACEMENT_ID"
            }
            .show {}

        findViewById<TextView>(R.id.textView01).setOnClickListener {
            ImagePicker
                .with(this)
                .setOption {
                    this.camera = true
                }
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
