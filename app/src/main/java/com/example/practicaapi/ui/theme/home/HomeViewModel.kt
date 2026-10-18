package com.example.practicaapi.ui.theme.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaapi.data.CoctelRepository
import com.example.practicaapi.data.FreeMealRepository
import com.example.practicaapi.model.Coctel
import com.example.practicaapi.model.PlatoComida
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mealRepository: FreeMealRepository = FreeMealRepository(),
    private val coctelRepository: CoctelRepository = CoctelRepository()
) : ViewModel() {

    // API 1: Comidas (TheMealDB)
    var listaPlatos by mutableStateOf<List<PlatoComida>>(emptyList())
        private set

    var cargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    // API 2: Cócteles (TheCocktailDB)
    var coctel by mutableStateOf<Coctel?>(null)
        private set

    var cargandoCoctel by mutableStateOf(false)
        private set

    var errorCoctel by mutableStateOf<String?>(null)
        private set

    init {
        cargarComida()
        cargarCoctel()
    }

    fun cargarComida() {
        viewModelScope.launch {
            cargando = true
            mensajeError = null
            try {
                listaPlatos = mealRepository.obtenerPlatosPorLetra("b")
            } catch (e: Exception) {
                mensajeError = e.localizedMessage ?: "Error al cargar la comida"
            } finally {
                cargando = false
            }
        }
    }

    fun cargarCoctel() {
        viewModelScope.launch {
            cargandoCoctel = true
            errorCoctel = null
            try {
                coctel = coctelRepository.obtenerCoctelAleatorio()
            } catch (e: Exception) {
                errorCoctel = e.localizedMessage ?: "Error al cargar el cóctel"
            } finally {
                cargandoCoctel = false
            }
        }
    }
}
