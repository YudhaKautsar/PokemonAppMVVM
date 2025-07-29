package com.yudha.pokemonapp.domain.repository

import com.yudha.pokemonapp.domain.entity.User
import com.yudha.pokemonapp.domain.entity.UserId
import com.yudha.pokemonapp.domain.entity.Username
import com.yudha.pokemonapp.domain.entity.Email

/**
 * Domain User Repository Interface
 * Defines the contract for user data operations in the domain layer
 */
interface UserRepository {
    
    /**
     * Authenticate user with username and password
     */
    suspend fun authenticate(username: Username, password: String): Result<User>
    
    /**
     * Register a new user
     */
    suspend fun register(
        username: Username,
        email: Email,
        password: String
    ): Result<User>
    
    /**
     * Find user by ID
     */
    suspend fun findById(userId: UserId): User?
    
    /**
     * Find user by username
     */
    suspend fun findByUsername(username: Username): User?
    
    /**
     * Find user by email
     */
    suspend fun findByEmail(email: Email): User?
    
    /**
     * Update user profile
     */
    suspend fun updateProfile(user: User): Result<User>
    
    /**
     * Check if username exists
     */
    suspend fun isUsernameExists(username: Username): Boolean
    
    /**
     * Check if email exists
     */
    suspend fun isEmailExists(email: Email): Boolean
    
    /**
     * Delete user
     */
    suspend fun delete(userId: UserId): Result<Unit>
}