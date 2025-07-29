package com.yudha.pokemonapp.network

import com.yudha.pokemonapp.data.model.LoginRequest
import com.yudha.pokemonapp.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}