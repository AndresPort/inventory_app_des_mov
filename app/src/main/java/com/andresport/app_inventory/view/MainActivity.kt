package com.andresport.app_inventory.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.andresport.app_inventory.R
import com.andresport.app_inventory.data.AppDatabase
import com.andresport.app_inventory.repository.ProductRepository
import com.andresport.app_inventory.viewmodel.ProductViewModel
import com.andresport.app_inventory.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dao = AppDatabase.getInstance(this).productDao()
        val repository = ProductRepository(dao)
        viewModel = ViewModelProvider(this, ViewModelFactory(repository))
            .get(ProductViewModel::class.java)

        viewModel.products.observe(this) { products ->
            // Actualiza RecyclerView o UI
        }

        viewModel.loadProducts()
    }
}
