package com.yudha.pokemonapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yudha.pokemonapp.data.dao.UserDao
import com.yudha.pokemonapp.data.dao.PokemonDao
import com.yudha.pokemonapp.data.entity.User
import com.yudha.pokemonapp.data.local.entity.PokemonEntity

@Database(
    entities = [User::class, PokemonEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun pokemonDao(): PokemonDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `pokemon` (" +
                        "`id` INTEGER NOT NULL, " +
                        "`name` TEXT NOT NULL, " +
                        "`imageUrl` TEXT NOT NULL, " +
                        "`offset` INTEGER NOT NULL DEFAULT 0, " +
                        "PRIMARY KEY(`id`))"
                )
            }
        }
        
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN profileImagePath TEXT")
                database.execSQL("ALTER TABLE users ADD COLUMN fullName TEXT")
                database.execSQL("ALTER TABLE users ADD COLUMN phoneNumber TEXT")
            }
        }
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pokemon_app_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}