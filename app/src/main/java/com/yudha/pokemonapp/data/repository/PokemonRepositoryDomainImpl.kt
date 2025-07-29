package com.yudha.pokemonapp.data.repository

import com.yudha.pokemonapp.data.mapper.PokemonMapper
import com.yudha.pokemonapp.network.PokemonApiService
import com.yudha.pokemonapp.domain.entity.Pokemon
import com.yudha.pokemonapp.domain.entity.PokemonId
import com.yudha.pokemonapp.domain.entity.PokemonName
import com.yudha.pokemonapp.domain.repository.PokemonRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of PokemonRepository for the domain layer
 * Handles API calls and converts between API response and domain entities
 */
@Singleton
class PokemonRepositoryDomainImpl @Inject constructor(
    private val pokemonApiService: PokemonApiService
) : PokemonRepository {
    
    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>> {
        return try {
            val response = pokemonApiService.getPokemonList(limit, offset)
            
            if (response.isSuccessful && response.body() != null) {
                // Get detailed information for each Pokemon
                val pokemonDetails = response.body()!!.results.map { pokemonInfo ->
                    val detailResponse = pokemonApiService.getPokemonDetailByName(pokemonInfo.name)
                    if (detailResponse.isSuccessful && detailResponse.body() != null) {
                        PokemonMapper.toDomain(detailResponse.body()!!)
                    } else {
                        throw Exception("Failed to get Pokemon detail for ${pokemonInfo.name}")
                    }
                }
                Result.success(pokemonDetails)
            } else {
                Result.failure(Exception("Failed to get Pokemon list: ${response.message()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getPokemonById(pokemonId: PokemonId): Result<Pokemon> {
        return try {
            val response = pokemonApiService.getPokemonDetail(pokemonId.value)
            if (response.isSuccessful && response.body() != null) {
                val domainPokemon = PokemonMapper.toDomain(response.body()!!)
                Result.success(domainPokemon)
            } else {
                Result.failure(Exception("Failed to get Pokemon by ID: ${response.message()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getPokemonByName(pokemonName: PokemonName): Result<Pokemon> {
        return try {
            val response = pokemonApiService.getPokemonDetailByName(pokemonName.value)
            if (response.isSuccessful && response.body() != null) {
                val domainPokemon = PokemonMapper.toDomain(response.body()!!)
                Result.success(domainPokemon)
            } else {
                Result.failure(Exception("Failed to get Pokemon by name: ${response.message()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun searchPokemon(query: String): Result<List<Pokemon>> {
        return try {
            // For now, we'll implement a simple search by getting all Pokemon
            // and filtering by name. In a real app, you might want to implement
            // server-side search or use a local database for better performance.
            val response = pokemonApiService.getPokemonList(1000, 0)
            
            if (response.isSuccessful && response.body() != null) {
                val filteredPokemon = response.body()!!.results
                    .filter { it.name.contains(query, ignoreCase = true) }
                    .take(20) // Limit results
                    .map { pokemonInfo ->
                        val detailResponse = pokemonApiService.getPokemonDetailByName(pokemonInfo.name)
                        if (detailResponse.isSuccessful && detailResponse.body() != null) {
                            PokemonMapper.toDomain(detailResponse.body()!!)
                        } else {
                            throw Exception("Failed to get Pokemon detail for ${pokemonInfo.name}")
                        }
                    }
                
                Result.success(filteredPokemon)
            } else {
                Result.failure(Exception("Failed to search Pokemon: ${response.message()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getFavoritePokemon(): Result<List<Pokemon>> {
        // TODO: Implement favorites functionality with local database
        return Result.success(emptyList())
    }
    
    override suspend fun addToFavorites(pokemonId: PokemonId): Result<Unit> {
        // TODO: Implement favorites functionality with local database
        return Result.success(Unit)
    }
    
    override suspend fun removeFromFavorites(pokemonId: PokemonId): Result<Unit> {
        // TODO: Implement favorites functionality with local database
        return Result.success(Unit)
    }
    
    override suspend fun isFavorite(pokemonId: PokemonId): Boolean {
        // TODO: Implement favorites functionality with local database
        return false
    }
}