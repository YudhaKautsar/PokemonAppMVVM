package com.yudha.pokemonapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yudha.pokemonapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: HomeViewModel
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
        setupViewModel()
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[HomeViewModel::class.java]
    }
    
    private fun setupRecyclerView() {
        pokemonAdapter = PokemonAdapter { pokemon ->
            // Handle item click - you can navigate to detail screen here
            Toast.makeText(context, "Clicked: ${pokemon.name}", Toast.LENGTH_SHORT).show()
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
                query?.let { viewModel.searchPokemon(it) }
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.searchPokemon("")
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
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.isLoadingMore.observe(viewLifecycleOwner) { isLoadingMore ->
            binding.progressBarLoadMore.visibility = if (isLoadingMore) View.VISIBLE else View.GONE
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