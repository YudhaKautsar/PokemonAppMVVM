package com.yudha.pokemonapp.domain.usecase.pokemon

import com.yudha.pokemonapp.domain.entity.Pokemon
import com.yudha.pokemonapp.domain.repository.PokemonRepository
import javax.inject.Inject

class SearchPokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(query: String): Result<List<Pokemon>> {
        return if (query.isBlank()) {
            Result.failure(IllegalArgumentException("Search query cannot be empty"))
        } else {
            pokemonRepository.searchPokemon(query)
        }
    }
}