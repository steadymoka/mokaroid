package moka.land.adhelper


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.NativeAdOptions.ADCHOICES_BOTTOM_LEFT
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import moka.land.base.*
import moka.land.base.BuildConfig
import com.facebook.ads.AdOptionsView


class NativeAdView constructor(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {

    inner class Option {
        var fbAudienceKey: String? = null
        var admobKey: String? = null
        var isMedia: Boolean = true
        var period: Period = Period.ADMOB_FACEBOOK
        var nativeLayoutResId: Int = R.layout.view_moka_ad_native
    }

    inner class Runner {
        fun showNative(callback: ((isSuccess: Boolean) -> Unit)? = null) {
            this@NativeAdView.callback = callback
            showAdNative()
        }
    }

    private lateinit var option: Option
    private var callback: ((isSuccess: Boolean) -> Unit)? = null

    fun setOption(block: Option.() -> Unit): Runner {
        val option = Option()
        option.block()
        this.option = option
        return Runner()
    }

    private fun showAdNative() {
        when (option.period) {
            Period.FACEBOOK_ADMOB -> {
                loadFBAudienceNativeAd {
                    loadAdmobNativeAd {
                        showError()
                        callback?.invoke(false)
                    }
                }
            }

            Period.ADMOB_FACEBOOK -> {
                loadAdmobNativeAd {
                    loadFBAudienceNativeAd {
                        showError()
                        callback?.invoke(false)
                    }
                }
            }
        }
    }

    fun onDestroy() {
    }

    fun showError() {

    }

    private fun loadFBAudienceNativeAd(fail: () -> Unit) {
        if (option.fbAudienceKey.isNullOrEmpty()) {
            fail()
            return
        }
        View.inflate(context, option.nativeLayoutResId, this)
        findViewById<FrameLayout>(R.id.frameLayout_media).visibleOrGone(option.isMedia)

        val nativeAd = NativeAd(context, option.fbAudienceKey)
        nativeAd.setAdListener(object : NativeAdListener {
            override fun onMediaDownloaded(p0: Ad?) {
            }

            override fun onAdLoaded(ad: Ad) {
                if (ad != nativeAd) {
                    return
                }

                inflateNativeAdViews(nativeAd)
                callback?.invoke(true)
            }

            override fun onError(ad: Ad?, error: AdError?) {
                log("facebook error : ${error?.errorCode} / ${error?.errorMessage}")
                fail()
            }

            override fun onAdClicked(p0: Ad?) {
            }

            override fun onLoggingImpression(p0: Ad?) {
            }
        })
        nativeAd.loadAd(NativeAdBase.MediaCacheFlag.ALL)
    }

    private fun inflateNativeAdViews(facebookNativeAd: NativeAd) {
        findViewById<FrameLayout>(R.id.frameLayout_media).visibleOrGone(option.isMedia)

        // layout setting
        val relativeLayout_container = findViewById<ConstraintLayout>(R.id.constraintLayout_ad).apply {
            visible()
        }
        findViewById<FrameLayout>(R.id.frameLayout_loading).gone()

        val mediaFrame = findViewById<FrameLayout>(R.id.frameLayout_media)
        val mediaView = com.facebook.ads.MediaView(context)
        if (option.isMedia) {
            mediaFrame.addView(mediaView)
        }

        // Add the AdChoices icon
        findViewById<FrameLayout>(R.id.frameLayout_adChoice).apply {
            addView(AdOptionsView(context, facebookNativeAd, null).apply {})
        }

        // Setting the Text.
        findViewById<TextView>(R.id.textView_sponsored).apply {
            text = "Sponsored by ${facebookNativeAd.advertiserName}"
        }
        val textView_title = findViewById<TextView>(R.id.textView_title).apply {
            text = facebookNativeAd.adSocialContext
        }
        findViewById<TextView>(R.id.textView_socialContext).text = facebookNativeAd.adBodyText
        val textView_callToAction = findViewById<TextView>(R.id.textView_callToAction).apply {
            text = facebookNativeAd.adCallToAction
        }

        // Register the Title and CTA button to listen for clicks.
        val clickableViews: ArrayList<View> = ArrayList()
        clickableViews.add(mediaView)
        clickableViews.add(textView_title)
        clickableViews.add(textView_callToAction)

        facebookNativeAd.registerViewForInteraction(
            relativeLayout_container,
            mediaView,
            findViewById<ImageView>(R.id.imageView_adIcon),
            clickableViews)
    }

    private fun loadAdmobNativeAd(fail: () -> Unit) {
        if (option.admobKey.isNullOrEmpty()) {
            fail()
            return
        }

        val nativeAdView = UnifiedNativeAdView(context)
        nativeAdView.addView(View.inflate(context, option.nativeLayoutResId, null))
        nativeAdView.findViewById<FrameLayout>(R.id.frameLayout_media).visibleOrGone(option.isMedia)
        addView(nativeAdView)

        AdLoader
            .Builder(context, option.admobKey)
            .forUnifiedNativeAd { unifiedNativeAd ->
                populateUnifiedNativeAdView(unifiedNativeAd, nativeAdView)
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(ADCHOICES_BOTTOM_LEFT)
                    .build()
            )
            .withAdListener(object : AdListener() {

                override fun onAdFailedToLoad(p0: Int) {
                    super.onAdFailedToLoad(p0)
                    log("onAdFailedToLoad is called")
                    fail()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    log("onAdLoaded is called")
                }
            })
            .build()
            .loadAd(
                AdRequest.Builder()
                    .apply { if (BuildConfig.DEBUG) addTestDevice(AdHelper.ADMOB_TEST_DEVICE) }
                    .build()
            )
    }

    private fun populateUnifiedNativeAdView(adItem: UnifiedNativeAd, nativeAdView: UnifiedNativeAdView) {
        nativeAdView.findViewById<FrameLayout>(R.id.frameLayout_loading).gone()
        nativeAdView.findViewById<ConstraintLayout>(R.id.constraintLayout_ad).visible()

        if (null != adItem.icon) {
            val iconView = nativeAdView.findViewById<ImageView>(R.id.imageView_adIcon)
            iconView.setImageDrawable(adItem.icon.drawable)
            nativeAdView.iconView = iconView
        }
        else {
            nativeAdView.findViewById<CardView>(R.id.cardView_adIcon).gone()
        }

        val headlineView = nativeAdView.findViewById<TextView>(R.id.textView_title)
        headlineView.text = adItem.headline
        nativeAdView.headlineView = headlineView

        val bodyView = nativeAdView.findViewById<TextView>(R.id.textView_socialContext)
        bodyView.text = adItem.body
        nativeAdView.bodyView = bodyView

        val callToAction = nativeAdView.findViewById<TextView>(R.id.textView_callToAction)
        callToAction.text = adItem.callToAction
        nativeAdView.callToActionView = callToAction

        if (null != adItem.adChoicesInfo) {
            val adChoiceImageView = nativeAdView.findViewById<ImageView>(R.id.imageView_adChoice)
            adChoiceImageView.setImageDrawable(adItem.adChoicesInfo.images[0].drawable)
        }

        val sponsorTextView = nativeAdView.findViewById<TextView>(R.id.textView_sponsored)
        sponsorTextView.text = "Sponsored by ${adItem.advertiser}"
        nativeAdView.advertiserView = sponsorTextView

        if (option.isMedia) {
            val mediaFrame = nativeAdView.findViewById<FrameLayout>(R.id.frameLayout_media)
            val mediaView = MediaView(context).apply {
                setMediaContent(adItem.mediaContent)
                setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                nativeAdView.mediaView = this
            }
            mediaFrame.addView(mediaView)
        }

        nativeAdView.setNativeAd(adItem)
    }

}
