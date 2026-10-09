package com.hindustanhardware.inventory.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dao = AppDatabase
        .getInstance(application)
        .productDao()

    val products = dao.getAllProducts()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val productCount = dao.getProductCount()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val totalUnits = dao.getTotalUnits()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val inStockCount = dao.getInStockCount()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val lowStockCount = dao.getLowStockCount()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val outOfStockCount = dao.getOutOfStockCount()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    fun addProduct(
        product: Product,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                dao.insertProduct(product)
                onComplete(true)
            } catch (exception: Exception) {
                onComplete(false)
            }
        }
    }

    fun deleteProduct(
        product: Product,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                dao.deleteProduct(product)
                onComplete(true)
            } catch (exception: Exception) {
                onComplete(false)
            }
        }
    }

    fun updateProduct(
        product: Product,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                dao.updateProduct(product)
                onComplete(true)
            } catch (exception: Exception) {
                onComplete(false)
            }
        }
    }

    fun searchProducts(query: String) =
        dao.searchProducts(query)
}
