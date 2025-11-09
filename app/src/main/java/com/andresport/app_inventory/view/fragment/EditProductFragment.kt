package com.andresport.app_inventory.view.fragment


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.andresport.app_inventory.viewmodel.ViewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andresport.app_inventory.R
import com.google.android.material.appbar.MaterialToolbar

import com.andresport.app_inventory.data.AppDatabase
import com.andresport.app_inventory.repository.ProductRepository
import com.andresport.app_inventory.viewmodel.EditProductViewModel
import android.widget.EditText

class EditProductFragment : Fragment(R.layout.fragment_edit_product) {

    private val args: EditProductFragmentArgs by navArgs()

    private val viewModel: EditProductViewModel by viewModels {
        // Ahora esta fábrica es capaz de crear tu EditProductViewModel
        ViewModelFactory(
            ProductRepository(
                AppDatabase.getInstance(requireContext()).productDao()
            )
        )
    }

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

        // criterio 3

        // Obtener referencias a los EditText del layout
        val etName = view.findViewById<EditText>(R.id.etName)
        val etPrice = view.findViewById<EditText>(R.id.etPrice)
        val etQuantity = view.findViewById<EditText>(R.id.etQuantity)

        viewModel.product.observe(viewLifecycleOwner, Observer { product ->
            // Este bloque se ejecutará cada vez que el LiveData 'product' cambie.
            // Usamos 'product?.let' para ejecutar el código solo si el producto no es nulo.
            product?.let {
                // 3. Rellenar los campos de la UI con los datos recibidos
                etName.setText(it.productName) // Usamos 'productName' de tu data class
                etPrice.setText(it.unitPrice.toString()) // Usamos 'unitPrice'
                etQuantity.setText(it.stock.toString()) // Usamos 'stock'
            }
        })

        // Iniciar la carga de datos.

        viewModel.loadProduct(args.productId.toString())
    }
}
