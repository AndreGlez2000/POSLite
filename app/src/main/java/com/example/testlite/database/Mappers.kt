package com.example.testlite.database

import com.example.testlite.Category
import com.example.testlite.Product
import com.example.testlite.database.entities.CategoriaEntity
import com.example.testlite.database.entities.ProductEntity

// Extension functions to convert between Entity and Domain models

fun CategoriaEntity.toDomain() = Category(
    id = this.id,
    name = this.nombre
)

fun Category.toEntity() = CategoriaEntity(
    id = this.id,
    nombre = this.name
)

fun ProductEntity.toDomain() = Product(
    sku = this.sku,
    name = this.nombre,
    price = this.precio,
    categoryId = this.idCategoriaFk
)

fun Product.toEntity() = ProductEntity(
    sku = this.sku,
    nombre = this.name,
    precio = this.price,
    idCategoriaFk = this.categoryId
)
