package com.example.testlite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.testlite.database.AppDatabase
import com.example.testlite.repository.InventoryRepository
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {
    private val inventoryRepository: InventoryRepository
    
    val categories: LiveData<List<Category>>
    val products: LiveData<List<Product>>

    init {
        val database = AppDatabase.getDatabase(application)
        inventoryRepository = InventoryRepository(
            database.categoriaDao(),
            database.productDao()
        )
        categories = inventoryRepository.allCategorias.asLiveData()
        products = inventoryRepository.allProducts.asLiveData()
    }

    fun addCategory(name: String) {
        val capitalizedName = name.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        viewModelScope.launch {
            inventoryRepository.addCategory(capitalizedName)
        }
    }

    fun addProduct(name: String, price: Double, sku: String, categoryId: Int) {
        val capitalizedName = name.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        viewModelScope.launch {
            inventoryRepository.addProduct(capitalizedName, price, sku, categoryId)
        }
    }

    fun getProductsByCategory(categoryId: Int): List<Product> {
        return products.value.orEmpty().filter { it.categoryId == categoryId }
    }

    fun deleteProduct(sku: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                inventoryRepository.deleteProduct(sku)
                onSuccess()
            } catch (e: Exception) {
                onError("Error al eliminar producto: ${e.message}")
            }
        }
    }

    fun deleteCategory(id: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val productCount = inventoryRepository.getProductCountByCategory(id)
                inventoryRepository.deleteCategory(id)
                onSuccess()
            } catch (e: Exception) {
                onError("Error al eliminar categoría: ${e.message}")
            }
        }
    }

    fun getProductCountByCategory(categoryId: Int, callback: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                val count = inventoryRepository.getProductCountByCategory(categoryId)
                callback(count)
            } catch (e: Exception) {
                callback(0)
            }
        }
    }
}
