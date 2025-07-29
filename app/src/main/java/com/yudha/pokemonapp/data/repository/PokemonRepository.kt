package com.yudha.pokemonapp.data.repository

import android.content.Context
import com.yudha.pokemonapp.data.dao.PokemonDao
import com.yudha.pokemonapp.data.local.entity.PokemonEntity
import com.yudha.pokemonapp.data.model.PokemonListResponse
import com.yudha.pokemonapp.data.model.PokemonResult
import com.yudha.pokemonapp.data.model.PokemonDetail
import com.yudha.pokemonapp.network.PokemonApiService
import com.yudha.pokemonapp.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(
    private val context: Context,
    private val apiService: PokemonApiService,
    private val pokemonDao: PokemonDao
) {
    
    suspend fun getPokemonList(limit: Int = 10, offset: Int = 0): Result<PokemonListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Coba ambil dari cache lokal dulu
                val cachedPokemon = pokemonDao.getPokemonList(limit, offset)
                
                if (cachedPokemon.isNotEmpty()) {
                    // Jika ada data di cache, return data cache
                    val pokemonResults = cachedPokemon.map { entity ->
                        PokemonResult(
                            name = entity.name,
                            url = "https://pokeapi.co/api/v2/pokemon/${entity.id}/"
                        )
                    }
                    val response = PokemonListResponse(
                        count = pokemonDao.getPokemonCount(),
                        next = null,
                        previous = null,
                        results = pokemonResults
                    )
                    return@withContext Result.Success(response)
                }
                
                // Jika cache kosong, coba ambil dari API
                val response = apiService.getPokemonList(limit, offset)
                if (response.isSuccessful) {
                    response.body()?.let { pokemonList ->
                        // Simpan ke cache
                        val pokemonEntities = pokemonList.results.mapIndexed { index, pokemon ->
                            val pokemonId = offset + index + 1
                            PokemonEntity(
                                id = pokemonId,
                                name = pokemon.name,
                                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemonId}.png",
                                offset = offset
                            )
                        }
                        pokemonDao.insertPokemon(pokemonEntities)
                        Result.Success(pokemonList)
                    } ?: Result.Error(Exception("Empty response"))
                } else {
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                // Jika API gagal, coba ambil dari cache sebagai fallback
                try {
                    val cachedPokemon = pokemonDao.getPokemonList(limit, offset)
                    if (cachedPokemon.isNotEmpty()) {
                        val pokemonResults = cachedPokemon.map { entity ->
                            PokemonResult(
                                name = entity.name,
                                url = "https://pokeapi.co/api/v2/pokemon/${entity.id}/"
                            )
                        }
                        val response = PokemonListResponse(
                            count = pokemonDao.getPokemonCount(),
                            next = null,
                            previous = null,
                            results = pokemonResults
                        )
                        Result.Success(response)
                    } else {
                        Result.Error(e)
                    }
                } catch (cacheException: Exception) {
                    Result.Error(e)
                }
            }
        }
    }
    
    suspend fun searchPokemon(query: String): Result<PokemonListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Cari di cache lokal dulu
                val cachedPokemon = pokemonDao.searchPokemon(query)
                
                if (cachedPokemon.isNotEmpty()) {
                    // Jika ada hasil di cache, return data cache
                    val pokemonResults = cachedPokemon.map { entity ->
                        PokemonResult(
                            name = entity.name,
                            url = "https://pokeapi.co/api/v2/pokemon/${entity.id}/"
                        )
                    }
                    val response = PokemonListResponse(
                        count = cachedPokemon.size,
                        next = null,
                        previous = null,
                        results = pokemonResults
                    )
                    return@withContext Result.Success(response)
                }
                
                // Jika tidak ada di cache, coba dari API (hanya jika ada internet)
                try {
                    val response = apiService.getPokemonList(1000, 0) // Ambil banyak untuk search
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
                } catch (apiException: Exception) {
                    // Jika API gagal, return empty result
                    val emptyResponse = PokemonListResponse(
                        count = 0,
                        next = null,
                        previous = null,
                        results = emptyList()
                    )
                    Result.Success(emptyResponse)
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
    
    suspend fun getPokemonDetail(id: Int): Response<PokemonDetail> {
        return withContext(Dispatchers.IO) {
            apiService.getPokemonDetail(id)
        }
    }
    
    suspend fun getPokemonDetailByName(name: String): Response<PokemonDetail> {
        return withContext(Dispatchers.IO) {
            apiService.getPokemonDetailByName(name)
        }
    }
}