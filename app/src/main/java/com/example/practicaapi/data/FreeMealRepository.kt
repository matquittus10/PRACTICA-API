package com.example.practicaapi.data

import com.example.practicaapi.data.remote.FreeMealApiService
import com.example.practicaapi.data.remote.RetrofitClient
import com.example.practicaapi.model.PlatoComida

class FreeMealRepository(
    private val apiService: FreeMealApiService = RetrofitClient.api
) {
    suspend fun obtenerPlatosPorLetra(letra: String = "a"): List<PlatoComida> {
        val response = apiService.buscarPlatosPorLetra(letra)
        return MapeadorFreeMeal.aListaPlatos(response.meals)
    }
}
