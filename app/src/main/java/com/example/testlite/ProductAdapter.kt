package com.example.testlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private var items: List<Product>,
    private val onItemClick: (Product) -> Unit = {},
    private val onDeleteClick: (Product) -> Unit = {}
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = items[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = "$${String.format("%.2f", product.price)}"
        holder.itemView.setOnClickListener { onItemClick(product) }
        holder.itemView.setOnLongClickListener {
            onDeleteClick(product)
            true
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<Product>) {
        items = newItems
        notifyDataSetChanged()
    }
}
