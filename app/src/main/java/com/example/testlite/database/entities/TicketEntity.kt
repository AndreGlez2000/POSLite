package com.example.testlite.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ticket")
data class TicketEntity(
    @PrimaryKey(autoGenerate = true) 
    @ColumnInfo(name = "ticket_id") val ticketId: Int = 0,
    @ColumnInfo(name = "fecha_hora") val fechaHora: Long,
    @ColumnInfo(name = "total") val total: Double
)
