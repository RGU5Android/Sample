package com.rgu5android.sample.di.module

import com.rgu5android.sample.ui.detail.CountryProvinceListFragment
import com.rgu5android.sample.ui.list.CountryListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeCountryListFragment(): CountryListFragment

    @ContributesAndroidInjector
    abstract fun contributeCountryDetailFragment(): CountryProvinceListFragment
}