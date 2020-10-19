package com.duckbuddyy.nasarest.di

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.duckbuddyy.nasarest.api.NasaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(NasaApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideNasaApi(retrofit: Retrofit):NasaApi =
        retrofit.create(NasaApi::class.java)

}