package com.example.testlite.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.testlite.database.entities.TicketEntity
import com.example.testlite.database.entities.TicketItemEntity

data class TicketWithItems(
    @Embedded val ticket: TicketEntity,
    @Relation(
        parentColumn = "ticket_id",
        entityColumn = "ticket_id_fk"
    )
    val items: List<TicketItemEntity>
)
