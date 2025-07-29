package com.yudha.pokemonapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val email: String,
    val password: String,
    val profileImagePath: String? = null,
    val fullName: String? = null,
    val phoneNumber: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)