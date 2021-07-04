package com.rgu5android.sample.di.module

import com.rgu5android.sample.data.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class ApiServiceModule {

	@Provides
	@Singleton
	fun provideRetrofitApiService(retrofit: Retrofit): ApiService {
		return retrofit.create(ApiService::class.java)
	}
}
