package com.example.testlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketDetailAdapter : RecyclerView.Adapter<TicketDetailAdapter.ViewHolder>() {
    
    private var items = listOf<TicketDetailItem>()
    
    fun updateList(newItems: List<TicketDetailItem>) {
        items = newItems
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket_detail, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    
    override fun getItemCount() = items.size
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductName: TextView = itemView.findViewById(R.id.tv_detail_product_name)
        private val tvSku: TextView = itemView.findViewById(R.id.tv_detail_sku)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tv_detail_quantity)
        private val tvPrice: TextView = itemView.findViewById(R.id.tv_detail_price)
        private val tvSubtotal: TextView = itemView.findViewById(R.id.tv_detail_subtotal)
        
        fun bind(item: TicketDetailItem) {
            tvProductName.text = item.productName
            tvSku.text = "SKU: ${item.sku}"
            tvQuantity.text = "Cant: ${item.quantity}"
            tvPrice.text = "$${String.format("%.2f", item.price)}"
            tvSubtotal.text = "$${String.format("%.2f", item.subtotal)}"
        }
    }
}
