package com.andresport.app_inventory.utils

/**
 * Esta clase es para gestionar la sesión del usuario en memoria.
 * La sesión se borrará cuando la aplicación se cierre completamente (proceso terminado).
 * Se encarga de guardar, recuperar y borrar el token de autenticación.
 */
object SessionManager {

    private var authToken: String? = null

    /**
     * La función recibe un token y lo guarda en memoria.
     * Esto mantiene la sesión del usuario activa mientras la app esté viva.
     */
    fun saveAuthToken(token: String) {
        authToken = token
    }

    /**
     * Esta función es para obtener el token de autenticación guardado en memoria.
     * Retorna el token si existe, de lo contrario retorna null.
     */
    fun fetchAuthToken(): String? {
        return authToken
    }

    /**
     * Esta función es para borrar el token de autenticación de la memoria.
     * Se utiliza para cerrar la sesión del usuario.
     */
    fun clearAuthToken() {
        authToken = null
    }
}
