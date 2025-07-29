package com.yudha.pokemonapp.di

import android.content.Context
import androidx.room.Room
import com.yudha.pokemonapp.data.local.db.AppDatabase
import com.yudha.pokemonapp.data.local.db.PokemonDao
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
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "pokemon_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(appDatabase: AppDatabase): PokemonDao {
        return appDatabase.pokemonDao()
    }
}