package com.example.practicaapi.model

data class PlatoComida(
    val id: String,
    val nombre: String,
    val categoria: String,
    val pais: String,
    val instrucciones: String,
) {
    fun aPlatoComidaResumen(): PlatoComidaResumen {
        return PlatoComidaResumen(
            id = id,
            nombre = nombre,
            categoria = categoria
        )
    }
}

data class PlatoComidaResumen(
    val id: String,
    val nombre: String,
    val categoria: String
)
