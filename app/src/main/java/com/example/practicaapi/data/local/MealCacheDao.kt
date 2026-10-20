package com.example.practicaapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealCacheDao {
    @Query("SELECT * FROM meals_cache")
    suspend fun obtenerMealsCache(): List<MealCacheEntity>

    @Query("SELECT * FROM meals_cache")
    fun observarMealsCache(): Flow<List<MealCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(meals: List<MealCacheEntity>)

    @Query("DELETE FROM meals_cache")
    suspend fun limpiarCache()
}
