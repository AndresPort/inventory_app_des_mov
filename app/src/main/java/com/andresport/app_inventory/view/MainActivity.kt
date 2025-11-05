package com.andresport.app_inventory.view
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.andresport.app_inventory.R
import com.andresport.app_inventory.data.AppDatabase
import com.andresport.app_inventory.model.Product
import com.andresport.app_inventory.repository.ProductRepository
import com.andresport.app_inventory.viewmodel.ProductViewModel
import com.andresport.app_inventory.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dao = AppDatabase.getInstance(this).productDao()
        val repository = ProductRepository(dao)
        viewModel = ViewModelProvider(this, ViewModelFactory(repository))
            .get(ProductViewModel::class.java)

        viewModel.loadProducts()

        // 🔽 Inserta productos de prueba
        lifecycleScope.launch {
            viewModel.addProduct(Product("P001", "Laptop HP", 2500.0, 10))
            viewModel.addProduct(Product("P002", "Mouse Logitech", 120.0, 50))
            viewModel.addProduct(Product("P003", "Teclado Mecánico", 300.0, 25))
        }
    }
}
