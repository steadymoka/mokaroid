package moka.land.adhelper

import android.annotation.SuppressLint
import android.content.Context
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import moka.land.base.log

/**
 * 보상형 동영상 광고 (RewardAD)
 * Admob, AudienceNetwork
 */

@SuppressLint("StaticFieldLeak")
class RewardedAdHelper private constructor() {

    companion object {

        var mAd_admob: RewardedVideoAd? = null
        var mAd_audience: com.facebook.ads.RewardedVideoAd? = null

        var admobKey: String? = ""
        var period: Period? = null

        fun initAd(context: Context, admobKey: String, audienceKey: String, period: Period) {
            this.admobKey = admobKey
            this.period = period

            /* admob */
            mAd_admob = MobileAds.getRewardedVideoAdInstance(context)

            /* audience */
            mAd_audience = com.facebook.ads.RewardedVideoAd(context, audienceKey)
        }

        fun loadAd(callback: (isSuccessLoad: Boolean) -> Unit) {
            when (period) {
                Period.ADMOB_FACEBOOK -> {
                    loadRewardedVideoAd_admob {
                        if (it)
                            callback(true)
                        else
                            loadRewardedVideoAd_audience {
                                callback(it)
                            }
                    }
                }
                else -> {
                    loadRewardedVideoAd_audience {
                        if (it)
                            callback(true)
                        else
                            loadRewardedVideoAd_admob {
                                callback(it)
                            }
                    }
                }
            }
        }

        var onRewarded: (() -> Unit)? = null

        fun show(onRewarded: () -> Unit) {
            this.onRewarded = onRewarded

            when (period) {
                Period.ADMOB_FACEBOOK -> {
                    if (mAd_admob?.isLoaded == true) {
                        mAd_admob?.show()
                    }
                    else if (mAd_audience?.isAdLoaded == true && mAd_audience?.isAdInvalidated == false) {
                        mAd_audience?.show()
                    }
                }
                else -> {
                    if (mAd_audience?.isAdLoaded == true && mAd_audience?.isAdInvalidated == false) {
                        mAd_audience?.show()
                    }
                    else if (mAd_admob?.isLoaded == true) {
                        mAd_admob?.show()
                    }
                }
            }
        }

        fun resume(context: Context) {
            mAd_admob?.resume(context)
        }

        fun pause(context: Context) {
            mAd_admob?.pause(context)
        }

        fun destroy(context: Context) {
            mAd_admob?.destroy(context)
            mAd_audience?.destroy()

            mAd_admob = null
            mAd_audience = null

            admobKey = null
            period = null
            onRewarded = null
        }

        /**
         */

        private fun loadRewardedVideoAd_admob(callback: (isSuccessLoad: Boolean) -> Unit) {
            mAd_admob!!.rewardedVideoAdListener = object : RewardedVideoAdListener {

                override fun onRewardedVideoCompleted() {
                }

                override fun onRewardedVideoAdClosed() {
                    loadAdmob()
                }

                override fun onRewardedVideoAdLeftApplication() {
                }

                override fun onRewardedVideoAdLoaded() {
                    callback(true)
                }

                override fun onRewardedVideoAdOpened() {
                }

                override fun onRewarded(p0: RewardItem?) {
                    onRewarded?.invoke()
                }

                override fun onRewardedVideoStarted() {
                }

                override fun onRewardedVideoAdFailedToLoad(p0: Int) {
                    log("Rewarded video admob ad failed to load: $p0")
                    callback(false)
                }
            }

            loadAdmob()
        }

        private fun loadAdmob() {
            if (AdHelper.ADMOB_TEST_DEVICE.isEmpty()) {
                mAd_admob!!.loadAd(admobKey,
                    AdRequest
                        .Builder()
                        .build()
                )
            }
            else {
                mAd_admob!!.loadAd(admobKey,
                    AdRequest
                        .Builder()
                        .addTestDevice(AdHelper.ADMOB_TEST_DEVICE)
                        .build()
                )
            }
        }

        private fun loadRewardedVideoAd_audience(callback: (isSuccessLoad: Boolean) -> Unit) {
            mAd_audience!!.setAdListener(object : com.facebook.ads.RewardedVideoAdListener {
                override fun onError(ad: Ad, error: AdError) {
                    // Rewarded video ad failed to load
                    log("Rewarded video audience ad failed to load: ${error.errorMessage}")
                    callback(false)
                }

                override fun onAdLoaded(ad: Ad) {
                    // Rewarded video ad is loaded and ready to be displayed
                    callback(true)
                }

                override fun onAdClicked(ad: Ad) {
                    // Rewarded video ad clicked
                }

                override fun onLoggingImpression(ad: Ad) {
                    // Rewarded Video ad impression - the event will fire when the
                    // video starts playing
                }

                override fun onRewardedVideoCompleted() {
                    // Rewarded Video View Complete - the video has been played to the end.
                    // You can use this event to initialize your reward

                    // Call method to give reward
                    onRewarded?.invoke()
                }

                override fun onRewardedVideoClosed() {
                    // The Rewarded Video ad was closed - this can occur during the video
                    // by closing the app, or closing the end card.
                    mAd_audience?.loadAd()
                }
            })

            mAd_audience?.loadAd()
        }
    }

}