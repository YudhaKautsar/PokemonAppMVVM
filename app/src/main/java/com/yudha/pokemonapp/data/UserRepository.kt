package com.yudha.pokemonapp.data

import com.auth0.android.jwt.JWT
import com.yudha.pokemonapp.data.local.UserPreferences
import com.yudha.pokemonapp.data.local.dao.PokemonDao
import com.yudha.pokemonapp.data.local.entity.PokemonEntity
import com.yudha.pokemonapp.data.model.LoginRequest
import com.yudha.pokemonapp.network.AuthApiService
import kotlinx.coroutines.flow.first

class UserRepository(
    private val apiService: AuthApiService,
    private val userPreferences: UserPreferences,
    private val pokemonDao: PokemonDao // Contoh jika ingin menyimpan data user ke db
) {

    val authToken = userPreferences.authToken

    suspend fun login(request: LoginRequest) {
        val response = apiService.login(request)
        if (response.isSuccessful) {
            val token = response.body()?.token
            if (token != null) {
                userPreferences.saveAuthToken(token)
                decodeAndSaveUserData(token)
            }
        } else {
            throw Exception("Login failed")
        }
    }

    private suspend fun decodeAndSaveUserData(token: String) {
        val jwt = JWT(token)
        val userId = jwt.getClaim("user_id").asInt()
        val name = jwt.getClaim("name").asString()
        val imageUrl = jwt.getClaim("image_url").asString()

        if (userId != null && name != null && imageUrl != null) {
            val userEntity = PokemonEntity(userId, name, imageUrl) // Menggunakan PokemonEntity sebagai contoh
            pokemonDao.insertPokemon(userEntity)
        }
    }
}