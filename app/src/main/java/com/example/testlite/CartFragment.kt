package com.example.testlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import androidx.core.graphics.toColorInt

class CartFragment : Fragment() {
    
    private val cartViewModel: CartViewModel by activityViewModels()
    private val ticketViewModel: TicketViewModel by activityViewModels()
    private lateinit var adapter: CartItemAdapter
    private var currentTotal: Double = 0.0
    
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
        val tvChange: TextView = view.findViewById(R.id.tv_change)
        val etPaymentAmount: TextInputEditText = view.findViewById(R.id.et_payment_amount)
        val btnCompleteSale: Button = view.findViewById(R.id.btn_complete_sale)
        val btnClearCart: android.widget.ImageButton = view.findViewById(R.id.btn_clear_cart)
        val tvCartHeader: TextView = view.findViewById(R.id.tv_cart_header)
        
        // Set current date
        val currentDate = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            .format(java.util.Date())
        tvCartHeader.text = currentDate
        
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
            
            // Reset payment input and change when cart changes
            if (items.isEmpty()) {
                etPaymentAmount.text?.clear()
                tvChange.visibility = View.GONE
            }
        }
        
        // Observe total price
        cartViewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            currentTotal = total
            tvTotal.text = "$${String.format("%.2f", total)}"
            
            // Recalculate change if payment amount is entered
            val paymentText = etPaymentAmount.text.toString()
            if (paymentText.isNotEmpty()) {
                calculateAndShowChange(paymentText, tvChange)
            }
        }
        
        // Listen to payment amount changes
        etPaymentAmount.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val paymentText = etPaymentAmount.text.toString()
                calculateAndShowChange(paymentText, tvChange)
            }
        }
        
        // Complete sale button
        btnCompleteSale.setOnClickListener {
            val paymentText = etPaymentAmount.text.toString()
            val paymentAmount = paymentText.toDoubleOrNull()
            
            // Validate payment amount if provided
            if (paymentAmount != null && paymentAmount < currentTotal) {
                Toast.makeText(requireContext(), "El pago es insuficiente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val change = if (paymentAmount != null) paymentAmount - currentTotal else 0.0
            val changeMessage = if (change > 0) {
                "Cambio a entregar: $${String.format("%.2f", change)}"
            } else {
                "Pago exacto"
            }
            
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Confirmar Venta")
                .setMessage("Total: $${String.format("%.2f", currentTotal)}\n$changeMessage\n\n¿Deseas completar la venta?")
                .setPositiveButton("Sí") { _, _ ->
                    val productsList = cartViewModel.getProductsList()
                    
                    ticketViewModel.completeSale(
                        cartItems = productsList,
                        onSuccess = {
                            cartViewModel.clearCart()
                            etPaymentAmount.text?.clear()
                            tvChange.visibility = View.GONE
                            
                            // Show final change message
                            if (change > 0) {
                                Toast.makeText(requireContext(), "Venta completada. Cambio: $${String.format("%.2f", change)}", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(requireContext(), "Venta completada exitosamente", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onError = { error ->
                            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                .setNegativeButton("No", null)
                .show()
        }
        
        // Clear cart button
        btnClearCart.setOnClickListener {
            if (cartViewModel.cartItems.value?.isNotEmpty() == true) {
                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Cancelar Venta")
                    .setMessage("¿Estás seguro de que deseas vaciar el carrito?")
                    .setPositiveButton("Sí") { _, _ ->
                        cartViewModel.clearCart()
                        etPaymentAmount.text?.clear()
                        tvChange.visibility = View.GONE
                        Toast.makeText(requireContext(), "Carrito vaciado", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
    }

    private fun calculateAndShowChange(paymentText: String, tvChange: TextView) {
        if (paymentText.isEmpty()) {
            tvChange.visibility = View.GONE
            return
        }
        
        val paymentAmount = paymentText.toDoubleOrNull()
        if (paymentAmount != null && paymentAmount >= currentTotal) {
            val change = paymentAmount - currentTotal
            tvChange.text = "Cambio: $${String.format("%.2f", change)}"
            tvChange.setTextColor(android.graphics.Color.parseColor("#00BFA5"))
            tvChange.visibility = View.VISIBLE
        } else if (paymentAmount != null) {
            tvChange.text = "Pago insuficiente"
            tvChange.setTextColor(android.graphics.Color.parseColor("#00BFA5"))
            tvChange.visibility = View.VISIBLE
        } else {
            tvChange.visibility = View.GONE
        }
    }
}