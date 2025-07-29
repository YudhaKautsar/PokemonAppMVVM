package com.yudha.pokemonapp.ui.detail

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yudha.pokemonapp.R
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
            val context = binding.root.context
            return when (typeName.lowercase()) {
                "normal" -> ContextCompat.getColor(context, R.color.type_normal)
                "fire" -> ContextCompat.getColor(context, R.color.type_fire)
                "water" -> ContextCompat.getColor(context, R.color.type_water)
                "electric" -> ContextCompat.getColor(context, R.color.type_electric)
                "grass" -> ContextCompat.getColor(context, R.color.type_grass)
                "ice" -> ContextCompat.getColor(context, R.color.type_ice)
                "fighting" -> ContextCompat.getColor(context, R.color.type_fighting)
                "poison" -> ContextCompat.getColor(context, R.color.type_poison)
                "ground" -> ContextCompat.getColor(context, R.color.type_ground)
                "flying" -> ContextCompat.getColor(context, R.color.type_flying)
                "psychic" -> ContextCompat.getColor(context, R.color.type_psychic)
                "bug" -> ContextCompat.getColor(context, R.color.type_bug)
                "rock" -> ContextCompat.getColor(context, R.color.type_rock)
                "ghost" -> ContextCompat.getColor(context, R.color.type_ghost)
                "dragon" -> ContextCompat.getColor(context, R.color.type_dragon)
                "dark" -> ContextCompat.getColor(context, R.color.type_dark)
                "steel" -> ContextCompat.getColor(context, R.color.type_steel)
                "fairy" -> ContextCompat.getColor(context, R.color.type_fairy)
                else -> ContextCompat.getColor(context, R.color.type_unknown)
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