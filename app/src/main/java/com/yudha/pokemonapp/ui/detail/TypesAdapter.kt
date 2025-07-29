package com.yudha.pokemonapp.ui.detail

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yudha.pokemonapp.data.model.PokemonType
import com.yudha.pokemonapp.databinding.ItemTypeBinding

class TypesAdapter : ListAdapter<PokemonType, TypesAdapter.TypeViewHolder>(
    TypeDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val binding = ItemTypeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TypeViewHolder(private val binding: ItemTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(type: PokemonType) {
            binding.apply {
                textTypeName.text = type.type.name.replaceFirstChar { it.uppercase() }
                
                // Set background color based on type
                val backgroundColor = getTypeColor(type.type.name)
                cardType.setCardBackgroundColor(backgroundColor)
                
                // Set text color for better contrast
                textTypeName.setTextColor(Color.WHITE)
            }
        }
        
        private fun getTypeColor(typeName: String): Int {
            return when (typeName.lowercase()) {
                "normal" -> Color.parseColor("#A8A878")
                "fire" -> Color.parseColor("#F08030")
                "water" -> Color.parseColor("#6890F0")
                "electric" -> Color.parseColor("#F8D030")
                "grass" -> Color.parseColor("#78C850")
                "ice" -> Color.parseColor("#98D8D8")
                "fighting" -> Color.parseColor("#C03028")
                "poison" -> Color.parseColor("#A040A0")
                "ground" -> Color.parseColor("#E0C068")
                "flying" -> Color.parseColor("#A890F0")
                "psychic" -> Color.parseColor("#F85888")
                "bug" -> Color.parseColor("#A8B820")
                "rock" -> Color.parseColor("#B8A038")
                "ghost" -> Color.parseColor("#705898")
                "dragon" -> Color.parseColor("#7038F8")
                "dark" -> Color.parseColor("#705848")
                "steel" -> Color.parseColor("#B8B8D0")
                "fairy" -> Color.parseColor("#EE99AC")
                else -> Color.parseColor("#68A090")
            }
        }
    }

    private class TypeDiffCallback : DiffUtil.ItemCallback<PokemonType>() {
        override fun areItemsTheSame(oldItem: PokemonType, newItem: PokemonType): Boolean {
            return oldItem.type.name == newItem.type.name
        }

        override fun areContentsTheSame(oldItem: PokemonType, newItem: PokemonType): Boolean {
            return oldItem == newItem
        }
    }
}