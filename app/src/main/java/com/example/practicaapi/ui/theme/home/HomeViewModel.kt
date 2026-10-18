package com.example.practicaapi.ui.theme.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaapi.data.FreeMealRepository
import com.example.practicaapi.model.PlatoComida
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: FreeMealRepository = FreeMealRepository()
) : ViewModel() {

    var listaPlatos by mutableStateOf<List<PlatoComida>>(emptyList())
        private set

    var cargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    init {
        cargarComida()
    }

    fun cargarComida() {
        viewModelScope.launch {
            cargando = true
            mensajeError = null
            try {
                listaPlatos = repository.obtenerPlatosPorLetra("b")
            } catch (e: Exception) {
                mensajeError = e.localizedMessage ?: "Error al cargar la comida"
            } finally {
                cargando = false
            }
        }
    }
}
