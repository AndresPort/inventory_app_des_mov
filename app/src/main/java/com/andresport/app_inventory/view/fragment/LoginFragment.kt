package com.andresport.app_inventory.view.fragment

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
import com.andresport.app_inventory.R
import com.andresport.app_inventory.databinding.FragmentLoginWindowBinding
import com.andresport.app_inventory.viewmodel.LoginViewModel
import com.andresport.app_inventory.utils.BiometricUtils
import com.andresport.app_inventory.utils.BiometricAuthCallback
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class LoginFragment : Fragment() {

    // Aquí se obtiene el objeto que contenga el resultado del binding con la vista.
    private lateinit var binding: FragmentLoginWindowBinding
    // Se inicializa el ViewModel que gestiona la lógica de esta vista.
    private val loginViewModel: LoginViewModel by viewModels()
    // Se declara un Executor para manejar la autenticación biométrica en un hilo separado.
    private lateinit var executor: Executor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Se infla la vista y se asigna al objeto binding.
        binding = FragmentLoginWindowBinding.inflate(inflater)
        // Se asigna el ciclo de vida del fragmento al binding para observar los LiveData.
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Se inicializa el Executor en un solo hilo.
        executor = Executors.newSingleThreadExecutor()
        
        // Se llama a la función para verificar si ya hay una sesión activa.
        loginViewModel.checkUserLoggedIn()
        
        // Se configuran los listeners de los elementos de la vista.
        controladores()
        // Se configuran los observadores de los LiveData del ViewModel.
        observadorViewModel()
    }

    private fun controladores() {
        // Listener para el clic en el icono de huella, que inicia la autenticación.
        binding.lottieFingerprintScan.setOnClickListener {
            checkBiometricSupportAndAuthenticate()
        }
    }

    private fun observadorViewModel(){
        // Observador que se activa si el usuario ya ha iniciado sesión.
        loginViewModel.isUserLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            // Si isLoggedIn es true, se navega a la pantalla de inventario.
            if (isLoggedIn) {
                findNavController().navigate(R.id.action_LoginFragment_to_InventarioFragment)
            }
        }

        // Observador que se activa cuando el inicio de sesión es exitoso.
        loginViewModel.loginSuccess.observe(viewLifecycleOwner) { isSuccess ->
            // Si isSuccess es true, se navega a la pantalla de inventario.
            if (isSuccess) {
                findNavController().navigate(R.id.action_LoginFragment_to_InventarioFragment)
            }
        }

        // Observador que muestra un mensaje (Toast) enviado desde el ViewModel.
        loginViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Esta función es para verificar si el dispositivo soporta autenticación biométrica.
     * Si hay soporte, muestra el diálogo de autenticación.
     */
    private fun checkBiometricSupportAndAuthenticate() {
        // Se obtiene el estado del soporte biométrico del dispositivo.
        val supportStatus = BiometricUtils.checkBiometricSupport(requireContext())

        // Si el soporte es exitoso, se muestra el diálogo biométrico.
        if (supportStatus == BiometricManager.BIOMETRIC_SUCCESS) {
            showBiometricPrompt()
        } else {
            // Si no hay soporte, se obtiene un mensaje de error y se envía al ViewModel.
            val message = BiometricUtils.getMessageForBiometricStatus(requireContext(), supportStatus)
            loginViewModel.onAuthenticationFailureOrError(message)
        }
    }

    /**
     * Esta función es para mostrar el diálogo de autenticación biométrica.
     * Configura el título, subtítulo y otros textos del diálogo.
     */
    private fun showBiometricPrompt() {
        // Se crea la información del diálogo biométrico.
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Acceso a Inventory")
            .setSubtitle("Usa tu huella para acceder sin contraseña")
            .setNegativeButtonText("Usar otro método")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        // Se crea el objeto BiometricPrompt, pasando el fragmento, el executor y el callback.
        val biometricPrompt = BiometricPrompt(
            this, 
            executor,
            BiometricAuthCallback(loginViewModel)
        )

        // Se inicia la autenticación biométrica.
        biometricPrompt.authenticate(promptInfo)
    }
}
