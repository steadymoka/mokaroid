package moka.land.adhelper

import android.app.Activity
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import moka.land.base.log
import com.google.android.gms.ads.interstitial.InterstitialAd as AdmobInterstitialAd


class InterstitialAdHelper private constructor(
    var context: Activity
) {

    var onShow: (() -> Boolean)? = null
    var onClose: (() -> Boolean)? = null

    fun showAdmobAudience(admobKey: String, facebookKey: String) {
        showAdmob(admobKey, {}, {
            showAudience(facebookKey, {}, {})
        })
    }

    fun showAudienceAdmob(admobKey: String, facebookKey: String) {
        showAudience(facebookKey, {}, {
            showAdmob(admobKey, {}, {})
        })
    }

    fun showAdmob(key: String, success: () -> Unit, fail: () -> Unit) {
        val request = AdRequest.Builder().build()
        AdmobInterstitialAd.load(context, key, request, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                log("show_admob_InterstitialAd fail")
                fail()
            }

            override fun onAdLoaded(interstitialAd: AdmobInterstitialAd) {
                super.onAdLoaded(interstitialAd)
                log("Success admob ad")

                interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        onClose?.invoke()
                        log("show_admob_InterstitialAd adClosed")
                    }
                }
                if (null == onShow || onShow?.invoke() == true) {
                    interstitialAd.show(context)
                }
                success()
            }
        }
        )
    }

    fun showAudience(key: String, success: () -> Unit, fail: () -> Unit) {
        val interstitialAd = InterstitialAd(context, key)
        val config = interstitialAd
            .buildLoadAdConfig()
            .withAdListener(object : InterstitialAdListener {
                override fun onInterstitialDisplayed(p0: Ad?) {
                }

                override fun onAdClicked(p0: Ad?) {
                }

                override fun onInterstitialDismissed(p0: Ad?) {
                    log("show_facebook_InterstitialAd dismissed")
                    onClose?.invoke()
                }

                override fun onError(p0: Ad?, p1: AdError?) {
                    log("show_facebook_InterstitialAd fail")
                    fail()
                }

                override fun onAdLoaded(p0: Ad?) {
                    log("Success audience network ad")
                    if (null == onShow || onShow?.invoke() == true) {
                        interstitialAd.show()
                    }
                    success()
                }

                override fun onLoggingImpression(p0: Ad?) {
                }
            })
            .build()
        interstitialAd.loadAd(config)
    }

    companion object {
        fun with(context: Activity): InterstitialAdHelper {
            return InterstitialAdHelper(context)
        }
    }

}
