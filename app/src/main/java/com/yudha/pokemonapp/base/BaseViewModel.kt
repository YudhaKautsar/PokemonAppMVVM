package com.yudha.pokemonapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yudha.pokemonapp.util.CrashlyticsHelper
import com.yudha.pokemonapp.util.Event
import com.yudha.pokemonapp.util.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


abstract class BaseViewModel(
    private val logger: Logger, // Disediakan melalui DI
    private val crashlytics: CrashlyticsHelper // Disediakan melalui DI
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorEvent = MutableLiveData<Event<String>>()
    val errorEvent: LiveData<Event<String>> = _errorEvent

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val message = "Coroutine exception: ${throwable.localizedMessage}"
        logger.e("BaseViewModel", message, throwable)
        crashlytics.recordException(throwable)
        _errorEvent.postValue(Event(message))
        hideLoading()
    }

    protected fun showLoading() {
        _isLoading.value = true
    }

    protected fun hideLoading() {
        _isLoading.value = false
    }

    protected fun showError(message: String) {
        _errorEvent.value = Event(message)
    }

    protected fun launchCoroutine(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(exceptionHandler) {
            showLoading()
            block()
            hideLoading()
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Batalkan semua coroutine jika ViewModel dihancurkan
    }
}