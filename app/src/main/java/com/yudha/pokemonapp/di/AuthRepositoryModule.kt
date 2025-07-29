package com.yudha.pokemonapp.di

import android.content.Context
import com.yudha.pokemonapp.data.database.AppDatabase
import com.yudha.pokemonapp.data.repository.AuthRepository
import com.yudha.pokemonapp.data.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {
    
    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepository(userDao, context)
    }
}