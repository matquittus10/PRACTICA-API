package com.example.practicaapi.data

import com.example.practicaapi.data.remote.DrinkDto
import com.example.practicaapi.model.Coctel

object MapeadorCoctel {
    fun aCoctel(dto: DrinkDto): Coctel {
        val esAlcoholico = dto.strAlcoholic?.equals("Alcoholic", ignoreCase = true) == true
        return Coctel(
            nombre = dto.strDrink.orEmpty(),
            categoria = dto.strCategory.orEmpty(),
            esAlcoholico = esAlcoholico,
            instrucciones = dto.strInstructions.orEmpty(),
            imagen = dto.strDrinkThumb.orEmpty()
        )
    }

    fun aListaCocteles(dtos: List<DrinkDto>?): List<Coctel> {
        return dtos?.map { aCoctel(it) } ?: emptyList()
    }
}
