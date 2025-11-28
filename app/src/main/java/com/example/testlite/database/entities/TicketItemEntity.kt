package com.example.testlite.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "TicketItem",
    foreignKeys = [
        ForeignKey(
            entity = TicketEntity::class,
            parentColumns = ["ticket_id"],
            childColumns = ["ticket_id_fk"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["sku"],
            childColumns = ["sku_producto_fk"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("ticket_id_fk"), Index("sku_producto_fk")]
)
data class TicketItemEntity(
    @PrimaryKey(autoGenerate = true) 
    @ColumnInfo(name = "item_id") val itemId: Int = 0,
    @ColumnInfo(name = "ticket_id_fk") val ticketIdFk: Int,
    @ColumnInfo(name = "sku_producto_fk") val skuProductoFk: String,
    @ColumnInfo(name = "cantidad") val cantidad: Int,
    @ColumnInfo(name = "precio_en_ese_momento") val precioEnEseMomento: Double
)
