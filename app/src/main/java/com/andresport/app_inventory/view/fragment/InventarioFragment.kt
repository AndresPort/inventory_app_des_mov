package com.andresport.app_inventory.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andresport.app_inventory.R
import com.andresport.app_inventory.data.AppDatabase
import com.andresport.app_inventory.repository.ProductRepository
import com.andresport.app_inventory.view.adapter.ProductAdapter
import com.andresport.app_inventory.viewmodel.ProductViewModel
import com.andresport.app_inventory.viewmodel.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class InventarioFragment : Fragment() {

    private lateinit var viewModel: ProductViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var fabAddProduct: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inventario, container, false)

        // Configurar toolbar
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarInventario)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    // Acción de cerrar sesión (pendiente)
                    true
                }
                else -> false
            }
        }

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductAdapter()
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar botón flotante
        fabAddProduct = view.findViewById(R.id.fabAddProduct)
        fabAddProduct.setOnClickListener {
            openAddProductFragment()
        }

        // Inicializar ProgressBar
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        // Inicializar ViewModel
        val dao = AppDatabase.getInstance(requireContext()).productDao()
        val repository = ProductRepository(dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        // Mostrar progress mientras se cargan los productos
        progressBar.visibility = View.VISIBLE

        // Observar productos
        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.setProducts(products)
            // Ocultar progress cuando ya se cargaron los datos
            progressBar.visibility = View.GONE
        }

        // Cargar productos desde la BD
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadProducts()
        }
    }

    private fun openAddProductFragment() {
        findNavController().navigate(R.id.action_inventarioFragment_to_addProductFragment)
    }
}
