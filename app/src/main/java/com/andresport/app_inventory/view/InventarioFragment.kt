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

        // Observa los productos
        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.setProducts(products)
        }

        // Carga los productos
        viewModel.loadProducts()
    }
}
