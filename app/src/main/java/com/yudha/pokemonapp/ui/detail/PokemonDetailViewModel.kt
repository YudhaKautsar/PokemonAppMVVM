package com.yudha.pokemonapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yudha.pokemonapp.data.model.PokemonDetail
import com.yudha.pokemonapp.data.repository.PokemonRepository
import com.yudha.pokemonapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    
    private val _pokemonDetail = MutableLiveData<Result<PokemonDetail>>()
    val pokemonDetail: LiveData<Result<PokemonDetail>> = _pokemonDetail

    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            _pokemonDetail.value = Result.Loading
            try {
                val response = pokemonRepository.getPokemonDetail(id)
                if (response.isSuccessful && response.body() != null) {
                    _pokemonDetail.value = Result.Success(response.body()!!)
                } else {
                    _pokemonDetail.value = Result.Error(Exception("Failed to load Pokemon detail: ${response.message()}"))
                }
            } catch (e: Exception) {
                _pokemonDetail.value = Result.Error(e)
            }
        }
    }

    fun loadPokemonDetailByName(name: String) {
        viewModelScope.launch {
            _pokemonDetail.value = Result.Loading
            try {
                val response = pokemonRepository.getPokemonDetailByName(name)
                if (response.isSuccessful && response.body() != null) {
                    _pokemonDetail.value = Result.Success(response.body()!!)
                } else {
                    _pokemonDetail.value = Result.Error(Exception("Failed to load Pokemon detail: ${response.message()}"))
                }
            } catch (e: Exception) {
                _pokemonDetail.value = Result.Error(e)
            }
        }
    }
}