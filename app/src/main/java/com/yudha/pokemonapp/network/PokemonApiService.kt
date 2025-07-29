package com.yudha.pokemonapp.network

import com.yudha.pokemonapp.data.model.PokemonListResponse // Ganti dengan model data Anda
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonListResponse> // Ganti dengan model data Anda

    // Tambahkan endpoint lain di sini
}