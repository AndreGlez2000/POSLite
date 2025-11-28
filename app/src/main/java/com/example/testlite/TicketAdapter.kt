package com.example.testlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testlite.database.entities.TicketEntity
import java.text.SimpleDateFormat
import java.util.*

class TicketAdapter(
    private val onTicketClick: (TicketEntity) -> Unit,
    private val onDeleteClick: (TicketEntity) -> Unit
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {
    
    private var tickets = listOf<TicketEntity>()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    fun updateList(newTickets: List<TicketEntity>) {
        tickets = newTickets
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(tickets[position], dateFormat, onTicketClick, onDeleteClick)
    }
    
    override fun getItemCount() = tickets.size
    
    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTicketId: TextView = itemView.findViewById(R.id.tv_ticket_id)
        private val tvTicketDate: TextView = itemView.findViewById(R.id.tv_ticket_date)
        private val tvTicketTotal: TextView = itemView.findViewById(R.id.tv_ticket_total)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete_ticket)
        
        fun bind(
            ticket: TicketEntity,
            dateFormat: SimpleDateFormat,
            onTicketClick: (TicketEntity) -> Unit,
            onDeleteClick: (TicketEntity) -> Unit
        ) {
            tvTicketId.text = "Ticket #${ticket.ticketId}"
            tvTicketDate.text = dateFormat.format(Date(ticket.fechaHora))
            tvTicketTotal.text = "Total: $${String.format("%.2f", ticket.total)}"
            
            itemView.setOnClickListener { onTicketClick(ticket) }
            itemView.setOnLongClickListener {
                onDeleteClick(ticket)
                true
            }
            btnDelete.setOnClickListener { onDeleteClick(ticket) }
        }
    }
}
