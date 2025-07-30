package com.yudha.pokemonapp.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yudha.pokemonapp.data.local.UserPreferences
import com.yudha.pokemonapp.data.repository.AuthRepository
import com.yudha.pokemonapp.data.entity.User
import com.yudha.pokemonapp.data.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val userDao: UserDao,
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : AndroidViewModel(application) {
    
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess
    
    init {
        loadUserProfile()
    }
    
    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userId = userPreferences.getUserId()
                if (userId != -1L) {
                    val user = userDao.getUserById(userId)
                    _user.value = user
                } else {
                    _errorMessage.value = "User not found"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateUserProfile(
        fullName: String?,
        phoneNumber: String?,
        profileImagePath: String?
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val currentUser = _user.value
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        fullName = fullName,
                        phoneNumber = phoneNumber,
                        profileImagePath = profileImagePath
                    )
                    userDao.updateUser(updatedUser)
                    _user.value = updatedUser
                    _updateSuccess.value = true
                } else {
                    _errorMessage.value = "User not found"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> = _logoutSuccess
    
    fun logout() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                // Clear user preferences first
                userPreferences.clear()
                
                // Logout from auth repository
                authRepository.logout()
                
                // Clear user data
                _user.value = null
                _logoutSuccess.value = true
                
            } catch (e: Exception) {
                _errorMessage.value = "Failed to logout: ${e.message}"
                _logoutSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearLogoutSuccess() {
        _logoutSuccess.value = false
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun clearUpdateSuccess() {
        _updateSuccess.value = false
    }
}