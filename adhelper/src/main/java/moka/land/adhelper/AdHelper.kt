package moka.land.adhelper

import android.content.Context
import com.facebook.ads.*
import com.google.android.gms.ads.MobileAds
import moka.land.base.BuildConfig.DEBUG


object AdHelper {

    var ADMOB_TEST_DEVICE: String = ""

    fun initialize(context: Context) {
        MobileAds.initialize(context)

        if (DEBUG) {
            AdSettings.turnOnSDKDebugger(context)
            AdSettings.setDebugBuild(true)
        }
        AudienceNetworkAds
            .buildInitSettings(context)
            .initialize()
    }

}
