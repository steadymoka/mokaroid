package moka.land

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import moka.land.adhelper.AdHelper
import moka.land.base.DEBUG
import moka.land.modules.networkModule
import moka.land.modules.roomModule
import moka.land.modules.viewModelModule
import org.koin.android.ext.android.startKoin

@SuppressLint("StaticFieldLeak")
class _Application : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        DEBUG = BuildConfig.DEBUG

        AdHelper.initialize(this)
        AdHelper.ADMOB_TEST_DEVICE = "1EBE23DC3B505A08F0AE45822B6B28D1"

        /* Koin */
        startKoin(
            androidContext = this,
            modules = listOf(
                viewModelModule,
                roomModule,
                networkModule
            )
        )

        /* Stetho */
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

}
