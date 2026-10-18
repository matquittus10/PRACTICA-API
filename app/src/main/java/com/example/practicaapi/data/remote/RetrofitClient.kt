package com.example.practicaapi.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Única instancia de Retrofit de la app (patrón singleton con `object`). */
object RetrofitClient {

    // Debe terminar en "/" para que Retrofit pueda concatenar las rutas relativas.
    private const val BASE_URL_MEAL = "https://www.themealdb.com/api/json/v1/1/"
    private const val BASE_URL_COCKTAIL = "https://www.thecocktaildb.com/api/json/v1/1/"
    private const val BASE_URL_ANIMAL = "https://extinct-api.herokuapp.com/api/v1/"

    private val clienteHttp: OkHttpClient = OkHttpClient.Builder()
        // Muestra cada petición en Logcat (filtrar por "OkHttp"): útil para depurar y para clase.
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    // API 1: Platos de Comida (TheMealDB)
    val api: FreeMealApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_MEAL)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create()) // JSON <-> objetos Kotlin
            .build()
            .create(FreeMealApiService::class.java)
    }

    // API 2: Cócteles (TheCocktailDB)
    val cocktailApi: CocktailApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_COCKTAIL)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create()) // JSON <-> objetos Kotlin
            .build()
            .create(CocktailApiService::class.java)
    }

    // API 3: Animales Extintos (Extinct API)
    val animalApi: AnimalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_ANIMAL)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create()) // JSON <-> objetos Kotlin
            .build()
            .create(AnimalApiService::class.java)
    }
}
