package com.andresport.app_inventory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andresport.app_inventory.repository.ProductRepository

class ViewModelFactory(
    private val repository: ProductRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
