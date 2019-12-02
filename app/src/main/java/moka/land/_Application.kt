package moka.land

import android.app.Application
import moka.land.adhelper.AdHelper
import moka.land.base.DEBUG
import moka.land.modules.roomModule
import moka.land.modules.viewModelModule
import org.koin.android.ext.android.startKoin

class _Application : Application() {

    override fun onCreate() {
        super.onCreate()
        DEBUG = BuildConfig.DEBUG

        AdHelper.initialize(this)
        AdHelper.ADMOB_TEST_DEVICE = "1EBE23DC3B505A08F0AE45822B6B28D1"

        /* Koin */
        startKoin(
            androidContext = this,
            modules = listOf(
                viewModelModule,
                roomModule
            )
        )
    }

}
