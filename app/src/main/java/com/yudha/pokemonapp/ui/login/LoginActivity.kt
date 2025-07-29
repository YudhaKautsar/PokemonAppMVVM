package com.yudha.pokemonapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import com.yudha.pokemonapp.base.BaseActivity
import com.yudha.pokemonapp.databinding.ActivityLoginBinding
import com.yudha.pokemonapp.ui.main.MainActivity
import com.yudha.pokemonapp.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()
    override fun getViewBinding(inflater: LayoutInflater): ActivityLoginBinding {

        return ActivityLoginBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            viewModel.doLogin(email, password)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.loginSuccess.observe(this, EventObserver {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }

    override fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}