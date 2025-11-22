package com.andresport.app_inventory.view

import android.R.attr.enabled
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.andresport.app_inventory.R
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var auth: FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()


        val emailEditText = view.findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<TextInputEditText>(R.id.passwordEditText)
        val passwordInputLayout = view.findViewById<TextInputLayout>(R.id.passwordInputLayout)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val registerButton = view.findViewById<Button>(R.id.registerButton)


        // Función para actualizar estado del botón
        fun updateLoginButtonState() {

            val enabled = emailEditText.text?.isNotEmpty() == true &&
                    (passwordEditText.text?.length ?: 0) >= 6


            loginButton.isEnabled = enabled

            // Cambiar color y estilo del texto según el estado
            if (enabled) {
                loginButton.setTextColor(Color.WHITE)
                loginButton.setTypeface(null, Typeface.BOLD)
            } else {
                loginButton.setTextColor(Color.parseColor("#B0B0B0")) // gris
                loginButton.setTypeface(null, Typeface.NORMAL)
            }
            // Activar o desactivar botón Registrarse
            registerButton.isEnabled = enabled
            // Cambiar su color según estado
            if (enabled) {
                registerButton.setTextColor(Color.WHITE)
                registerButton.setTypeface(null, Typeface.BOLD)
            } else {
                registerButton.setTextColor(Color.parseColor("#9EA1A1")) // gris
                registerButton.setTypeface(null, Typeface.NORMAL)
            }
        }

        // Escuchar cambios en los campos
        emailEditText.addTextChangedListener { updateLoginButtonState() }
        passwordEditText.addTextChangedListener { updateLoginButtonState() }
        passwordEditText.addTextChangedListener { text ->
            val input = text.toString()

            // Solo números
            if (input.any { !it.isDigit() }) {
                passwordInputLayout.error = "Solo números"
                return@addTextChangedListener
            }

            // Validación mínima
            if (input.length < 6) {
                passwordInputLayout.error = "Mínimo 6 dígitos"
                passwordInputLayout.setBoxStrokeColor(
                    ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
                )
            } else {
                passwordInputLayout.error = null
                passwordInputLayout.setBoxStrokeColor(
                    ContextCompat.getColor(requireContext(), android.R.color.white)
                )
            }
        }
        // Código del Paso 3: iniciar sesión con Firebase
        loginButton.setOnClickListener {
            val email = emailEditText.text?.toString()?.trim() ?: ""
            val password = passwordEditText.text?.toString() ?: ""

            // Validación local mínima
            if (email.isEmpty() || password.length < 6) {
                Toast.makeText(requireContext(), "Login incorrecto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación real usando Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val action = LoginFragmentDirections.actionLoginFragmentToInventarioFragment()
                        findNavController().navigate(action)
                    } else {
                        val errorMessage = when (task.exception?.message) {
                            "The email address is badly formatted." -> "Formato de correo inválido"
                            "There is no user record corresponding to this identifier." -> "Usuario no registrado"
                            "The password is invalid or the user does not have a password." -> "Contraseña incorrecta"
                            else -> "Login incorrecto"
                        }

                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }

        }
        registerButton.setOnClickListener {
            val email = emailEditText.text?.toString()?.trim() ?: ""
            val password = passwordEditText.text?.toString() ?: ""

            if (email.isEmpty() || password.length < 6) {
                Toast.makeText(requireContext(), "Error en el registro", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Aquí aún no navegamos (eso es el criterio 14)
                    } else {
                        Toast.makeText(requireContext(), "Error en el registro", Toast.LENGTH_SHORT).show()
                    }
                }
        }


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}