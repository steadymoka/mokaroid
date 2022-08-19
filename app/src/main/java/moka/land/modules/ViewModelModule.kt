package moka.land.modules

import moka.land.ui.main.MainViewModel
import moka.land.ui.profile.ProfileViewModel
import moka.land.ui.repository.RepositoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { ProfileViewModel(get()) }
    viewModel { RepositoryViewModel(get()) }
}
