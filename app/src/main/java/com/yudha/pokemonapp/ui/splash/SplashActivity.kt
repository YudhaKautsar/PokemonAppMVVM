package com.yudha.pokemonapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

import com.yudha.pokemonapp.data.repository.AuthRepository
import com.yudha.pokemonapp.data.database.AppDatabase
import com.yudha.pokemonapp.ui.main.MainActivity
import com.yudha.pokemonapp.ui.auth.LoginActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        authRepository = AuthRepository(database.userDao(), this)

        Handler(Looper.getMainLooper()).postDelayed({
            if (authRepository.isLoggedIn()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000) // 2 seconds delay
    }
}