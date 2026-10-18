package com.example.practicaapi.data.remote

import com.google.gson.annotations.SerializedName

data class FreeMealResponseDto(
    @SerializedName("meals")
    val meals: List<MealDto>? = null
)

data class MealDto(
    @SerializedName("idMeal")
    val idMeal: String? = null,
    @SerializedName("strMeal")
    val strMeal: String? = null,
    @SerializedName("strCategory")
    val strCategory: String? = null,
    @SerializedName("strArea")
    val strArea: String? = null,
    @SerializedName("strInstructions")
    val strInstructions: String? = null
)
