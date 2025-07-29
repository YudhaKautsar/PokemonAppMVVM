package com.yudha.pokemonapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yudha.pokemonapp.base.BaseViewModel
import com.yudha.pokemonapp.data.UserRepository
import com.yudha.pokemonapp.data.model.LoginRequest
import com.yudha.pokemonapp.util.CrashlyticsHelper
import com.yudha.pokemonapp.util.Event
import com.yudha.pokemonapp.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository, logger: Logger, crashlytics: CrashlyticsHelper
) : BaseViewModel(logger, crashlytics) {

    private val _loginSuccess = MutableLiveData<Event<Boolean>>()
    val loginSuccess: LiveData<Event<Boolean>> = _loginSuccess

    fun doLogin(email: String, password: String) {
        launchCoroutine {
            val request = LoginRequest(email, password)
            userRepository.login(request)
            _loginSuccess.postValue(Event(true))
        }
    }
}