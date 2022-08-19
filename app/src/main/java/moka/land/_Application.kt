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
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

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
        AdHelper.setTestDevice {
            ADMOB = "1EBE23DC3B505A08F0AE45822B6B28D1"
            AUDIENCE = "7e79f087-78e2-4aeb-a70a-d0299c9cad33"
        }

        /* Koin */
        startKoin {
            androidContext(this@_Application)
            modules(
                viewModelModule,
                roomModule,
                networkModule
            )
        }

        /* Stetho */
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

}
