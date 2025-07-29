package com.yudha.pokemonapp.data.model

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)