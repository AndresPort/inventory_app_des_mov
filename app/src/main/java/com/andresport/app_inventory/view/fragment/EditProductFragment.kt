package com.andresport.app_inventory.view.fragment


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andresport.app_inventory.R
import com.google.android.material.appbar.MaterialToolbar

class EditProductFragment : Fragment(R.layout.fragment_edit_product) {

    private val args: EditProductFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Criterio 1: Configuración de la Toolbar ---
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarEditProduct)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // --- Criterio 2: Mostrar el ID del Producto ---
        val tvProductValue = view.findViewById<TextView>(R.id.tvProductIdValue)

        // Asigna el valor del ID. Por ahora, mostrará el valor por defecto "-1"
        // si se llegara a esta pantalla sin pasar un ID.
        tvProductValue.text = args.productId.toString()
    }
}
