package com.example.testlite.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Producto",
    foreignKeys = [ForeignKey(
        entity = CategoriaEntity::class,
        parentColumns = ["id"],
        childColumns = ["id_categoria_fk"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("id_categoria_fk")]
)
data class ProductEntity(
    @PrimaryKey val sku: String,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "precio") val precio: Double,
    @ColumnInfo(name = "id_categoria_fk") val idCategoriaFk: Int,
    @ColumnInfo(name = "is_active", defaultValue = "1") val isActive: Boolean = true
)
