package com.yudha.pokemonapp.di

import com.yudha.pokemonapp.domain.repository.UserRepository
import com.yudha.pokemonapp.domain.repository.PokemonRepository as DomainPokemonRepository
import com.yudha.pokemonapp.domain.usecase.auth.LoginUseCase
import com.yudha.pokemonapp.domain.usecase.auth.LogoutUseCase
import com.yudha.pokemonapp.domain.usecase.auth.RegisterUseCase
import com.yudha.pokemonapp.domain.usecase.pokemon.GetPokemonListUseCase
import com.yudha.pokemonapp.domain.usecase.pokemon.GetPokemonDetailUseCase
import com.yudha.pokemonapp.domain.usecase.pokemon.SearchPokemonUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for use case dependencies
 * Provides use case instances for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    @Provides
    @Singleton
    fun provideLoginUseCase(
        userRepository: UserRepository
    ): LoginUseCase {
        return LoginUseCase(userRepository)
    }
    
    @Provides
    @Singleton
    fun provideRegisterUseCase(
        userRepository: UserRepository
    ): RegisterUseCase {
        return RegisterUseCase(userRepository)
    }
    
    @Provides
    @Singleton
    fun provideLogoutUseCase(
        userRepository: UserRepository
    ): LogoutUseCase {
        return LogoutUseCase(userRepository)
    }
    
    @Provides
    @Singleton
    fun provideGetPokemonListUseCase(
        pokemonRepository: DomainPokemonRepository
    ): GetPokemonListUseCase {
        return GetPokemonListUseCase(pokemonRepository)
    }
    
    @Provides
    @Singleton
    fun provideGetPokemonDetailUseCase(
        pokemonRepository: DomainPokemonRepository
    ): GetPokemonDetailUseCase {
        return GetPokemonDetailUseCase(pokemonRepository)
    }
    
    @Provides
    @Singleton
    fun provideSearchPokemonUseCase(
        pokemonRepository: DomainPokemonRepository
    ): SearchPokemonUseCase {
        return SearchPokemonUseCase(pokemonRepository)
    }
}