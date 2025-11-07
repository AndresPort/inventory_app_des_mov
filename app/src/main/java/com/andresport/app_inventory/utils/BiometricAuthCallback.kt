package com.andresport.app_inventory.utils

import androidx.biometric.BiometricPrompt
import com.andresport.app_inventory.viewmodel.LoginViewModel

/**
 * Clase que extiende BiometricPrompt.AuthenticationCallback.
 * Su única responsabilidad es recibir los resultados del SO y notificar al ViewModel.
 */
class BiometricAuthCallback(
    private val loginViewModel: LoginViewModel
) : BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        // Delega la lógica de mostrar el mensaje al ViewModel
        loginViewModel.onAuthenticationFailureOrError("Error ($errorCode): $errString")
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        // Delega la lógica de éxito al ViewModel (que activará la navegación)
        loginViewModel.onAuthenticationSuccess()
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        // Delega la lógica del mensaje de fallo al ViewModel
        loginViewModel.onAuthenticationFailureOrError("Huella no reconocida. Inténtalo de nuevo.")
    }
}