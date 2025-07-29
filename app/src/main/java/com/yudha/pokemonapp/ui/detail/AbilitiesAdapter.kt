package com.yudha.pokemonapp.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yudha.pokemonapp.data.model.PokemonAbility
import com.yudha.pokemonapp.databinding.ItemAbilityBinding

class AbilitiesAdapter : ListAdapter<PokemonAbility, AbilitiesAdapter.AbilityViewHolder>(
    AbilityDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbilityViewHolder {
        val binding = ItemAbilityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AbilityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AbilityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AbilityViewHolder(private val binding: ItemAbilityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ability: PokemonAbility) {
            binding.apply {
                textAbilityName.text = ability.ability.name.replaceFirstChar { it.uppercase() }
                textAbilitySlot.text = "Slot ${ability.slot}"
                
                if (ability.isHidden) {
                    textAbilityType.text = "Hidden Ability"
                    cardAbility.alpha = 0.8f
                } else {
                    textAbilityType.text = "Normal Ability"
                    cardAbility.alpha = 1.0f
                }
            }
        }
    }

    private class AbilityDiffCallback : DiffUtil.ItemCallback<PokemonAbility>() {
        override fun areItemsTheSame(oldItem: PokemonAbility, newItem: PokemonAbility): Boolean {
            return oldItem.ability.name == newItem.ability.name
        }

        override fun areContentsTheSame(oldItem: PokemonAbility, newItem: PokemonAbility): Boolean {
            return oldItem == newItem
        }
    }
}