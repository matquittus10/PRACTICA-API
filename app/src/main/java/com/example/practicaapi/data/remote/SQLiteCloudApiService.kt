package com.example.practicaapi.data.remote

import com.example.practicaapi.data.local.MealFavoritoEntity
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface SQLiteCloudApiService {

    @Headers("Content-Type: application/json")
    @POST("v2/weblink/sql")
    suspend fun obtenerFavoritosV2(
        @Header("Authorization") bearerToken: String,
        @Body request: SqlQueryRequest
    ): SqlQueryResponse<MealFavoritoEntity>

    @Headers("Content-Type: application/json")
    @POST("v2/weblink/sql")
    suspend fun ejecutarComandoV2(
        @Header("Authorization") bearerToken: String,
        @Body request: SqlQueryRequest
    ): SqlQueryResponse<Any>

    @Headers("Content-Type: application/json")
    @POST("v2/sql")
    suspend fun obtenerFavoritosSql(
        @Header("Authorization") bearerToken: String,
        @Body request: SqlQueryRequest
    ): SqlQueryResponse<MealFavoritoEntity>

    @Headers("Content-Type: application/json")
    @POST("v2/sql")
    suspend fun ejecutarComandoSql(
        @Header("Authorization") bearerToken: String,
        @Body request: SqlQueryRequest
    ): SqlQueryResponse<Any>
}
