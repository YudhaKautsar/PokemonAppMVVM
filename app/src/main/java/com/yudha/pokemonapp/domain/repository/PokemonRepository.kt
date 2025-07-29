package com.yudha.pokemonapp.domain.repository

import com.yudha.pokemonapp.domain.entity.Pokemon
import com.yudha.pokemonapp.domain.entity.PokemonId
import com.yudha.pokemonapp.domain.entity.PokemonName

/**
 * Domain Pokemon Repository Interface
 * Defines the contract for Pokemon data operations in the domain layer
 */
interface PokemonRepository {
    
    /**
     * Get list of Pokemon with pagination
     */
    suspend fun getPokemonList(
        limit: Int = 20,
        offset: Int = 0
    ): Result<List<Pokemon>>
    
    /**
     * Get Pokemon by ID
     */
    suspend fun getPokemonById(pokemonId: PokemonId): Result<Pokemon>
    
    /**
     * Get Pokemon by name
     */
    suspend fun getPokemonByName(pokemonName: PokemonName): Result<Pokemon>
    
    /**
     * Search Pokemon by name pattern
     */
    suspend fun searchPokemon(query: String): Result<List<Pokemon>>
    
    /**
     * Get favorite Pokemon list
     */
    suspend fun getFavoritePokemon(): Result<List<Pokemon>>
    
    /**
     * Add Pokemon to favorites
     */
    suspend fun addToFavorites(pokemonId: PokemonId): Result<Unit>
    
    /**
     * Remove Pokemon from favorites
     */
    suspend fun removeFromFavorites(pokemonId: PokemonId): Result<Unit>
    
    /**
     * Check if Pokemon is in favorites
     */
    suspend fun isFavorite(pokemonId: PokemonId): Boolean
}