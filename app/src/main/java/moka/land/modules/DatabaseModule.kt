package moka.land.modules

import moka.land.model._Database
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module

val roomModule = module {

    single { _Database.getInstance(androidApplication()) }

    single(createOnStart = false) { get<_Database>().getUserDao() }

}
