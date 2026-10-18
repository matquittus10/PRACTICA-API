package com.example.practicaapi.data.remote

import com.google.gson.annotations.SerializedName

data class AnimalResponseDto(
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("data")
    val data: List<AnimalDto>? = null
)

data class AnimalDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("commonName")
    val commonName: String? = null,
    @SerializedName("binomialName")
    val binomialName: String? = null,
    @SerializedName("lastRecord")
    val lastRecord: String? = null,
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("shortDescription")
    val shortDesc: String? = null
)
