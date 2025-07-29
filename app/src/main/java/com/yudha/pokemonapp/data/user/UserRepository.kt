package com.yudha.pokemonapp.data.user

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val userPreferences: UserPreferences,
// ... existing code ...