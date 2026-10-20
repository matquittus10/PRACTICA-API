package com.example.practicaapi.data

import com.example.practicaapi.data.local.MealFavoritoDao
import com.example.practicaapi.data.local.MealFavoritoEntity
import kotlinx.coroutines.flow.Flow

class FavoritoRepository(private val dao: MealFavoritoDao) {
    fun observarFavoritos(): Flow<List<MealFavoritoEntity>> {
        return dao.obtenerFavoritos()
    }

    fun observarIdsFavoritos(): Flow<List<String>> {
        return dao.observarIdsFavoritos()
    }

    suspend fun agregar(favorito: MealFavoritoEntity) {
        dao.insertar(favorito)
    }

    suspend fun quitar(id: String) {
        dao.eliminarPorId(id)
    }
}
