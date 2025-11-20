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
        
        // Setup RecyclerView
        adapter = CartItemAdapter()
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
            val cartItems = cartViewModel.cartItems.value ?: emptyList()
            
            ticketViewModel.completeSale(
                cartItems = cartItems,
                onSuccess = {
                    cartViewModel.clearCart()
                    Toast.makeText(requireContext(), "Venta completada exitosamente", Toast.LENGTH_SHORT).show()
                },
                onError = { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}