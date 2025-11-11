package com.andresport.app_inventory.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.andresport.app_inventory.data.AppDatabase
import com.andresport.app_inventory.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productDao = AppDatabase.getInstance(application).productDao()

    fun insertProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productDao.insertProduct(product)
        }
    }
}
