package com.example.testlite.database.dao

import androidx.room.*
import com.example.testlite.database.entities.TicketEntity
import com.example.testlite.database.entities.TicketItemEntity
import com.example.testlite.database.relations.TicketWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Insert
    suspend fun insertTicket(ticket: TicketEntity): Long
    
    @Insert
    suspend fun insertTicketItems(items: List<TicketItemEntity>)
    
    @Transaction
    suspend fun insertTicketWithItems(ticket: TicketEntity, items: List<TicketItemEntity>) {
        val ticketId = insertTicket(ticket)
        val itemsWithTicketId = items.map { it.copy(ticketIdFk = ticketId.toInt()) }
        insertTicketItems(itemsWithTicketId)
    }
    
    @Query("SELECT * FROM Ticket ORDER BY fecha_hora DESC")
    fun getAllTickets(): Flow<List<TicketEntity>>
    
    @Transaction
    @Query("SELECT * FROM Ticket WHERE ticket_id = :ticketId")
    suspend fun getTicketWithItems(ticketId: Int): TicketWithItems?
}
