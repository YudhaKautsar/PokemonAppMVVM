package com.yudha.pokemonapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yudha.pokemonapp.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yudha.pokemonapp.domain.usecase.auth.LoginUseCase
import com.yudha.pokemonapp.domain.usecase.auth.LogoutUseCase
import com.yudha.pokemonapp.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase
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
                val result = loginUseCase(username, password)
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
    
    fun register(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = registerUseCase(username, email, password, confirmPassword)
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
    
    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
    
    fun isLoggedIn(): Boolean {
        // TODO: Implement proper login state check
        // This could check shared preferences, token validity, etc.
        return false
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