package com.example.practicaapi.data

import com.example.practicaapi.data.local.MealFavoritoEntity
import com.example.practicaapi.data.remote.MealDto
import com.example.practicaapi.model.PlatoComida
import com.example.practicaapi.model.PlatoComidaResumen

object MapeadorFreeMeal {
    fun aPlatoComida(dto: MealDto): PlatoComida {
        return PlatoComida(
            id = dto.idMeal.orEmpty(),
            nombre = dto.strMeal.orEmpty(),
            categoria = dto.strCategory.orEmpty(),
            pais = dto.strArea.orEmpty(),
            instrucciones = dto.strInstructions.orEmpty()
        )
    }

    fun aListaPlatos(dtos: List<MealDto>?): List<PlatoComida> {
        return dtos?.map { aPlatoComida(it) } ?: emptyList()
    }

    fun MealFavoritoEntity.aPlatoComida(): PlatoComida {
        return PlatoComida(
            id = id,
            nombre = nombre,
            categoria = categoria,
            pais = "",
            instrucciones = ""
        )
    }

    fun PlatoComidaResumen.aFavoritoEntity(): MealFavoritoEntity {
        return MealFavoritoEntity(
            id = id,
            nombre = nombre,
            categoria = categoria
        )
    }
}
