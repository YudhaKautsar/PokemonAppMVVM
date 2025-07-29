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
import com.yudha.pokemonapp.data.model.TypeInfo
import com.yudha.pokemonapp.data.model.AbilityInfo
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
        viewModel.pokemon.observe(this) { pokemon ->
            pokemon?.let {
                // Set basic info
                binding.apply {
                    textPokemonName.text = it.name.value.replaceFirstChar { char -> char.uppercase() }
                    textPokemonId.text = getString(R.string.pokemon_id_format, it.id.value.toString().padStart(3, '0'))
                    textHeight.text = getString(R.string.height_format, String.format("%.1f", it.physicalAttributes.height / 10.0))
                    textWeight.text = getString(R.string.weight_format, String.format("%.1f", it.physicalAttributes.weight / 10.0))
                    
                    it.physicalAttributes.baseExperience?.let { exp ->
                        textBaseExperience.text = exp.toString()
                    }
                    
                    // Load image
                    val imageUrl = it.sprites.frontDefault
                        ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${it.id.value}.png"
                        
                    Glide.with(this@PokemonDetailActivity)
                        .load(imageUrl)
                        .placeholder(R.drawable.pokemon_logo)
                        .error(R.drawable.pokemon_logo)
                        .into(imagePokemon)
                        
                    // Set types
                    val typesList = it.types.map { type ->
                        com.yudha.pokemonapp.data.model.PokemonType(
                            slot = type.slot,
                            type = TypeInfo(
                                name = type.name.value,
                                url = ""
                            )
                        )
                    }
                    typesAdapter.submitList(typesList)
                        
                    // Set abilities
                    val abilitiesList = it.abilities.map { ability ->
                        com.yudha.pokemonapp.data.model.PokemonAbility(
                            isHidden = ability.isHidden,
                            slot = ability.slot,
                            ability = AbilityInfo(
                                name = ability.name.value,
                                url = ""
                            )
                        )
                    }
                    abilitiesAdapter.submitList(abilitiesList)
                }
            }
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        
        viewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    private fun loadPokemonDetail() {
        val pokemonId = intent.getIntExtra(EXTRA_POKEMON_ID, -1)
        val pokemonName = intent.getStringExtra(EXTRA_POKEMON_NAME)
        
        when {
            pokemonId != -1 -> viewModel.loadPokemonDetailById(pokemonId)
            !pokemonName.isNullOrEmpty() -> viewModel.loadPokemonDetail(pokemonName)
            else -> {
                Toast.makeText(this, getString(R.string.invalid_pokemon_data), Toast.LENGTH_SHORT).show()
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