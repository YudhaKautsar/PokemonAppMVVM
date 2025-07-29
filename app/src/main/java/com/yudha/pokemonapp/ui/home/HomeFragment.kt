package com.yudha.pokemonapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yudha.pokemonapp.databinding.FragmentHomeBinding
import com.yudha.pokemonapp.ui.detail.PokemonDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var pokemonAdapter: PokemonAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        pokemonAdapter = PokemonAdapter { pokemon ->
            // Navigate to detail screen
            val intent = Intent(requireContext(), PokemonDetailActivity::class.java)
            intent.putExtra(PokemonDetailActivity.EXTRA_POKEMON_NAME, pokemon.name)
            startActivity(intent)
        }
        
        binding.recyclerViewPokemon.apply {
            adapter = pokemonAdapter
            layoutManager = LinearLayoutManager(context)
            
            // Add scroll listener for pagination
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= 10) {
                        viewModel.loadMorePokemon()
                    }
                }
            })
        }
    }
    
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        viewModel.searchPokemon(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.loadPokemonList()
                }
                return true
            }
        })
    }
    
    private fun observeViewModel() {
        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            pokemonAdapter.submitList(pokemonList)
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Hanya tampilkan progress bar utama jika list kosong
            val showMainProgress = isLoading && (viewModel.pokemonList.value?.isEmpty() == true)
            binding.progressBar.visibility = if (showMainProgress) View.VISIBLE else View.GONE
        }
        
        viewModel.isLoadingMore.observe(viewLifecycleOwner) { isLoadingMore ->
            // Gunakan footer loading di adapter
            pokemonAdapter.setLoading(isLoadingMore)
            // Sembunyikan progress bar terpisah
            binding.progressBarLoadMore.visibility = View.GONE
        }
        
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                binding.textError.text = errorMessage
                binding.textError.visibility = View.VISIBLE
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            } else {
                binding.textError.visibility = View.GONE
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}