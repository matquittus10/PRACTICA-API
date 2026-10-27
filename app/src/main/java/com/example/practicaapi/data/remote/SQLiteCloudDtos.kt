package com.example.practicaapi.data.remote

import com.google.gson.annotations.SerializedName

data class SqlQueryRequest(
    @SerializedName("database") val database: String = "meal_db.sqlite",
    @SerializedName("sql") val sql: String
)

data class SqlQueryResponse<T>(
    @SerializedName("data") val data: List<T>? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("error") val error: String? = null
)
