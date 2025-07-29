package com.yudha.pokemonapp.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.yudha.pokemonapp.data.dao.UserDao
import com.yudha.pokemonapp.data.entity.User
import java.security.MessageDigest

class AuthRepository(private val userDao: UserDao, private val context: Context) {
    
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "auth_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    suspend fun register(username: String, email: String, password: String): Result<User> {
        return try {
            // Check if username already exists
            if (userDao.isUsernameExists(username) > 0) {
                return Result.failure(Exception("Username sudah digunakan"))
            }
            
            // Check if email already exists
            if (userDao.isEmailExists(email) > 0) {
                return Result.failure(Exception("Email sudah digunakan"))
            }
            
            // Hash password
            val hashedPassword = hashPassword(password)
            
            // Create user
            val user = User(
                username = username,
                email = email,
                password = hashedPassword
            )
            
            // Insert user to database
            val userId = userDao.insertUser(user)
            val createdUser = user.copy(id = userId)
            
            Result.success(createdUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun login(username: String, password: String): Result<User> {
        return try {
            val hashedPassword = hashPassword(password)
            val user = userDao.loginUser(username, hashedPassword)
            
            if (user != null) {
                // Save login session
                saveLoginSession(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Username atau password salah"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun saveLoginSession(user: User) {
        sharedPreferences.edit()
            .putLong("user_id", user.id)
            .putString("username", user.username)
            .putString("email", user.email)
            .putBoolean("is_logged_in", true)
            .apply()
    }
    
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }
    
    fun getCurrentUser(): User? {
        if (!isLoggedIn()) return null
        
        val userId = sharedPreferences.getLong("user_id", 0)
        val username = sharedPreferences.getString("username", "") ?: ""
        val email = sharedPreferences.getString("email", "") ?: ""
        
        return if (userId > 0 && username.isNotEmpty() && email.isNotEmpty()) {
            User(id = userId, username = username, email = email, password = "")
        } else {
            null
        }
    }
    
    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
    
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}