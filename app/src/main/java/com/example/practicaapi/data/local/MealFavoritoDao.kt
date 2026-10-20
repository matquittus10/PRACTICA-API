package com.example.practicaapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealFavoritoDao {
    @Query("SELECT * FROM favoritos ORDER BY agregadoEn DESC")
    fun obtenerFavoritos(): Flow<List<MealFavoritoEntity>>

    @Query("SELECT id FROM favoritos")
    fun observarIdsFavoritos(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(favorito: MealFavoritoEntity)

    @Query("DELETE FROM favoritos WHERE id = :id")
    suspend fun eliminarPorId(id: String)
}
