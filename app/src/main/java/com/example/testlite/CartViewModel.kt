package com.example.testlite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    fun addToCart(product: Product) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        
        // Check if product already exists in cart
        val existingItem = currentList.find { it.product.sku == product.sku }
        
        if (existingItem != null) {
            // Increment quantity
            existingItem.quantity++
        } else {
            // Add new item
            currentList.add(CartItem(product, 1))
        }
        
        _cartItems.value = currentList
        updateTotal()
    }

    fun removeFromCart(cartItem: CartItem) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        currentList.remove(cartItem)
        _cartItems.value = currentList
        updateTotal()
    }
    
    fun incrementQuantity(cartItem: CartItem) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        val item = currentList.find { it.product.sku == cartItem.product.sku }
        item?.quantity = (item?.quantity ?: 0) + 1
        _cartItems.value = currentList
        updateTotal()
    }
    
    fun decrementQuantity(cartItem: CartItem) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        val item = currentList.find { it.product.sku == cartItem.product.sku }
        
        if (item != null) {
            if (item.quantity > 1) {
                item.quantity--
            } else {
                currentList.remove(item)
            }
        }
        
        _cartItems.value = currentList
        updateTotal()
    }
    
    fun getProductsList(): List<Product> {
        // Convert CartItems back to Product list for ticket creation
        val products = mutableListOf<Product>()
        _cartItems.value?.forEach { cartItem ->
            repeat(cartItem.quantity) {
                products.add(cartItem.product)
            }
        }
        return products
    }

    private fun updateTotal() {
        val total = _cartItems.value.orEmpty().sumOf { it.subtotal }
        _totalPrice.value = total
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
        _totalPrice.value = 0.0
    }
}
