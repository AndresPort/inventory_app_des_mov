package com.andresport.app_inventory.view.fragment // <-- Tu paquete base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

// --- TUS IMPORTACIONES ---
import com.andresport.app_inventory.R
import com.andresport.app_inventory.databinding.FragmentLoginWindowBinding
import com.andresport.app_inventory.viewmodel.LoginViewModel // 1. ViewModel
import com.andresport.app_inventory.utils.BiometricUtils     // 2. Utils (el Object)
import com.andresport.app_inventory.utils.BiometricAuthCallback // 3. Callback
// --- FIN DE TUS IMPORTACIONES ---

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginWindowBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var executor: Executor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 1. Inflar el binding
        binding = FragmentLoginWindowBinding.inflate(inflater)

        // 2. 🔑 CORRECCIÓN: Usar viewLifecycleOwner, que es el LifecycleOwner correcto para las vistas del Fragment.
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializa el Executor para el biométrico
        executor = Executors.newSingleThreadExecutor()
        controladores()
        observadorViewModel()
    }

    private fun controladores() {
        // Clic en la animación Lottie
        binding.lottieFingerprintScan.setOnClickListener {
            checkBiometricSupportAndAuthenticate()
        }
    }

    private fun observadorViewModel(){
        // 1. Observa el comando de navegación (disparado por el ViewModel en caso de éxito)
        loginViewModel.loginSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                findNavController().navigate(R.id.action_LoginFragment_to_InventarioFragment)
            }
        }

        // 2. Observa el comando de Toast (disparado por el ViewModel en caso de fallo/error)
        loginViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // ----------------------------------------------------------------------------------
    // Métodos del Biométrico
    // ----------------------------------------------------------------------------------

    private fun checkBiometricSupportAndAuthenticate() {
        val supportStatus = BiometricUtils.checkBiometricSupport(requireContext())

        if (supportStatus == BiometricManager.BIOMETRIC_SUCCESS) {
            showBiometricPrompt()
        } else {
            // Si no hay soporte, notifica el error de inmediato
            val message = BiometricUtils.getMessageForBiometricStatus(requireContext(), supportStatus)
            loginViewModel.onAuthenticationFailureOrError(message)
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Acceso a Inventory")
            .setSubtitle("Usa tu huella para acceder sin contraseña")
            .setNegativeButtonText("Usar otro método")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        val biometricPrompt = BiometricPrompt(
            this, // El Fragmento es el LifecycleOwner
            executor,
            BiometricAuthCallback(loginViewModel) // 🔑 La clave: Pasa el ViewModel al Callback
        )

        biometricPrompt.authenticate(promptInfo)
    }
}