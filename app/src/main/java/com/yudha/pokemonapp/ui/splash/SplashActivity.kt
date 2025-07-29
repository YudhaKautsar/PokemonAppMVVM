package com.yudha.pokemonapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.yudha.pokemonapp.data.local.UserPreferences
import com.yudha.pokemonapp.ui.main.MainActivity
import com.yudha.pokemonapp.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            userPreferences.authToken.asLiveData().observe(this) { token ->
                if (token.isNullOrEmpty()) {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            }
        }, 2000) // 2 seconds delay
    }
}