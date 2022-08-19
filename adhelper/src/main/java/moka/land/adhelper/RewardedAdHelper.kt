package moka.land.adhelper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import moka.land.base.log

/**
 * 보상형 동영상 광고 (RewardAD)
 * Admob, AudienceNetwork
 */

@SuppressLint("StaticFieldLeak")
class RewardedAdHelper private constructor() {

    companion object {

        var context: Activity? = null

        var mAd_admob: RewardedAd? = null
        var mAd_audience: com.facebook.ads.RewardedVideoAd? = null

        var admobKey: String? = ""
        var period: Period? = null

        fun initAd(context: Activity, admobKey: String, audienceKey: String, period: Period) {
            this.context = context
            this.admobKey = admobKey
            this.period = period

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
                    if (null != mAd_admob) {
                        mAd_admob?.show(context!!) { rewardItem ->
                            onRewarded.invoke()
                        }
                    } else if (mAd_audience?.isAdLoaded == true && mAd_audience?.isAdInvalidated == false) {
                        mAd_audience?.show()
                    }
                }
                else -> {
                    if (mAd_audience?.isAdLoaded == true && mAd_audience?.isAdInvalidated == false) {
                        mAd_audience?.show()
                    } else if (null != mAd_admob) {
                        mAd_admob?.show(context!!) { rewardItem ->
                            onRewarded.invoke()
                        }
                    }
                }
            }
        }

        fun destroy(context: Context) {
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
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(context!!, admobKey!!, adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mAd_admob = null
                    callback(false)
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mAd_admob = rewardedAd
                    callback(true)

                    mAd_admob!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdClicked() {
                            // Called when a click is recorded for an ad.
                        }

                        override fun onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            mAd_admob = null
                        }

                        override fun onAdImpression() {
                            // Called when an impression is recorded for an ad.
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                        }
                    }
                }
            })
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