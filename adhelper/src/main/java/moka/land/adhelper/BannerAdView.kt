package moka.land.adhelper


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdListener
import com.facebook.ads.AdSize
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import moka.land.adhelper.databinding.ViewMokaAdBannerBinding

interface Runner {

    fun show(callback: ((isSuccess: Boolean) -> Unit)? = null)
}

class BannerAdView constructor(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet), Runner {

    var _view: ViewMokaAdBannerBinding

    init {
        _view = ViewMokaAdBannerBinding.inflate(LayoutInflater.from(context))
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
        _view.bannerContainer.visibility = View.GONE
        _view.textViewBannerNoFill.visibility = View.VISIBLE
    }

    private fun loadFacebookBannerAd(fail: () -> Unit) {
        if (option.fbAudienceKey.isNullOrEmpty()) {
            fail()
            return
        }

        facebookBannerAd = com.facebook.ads.AdView(context, option.fbAudienceKey, AdSize.BANNER_HEIGHT_50)
        _view.bannerContainer.removeAllViews()
        _view.bannerContainer.addView(facebookBannerAd)

        val config = facebookBannerAd
            ?.buildLoadAdConfig()
            ?.withAdListener(object : AdListener {
                override fun onAdLoaded(ad: Ad) {
                    callback?.invoke(true)
                }

                override fun onError(ad: Ad?, error: AdError?) {
                    _view.bannerContainer.removeAllViews()
                    fail()
                }

                override fun onAdClicked(p0: Ad?) {
                }

                override fun onLoggingImpression(p0: Ad?) {
                }
            })
            ?.build()
        facebookBannerAd?.loadAd(config)
    }

    private fun loadAdmobBannerAd(fail: () -> Unit) {
        if (option.admobKey.isNullOrEmpty()) {
            fail()
            return
        }

        admobBannerAdView = AdView(context).apply {
//            setAdSize(com.google.android.gms.ads.AdSize.BANNER)
            adUnitId = option.admobKey!!
        }
        _view.bannerContainer.removeAllViews()
        _view.bannerContainer.addView(admobBannerAdView)

        admobBannerAdView?.adListener = object : com.google.android.gms.ads.AdListener() {

            override fun onAdLoaded() {
                super.onAdLoaded()
                callback?.invoke(true)
            }

            override fun onAdFailedToLoad(errorCode: LoadAdError) {
                _view.bannerContainer.removeAllViews()
                fail()
            }
        }
        admobBannerAdView?.loadAd(
            AdRequest
                .Builder()
                .build()
        )
    }

}