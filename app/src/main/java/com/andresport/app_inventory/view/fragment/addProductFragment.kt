package com.andresport.app_inventory.view.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.andresport.app_inventory.R
import com.andresport.app_inventory.viewmodel.AddProductViewModel


class addProductFragment : Fragment() {

    companion object {
        fun newInstance() = addProductFragment()
    }

    private lateinit var productRefTIET: com.google.android.material.textfield.TextInputEditText
    private lateinit var productNameTIET: com.google.android.material.textfield.TextInputEditText
    private lateinit var unitPriceTIET: com.google.android.material.textfield.TextInputEditText
    private lateinit var stockTIET: com.google.android.material.textfield.TextInputEditText
    private lateinit var saveBtn: android.widget.Button


    private lateinit var returnIc: ImageView

    private val viewModel: AddProductViewModel by viewModels {
        androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        returnIc = view.findViewById(R.id.returnIc)
        returnIc.setOnClickListener { returnInventoryPage() }

        productRefTIET = view.findViewById(R.id.productRefTIET)
        productNameTIET = view.findViewById(R.id.productNameTIET)
        unitPriceTIET = view.findViewById(R.id.unitPriceTIET)
        stockTIET = view.findViewById(R.id.stockTIET)
        saveBtn = view.findViewById(R.id.saveBtn)

        // Habilitación del botón
        val textWatcher = object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val ref = productRefTIET.text.toString().trim()
                val name = productNameTIET.text.toString().trim()
                val price = unitPriceTIET.text.toString().trim()
                val stock = stockTIET.text.toString().trim()
                saveBtn.isEnabled = ref.isNotEmpty() && name.isNotEmpty() && price.isNotEmpty() && stock.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        productRefTIET.addTextChangedListener(textWatcher)
        productNameTIET.addTextChangedListener(textWatcher)
        unitPriceTIET.addTextChangedListener(textWatcher)
        stockTIET.addTextChangedListener(textWatcher)

        // Guardar producto
        saveBtn.setOnClickListener {
            val ref = productRefTIET.text.toString().trim()
            val name = productNameTIET.text.toString().trim()
            val price = unitPriceTIET.text.toString()
                .replace(".", "")
                .replace(",", ".")
                .toDoubleOrNull() ?: 0.0
            val stock = stockTIET.text.toString().toLongOrNull() ?: 0L

            val product = com.andresport.app_inventory.model.Product(
                productRef = ref,
                productName = name,
                unitPrice = price,
                stock = stock
            )

            viewModel.insertProduct(product)

            // Mostrar confirmación
            android.widget.Toast.makeText(
                requireContext(),
                "Producto guardado correctamente",
                android.widget.Toast.LENGTH_SHORT
            ).show()

            // Navegar al fragmento de inventario
            returnInventoryPage()
        }
    }



    fun returnInventoryPage(){
        findNavController().navigate(R.id.action_addProductFragment_to_inventarioFragment)
    }
}