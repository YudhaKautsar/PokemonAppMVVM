package com.yudha.pokemonapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.yudha.pokemonapp.R
import com.yudha.pokemonapp.data.repository.AuthRepository
import com.yudha.pokemonapp.data.database.AppDatabase
import com.yudha.pokemonapp.ui.main.MainActivity
import com.yudha.pokemonapp.ui.auth.LoginActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        val database = AppDatabase.getDatabase(this)
        authRepository = AuthRepository(database.userDao(), this)
        
        // Start animations
        startAnimations()

        Handler(Looper.getMainLooper()).postDelayed({
            if (authRepository.isLoggedIn()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 3000) // 3 seconds delay to show animations
    }
    
    private fun startAnimations() {
        val pokemonLogo = findViewById<ImageView>(R.id.pokemonLogo)
        val appTitle = findViewById<TextView>(R.id.appTitle)
        val subtitle = findViewById<TextView>(R.id.subtitle)
        
        // Load animations
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation)
        
        // Apply animations
        pokemonLogo.startAnimation(rotateAnimation)
        appTitle.startAnimation(fadeInAnimation)
        subtitle.startAnimation(fadeInAnimation)
    }
}