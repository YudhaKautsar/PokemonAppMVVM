package com.yudha.pokemonapp.di

import android.content.Context
import androidx.room.Room
import com.yudha.pokemonapp.data.dao.PokemonDao
import com.yudha.pokemonapp.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun providePokemonDao(database: AppDatabase): PokemonDao {
        return database.pokemonDao()
    }
}