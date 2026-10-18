package com.example.practicaapi.data

import com.example.practicaapi.data.remote.MealDto
import com.example.practicaapi.model.PlatoComida

object MapeadorFreeMeal {
    fun aPlatoComida(dto: MealDto): PlatoComida {
        return PlatoComida(
            nombre = dto.strMeal.orEmpty(),
            categoria = dto.strCategory.orEmpty(),
            pais = dto.strArea.orEmpty(),
            instrucciones = dto.strInstructions.orEmpty()
        )
    }

    fun aListaPlatos(dtos: List<MealDto>?): List<PlatoComida> {
        return dtos?.map { aPlatoComida(it) } ?: emptyList()
    }
}
