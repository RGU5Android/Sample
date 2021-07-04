package com.rgu5android.sample.di.module

import com.rgu5android.sample.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
	@ContributesAndroidInjector
	abstract fun contributeNavigationActivity(): MainActivity
}