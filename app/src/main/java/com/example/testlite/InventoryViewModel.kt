package com.example.testlite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InventoryViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    init {
        // Initial dummy data
        _categories.value = listOf(
            Category(1, "Papas"),
            Category(2, "Sodas"),
            Category(3, "Galletas"),
            Category(4, "Lácteos"),
            Category(5, "Carnes"),
            Category(6, "Frutas"),
            Category(7, "Verduras"),
            Category(8, "Limpieza")
        )
        _products.value = listOf(
            Product("123", "Sabritas Sal", 15.0, 1),
            Product("124", "Ruffles Queso", 15.0, 1),
            Product("125", "Coca Cola 600ml", 18.0, 2),
            Product("126", "Pepsi 600ml", 16.0, 2),
            Product("127", "Emperador Chocolate", 20.0, 3),
            Product("128", "Leche Lala 1L", 28.0, 4),
            Product("129", "Huevo 1kg", 45.0, 5),
            Product("130", "Manzana Roja kg", 35.0, 6)
        )
    }

    fun addCategory(name: String) {
        val currentList = _categories.value.orEmpty().toMutableList()
        val newId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
        currentList.add(Category(newId, name))
        _categories.value = currentList
    }

    fun addProduct(name: String, price: Double, sku: String, categoryId: Int) {
        val currentList = _products.value.orEmpty().toMutableList()
        currentList.add(Product(sku, name, price, categoryId))
        _products.value = currentList
    }

    fun getProductsByCategory(categoryId: Int): List<Product> {
        return _products.value.orEmpty().filter { it.categoryId == categoryId }
    }
}
