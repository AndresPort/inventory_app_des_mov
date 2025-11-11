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
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@MainActivity)

            // Verificar si ya existe
            val exists = db.productDao().getProductById("P001")
            if (exists == null) {
                val productoTest = Product(
                    productRef = "P001",
                    productName = "Laptop HP Pavilion",
                    unitPrice = 1200.50,
                    stock = 10L
                )
                db.productDao().insertProduct(productoTest)
                android.util.Log.d("MainActivity", " Producto P001 insertado")
            }
        }



    }
}
