package com.yudha.pokemonapp.domain.usecase.auth

import com.yudha.pokemonapp.domain.entity.User
import com.yudha.pokemonapp.domain.entity.Username
import com.yudha.pokemonapp.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Use Case for user login
 * Encapsulates the business logic for user authentication
 */
class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    suspend operator fun invoke(username: String, password: String): Result<User> {
        return try {
            // Validate input
            if (username.isBlank()) {
                return Result.failure(IllegalArgumentException("Username cannot be empty"))
            }
            
            if (password.isBlank()) {
                return Result.failure(IllegalArgumentException("Password cannot be empty"))
            }
            
            if (password.length < 6) {
                return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
            }
            
            // Create domain value object
            val usernameVO = Username(username)
            
            // Authenticate user
            userRepository.authenticate(usernameVO, password)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}