package com.andresport.app_inventory.model

import android.content.Context
import com.andresport.app_inventory.data.AppDatabase
import com.andresport.app_inventory.data.ProductDao

/**
 * Repositorio que maneja las operaciones de datos para el inventario.
 * Actúa como la capa del Modelo en la arquitectura MVC.
 */
class InventoryRepository(context: Context) {

    private val productDao: ProductDao

    init {
        // CORRECCIÓN: Usamos getInstance, que es el nombre correcto del método en AppDatabase.kt
        val database = AppDatabase.getInstance(context)
        productDao = database.productDao()
    }

    /**
     * Criterio 8: Calcula el valor total del inventario multiplicando el precio de cada producto
     * por su cantidad y sumando los totales.
     */
    suspend fun getTotalInventoryValue(): Double {
        val allProducts = productDao.getAllProducts() // Obtenemos todos los productos
        var totalValue = 0.0
        for (product in allProducts) {
            totalValue += product.unitPrice * product.stock
        }
        return totalValue
    }
}
