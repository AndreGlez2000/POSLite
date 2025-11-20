package com.example.testlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() {
    
    private val cartViewModel: CartViewModel by activityViewModels()
    private val ticketViewModel: TicketViewModel by activityViewModels()
    private lateinit var adapter: CartItemAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val rvCartItems: RecyclerView = view.findViewById(R.id.rv_cart_items)
        val tvTotal: TextView = view.findViewById(R.id.tv_total)
        val btnCompleteSale: Button = view.findViewById(R.id.btn_complete_sale)
        val btnClearCart: android.widget.ImageButton = view.findViewById(R.id.btn_clear_cart)
        
        // Setup RecyclerView with quantity controls
        adapter = CartItemAdapter(
            onIncreaseClick = { cartItem ->
                cartViewModel.incrementQuantity(cartItem)
            },
            onDecreaseClick = { cartItem ->
                cartViewModel.decrementQuantity(cartItem)
            }
        )
        rvCartItems.adapter = adapter
        rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        
        // Observe cart items
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateList(items)
            btnCompleteSale.isEnabled = items.isNotEmpty()
        }
        
        // Observe total price
        cartViewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            tvTotal.text = "Total: $${String.format("%.2f", total)}"
        }
        
        // Complete sale button
        btnCompleteSale.setOnClickListener {
            val productsList = cartViewModel.getProductsList()
            
            ticketViewModel.completeSale(
                cartItems = productsList,
                onSuccess = {
                    cartViewModel.clearCart()
                    Toast.makeText(requireContext(), "Venta completada exitosamente", Toast.LENGTH_SHORT).show()
                },
                onError = { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            )
        }
        
        // Clear cart button
        btnClearCart.setOnClickListener {
            if (cartViewModel.cartItems.value?.isNotEmpty() == true) {
                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Cancelar Venta")
                    .setMessage("¿Estás seguro de que deseas vaciar el carrito?")
                    .setPositiveButton("Sí") { _, _ ->
                        cartViewModel.clearCart()
                        Toast.makeText(requireContext(), "Carrito vaciado", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
    }
}