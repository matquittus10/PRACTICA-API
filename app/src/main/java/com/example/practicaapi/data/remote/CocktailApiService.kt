package com.example.practicaapi.data.remote

import retrofit2.http.GET

interface CocktailApiService {
    @GET("random.php")
    suspend fun obtenerCoctelAleatorio(): CoctelResponseDto
}
