package com.andresport.app_inventory.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andresport.app_inventory.utils.SessionManager


// PASO 1: Definir los posibles estados de autenticación con una Sealed Class.
// Esto nos permite manejar todos los casos (logueado, no logueado, error) en un solo lugar.
sealed class AuthenticationState {
    object AUTHENTICATED : AuthenticationState()
    object UNAUTHENTICATED : AuthenticationState()
    data class AUTH_ERROR(val message: String) : AuthenticationState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    // PASO 2: Usar un único LiveData para manejar el estado.
    private val _authenticationState = MutableLiveData<AuthenticationState>()
    val authenticationState: LiveData<AuthenticationState> = _authenticationState

    // LiveData para mensajes Toast, esto lo mantenemos igual.
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    /**
     * Verifica si hay una sesión activa al iniciar la app.
     */
    fun checkUserLoggedIn() {
        val token = sessionManager.fetchAuthToken()
        if (!token.isNullOrEmpty()) {
            _authenticationState.value = AuthenticationState.AUTHENTICATED
        } else {
            _authenticationState.value = AuthenticationState.UNAUTHENTICATED
        }
    }

    /**
     * Se ejecuta cuando la autenticación biométrica es correcta.
     */
    fun onAuthenticationSuccess() {
        sessionManager.saveAuthToken("user_logged_in")
        _authenticationState.value = AuthenticationState.AUTHENTICATED
    }

    /**
     * Se ejecuta al fallar la autenticación.
     */
    fun onAuthenticationFailureOrError(message: String) {
        // En lugar de cambiar el estado principal, solo mostramos un mensaje.
        _toastMessage.value = message
    }

    /**
     * Cierra la sesión del usuario. Esta función será llamada desde InventarioFragment.
     */
    fun logout() {
        sessionManager.clearAuthToken() // Asumiendo que SessionManager tiene esta función.
        _authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }
}
