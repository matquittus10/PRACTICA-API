package com.example.practicaapi.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaapi.data.AuthRepository
import com.example.practicaapi.model.Usuario
import com.example.practicaapi.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado inmutable del formulario de inicio de sesion y registro. */
data class AuthUiState(
    val email: String = "",
    val contrasena: String = "",
    val modoRegistro: Boolean = false,
    val mostrarContrasena: Boolean = false,
    val cargando: Boolean = false,
    val error: String? = null,
    val info: String? = null
)

class AuthViewModel(
    private val repositorio: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    /**
     * Usuario con sesion abierta (null si no hay). El valor inicial sale de `usuarioActual()`,
     * asi la app sabe desde el primer fotograma si debe mostrar el login o la lista.
     */
    val usuario: StateFlow<Usuario?> = repositorio.observarSesion()
        .stateIn(viewModelScope, SharingStarted.Eagerly, repositorio.usuarioActual())

    fun onEmailCambia(valor: String) = _uiState.update { it.copy(email = valor, error = null, info = null) }

    fun onContrasenaCambia(valor: String) = _uiState.update { it.copy(contrasena = valor, error = null, info = null) }

    fun alternarMostrarContrasena() = _uiState.update { it.copy(mostrarContrasena = !it.mostrarContrasena) }

    fun alternarModo() = _uiState.update { it.copy(modoRegistro = !it.modoRegistro, error = null, info = null) }

    fun enviar() {
        val estado = _uiState.value
        val email = estado.email.trim()
        validar(email, estado.contrasena)?.let { mensaje ->
            _uiState.update { it.copy(error = mensaje) }
            return
        }
        _uiState.update { it.copy(cargando = true, error = null, info = null) }
        viewModelScope.launch {
            val resultado = if (estado.modoRegistro) {
                repositorio.registrar(email, estado.contrasena)
            } else {
                repositorio.iniciarSesion(email, estado.contrasena)
            }
            // Si sale bien no hay nada mas que hacer aqui: `usuario` cambia y la navegacion reacciona.
            _uiState.update {
                when (resultado) {
                    is Resultado.Exito -> it.copy(cargando = false, contrasena = "")
                    is Resultado.Error -> it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }

    fun recuperarContrasena() {
        val email = _uiState.value.email.trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(error = "Escribe tu correo para enviarte el enlace de recuperación.") }
            return
        }
        _uiState.update { it.copy(cargando = true, error = null, info = null) }
        viewModelScope.launch {
            val resultado = repositorio.enviarCorreoDeRecuperacion(email)
            _uiState.update {
                when (resultado) {
                    is Resultado.Exito -> it.copy(cargando = false, info = "Te enviamos un correo para restablecer la contraseña.")
                    is Resultado.Error -> it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }

    fun cerrarSesion() {
        repositorio.cerrarSesion()
        _uiState.value = AuthUiState()
    }

    /** Validacion en el telefono: ahorra una llamada a Firebase cuando el dato es obviamente malo. */
    private fun validar(email: String, contrasena: String): String? = when {
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Escribe un correo válido."
        contrasena.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
        else -> null
    }
}
