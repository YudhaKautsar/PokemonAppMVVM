package com.yudha.pokemonapp.di

import com.yudha.pokemonapp.data.remote.network.ApiClient
import com.yudha.pokemonapp.data.remote.network.AppApi
import com.yudha.pokemonapp.data.remote.api.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return ApiClient.create()
    }

    @Provides
    @Singleton
    fun provideAppApi(retrofit: Retrofit): AppApi {
        return AppApi(retrofit)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(appApi: AppApi): AuthApiService {
        return appApi.authApiService
    }
}