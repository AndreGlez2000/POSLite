package com.example.testlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartItemAdapter : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {
    
    private var items = listOf<Product>()
    
    fun updateList(newItems: List<Product>) {
        items = newItems
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartItemViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(items[position])
    }
    
    override fun getItemCount() = items.size
    
    class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        private val tvProductSku: TextView = itemView.findViewById(R.id.tv_product_sku)
        private val tvProductPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        
        fun bind(product: Product) {
            tvProductName.text = product.name
            tvProductSku.text = "SKU: ${product.sku}"
            tvProductPrice.text = "$${String.format("%.2f", product.price)}"
        }
    }
}
