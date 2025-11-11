package com.andresport.app_inventory.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
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

// Heredamos de Fragment(R.layout.fragment_inventario) para que infle el layout automáticamente.
// Esto nos permite eliminar el método onCreateView.
class InventarioFragment : Fragment(R.layout.fragment_inventario) {

    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- INICIO DE LA CORRECCIÓN ---

        // 1. Buscamos el Toolbar en la vista.
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarInventario)

        // 2. Configura el listener para los íconos del MENÚ (definidos en menu_toolbar_inventario.xml).
        // Este es el método correcto para manejar clics en íconos de menú.
        toolbar.setOnMenuItemClickListener { menuItem ->
            // Verificamos el ID del ítem del menú que fue presionado.
            when (menuItem.itemId) {
                // Usamos el ID de tu menú: R.id.action_logout.
                R.id.action_logout -> {
                    // Llamamos a la función para volver al Login.
                    returnToLogInFragment()
                    // Retornamos 'true' para indicar que hemos manejado el evento.
                    true
                }
                else -> {
                    // Si es otro ítem, retornamos 'false' para que el sistema lo maneje.
                    false
                }
            }
        }
        // --- FIN DE LA CORRECCIÓN ---


        // --- Resto de tu código de inicialización (sin cambios) ---

        // Inicializar RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductAdapter()
        recyclerView.adapter = adapter

        // Inicializar botón flotante
        val fabAddProduct = view.findViewById<FloatingActionButton>(R.id.fabAddProduct)
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

        // Observar y cargar productos
        progressBar.visibility = View.VISIBLE
        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.setProducts(products)
            progressBar.visibility = View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadProducts()
        }

        // Listener para resultados de otros fragments (como editar producto)
        setFragmentResultListener("editProductRequest") { _, bundle ->
            val wasUpdated = bundle.getBoolean("productUpdated", false)
            if (wasUpdated) {
                // Recargar la lista de productos si hubo una actualización
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.loadProducts()
                }
                Toast.makeText(requireContext(), "Lista actualizada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openAddProductFragment() {
        findNavController().navigate(R.id.action_inventarioFragment_to_addProductFragment)
    }

    private fun returnToLogInFragment() {
        // Navega de vuelta al Login.
        // RECOMENDACIÓN: Asegúrate de que esta acción en tu nav_graph.xml limpie la pila de navegación
        // para evitar que el usuario pueda volver al inventario con el botón "Atrás" del dispositivo.
        // Ejemplo en nav_graph.xml para la acción:
        // app:popUpTo="@id/nav_graph"
        // app:popUpToInclusive="true"
        findNavController().navigate(R.id.action_inventarioFragment_to_LoginFragment)
    }
}
