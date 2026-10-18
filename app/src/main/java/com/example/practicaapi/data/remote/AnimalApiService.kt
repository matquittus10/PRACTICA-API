package com.example.practicaapi.data.remote

import retrofit2.http.GET

interface AnimalApiService {
    @GET("animal/")
    suspend fun obtenerAnimales(): AnimalResponseDto
}
