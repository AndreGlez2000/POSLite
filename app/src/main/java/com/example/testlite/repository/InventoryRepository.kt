package com.example.testlite.repository

import com.example.testlite.Category
import com.example.testlite.Product
import com.example.testlite.database.dao.CategoriaDao
import com.example.testlite.database.dao.ProductDao
import com.example.testlite.database.entities.CategoriaEntity
import com.example.testlite.database.entities.ProductEntity
import com.example.testlite.database.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InventoryRepository(
    private val categoriaDao: CategoriaDao,
    private val productDao: ProductDao
) {
    val allCategorias: Flow<List<Category>> = categoriaDao.getAllCategorias()
        .map { entities -> entities.map { it.toDomain() } }
    
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
        .map { entities -> entities.map { it.toDomain() } }
    
    fun getProductsByCategory(categoryId: Int): Flow<List<Product>> =
        productDao.getProductsByCategory(categoryId)
            .map { entities -> entities.map { it.toDomain() } }
    
    suspend fun addCategory(name: String): Long {
        return categoriaDao.insert(CategoriaEntity(nombre = name))
    }
    
    suspend fun addProduct(name: String, price: Double, sku: String, categoryId: Int) {
        productDao.insert(ProductEntity(sku, name, price, categoryId))
    }
    
    suspend fun getProductBySku(sku: String): Product? {
        return productDao.getProductBySku(sku)?.toDomain()
    }
}
