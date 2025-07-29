package com.yudha.pokemonapp.di

import com.yudha.pokemonapp.data.repository.UserRepositoryImpl
import com.yudha.pokemonapp.data.repository.PokemonRepositoryDomainImpl
import com.yudha.pokemonapp.domain.repository.UserRepository
import com.yudha.pokemonapp.domain.repository.PokemonRepository as DomainPokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for domain layer dependencies
 * Binds domain repository interfaces to their implementations
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindPokemonRepository(
        pokemonRepositoryImpl: PokemonRepositoryDomainImpl
    ): DomainPokemonRepository
}