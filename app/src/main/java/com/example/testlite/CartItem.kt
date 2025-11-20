package com.example.testlite

data class CartItem(
    val product: Product,
    var quantity: Int = 1
) {
    val subtotal: Double
        get() = product.price * quantity
}
