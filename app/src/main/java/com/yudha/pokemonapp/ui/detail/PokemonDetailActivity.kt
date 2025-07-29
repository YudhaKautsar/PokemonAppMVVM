package com.yudha.pokemonapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.yudha.pokemonapp.R
import com.yudha.pokemonapp.databinding.ActivityPokemonDetailBinding
import com.yudha.pokemonapp.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailActivity : AppCompatActivity() {

    private var _binding: ActivityPokemonDetailBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: PokemonDetailViewModel by viewModels()
    private lateinit var abilitiesAdapter: AbilitiesAdapter
    private lateinit var typesAdapter: TypesAdapter

    companion object {
        const val EXTRA_POKEMON_ID = "extra_pokemon_id"
        const val EXTRA_POKEMON_NAME = "extra_pokemon_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        observeViewModel()
        loadPokemonDetail()
    }

    private fun setupUI() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Setup RecyclerViews
        abilitiesAdapter = AbilitiesAdapter()
        binding.recyclerViewAbilities.apply {
            layoutManager = LinearLayoutManager(this@PokemonDetailActivity)
            adapter = abilitiesAdapter
        }

        typesAdapter = TypesAdapter()
        binding.recyclerViewTypes.apply {
            layoutManager = LinearLayoutManager(this@PokemonDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = typesAdapter
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewModel.pokemonDetail.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    val pokemon = result.data
                    
                    // Set basic info
                    binding.apply {
                        textPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
                        textPokemonId.text = "#${pokemon.id.toString().padStart(3, '0')}"
                        textHeight.text = "${pokemon.height / 10.0} m"
                        textWeight.text = "${pokemon.weight / 10.0} kg"
                        
                        pokemon.baseExperience?.let {
                            textBaseExperience.text = it.toString()
                        }
                        
                        // Load image
                        val imageUrl = pokemon.sprites.other?.officialArtwork?.frontDefault
                            ?: pokemon.sprites.frontDefault
                            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
                        
                        Glide.with(this@PokemonDetailActivity)
                            .load(imageUrl)
                            .placeholder(R.drawable.pokemon_logo)
                            .error(R.drawable.pokemon_logo)
                            .into(imagePokemon)
                    }
                    
                    // Set abilities
                    abilitiesAdapter.submitList(pokemon.abilities)
                    
                    // Set types
                    typesAdapter.submitList(pokemon.types)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.exception.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadPokemonDetail() {
        val pokemonId = intent.getIntExtra(EXTRA_POKEMON_ID, -1)
        val pokemonName = intent.getStringExtra(EXTRA_POKEMON_NAME)
        
        when {
            pokemonId != -1 -> viewModel.loadPokemonDetail(pokemonId)
            !pokemonName.isNullOrEmpty() -> viewModel.loadPokemonDetailByName(pokemonName)
            else -> {
                Toast.makeText(this, "Invalid Pokemon data", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.contentLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}