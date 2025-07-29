package com.yudha.pokemonapp.domain.usecase.auth

import com.yudha.pokemonapp.domain.entity.User
import com.yudha.pokemonapp.domain.entity.Username
import com.yudha.pokemonapp.domain.entity.Email
import com.yudha.pokemonapp.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Use Case for user registration
 * Encapsulates the business logic for user registration
 */
class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<User> {
        return try {
            // Validate input
            validateInput(username, email, password, confirmPassword)
            
            // Create domain value objects
            val usernameVO = Username(username)
            val emailVO = Email(email)
            
            // Check if username already exists
            if (userRepository.isUsernameExists(usernameVO)) {
                return Result.failure(IllegalArgumentException("Username already exists"))
            }
            
            // Check if email already exists
            if (userRepository.isEmailExists(emailVO)) {
                return Result.failure(IllegalArgumentException("Email already exists"))
            }
            
            // Register user
            userRepository.register(usernameVO, emailVO, password)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun validateInput(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (username.isBlank()) {
            throw IllegalArgumentException("Username cannot be empty")
        }
        
        if (email.isBlank()) {
            throw IllegalArgumentException("Email cannot be empty")
        }
        
        if (password.isBlank()) {
            throw IllegalArgumentException("Password cannot be empty")
        }
        
        if (password.length < 6) {
            throw IllegalArgumentException("Password must be at least 6 characters")
        }
        
        if (password != confirmPassword) {
            throw IllegalArgumentException("Passwords do not match")
        }
    }
}