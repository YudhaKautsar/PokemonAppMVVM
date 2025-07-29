package com.yudha.pokemonapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yudha.pokemonapp.R
import com.yudha.pokemonapp.domain.entity.Pokemon
import com.yudha.pokemonapp.databinding.ItemPokemonBinding
import com.yudha.pokemonapp.databinding.ItemLoadingFooterBinding

class PokemonAdapter(
    private val onItemClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        private const val VIEW_TYPE_POKEMON = 0
        private const val VIEW_TYPE_LOADING = 1
    }
    
    private var pokemonList = mutableListOf<Pokemon>()
    private var isLoading = false
    
    fun submitList(list: List<Pokemon>) {
        pokemonList.clear()
        pokemonList.addAll(list)
        notifyDataSetChanged()
    }
    
    fun setLoading(loading: Boolean) {
        val wasLoading = isLoading
        isLoading = loading
        
        if (wasLoading && !loading) {
            // Remove loading footer with animation
            notifyItemRemoved(pokemonList.size)
        } else if (!wasLoading && loading) {
            // Add loading footer with animation
            notifyItemInserted(pokemonList.size)
        }
    }
    
    override fun getItemCount(): Int {
        return pokemonList.size + if (isLoading) 1 else 0
    }
    
    override fun getItemViewType(position: Int): Int {
        return if (position < pokemonList.size) {
            VIEW_TYPE_POKEMON
        } else {
            VIEW_TYPE_LOADING
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_POKEMON -> {
                val binding = ItemPokemonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PokemonViewHolder(binding)
            }
            VIEW_TYPE_LOADING -> {
                val binding = ItemLoadingFooterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                LoadingViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PokemonViewHolder -> {
                holder.bind(pokemonList[position])
            }
            is LoadingViewHolder -> {
                // Animate loading footer
                val fadeIn = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_in)
                holder.itemView.startAnimation(fadeIn)
            }
        }
    }
    
    inner class LoadingViewHolder(private val binding: ItemLoadingFooterBinding) :
        RecyclerView.ViewHolder(binding.root)
    
    inner class PokemonViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(pokemon: Pokemon) {
            binding.apply {
                textPokemonName.text = pokemon.name.value.replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase() else it.toString() 
                }
                
                // Use Pokemon ID directly
                val pokemonId = pokemon.id.value
                val imageUrl = pokemon.sprites.frontDefault ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
                
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
    

}