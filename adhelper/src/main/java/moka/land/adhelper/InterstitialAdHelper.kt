package moka.land.adhelper

import android.content.Context
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.AdRequest
import moka.land.base.BuildConfig.DEBUG
import moka.land.base.log


class InterstitialAdHelper private constructor(
    var context: Context
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
        val mInterstitialAd = com.google.android.gms.ads.InterstitialAd(context)
        mInterstitialAd.adUnitId = key
        mInterstitialAd.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                log("show_admob_InterstitialAd fail")
                fail()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                log("Success admob ad")
                if (null == onShow || onShow?.invoke() == true) {
                    mInterstitialAd.show()
                }
                success()
            }

            override fun onAdClosed() {
                onClose?.invoke()
                super.onAdClosed()
                log("show_admob_InterstitialAd adClosed")
            }
        }

        mInterstitialAd.loadAd(
            AdRequest
                .Builder()
                .apply { if (DEBUG) addTestDevice(AdHelper.ADMOB_TEST_DEVICE) }
                .build())
    }

    fun showAudience(key: String, success: () -> Unit, fail: () -> Unit) {
        val interstitialAd = InterstitialAd(context, key)
        interstitialAd.setAdListener(object : InterstitialAdListener {
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
        interstitialAd.loadAd()
    }

    companion object {
        fun with(context: Context): InterstitialAdHelper {
            return InterstitialAdHelper(context)
        }
    }

}
