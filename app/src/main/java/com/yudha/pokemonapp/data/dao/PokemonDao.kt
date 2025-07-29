package com.yudha.pokemonapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yudha.pokemonapp.data.local.entity.PokemonEntity

@Dao
interface PokemonDao {
    
    @Query("SELECT * FROM pokemon ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonEntity>
    
    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :searchQuery || '%' ORDER BY id ASC")
    suspend fun searchPokemon(searchQuery: String): List<PokemonEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: List<PokemonEntity>)
    
    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun getPokemonCount(): Int
    
    @Query("DELETE FROM pokemon")
    suspend fun clearAllPokemon()
}