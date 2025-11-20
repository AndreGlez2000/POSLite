package com.example.testlite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<Product>>()
    val cartItems: LiveData<List<Product>> = _cartItems

    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    fun addToCart(product: Product) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        currentList.add(product)
        _cartItems.value = currentList
        updateTotal()
    }

    fun removeFromCart(product: Product) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        currentList.remove(product)
        _cartItems.value = currentList
        updateTotal()
    }

    private fun updateTotal() {
        val total = _cartItems.value.orEmpty().sumOf { it.price }
        _totalPrice.value = total
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
        _totalPrice.value = 0.0
    }
}
