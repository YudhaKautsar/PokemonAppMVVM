package com.yudha.pokemonapp.data.repository

import android.content.Context
import com.yudha.pokemonapp.data.dao.UserDao
import com.yudha.pokemonapp.data.mapper.UserMapper
import com.yudha.pokemonapp.domain.entity.User as DomainUser
import com.yudha.pokemonapp.domain.entity.UserId
import com.yudha.pokemonapp.domain.entity.Username
import com.yudha.pokemonapp.domain.entity.Email
import com.yudha.pokemonapp.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of UserRepository for the domain layer
 * Handles data operations and converts between data and domain entities
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    @ApplicationContext private val context: Context
) : UserRepository {
    
    override suspend fun authenticate(username: Username, password: String): Result<DomainUser> {
        return try {
            val dataUser = userDao.getUserByUsername(username.value)
                ?: return Result.failure(Exception("User not found"))
            
            val hashedPassword = hashPassword(password)
            if (dataUser.password != hashedPassword) {
                return Result.failure(Exception("Invalid password"))
            }
            
            val domainUser = UserMapper.toDomain(dataUser)
            Result.success(domainUser)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun register(
        username: Username,
        email: Email,
        password: String
    ): Result<DomainUser> {
        return try {
            // Check if username already exists
            if (userDao.isUsernameExists(username.value) > 0) {
                return Result.failure(Exception("Username already exists"))
            }
            
            // Check if email already exists
            if (userDao.isEmailExists(email.value) > 0) {
                return Result.failure(Exception("Email already exists"))
            }
            
            // Hash password and create user
            val hashedPassword = hashPassword(password)
            val dataUser = UserMapper.createDataUser(
                username = username.value,
                email = email.value,
                hashedPassword = hashedPassword
            )
            
            val userId = userDao.insertUser(dataUser)
            val insertedUser = userDao.getUserById(userId)
                ?: return Result.failure(Exception("Failed to retrieve created user"))
            
            val domainUser = UserMapper.toDomain(insertedUser)
            Result.success(domainUser)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun findById(userId: UserId): DomainUser? {
        return try {
            val dataUser = userDao.getUserById(userId.value)
            dataUser?.let { UserMapper.toDomain(it) }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun findByUsername(username: Username): DomainUser? {
        return try {
            val dataUser = userDao.getUserByUsername(username.value)
            dataUser?.let { UserMapper.toDomain(it) }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun findByEmail(email: Email): DomainUser? {
        return try {
            val dataUser = userDao.getUserByEmail(email.value)
            dataUser?.let { UserMapper.toDomain(it) }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun updateProfile(user: DomainUser): Result<DomainUser> {
        return try {
            // Get current user data to preserve password
            val currentDataUser = userDao.getUserById(user.id.value)
                ?: return Result.failure(Exception("User not found"))
            
            val updatedDataUser = UserMapper.toData(user, currentDataUser.password)
            userDao.updateUser(updatedDataUser)
            
            Result.success(user)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun isUsernameExists(username: Username): Boolean {
        return try {
            userDao.isUsernameExists(username.value) > 0
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun isEmailExists(email: Email): Boolean {
        return try {
            userDao.isEmailExists(email.value) > 0
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun delete(userId: UserId): Result<Unit> {
        return try {
            userDao.deleteUser(userId.value)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}