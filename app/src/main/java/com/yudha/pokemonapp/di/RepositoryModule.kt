package com.yudha.pokemonapp.di

import android.content.Context
import com.yudha.pokemonapp.data.dao.PokemonDao
import com.yudha.pokemonapp.network.PokemonApiService
import com.yudha.pokemonapp.data.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePokemonRepository(
        @ApplicationContext context: Context,
        apiService: PokemonApiService,
        pokemonDao: PokemonDao
    ): PokemonRepository {
        return PokemonRepository(context, apiService, pokemonDao)
    }
}