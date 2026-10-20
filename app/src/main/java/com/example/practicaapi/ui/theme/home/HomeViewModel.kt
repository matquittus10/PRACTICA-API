package com.example.practicaapi.ui.theme.home

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaapi.data.AnimalRepository
import com.example.practicaapi.data.CoctelRepository
import com.example.practicaapi.data.FavoritoRepository
import com.example.practicaapi.data.FreeMealRepository
import com.example.practicaapi.data.MapeadorFreeMeal.aFavoritoEntity
import com.example.practicaapi.data.local.MealDatabase
import com.example.practicaapi.model.AnimalExtinto
import com.example.practicaapi.model.Coctel
import com.example.practicaapi.model.PlatoComida
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val mealRepository = FreeMealRepository()
    private val coctelRepository = CoctelRepository()
    private val animalRepository = AnimalRepository()
    private val favoritoRepository = FavoritoRepository(MealDatabase.obtener(application).favoritoDao())

    // API 1: Comidas (TheMealDB)
    var listaPlatos by mutableStateOf<List<PlatoComida>>(emptyList())
        private set

    var cargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    // Favoritos (Room)
    var idsFavoritos by mutableStateOf<Set<String>>(emptySet())
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
        observarFavoritos()
    }

    private fun observarFavoritos() {
        viewModelScope.launch {
            favoritoRepository.observarIdsFavoritos().collectLatest { ids ->
                idsFavoritos = ids.toSet()
            }
        }
    }

    fun alternarFavorito(plato: PlatoComida) {
        viewModelScope.launch {
            if (idsFavoritos.contains(plato.id)) {
                favoritoRepository.quitar(plato.id)
            } else {
                favoritoRepository.agregar(plato.aPlatoComidaResumen().aFavoritoEntity())
            }
        }
    }

    fun cargarComida() {
        viewModelScope.launch {
            cargando = true
            mensajeError = null
            try {
                listaPlatos = mealRepository.obtenerPlatosPorLetra("c")
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
