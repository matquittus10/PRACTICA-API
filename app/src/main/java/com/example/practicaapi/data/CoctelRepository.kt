package com.example.practicaapi.data

import com.example.practicaapi.data.remote.CocktailApiService
import com.example.practicaapi.data.remote.RetrofitClient
import com.example.practicaapi.model.Coctel

class CoctelRepository(
    private val apiService: CocktailApiService = RetrofitClient.cocktailApi
) {
    suspend fun obtenerCoctelAleatorio(): Coctel? {
        val response = apiService.obtenerCoctelAleatorio()
        val lista = MapeadorCoctel.aListaCocteles(response.drinks)
        return lista.firstOrNull()
    }
}
