package com.yudha.pokemonapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pokemon")
data class PokemonEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String
)