package com.yudha.pokemonapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yudha.pokemonapp.R
import com.yudha.pokemonapp.data.repository.AuthRepository
import com.yudha.pokemonapp.data.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository
) : AndroidViewModel(application) {
    
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult
    
    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> = _registerResult
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authRepository.login(username, password)
                result.onSuccess {
                    _loginResult.value = true
                }.onFailure { exception ->
                    _loginResult.value = false
                    _errorMessage.value = exception.message ?: "Login failed"
                }
            } catch (e: Exception) {
                _loginResult.value = false
                _errorMessage.value = e.message ?: "Login failed"
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
                result.onSuccess {
                    _registerResult.value = true
                }.onFailure { exception ->
                    _registerResult.value = false
                    _errorMessage.value = exception.message ?: "Registration failed"
                }
            } catch (e: Exception) {
                _registerResult.value = false
                _errorMessage.value = e.message ?: "Registration failed"
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
            username.isBlank() -> getApplication<Application>().getString(R.string.username_empty)
            password.isBlank() -> getApplication<Application>().getString(R.string.password_empty)
            password.length < 6 -> getApplication<Application>().getString(R.string.password_min_length)
            else -> null
        }
    }
    
    fun validateRegisterInput(username: String, email: String, password: String, confirmPassword: String): String? {
        return when {
            username.isBlank() -> getApplication<Application>().getString(R.string.username_empty)
            username.length < 3 -> getApplication<Application>().getString(R.string.username_min_length)
            email.isBlank() -> getApplication<Application>().getString(R.string.email_empty)
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> getApplication<Application>().getString(R.string.email_invalid)
            password.isBlank() -> getApplication<Application>().getString(R.string.password_empty)
            password.length < 6 -> getApplication<Application>().getString(R.string.password_min_length)
            password != confirmPassword -> getApplication<Application>().getString(R.string.password_mismatch)
            else -> null
        }
    }
}