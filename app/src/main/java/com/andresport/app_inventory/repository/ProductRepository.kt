package com.andresport.app_inventory.repository

import com.andresport.app_inventory.data.ProductDao
import com.andresport.app_inventory.model.Product

class ProductRepository(private val dao: ProductDao) {

    suspend fun getAllProducts(): List<Product> = dao.getAllProducts()

    suspend fun insertProduct(product: Product) = dao.insertProduct(product)

    suspend fun updateProduct(product: Product) = dao.updateProduct(product)

    suspend fun deleteProduct(product: Product) = dao.deleteProduct(product)
}
