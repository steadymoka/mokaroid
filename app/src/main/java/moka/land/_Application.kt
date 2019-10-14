package moka.land

import android.app.Application
import moka.land.modules.roomModule
import moka.land.modules.viewModelModule
import org.koin.android.ext.android.startKoin

class _Application : Application() {

    override fun onCreate() {
        super.onCreate()

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
