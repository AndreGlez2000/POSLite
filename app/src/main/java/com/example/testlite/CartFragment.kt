package com.example.testlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels

class CartFragment : Fragment() {

    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvCartItems = view.findViewById<TextView>(R.id.tv_cart_items)

        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            val sb = StringBuilder()
            sb.append("Items en el carrito:\n")
            items.forEach { product ->
                sb.append("- ${product.name} ($${product.price})\n")
            }
            tvCartItems.text = sb.toString()
        }
    }
}