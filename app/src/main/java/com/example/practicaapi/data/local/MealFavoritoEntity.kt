package com.example.practicaapi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class MealFavoritoEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val categoria: String,
    val agregadoEn: Long = System.currentTimeMillis()
)
