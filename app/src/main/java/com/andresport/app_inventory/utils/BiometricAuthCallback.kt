package com.andresport.app_inventory.utils
import android.os.Handler
import android.os.Looper
import androidx.biometric.BiometricPrompt
import com.andresport.app_inventory.viewmodel.LoginViewModel

class BiometricAuthCallback(
    private val viewModel: LoginViewModel
) : BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)

        // 🔑 Esto garantiza que el cambio se haga en el hilo principal
        Handler(Looper.getMainLooper()).post {
            viewModel.onAuthenticationSuccess()
        }
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        Handler(Looper.getMainLooper()).post {
            viewModel.onAuthenticationFailureOrError(errString.toString())
        }
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        Handler(Looper.getMainLooper()).post {
            viewModel.onAuthenticationFailureOrError("Huella no reconocida. Intenta de nuevo.")
        }
    }
}
