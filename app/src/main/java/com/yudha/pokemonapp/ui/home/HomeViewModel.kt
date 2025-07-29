package com.yudha.pokemonapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yudha.pokemonapp.domain.entity.Pokemon
import com.yudha.pokemonapp.domain.usecase.pokemon.GetPokemonListUseCase
import com.yudha.pokemonapp.domain.usecase.pokemon.SearchPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase
) : ViewModel() {
    
    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _isLoadingMore = MutableLiveData<Boolean>()
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore
    
    private var currentOffset = 0
    private val limit = 10
    private var isLastPage = false
    
    init {
        loadPokemonList()
    }
    
    fun loadPokemonList(refresh: Boolean = false) {
        if (refresh) {
            currentOffset = 0
            isLastPage = false
            _pokemonList.value = emptyList()
        }
        
        if (isLastPage) return
        
        viewModelScope.launch {
            if (currentOffset == 0) {
                _isLoading.value = true
            } else {
                _isLoadingMore.value = true
            }
            
            try {
                val result = getPokemonListUseCase(limit, currentOffset)
                result.onSuccess { pokemonList ->
                    val currentList = _pokemonList.value ?: emptyList()
                    
                    if (refresh || currentOffset == 0) {
                        _pokemonList.value = pokemonList
                    } else {
                        _pokemonList.value = currentList + pokemonList
                    }
                    
                    currentOffset += limit
                    isLastPage = pokemonList.size < limit
                    _errorMessage.value = null
                }.onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Failed to load Pokemon"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load Pokemon"
            } finally {
                _isLoading.value = false
                _isLoadingMore.value = false
            }
        }
    }
    

    
    fun loadMorePokemon() {
        if (!_isLoadingMore.value!! && !isLastPage) {
            loadPokemonList()
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun searchPokemon(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val result = searchPokemonUseCase(query)
                result.onSuccess { pokemonList ->
                    _pokemonList.value = pokemonList
                }.onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Search failed"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Search failed"
            } finally {
                _isLoading.value = false
            }
        }
    }
}