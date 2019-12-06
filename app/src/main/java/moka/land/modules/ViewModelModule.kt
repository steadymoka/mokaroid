package moka.land.modules

import moka.land.ui.main.MainViewModel
import moka.land.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { ProfileViewModel() }
}
