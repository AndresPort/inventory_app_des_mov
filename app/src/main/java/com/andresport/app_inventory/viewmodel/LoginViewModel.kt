package com.andresport.app_inventory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    // LiveData para la navegación: notifica al Fragmento para que cambie de pantalla.
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    // LiveData para mensajes: notifica al Fragmento para que muestre un Toast.
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    /**
     * Llamado por el Callback cuando la autenticación es exitosa.
     */
    fun onAuthenticationSuccess() {
        // Lógica de negocio si existiera (ej. guardar sesión, obtener token) iría aquí.
        _loginSuccess.value = true
    }

    /**
     * Llamado por el Callback en caso de fallo, error o si no hay soporte.
     */
    fun onAuthenticationFailureOrError(message: String) {
        _toastMessage.value = message
    }
}