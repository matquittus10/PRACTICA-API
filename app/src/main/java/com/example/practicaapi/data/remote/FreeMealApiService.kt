package com.example.practicaapi.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface FreeMealApiService {
    @GET("search.php")
    suspend fun buscarPlatosPorLetra(
        @Query("f") letra: String = "a"
    ): FreeMealResponseDto
}
