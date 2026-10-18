package com.example.practicaapi.data

import com.example.practicaapi.data.remote.AnimalApiService
import com.example.practicaapi.data.remote.RetrofitClient
import com.example.practicaapi.model.AnimalExtinto

class AnimalRepository(
    private val apiService: AnimalApiService = RetrofitClient.animalApi
) {
    suspend fun obtenerAnimales(): List<AnimalExtinto> {
        val response = apiService.obtenerAnimales()
        return MapeadorAnimal.aListaAnimales(response.data)
    }
}
