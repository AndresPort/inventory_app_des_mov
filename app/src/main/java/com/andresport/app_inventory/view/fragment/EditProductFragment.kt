package com.andresport.app_inventory.view.fragment


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.andresport.app_inventory.R

// El R.layout.fragment_edit_product debe coincidir con el nombre de tu archivo XML
class EditProductFragment : Fragment(R.layout.fragment_edit_product) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Inicio---
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarEditProduct)
        toolbar.setNavigationOnClickListener {
            // Esta línea utiliza el Navigation Component para regresar a la pantalla anterior
            // (HU 5.0: Detalle del producto)
            findNavController().navigateUp()
        }

    }
}
