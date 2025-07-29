package com.yudha.pokemonapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    @SerializedName("base_experience")
    val baseExperience: Int?,
    val abilities: List<PokemonAbility>,
    val types: List<PokemonType>,
    val stats: List<PokemonStat>,
    val sprites: PokemonSprites
)

data class PokemonAbility(
    val ability: AbilityInfo,
    @SerializedName("is_hidden")
    val isHidden: Boolean,
    val slot: Int
)

data class AbilityInfo(
    val name: String,
    val url: String
)

data class PokemonType(
    val slot: Int,
    val type: TypeInfo
)

data class TypeInfo(
    val name: String,
    val url: String
)

data class PokemonStat(
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatInfo
)

data class StatInfo(
    val name: String,
    val url: String
)

data class PokemonSprites(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("front_shiny")
    val frontShiny: String?,
    @SerializedName("back_default")
    val backDefault: String?,
    @SerializedName("back_shiny")
    val backShiny: String?,
    val other: OtherSprites?
)

data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork?
)

data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String?
)