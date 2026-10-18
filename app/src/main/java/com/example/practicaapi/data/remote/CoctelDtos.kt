package com.example.practicaapi.data.remote

import com.google.gson.annotations.SerializedName

data class CoctelResponseDto(
    @SerializedName("drinks")
    val drinks: List<DrinkDto>? = null
)

data class DrinkDto(
    @SerializedName("idDrink")
    val idDrink: String? = null,
    @SerializedName("strDrink")
    val strDrink: String? = null,
    @SerializedName("strCategory")
    val strCategory: String? = null,
    @SerializedName("strAlcoholic")
    val strAlcoholic: String? = null,
    @SerializedName("strInstructions")
    val strInstructions: String? = null,
    @SerializedName("strDrinkThumb")
    val strDrinkThumb: String? = null
)
