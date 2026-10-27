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
        // Muestra cada petición en Logcat con encabezados y cuerpo para depuración.
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
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

    // API 4: SQLiteCloud (Base de datos en la nube REST v2)
    private const val BASE_URL_SQLITE_CLOUD = "https://cs3ol83cvz.g3.sqlite.cloud/"
    const val SQLITE_CLOUD_API_KEY = "Bearer biwUFNw8AGAVxwIHBqKWoKj4muR7lfDvB8JkqorAskY"
    const val SQLITE_CLOUD_DB_NAME = "meal_db.sqlite"

    val sqliteCloudApi: SQLiteCloudApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_SQLITE_CLOUD)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SQLiteCloudApiService::class.java)
    }
}
