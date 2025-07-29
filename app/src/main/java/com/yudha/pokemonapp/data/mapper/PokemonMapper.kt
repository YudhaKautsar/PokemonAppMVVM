package com.yudha.pokemonapp.data.mapper

import com.yudha.pokemonapp.data.model.PokemonDetail as ApiPokemonDetail
import com.yudha.pokemonapp.data.model.PokemonAbility as ApiPokemonAbility
import com.yudha.pokemonapp.data.model.PokemonType as ApiPokemonType
import com.yudha.pokemonapp.data.model.PokemonStat as ApiPokemonStat
import com.yudha.pokemonapp.domain.entity.Pokemon as DomainPokemon
import com.yudha.pokemonapp.domain.entity.PokemonId
import com.yudha.pokemonapp.domain.entity.PokemonName
import com.yudha.pokemonapp.domain.entity.PhysicalAttributes
import com.yudha.pokemonapp.domain.entity.Ability
import com.yudha.pokemonapp.domain.entity.AbilityName
import com.yudha.pokemonapp.domain.entity.PokemonType
import com.yudha.pokemonapp.domain.entity.TypeName
import com.yudha.pokemonapp.domain.entity.Stat
import com.yudha.pokemonapp.domain.entity.StatName
import com.yudha.pokemonapp.domain.entity.PokemonSprites

/**
 * Mapper between API response and Domain Pokemon entities
 * Handles conversion from external API format to internal domain format
 */
object PokemonMapper {
    
    /**
     * Convert API Pokemon Detail to Domain Pokemon
     */
    fun toDomain(apiPokemon: ApiPokemonDetail): DomainPokemon {
        return DomainPokemon(
            id = PokemonId(apiPokemon.id),
            name = PokemonName(apiPokemon.name),
            physicalAttributes = PhysicalAttributes(
                height = apiPokemon.height,
                weight = apiPokemon.weight,
                baseExperience = apiPokemon.baseExperience
            ),
            abilities = apiPokemon.abilities.map { mapAbility(it) },
            types = apiPokemon.types.map { mapType(it) },
            stats = apiPokemon.stats.map { mapStat(it) },
            sprites = PokemonSprites(
                frontDefault = apiPokemon.sprites.frontDefault,
                frontShiny = apiPokemon.sprites.frontShiny,
                backDefault = apiPokemon.sprites.backDefault,
                backShiny = apiPokemon.sprites.backShiny
            )
        )
    }
    
    private fun mapAbility(apiAbility: ApiPokemonAbility): Ability {
        return Ability(
            name = AbilityName(apiAbility.ability.name),
            isHidden = apiAbility.isHidden,
            slot = apiAbility.slot
        )
    }
    
    private fun mapType(apiType: ApiPokemonType): PokemonType {
        return PokemonType(
            name = TypeName(apiType.type.name),
            slot = apiType.slot
        )
    }
    
    private fun mapStat(apiStat: ApiPokemonStat): Stat {
        return Stat(
            name = StatName(apiStat.stat.name),
            baseStat = apiStat.baseStat,
            effort = apiStat.effort
        )
    }
}