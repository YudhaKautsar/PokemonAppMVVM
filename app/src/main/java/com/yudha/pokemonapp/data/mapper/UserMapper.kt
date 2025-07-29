package com.yudha.pokemonapp.data.mapper

import com.yudha.pokemonapp.data.entity.User as DataUser
import com.yudha.pokemonapp.domain.entity.User as DomainUser
import com.yudha.pokemonapp.domain.entity.UserId
import com.yudha.pokemonapp.domain.entity.Username
import com.yudha.pokemonapp.domain.entity.Email
import com.yudha.pokemonapp.domain.entity.UserProfile

/**
 * Mapper between Data and Domain User entities
 * Handles conversion between different layers
 */
object UserMapper {
    
    /**
     * Convert Data User to Domain User
     */
    fun toDomain(dataUser: DataUser): DomainUser {
        return DomainUser(
            id = UserId.of(dataUser.id),
            username = Username(dataUser.username),
            email = Email(dataUser.email),
            profile = UserProfile(
                fullName = dataUser.fullName,
                phoneNumber = dataUser.phoneNumber,
                profileImagePath = dataUser.profileImagePath,
                createdAt = dataUser.createdAt
            )
        )
    }
    
    /**
     * Convert Domain User to Data User
     */
    fun toData(domainUser: DomainUser, hashedPassword: String): DataUser {
        return DataUser(
            id = domainUser.id.value,
            username = domainUser.username.value,
            email = domainUser.email.value,
            password = hashedPassword,
            fullName = domainUser.profile.fullName,
            phoneNumber = domainUser.profile.phoneNumber,
            profileImagePath = domainUser.profile.profileImagePath,
            createdAt = domainUser.profile.createdAt
        )
    }
    
    /**
     * Create Data User for registration
     */
    fun createDataUser(
        username: String,
        email: String,
        hashedPassword: String,
        fullName: String? = null,
        phoneNumber: String? = null,
        profileImagePath: String? = null
    ): DataUser {
        return DataUser(
            username = username,
            email = email,
            password = hashedPassword,
            fullName = fullName,
            phoneNumber = phoneNumber,
            profileImagePath = profileImagePath
        )
    }
}