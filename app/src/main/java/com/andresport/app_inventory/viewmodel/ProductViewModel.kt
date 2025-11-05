package com.andresport.app_inventory.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import com.andresport.app_inventory.repository.ProductRepository
import com.andresport.app_inventory.model.Product

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    fun loadProducts() {
        viewModelScope.launch {
            val entities = repository.getAllProducts()
            // map Product -> UI model Product
            _products.value = entities.map { e ->
                Product(
                    productRef = e.productRef,
                    productName = e.productName,
                    unitPrice = e.unitPrice,
                    stock = e.stock
                )
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            val entity = Product(
                productRef = product.productRef,
                productName = product.productName,
                stock = product.stock,
                unitPrice = product.unitPrice
            )
            repository.insertProduct(entity)
            loadProducts()
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            val entity = Product(
                productRef = product.productRef,
                productName = product.productName,
                stock = product.stock,
                unitPrice = product.unitPrice
            )
            repository.updateProduct(entity)
            loadProducts()
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            val entity = Product(
                productRef = product.productRef,
                productName = product.productName,
                stock = product.stock,
                unitPrice = product.unitPrice
            )
            repository.deleteProduct(entity)
            loadProducts()
        }
    }
}
