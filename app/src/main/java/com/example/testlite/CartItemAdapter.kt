package com.example.testlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class CartItemAdapter(
    private val onIncreaseClick: (CartItem) -> Unit,
    private val onDecreaseClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {
    
    private var items = listOf<CartItem>()
    
    fun updateList(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartItemViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(items[position], onIncreaseClick, onDecreaseClick)
    }
    
    override fun getItemCount() = items.size
    
    class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        private val tvSubtotal: TextView = itemView.findViewById(R.id.tv_subtotal)
        private val btnIncrease: MaterialButton = itemView.findViewById(R.id.btn_increase)
        private val btnDecrease: MaterialButton = itemView.findViewById(R.id.btn_decrease)
        
        fun bind(
            cartItem: CartItem,
            onIncreaseClick: (CartItem) -> Unit,
            onDecreaseClick: (CartItem) -> Unit
        ) {
            tvProductName.text = cartItem.product.name
            tvQuantity.text = cartItem.quantity.toString()
            tvSubtotal.text = "$${String.format("%.2f", cartItem.subtotal)}"
            
            btnIncrease.setOnClickListener { onIncreaseClick(cartItem) }
            btnDecrease.setOnClickListener { onDecreaseClick(cartItem) }
        }
    }
}
