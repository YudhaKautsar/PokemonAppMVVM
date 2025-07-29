package com.yudha.pokemonapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.yudha.pokemonapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
        observeViewModel()
    }
    
    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            performRegister()
        }
        
        binding.tvLogin.setOnClickListener {
            finish() // Go back to LoginActivity
        }
    }
    
    private fun observeViewModel() {
        authViewModel.registerResult.observe(this) { result ->
            result.onSuccess { user ->
                Toast.makeText(this, "Registrasi berhasil! Silakan login dengan akun Anda", Toast.LENGTH_LONG).show()
                finish() // Go back to LoginActivity
            }.onFailure { exception ->
                Toast.makeText(this, exception.message ?: "Registrasi gagal", Toast.LENGTH_SHORT).show()
            }
        }
        
        authViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnRegister.isEnabled = !isLoading
        }
    }
    
    private fun performRegister() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        
        // Clear previous errors
        binding.tilUsername.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
        
        // Validate input
        val validationError = authViewModel.validateRegisterInput(username, email, password, confirmPassword)
        if (validationError != null) {
            when {
                validationError.contains("Username") -> binding.tilUsername.error = validationError
                validationError.contains("Email") -> binding.tilEmail.error = validationError
                validationError.contains("Password") && !validationError.contains("Konfirmasi") -> binding.tilPassword.error = validationError
                validationError.contains("Konfirmasi") -> binding.tilConfirmPassword.error = validationError
                else -> Toast.makeText(this, validationError, Toast.LENGTH_SHORT).show()
            }
            return
        }
        
        // Perform registration
        authViewModel.register(username, email, password)
    }
}