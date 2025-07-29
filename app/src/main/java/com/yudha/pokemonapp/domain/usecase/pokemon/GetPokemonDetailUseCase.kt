package com.yudha.pokemonapp.domain.usecase.pokemon

import com.yudha.pokemonapp.domain.entity.Pokemon
import com.yudha.pokemonapp.domain.entity.PokemonId
import com.yudha.pokemonapp.domain.repository.PokemonRepository
import javax.inject.Inject

/**
 * Use Case for getting Pokemon detail
 * Encapsulates the business logic for retrieving detailed Pokemon information
 */
class GetPokemonDetailUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    
    suspend operator fun invoke(pokemonId: Int): Result<Pokemon> {
        return try {
            // Validate input
            if (pokemonId <= 0) {
                return Result.failure(IllegalArgumentException("Pokemon ID must be positive"))
            }
            
            // Create domain value object
            val pokemonIdVO = PokemonId(pokemonId)
            
            // Get Pokemon detail
            pokemonRepository.getPokemonById(pokemonIdVO)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend operator fun invoke(pokemonName: String): Result<Pokemon> {
        return try {
            // Validate input
            if (pokemonName.isBlank()) {
                return Result.failure(IllegalArgumentException("Pokemon name cannot be empty"))
            }
            
            // Create domain value object
            val pokemonNameVO = com.yudha.pokemonapp.domain.entity.PokemonName(pokemonName)
            
            // Get Pokemon detail
            pokemonRepository.getPokemonByName(pokemonNameVO)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}