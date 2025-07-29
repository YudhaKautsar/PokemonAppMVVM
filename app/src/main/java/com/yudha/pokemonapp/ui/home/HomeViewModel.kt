package com.yudha.pokemonapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yudha.pokemonapp.data.model.PokemonResult
import com.yudha.pokemonapp.data.repository.PokemonRepository
import com.yudha.pokemonapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    
    private val _pokemonList = MutableLiveData<List<PokemonResult>>()
    val pokemonList: LiveData<List<PokemonResult>> = _pokemonList
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _isLoadingMore = MutableLiveData<Boolean>()
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore
    
    private var currentOffset = 0
    private val limit = 10
    private var isLastPage = false
    private var currentQuery = ""
    
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
            
            val result = if (currentQuery.isEmpty()) {
                pokemonRepository.getPokemonList(limit, currentOffset)
            } else {
                pokemonRepository.searchPokemon(currentQuery)
            }
            
            when (result) {
                is Result.Success -> {
                    val newPokemon = result.data.results
                    val currentList = _pokemonList.value ?: emptyList()
                    
                    if (refresh || currentOffset == 0) {
                        _pokemonList.value = newPokemon
                    } else {
                        _pokemonList.value = currentList + newPokemon
                    }
                    
                    currentOffset += limit
                    isLastPage = newPokemon.size < limit
                    _errorMessage.value = null
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.message ?: "Unknown error occurred"
                }
            }
            
            _isLoading.value = false
            _isLoadingMore.value = false
        }
    }
    
    fun searchPokemon(query: String) {
        currentQuery = query
        currentOffset = 0
        isLastPage = false
        _pokemonList.value = emptyList()
        
        if (query.isEmpty()) {
            loadPokemonList(refresh = true)
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            
            val result = pokemonRepository.searchPokemon(query)
            
            when (result) {
                is Result.Success -> {
                    _pokemonList.value = result.data.results
                    currentOffset = limit
                    isLastPage = result.data.results.size < limit
                    _errorMessage.value = null
                }
                is Result.Error -> {
                    _errorMessage.value = result.exception.message ?: "Unknown error occurred"
                    _pokemonList.value = emptyList()
                }
            }
            
            _isLoading.value = false
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
}