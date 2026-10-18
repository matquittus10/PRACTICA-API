package com.example.practicaapi.ui.theme.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaapi.data.AnimalRepository
import com.example.practicaapi.data.CoctelRepository
import com.example.practicaapi.data.FreeMealRepository
import com.example.practicaapi.model.AnimalExtinto
import com.example.practicaapi.model.Coctel
import com.example.practicaapi.model.PlatoComida
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mealRepository: FreeMealRepository = FreeMealRepository(),
    private val coctelRepository: CoctelRepository = CoctelRepository(),
    private val animalRepository: AnimalRepository = AnimalRepository()
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

    // API 3: Animales Extintos (Extinct API)
    var listaAnimales by mutableStateOf<List<AnimalExtinto>>(emptyList())
        private set

    var animal by mutableStateOf<AnimalExtinto?>(null)
        private set

    var cargandoAnimales by mutableStateOf(false)
        private set

    var errorAnimales by mutableStateOf<String?>(null)
        private set

    init {
        cargarComida()
        cargarCoctel()
        cargarAnimales()
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

    fun cargarAnimales() {
        viewModelScope.launch {
            cargandoAnimales = true
            errorAnimales = null
            try {
                val listaCompleta = animalRepository.obtenerAnimales()
                listaAnimales = listaCompleta
                val disponibles = listaCompleta.filter { it.nombre != animal?.nombre }
                animal = if (disponibles.isNotEmpty()) disponibles.random() else listaCompleta.randomOrNull()
            } catch (e: Exception) {
                errorAnimales = e.localizedMessage ?: "Error al cargar los animales extintos"
            } finally {
                cargandoAnimales = false
            }
        }
    }
}
