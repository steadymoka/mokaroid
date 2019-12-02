package moka.land.adhelper


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
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
        var media: Boolean = true
        var period: Period = Period.ADMOB_FACEBOOK
        var nativeLayoutResId: Int = R.layout.mk_view_moka_ad_native
    }

    inner class Runner {
        fun showNative(callback: ((isSuccess: Boolean) -> Unit)? = null) {
            this@NativeAdView.callback = callback
            showAdNative()
        }
    }

    private lateinit var option: Option
    private var callback: ((isSuccess: Boolean) -> Unit)? = null

    private var audienceNativeAd: NativeAd? = null
    private var admobNativeAdView: UnifiedNativeAdView? = null

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
        admobNativeAdView?.destroy()
        audienceNativeAd?.destroy()
    }

    fun showError() {

    }

    private fun loadFBAudienceNativeAd(fail: () -> Unit) {
        log("=== Audience's native ad is loading")
        if (option.fbAudienceKey.isNullOrEmpty()) {
            log("=== Audience's native ad key is empty")
            fail()
            return
        }
        View.inflate(context, option.nativeLayoutResId, this)
        findViewById<FrameLayout>(R.id.frameLayout_media).visibleOrGone(option.media)

        audienceNativeAd = NativeAd(context, option.fbAudienceKey)
        audienceNativeAd!!.setAdListener(object : NativeAdListener {
            override fun onMediaDownloaded(p0: Ad?) {
            }

            override fun onAdLoaded(ad: Ad) {
                if (ad != audienceNativeAd) {
                    return
                }

                inflateNativeAdViews(audienceNativeAd!!)
                callback?.invoke(true)
            }

            override fun onError(ad: Ad?, error: AdError?) {
                log("=== Audience's ad failed to load / ${error?.errorCode} / ${error?.errorMessage}")
                fail()
            }

            override fun onAdClicked(p0: Ad?) {
            }

            override fun onLoggingImpression(p0: Ad?) {
            }
        })
        audienceNativeAd!!.loadAd(NativeAdBase.MediaCacheFlag.ALL)
    }

    private fun inflateNativeAdViews(facebookNativeAd: NativeAd) {
        // layout setting
        val relativeLayoutContainer = findViewById<ConstraintLayout>(R.id.constraintLayout_ad)
        relativeLayoutContainer.visible()

        // Add the AdChoices icon
        findViewById<FrameLayout>(R.id.frameLayout_adChoice).apply {
            addView(AdOptionsView(context, facebookNativeAd, null).apply {
                scaleX = 0.7f
                scaleY = 0.7f
                translationX = dip(-9).toFloat()
            })
        }

        // Ad thumbnail Icon
        findViewById<CardView>(R.id.cardView_adIcon).apply {
            updateLayoutParams { translationY = dip(-4).toFloat() }
        }

        // Setting the Text
        findViewById<TextView>(R.id.textView_sponsored).apply {
            text = "Sponsored by ${facebookNativeAd.advertiserName}"
            updateLayoutParams { translationX = dip(-16).toFloat() }
        }
        val textView_title = findViewById<TextView>(R.id.textView_title).apply {
            text = facebookNativeAd.adSocialContext
            updateLayoutParams { translationY = dip(-4).toFloat() }
        }
        findViewById<TextView>(R.id.textView_socialContext).apply {
            text = facebookNativeAd.adBodyText
            updateLayoutParams { translationY = dip(-4).toFloat() }
        }
        val textView_callToAction = findViewById<TextView>(R.id.textView_callToAction).apply {
            text = facebookNativeAd.adCallToAction
        }

        // media view
        val mediaView = com.facebook.ads.MediaView(context)
        if (option.media) {
            val mediaFrame = findViewById<FrameLayout>(R.id.frameLayout_media)
            mediaFrame.updateLayoutParams<ConstraintLayout.LayoutParams> { height = dip(160) }
            mediaFrame.visible()
            mediaFrame.addView(mediaView)
        }

        // Register the Title and CTA button to listen for clicks.
        val clickableViews: ArrayList<View> = ArrayList()
        clickableViews.add(mediaView)
        clickableViews.add(textView_title)
        clickableViews.add(textView_callToAction)

        facebookNativeAd.registerViewForInteraction(
            relativeLayoutContainer,
            mediaView,
            findViewById<ImageView>(R.id.imageView_adIcon),
            clickableViews)
    }

    private fun loadAdmobNativeAd(fail: () -> Unit) {
        log("=== Admob's native ad is loading")
        if (option.admobKey.isNullOrEmpty()) {
            log("=== Admob's key is empty")
            fail()
            return
        }

        admobNativeAdView = UnifiedNativeAdView(context)
        admobNativeAdView!!.addView(View.inflate(context, option.nativeLayoutResId, null))
        admobNativeAdView!!.findViewById<FrameLayout>(R.id.frameLayout_media).visibleOrGone(option.media)
        addView(admobNativeAdView)

        AdLoader
            .Builder(context, option.admobKey)
            .forUnifiedNativeAd { unifiedNativeAd ->
                populateUnifiedNativeAdView(unifiedNativeAd, admobNativeAdView!!)
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(ADCHOICES_BOTTOM_LEFT)
                    .build()
            )
            .withAdListener(object : AdListener() {

                override fun onAdFailedToLoad(fail: Int) {
                    super.onAdFailedToLoad(fail)
                    log("=== Admob's ad failed to load / ${fail}")
                    fail()
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
        nativeAdView.findViewById<ConstraintLayout>(R.id.constraintLayout_ad).visible()

        // Add the AdChoices icon
        if (null != adItem.adChoicesInfo) {
            val adChoiceImageView = nativeAdView.findViewById<ImageView>(R.id.imageView_adChoice)
            adChoiceImageView.setImageDrawable(adItem.adChoicesInfo.images[0].drawable)
        }

        // Ad thumbnail Icon
        if (null != adItem.icon) {
            val iconView = nativeAdView.findViewById<ImageView>(R.id.imageView_adIcon)
            iconView.setImageDrawable(adItem.icon.drawable)
            nativeAdView.iconView = iconView
        }
        else {
            nativeAdView.findViewById<CardView>(R.id.cardView_adIcon).gone()
        }

        // Setting the Text
        val headlineView = nativeAdView.findViewById<TextView>(R.id.textView_title)
        headlineView.text = adItem.headline
        nativeAdView.headlineView = headlineView

        val bodyView = nativeAdView.findViewById<TextView>(R.id.textView_socialContext)
        bodyView.text = adItem.body
        nativeAdView.bodyView = bodyView

        val sponsorTextView = nativeAdView.findViewById<TextView>(R.id.textView_sponsored)
        sponsorTextView.text = "Sponsored by ${adItem.advertiser}"
        nativeAdView.advertiserView = sponsorTextView

        val callToAction = nativeAdView.findViewById<TextView>(R.id.textView_callToAction)
        callToAction.text = adItem.callToAction
        nativeAdView.callToActionView = callToAction

        // media
        if (option.media) {
            val mediaView = MediaView(context).apply {
                setMediaContent(adItem.mediaContent)
                setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                nativeAdView.mediaView = this
            }

            val mediaFrame = nativeAdView.findViewById<FrameLayout>(R.id.frameLayout_media)
            mediaFrame.updateLayoutParams<ConstraintLayout.LayoutParams> { height = dip(160) }
            mediaFrame.visible()
            mediaFrame.addView(mediaView)
        }

        nativeAdView.setNativeAd(adItem)
    }

}
