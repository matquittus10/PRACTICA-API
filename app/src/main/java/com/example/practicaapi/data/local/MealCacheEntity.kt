package com.example.practicaapi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals_cache")
data class MealCacheEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val categoria: String,
    val pais: String,
    val instrucciones: String,
    val timestamp: Long = System.currentTimeMillis()
)
