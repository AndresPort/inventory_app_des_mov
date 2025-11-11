package com.andresport.app_inventory.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andresport.app_inventory.utils.SessionManager

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // Se inicializa el gestor de sesión para manejar los datos del usuario.
    private val sessionManager = SessionManager(application)

    // LiveData que notifica a la vista cuando el inicio de sesión es exitoso.
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    // LiveData que envía mensajes a la vista para ser mostrados.
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    // LiveData que comunica a la vista si ya existe una sesión activa.
    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> = _isUserLoggedIn

    /**
     * Esta función es para verificar si hay una sesión de usuario guardada.
     * Se obtiene el token de autenticación y se actualiza el LiveData _isUserLoggedIn.
     */
    fun checkUserLoggedIn() {
        // Aquí se obtiene el token guardado en SharedPreferences.
        val token = sessionManager.fetchAuthToken()
        // Se notifica a la vista si el token existe o no.
        _isUserLoggedIn.value = !token.isNullOrEmpty()
    }

    /**
     * Esta función se ejecuta cuando la autenticación biométrica es correcta.
     * Se guarda un token de sesión para mantener al usuario conectado.
     */
    fun onAuthenticationSuccess() {
        // Se guarda un token para identificar que la sesión está activa.
        sessionManager.saveAuthToken("user_logged_in") 
        // Se notifica a la vista que el login fue exitoso para navegar a la siguiente pantalla.
        _loginSuccess.value = true
    }

    /**
     * La función recibe un mensaje de error o fallo en la autenticación.
     * Se asigna el mensaje al LiveData para que la vista lo muestre.
     */
    fun onAuthenticationFailureOrError(message: String) {
        _toastMessage.value = message
    }
}
