package com.andresport.app_inventory.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andresport.app_inventory.R
import com.andresport.app_inventory.data.AppDatabase
import com.andresport.app_inventory.model.Product // Asegúrate de que esta importación esté
import com.andresport.app_inventory.repository.ProductRepository
import com.andresport.app_inventory.viewmodel.EditProductViewModel
import com.andresport.app_inventory.viewmodel.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditProductFragment : Fragment(R.layout.fragment_edit_product) {

    private val args: EditProductFragmentArgs by navArgs()

    private val viewModel: EditProductViewModel by viewModels {
        ViewModelFactory(
            ProductRepository(
                AppDatabase.getInstance(requireContext()).productDao()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarEditProduct)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val tvProductValue = view.findViewById<TextView>(R.id.tvProductIdValue)
        tvProductValue.text = args.productId.toString()

        val etName = view.findViewById<TextInputEditText>(R.id.etName)
        val etPrice = view.findViewById<TextInputEditText>(R.id.etPrice)
        val etQuantity = view.findViewById<TextInputEditText>(R.id.etQuantity) // Campo para la cantidad a editar
        val btnSaveChanges = view.findViewById<Button>(R.id.btnSaveChanges)

        val tilName = view.findViewById<TextInputLayout>(R.id.tilName)
        val tilPrice = view.findViewById<TextInputLayout>(R.id.tilPrice)
        val tilQuantity = view.findViewById<TextInputLayout>(R.id.tilQuantity)

        // --- Observar los datos del producto para rellenar los campos ---
        viewModel.product.observe(viewLifecycleOwner, Observer { product ->
            product?.let {

                etName.setText(it.productName)
                etPrice.setText(it.unitPrice.toString())
                etQuantity.setText(it.stock.toString()) // Mostramos el stock actual como valor inicial

                // Activar los layouts si ya tienen texto al cargar
                tilName.isEnabled = etName.text.toString().isNotBlank()
                tilPrice.isEnabled = etPrice.text.toString().isNotBlank()
                tilQuantity.isEnabled = etQuantity.text.toString().isNotBlank()
            }
        })

        // --- Lógica para el botón de "Editar" ---
        btnSaveChanges.setOnClickListener {
            val newName = etName.text.toString()
            val newPriceStr = etPrice.text.toString()
            val newQuantityStr = etQuantity.text.toString() // Esta es la nueva cantidad

            if (newName.isBlank() || newPriceStr.isBlank() || newQuantityStr.isBlank()) {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPrice = newPriceStr.toDoubleOrNull()
            val newQuantity = newQuantityStr.toLongOrNull() // Usamos toLongOrNull para que coincida con 'stock: Long'

            if (newPrice == null || newQuantity == null) {
                Toast.makeText(requireContext(), "El precio y la cantidad deben ser números válidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // El `stock` en la base de datos se actualizará con la nueva cantidad.
            viewModel.updateProduct(args.productId.toString(), newName, newPrice, newQuantity)

            Toast.makeText(requireContext(), "Producto actualizado con éxito", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        // --- Lógica para el estilo dinámico de los campos de texto ---
        fun setupTextWatcher(editText: TextInputEditText, textInputLayout: TextInputLayout) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    textInputLayout.isEnabled = !s.isNullOrBlank()
                }
            })
        }

        setupTextWatcher(etName, tilName)
        setupTextWatcher(etPrice, tilPrice)
        setupTextWatcher(etQuantity, tilQuantity)

        viewModel.loadProduct(args.productId.toString())
    }
}
