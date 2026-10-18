package com.example.practicaapi.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Unica instancia de Retrofit de la app (patron singleton con `object`). */
object RetrofitClient {

    // Debe terminar en "/" para que Retrofit pueda concatenar las rutas relativas.
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    private val clienteHttp: OkHttpClient = OkHttpClient.Builder()
        // Muestra cada peticion en Logcat (filtrar por "OkHttp"): util para depurar y para clase.
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    val api: FreeMealApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create()) // JSON <-> objetos Kotlin
            .build()
            .create(FreeMealApiService::class.java)
    }
}
