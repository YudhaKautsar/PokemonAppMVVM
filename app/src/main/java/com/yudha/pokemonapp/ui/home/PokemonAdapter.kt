package com.yudha.pokemonapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yudha.pokemonapp.R
import com.yudha.pokemonapp.data.model.PokemonResult
import com.yudha.pokemonapp.databinding.ItemPokemonBinding

class PokemonAdapter(
    private val onItemClick: (PokemonResult) -> Unit
) : ListAdapter<PokemonResult, PokemonAdapter.PokemonViewHolder>(PokemonDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class PokemonViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(pokemon: PokemonResult) {
            binding.apply {
                textPokemonName.text = pokemon.name.replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase() else it.toString() 
                }
                
                // Extract Pokemon ID from URL for image
                val pokemonId = extractPokemonId(pokemon.url)
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
                
                Glide.with(imagePokemon.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_home)
                    .error(R.drawable.ic_profile)
                    .into(imagePokemon)
                
                root.setOnClickListener {
                    onItemClick(pokemon)
                }
            }
        }
        
        private fun extractPokemonId(url: String): String {
            return url.trimEnd('/').split('/').last()
        }
    }
    
    class PokemonDiffCallback : DiffUtil.ItemCallback<PokemonResult>() {
        override fun areItemsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
            return oldItem.url == newItem.url
        }
        
        override fun areContentsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
            return oldItem == newItem
        }
    }
}