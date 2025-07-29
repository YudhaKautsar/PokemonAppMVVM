package com.yudha.pokemonapp.data.repository

import com.yudha.pokemonapp.data.model.PokemonListResponse
import com.yudha.pokemonapp.network.AppApi
import com.yudha.pokemonapp.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository {
    
    private val apiService = AppApi.pokemon
    
    suspend fun getPokemonList(limit: Int = 10, offset: Int = 0): Result<PokemonListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonList(limit, offset)
                if (response.isSuccessful) {
                    response.body()?.let { pokemonList ->
                        Result.Success(pokemonList)
                    } ?: Result.Error(Exception("Empty response"))
                } else {
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
    
    suspend fun searchPokemon(query: String, limit: Int = 10, offset: Int = 0): Result<PokemonListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonList(limit, offset)
                if (response.isSuccessful) {
                    response.body()?.let { pokemonList ->
                        val filteredResults = pokemonList.results.filter { 
                            it.name.contains(query, ignoreCase = true) 
                        }
                        val filteredResponse = pokemonList.copy(results = filteredResults)
                        Result.Success(filteredResponse)
                    } ?: Result.Error(Exception("Empty response"))
                } else {
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}