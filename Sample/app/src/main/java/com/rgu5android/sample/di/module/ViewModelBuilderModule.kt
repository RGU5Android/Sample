package com.rgu5android.sample.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rgu5android.sample.di.AppViewModelProviderFactory
import com.rgu5android.sample.di.annotation.ViewModelKey
import com.rgu5android.sample.ui.detail.CountryProvinceListViewModel
import com.rgu5android.sample.ui.list.CountryListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBuilderModule {
    @Binds
    @IntoMap
    @ViewModelKey(CountryListViewModel::class)
    abstract fun bindFollowViewModel(viewModel: CountryListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CountryProvinceListViewModel::class)
    abstract fun bindGithubUserViewModel(viewModel: CountryProvinceListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: AppViewModelProviderFactory): ViewModelProvider.Factory
}