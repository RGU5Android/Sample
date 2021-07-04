package com.rgu5android.sample.di.component

import android.app.Application
import com.rgu5android.sample.MainApplication
import com.rgu5android.sample.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityBuilderModule::class,
        AndroidSupportInjectionModule::class,
        ApiServiceModule::class,
        FragmentBuilderModule::class,
        NetworkModule::class,
        ViewModelBuilderModule::class
    ]
)
interface AppComponent : AndroidInjector<MainApplication> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun create(application: Application): Builder

        fun build(): AppComponent
    }
}