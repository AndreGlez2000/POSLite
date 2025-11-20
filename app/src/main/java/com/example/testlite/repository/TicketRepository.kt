package com.example.testlite.repository

import com.example.testlite.Product
import com.example.testlite.database.dao.TicketDao
import com.example.testlite.database.entities.TicketEntity
import com.example.testlite.database.entities.TicketItemEntity
import com.example.testlite.database.relations.TicketWithItems
import kotlinx.coroutines.flow.Flow

class TicketRepository(private val ticketDao: TicketDao) {
    val allTickets: Flow<List<TicketEntity>> = ticketDao.getAllTickets()
    
    suspend fun createTicket(cartItems: List<Product>): Long {
        // Group cart items by SKU to get quantities
        val itemGroups = cartItems.groupBy { it.sku }
        
        val total = cartItems.sumOf { it.price }
        val ticket = TicketEntity(
            fechaHora = System.currentTimeMillis(),
            total = total
        )
        
        val ticketItems = itemGroups.map { (sku, products) ->
            TicketItemEntity(
                ticketIdFk = 0, // Will be set by DAO
                skuProductoFk = sku,
                cantidad = products.size,
                precioEnEseMomento = products.first().price
            )
        }
        
        ticketDao.insertTicketWithItems(ticket, ticketItems)
        return 0 // Could return ticket ID if needed
    }
    
    suspend fun getTicketWithItems(ticketId: Int): TicketWithItems? {
        return ticketDao.getTicketWithItems(ticketId)
    }
    
    suspend fun deleteTicket(ticketId: Int) {
        val ticket = ticketDao.getTicketById(ticketId)
        if (ticket != null) {
            ticketDao.deleteTicket(ticket)
        }
    }
}
