package moka.land.app.main

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import moka.land.R
import moka.land.adhelper.*
import moka.land.base.log
import moka.land.imagehelper.picker.builder.ImagePicker
import moka.land.permissionmanager.PermissionManager
import moka.land.webview.WebViewActivity
import moka.moka.dialog.LoadingDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adView = findViewById<FrameLayout>(R.id.frameLayoutAd)

        val nativeAd = NativeAdView(this)
        adView.addView(nativeAd)

        nativeAd
            .setOption {
                media = true
                period = Period.FACEBOOK_ADMOB
                admobKey = "ca-app-pub-3940256099942544/2247696110"
                fbAudienceKey = "YOUR_PLACEMENT_ID"
            }
            .showNative {
                log("result: ${it}")
            }

        val banner = findViewById<BannerAdView>(R.id.bannerView)
        banner
            .setOption {
                period = Period.ADMOB_FACEBOOK
                admobKey = "ca-app-pub-3940256099942544/6300978111"
                fbAudienceKey = "YOUR_PLACEMENT_ID"
            }
            .show { log("banner result: ${it}") }

        findViewById<TextView>(R.id.textView01).setOnClickListener {
            PermissionManager
                .with(this)
                .setPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check { isGranted, deniedPermissions ->
                    if (isGranted) {
                        ImagePicker
                            .with(this)
                            .showSingle {
                                Log.wtf("moka", "it: $it")
                            }
                    }
                    else {
                        Toast.makeText(this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
                    }
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
