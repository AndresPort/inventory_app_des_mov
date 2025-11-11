package com.andresport.app_inventory.repository

import com.andresport.app_inventory.data.ProductDao
import com.andresport.app_inventory.model.Product
import kotlinx.coroutines.delay

class ProductRepository(private val dao: ProductDao) {

    suspend fun getAllProducts(): List<Product> = dao.getAllProducts()

    suspend fun insertProduct(product: Product) = dao.insertProduct(product)

    suspend fun updateProduct(product: Product) = dao.updateProduct(product)

    suspend fun deleteProduct(product: Product) = dao.deleteProduct(product)
    suspend fun getProductById(productRef: String): Product? {
        return dao.getProductById(productRef)
    }

    /*suspend fun getProductById(productRef: String): Product? {

        // Opcional: simula un pequeño retraso de red/BD para que parezca más real
        delay(500)

        // Usamos el constructor de TU data class Product
        return Product(
            productRef = productRef, // Usamos la referencia recibida para que sea consistente
            productName = "Articulo de Prueba Desde Repo", // Corresponde a 'productName'
            unitPrice = 199.99,                      // Corresponde a 'unitPrice'
            stock = 50L                              // Corresponde a 'stock'. La 'L' indica que es un número tipo Long.
        )
        // --- FIN DE LA SIMULACIÓN ---

        /* --- CÓDIGO REAL que se deberia usar despues---
        return productDao.getProductById(productRef)
        */
    }*/
}
