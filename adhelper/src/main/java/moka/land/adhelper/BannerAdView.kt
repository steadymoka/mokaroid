package moka.land.adhelper


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdListener
import com.facebook.ads.AdSize
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.view_moka_ad_banner.view.*
import moka.land.base.BuildConfig.DEBUG

interface Runner {

    fun show(callback: ((isSuccess: Boolean) -> Unit)? = null)
}

class BannerAdView constructor(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet), Runner {

    init {
        View.inflate(context, R.layout.view_moka_ad_banner, this)
    }

    inner class Option {
        var fbAudienceKey: String? = null
        var admobKey: String? = null
        var period: Period = Period.ADMOB_FACEBOOK
    }

    private lateinit var option: Option
    private var callback: ((isSuccess: Boolean) -> Unit)? = null

    private var admobBannerAdView: AdView? = null
    private var facebookBannerAd: com.facebook.ads.AdView? = null

    fun setOption(block: Option.() -> Unit): Runner {
        val option = Option()
        option.block()
        this.option = option
        return this
    }

    override fun show(callback: ((isSuccess: Boolean) -> Unit)?) {
        this@BannerAdView.callback = callback

        when (option.period) {
            Period.FACEBOOK_ADMOB -> {
                loadFacebookBannerAd {
                    loadAdmobBannerAd {
                        showError()
                        callback?.invoke(false)
                    }
                }
            }

            Period.ADMOB_FACEBOOK -> {
                loadAdmobBannerAd {
                    loadFacebookBannerAd {
                        showError()
                        callback?.invoke(false)
                    }
                }
            }
        }
    }

    fun onDestroy() {
        admobBannerAdView?.destroy()
        facebookBannerAd?.destroy()
    }

    fun showError() {
        banner_container.visibility = View.GONE
        textView_banner_noFill.visibility = View.VISIBLE
    }

    private fun loadFacebookBannerAd(fail: () -> Unit) {
        if (option.fbAudienceKey.isNullOrEmpty()) {
            fail()
            return
        }

        facebookBannerAd = com.facebook.ads.AdView(context, option.fbAudienceKey, AdSize.BANNER_HEIGHT_50)
        banner_container.removeAllViews()
        banner_container.addView(facebookBannerAd)

        facebookBannerAd?.setAdListener(object : AdListener {
            override fun onAdLoaded(ad: Ad) {
                callback?.invoke(true)
            }

            override fun onError(ad: Ad?, error: AdError?) {
                banner_container.removeAllViews()
                fail()
            }

            override fun onAdClicked(p0: Ad?) {
            }

            override fun onLoggingImpression(p0: Ad?) {
            }
        })
        facebookBannerAd?.loadAd()
    }

    private fun loadAdmobBannerAd(fail: () -> Unit) {
        if (option.admobKey.isNullOrEmpty()) {
            fail()
            return
        }

        admobBannerAdView = AdView(context).apply {
            adSize = com.google.android.gms.ads.AdSize.SMART_BANNER
            adUnitId = option.admobKey
        }
        banner_container.removeAllViews()
        banner_container.addView(admobBannerAdView)

        admobBannerAdView?.adListener = object : com.google.android.gms.ads.AdListener() {

            override fun onAdLoaded() {
                super.onAdLoaded()
                callback?.invoke(true)
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                banner_container.removeAllViews()
                fail()
            }
        }
        admobBannerAdView?.loadAd(
            AdRequest
                .Builder()
                .apply { if (DEBUG) addTestDevice(AdHelper.ADMOB_TEST_DEVICE) }
                .build()
        )
    }

}