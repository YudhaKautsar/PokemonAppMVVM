package com.yudha.pokemonapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yudha.pokemonapp.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Query("SELECT * FROM favorite_pokemon ORDER BY name ASC")
    fun getAllFavoritePokemon(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM favorite_pokemon WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity?

    @Query("DELETE FROM favorite_pokemon WHERE id = :id")
    suspend fun deletePokemonById(id: Int)
}