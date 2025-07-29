package com.yudha.pokemonapp.util

interface CrashlyticsHelper {
    fun log(message: String)
    fun recordException(throwable: Throwable)
}