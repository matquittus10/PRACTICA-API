package com.example.practicaapi.data

import com.example.practicaapi.data.local.MealCacheDao
import com.example.practicaapi.data.local.MealCacheEntity
import com.example.practicaapi.data.remote.FreeMealApiService
import com.example.practicaapi.data.remote.RetrofitClient
import com.example.practicaapi.model.PlatoComida

class FreeMealRepository(
    private val apiService: FreeMealApiService = RetrofitClient.api,
    private val cacheDao: MealCacheDao? = null
) {
    suspend fun obtenerPlatosPorLetra(letra: String): List<PlatoComida> {
        return try {
            val response = apiService.buscarPlatosPorLetra(letra)
            val lista = MapeadorFreeMeal.aListaPlatos(response.meals)
            if (lista.isNotEmpty() && cacheDao != null) {
                val entidades = lista.map { plato ->
                    MealCacheEntity(
                        id = plato.id,
                        nombre = plato.nombre,
                        categoria = plato.categoria,
                        pais = plato.pais,
                        instrucciones = plato.instrucciones
                    )
                }
                cacheDao.limpiarCache()
                cacheDao.insertarTodos(entidades)
            }
            lista
        } catch (e: Exception) {
            if (cacheDao != null) {
                val cache = cacheDao.obtenerMealsCache()
                if (cache.isNotEmpty()) {
                    return cache.map { entity ->
                        PlatoComida(
                            id = entity.id,
                            nombre = entity.nombre,
                            categoria = entity.categoria,
                            pais = entity.pais,
                            instrucciones = entity.instrucciones
                        )
                    }
                }
            }
            throw e
        }
    }
}
