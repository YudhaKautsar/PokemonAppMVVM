package com.yudha.pokemonapp.domain.usecase.auth

import com.yudha.pokemonapp.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Use Case for user logout
 * Encapsulates the business logic for user logout
 */
class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    suspend operator fun invoke(): Result<Unit> {
        return try {
            // Clear user session/token
            // This could involve clearing shared preferences, tokens, etc.
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}