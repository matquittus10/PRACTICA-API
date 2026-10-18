package com.example.practicaapi.data

import com.example.practicaapi.data.remote.AnimalDto
import com.example.practicaapi.model.AnimalExtinto

object MapeadorAnimal {
    fun aAnimalExtinto(dto: AnimalDto): AnimalExtinto {
        return AnimalExtinto(
            nombre = dto.commonName.orEmpty(),
            nombreCientifico = dto.binomialName.orEmpty(),
            ubicacion = dto.location.orEmpty(),
            ultimoRegistro = dto.lastRecord.orEmpty(),
            descripcion = dto.shortDesc.orEmpty()
        )
    }

    fun aListaAnimales(dtos: List<AnimalDto>?): List<AnimalExtinto> {
        return dtos?.map { aAnimalExtinto(it) } ?: emptyList()
    }
}
