package moka.land.adhelper

import android.content.Context
import com.facebook.ads.*
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import moka.land.base.BuildConfig.DEBUG
import moka.land.base.log
import java.util.*


object AdHelper {

    var testDevice: TestDevice? = null

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

    data class TestDevice(
        var ADMOB: String? = null,
        var AUDIENCE: String? = null
    )

    fun setTestDevice(setTestDevice: TestDevice.() -> Unit) {
        testDevice = TestDevice().apply(setTestDevice)

        val configuration = RequestConfiguration
            .Builder()
            .setTestDeviceIds(listOf(testDevice?.ADMOB ?: ""))
            .build()
        MobileAds.setRequestConfiguration(configuration)

        AdSettings.addTestDevice(testDevice?.AUDIENCE ?: "")
    }

}
