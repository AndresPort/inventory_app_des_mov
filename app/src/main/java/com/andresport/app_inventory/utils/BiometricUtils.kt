package com.andresport.app_inventory.utils // 👈 Usa tu nombre de paquete base

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS

// Clase de utilidad para verificar el estado del hardware de huella dactilar.
object BiometricUtils {

    /**
     * Verifica la disponibilidad de la biometría en el dispositivo.
     * Retorna el código de estado de BiometricManager.
     */
    fun checkBiometricSupport(context: Context): Int {
        // Obtenemos una instancia de BiometricManager para consultar el estado.
        val biometricManager = BiometricManager.from(context)

        // Verificamos si la autenticación de nivel fuerte (huella, rostro) es posible.
        return biometricManager.canAuthenticate(Authenticators.BIOMETRIC_STRONG)
    }

    /**
     * Mapea los códigos de estado/error biométricos a mensajes legibles.
     */
    fun getMessageForBiometricStatus(context: Context, status: Int): String {
        return when (status) {
            BIOMETRIC_SUCCESS -> "Autenticación biométrica lista."
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "El dispositivo no soporta la función de huella dactilar."
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "El hardware de huella está temporalmente no disponible. Intente de nuevo más tarde."
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "Debes registrar al menos una huella dactilar en la configuración de tu dispositivo para usar esta función."
            else -> "Error desconocido al verificar el soporte biométrico."
        }
    }
}