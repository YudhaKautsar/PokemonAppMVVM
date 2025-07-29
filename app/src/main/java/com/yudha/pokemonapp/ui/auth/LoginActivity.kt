package com.yudha.pokemonapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.yudha.pokemonapp.ui.main.MainActivity
import com.yudha.pokemonapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Check if user is already logged in
        if (authViewModel.isLoggedIn()) {
            navigateToMain()
            return
        }
        
        setupClickListeners()
        observeViewModel()
    }
    
    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            performLogin()
        }
        
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    
    private fun observeViewModel() {
        authViewModel.loginResult.observe(this) { result ->
            result.onSuccess { user ->
                Toast.makeText(this, "Login berhasil! Selamat datang ${user.username}", Toast.LENGTH_SHORT).show()
                navigateToMain()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message ?: "Login gagal", Toast.LENGTH_SHORT).show()
            }
        }
        
        authViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
        }
    }
    
    private fun performLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        
        // Clear previous errors
        binding.tilUsername.error = null
        binding.tilPassword.error = null
        
        // Validate input
        val validationError = authViewModel.validateLoginInput(username, password)
        if (validationError != null) {
            when {
                validationError.contains("Username") -> binding.tilUsername.error = validationError
                validationError.contains("Password") -> binding.tilPassword.error = validationError
                else -> Toast.makeText(this, validationError, Toast.LENGTH_SHORT).show()
            }
            return
        }
        
        // Perform login
        authViewModel.login(username, password)
    }
    
    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}