package com.yudha.pokemonapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.yudha.pokemonapp.util.EventObserver

/**
 * Kelas dasar abstrak untuk semua Activity dalam aplikasi.
 * Mengelola inisialisasi ViewBinding dan observasi ViewModel secara umum.
 *
 * @param VB Tipe dari ViewBinding class yang dihasilkan.
 * @param VM Tipe dari ViewModel yang mewarisi dari BaseViewModel.
 */
abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract val viewModel: VM

    /**
     * Fungsi untuk meng-inflate ViewBinding.
     * Contoh implementasi: `ActivityMainBinding.inflate(layoutInflater)`
     */
    abstract fun getViewBinding(inflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding(layoutInflater)
        setContentView(binding.root)

        setupObservers()
    }

    /**
     * Mengamati LiveData umum dari BaseViewModel.
     */
    protected open fun setupObservers() {
        viewModel.isLoading.observe(this) {
            handleLoading(it)
        }

        viewModel.errorEvent.observe(this, EventObserver { message ->
            showError(message)
        })
    }

    /**
     * Menangani visibilitas loading indicator.
     * Anda bisa meng-override ini untuk menampilkan/menyembunyikan ProgressBar atau dialog.
     * @param isLoading Status loading.
     */
    protected open fun handleLoading(isLoading: Boolean) {
        // Contoh: progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Menampilkan pesan error.
     * Anda bisa meng-override ini untuk menampilkan Snackbar atau dialog error.
     * @param message Pesan error yang akan ditampilkan.
     */
    protected open fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // Mencegah memory leak
    }
}