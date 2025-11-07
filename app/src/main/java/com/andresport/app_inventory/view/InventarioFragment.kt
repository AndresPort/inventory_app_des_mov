package com.andresport.app_inventory.view

import com.google.android.material.appbar.MaterialToolbar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andresport.app_inventory.R
import com.andresport.app_inventory.viewmodel.ProductViewModel
import com.andresport.app_inventory.view.adapter.ProductAdapter
import com.andresport.app_inventory.repository.ProductRepository
import com.andresport.app_inventory.viewmodel.ViewModelFactory
import com.andresport.app_inventory.data.AppDatabase
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.andresport.app_inventory.model.Product
import android.widget.ProgressBar

class InventarioFragment : Fragment() {
    private lateinit var viewModel: ProductViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inventario, container, false)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarInventario)

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    // Acción de cerrar sesión
                    true
                }
                else -> false
            }
        }
        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductAdapter()
        recyclerView.adapter = adapter
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // ✅ Inicializamos correctamente el ViewModel
        val dao = AppDatabase.getInstance(requireContext()).productDao()

        val repository = ProductRepository(dao)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val fabAdd = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddProduct)
        // Mostrar el progress mientras se cargan los productos
        progressBar.visibility = View.VISIBLE

        // Observa los productos
        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.setProducts(products)
            // Ocultar progress cuando los productos ya están listos
            progressBar.visibility = View.GONE
        }

        // Carga los productos
        viewLifecycleOwner.lifecycleScope.launch {
            val dao = AppDatabase.getInstance(requireContext()).productDao()
            viewModel.loadProducts()
        }
        fabAdd.setOnClickListener {
            // Sin funcionalidad por el momento
        }
    }
}
