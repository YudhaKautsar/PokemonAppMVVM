package com.yudha.pokemonapp.domain.entity

/**
 * Domain Pokemon Entity - Core business object
 */
data class Pokemon(
    val id: PokemonId,
    val name: PokemonName,
    val physicalAttributes: PhysicalAttributes,
    val abilities: List<Ability>,
    val types: List<PokemonType>,
    val stats: List<Stat>,
    val sprites: PokemonSprites
) {
    fun getMainType(): PokemonType? = types.firstOrNull()
    
    fun hasAbility(abilityName: String): Boolean {
        return abilities.any { it.name.value.equals(abilityName, ignoreCase = true) }
    }
    
    fun getTotalStats(): Int = stats.sumOf { it.baseStat }
}

/**
 * Value Objects for Pokemon domain
 */
@JvmInline
value class PokemonId(val value: Int) {
    init {
        require(value > 0) { "Pokemon ID must be positive" }
    }
}

@JvmInline
value class PokemonName(val value: String) {
    init {
        require(value.isNotBlank()) { "Pokemon name cannot be blank" }
    }
}

data class PhysicalAttributes(
    val height: Int, // in decimeters
    val weight: Int, // in hectograms
    val baseExperience: Int?
) {
    fun getHeightInMeters(): Double = height / 10.0
    fun getWeightInKilograms(): Double = weight / 10.0
}

data class Ability(
    val name: AbilityName,
    val isHidden: Boolean,
    val slot: Int
)

@JvmInline
value class AbilityName(val value: String) {
    init {
        require(value.isNotBlank()) { "Ability name cannot be blank" }
    }
}

data class PokemonType(
    val name: TypeName,
    val slot: Int
)

@JvmInline
value class TypeName(val value: String) {
    init {
        require(value.isNotBlank()) { "Type name cannot be blank" }
    }
}

data class Stat(
    val name: StatName,
    val baseStat: Int,
    val effort: Int
)

@JvmInline
value class StatName(val value: String) {
    init {
        require(value.isNotBlank()) { "Stat name cannot be blank" }
    }
}

data class PokemonSprites(
    val frontDefault: String?,
    val frontShiny: String?,
    val backDefault: String?,
    val backShiny: String?
) {
    fun getDisplayImage(): String? = frontDefault ?: frontShiny
}