package com.yudha.pokemonapp.domain.usecase.pokemon

import com.yudha.pokemonapp.domain.entity.Pokemon
import com.yudha.pokemonapp.domain.repository.PokemonRepository
import javax.inject.Inject

/**
 * Use Case for getting Pokemon list
 * Encapsulates the business logic for retrieving Pokemon list with pagination
 */
class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    
    suspend operator fun invoke(
        limit: Int = DEFAULT_LIMIT,
        offset: Int = 0
    ): Result<List<Pokemon>> {
        return try {
            // Validate input
            if (limit <= 0) {
                return Result.failure(IllegalArgumentException("Limit must be positive"))
            }
            
            if (offset < 0) {
                return Result.failure(IllegalArgumentException("Offset cannot be negative"))
            }
            
            if (limit > MAX_LIMIT) {
                return Result.failure(IllegalArgumentException("Limit cannot exceed $MAX_LIMIT"))
            }
            
            // Get Pokemon list
            pokemonRepository.getPokemonList(limit, offset)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    companion object {
        private const val DEFAULT_LIMIT = 20
        private const val MAX_LIMIT = 100
    }
}