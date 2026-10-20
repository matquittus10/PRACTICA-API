package com.example.practicaapi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MealFavoritoEntity::class, MealCacheEntity::class],
    version = 2,
    exportSchema = false
)
abstract class MealDatabase : RoomDatabase() {
    abstract fun favoritoDao(): MealFavoritoDao
    abstract fun cacheDao(): MealCacheDao

    companion object {
        @Volatile
        private var INSTANCE: MealDatabase? = null

        fun obtener(context: Context): MealDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "meal_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
