package com.yudha.pokemonapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yudha.pokemonapp.data.database.AppDatabase
import com.yudha.pokemonapp.data.entity.User
import com.yudha.pokemonapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val authRepository: AuthRepository
    
    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult
    
    private val _registerResult = MutableLiveData<Result<User>>()
    val registerResult: LiveData<Result<User>> = _registerResult
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    init {
        val database = AppDatabase.getDatabase(application)
        authRepository = AuthRepository(database.userDao(), application)
    }
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authRepository.login(username, password)
                _loginResult.value = result
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authRepository.register(username, email, password)
                _registerResult.value = result
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }
    
    fun getCurrentUser(): User? {
        return authRepository.getCurrentUser()
    }
    
    fun logout() {
        authRepository.logout()
    }
    
    fun validateLoginInput(username: String, password: String): String? {
        return when {
            username.isBlank() -> "Username tidak boleh kosong"
            password.isBlank() -> "Password tidak boleh kosong"
            password.length < 6 -> "Password minimal 6 karakter"
            else -> null
        }
    }
    
    fun validateRegisterInput(username: String, email: String, password: String, confirmPassword: String): String? {
        return when {
            username.isBlank() -> "Username tidak boleh kosong"
            username.length < 3 -> "Username minimal 3 karakter"
            email.isBlank() -> "Email tidak boleh kosong"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Format email tidak valid"
            password.isBlank() -> "Password tidak boleh kosong"
            password.length < 6 -> "Password minimal 6 karakter"
            password != confirmPassword -> "Konfirmasi password tidak cocok"
            else -> null
        }
    }
}