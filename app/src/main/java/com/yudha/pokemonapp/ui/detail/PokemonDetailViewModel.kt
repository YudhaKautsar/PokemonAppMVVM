package com.yudha.pokemonapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yudha.pokemonapp.domain.entity.Pokemon
import com.yudha.pokemonapp.domain.usecase.pokemon.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {
    
    private val _pokemon = MutableLiveData<Pokemon?>()
    val pokemon: LiveData<Pokemon?> = _pokemon
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadPokemonDetail(pokemonId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = getPokemonDetailUseCase(pokemonId)
                result.onSuccess { pokemon ->
                    _pokemon.value = pokemon
                    _errorMessage.value = null
                }.onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Failed to load Pokemon details"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load Pokemon details"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadPokemonDetailById(pokemonId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = getPokemonDetailUseCase(pokemonId)
                result.onSuccess { pokemon ->
                    _pokemon.value = pokemon
                    _errorMessage.value = null
                }.onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Failed to load Pokemon details"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load Pokemon details"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}