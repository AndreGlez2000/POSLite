package com.example.testlite.database.dao

import androidx.room.*
import com.example.testlite.database.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)
    
    @Update
    suspend fun update(product: ProductEntity)
    
    @Delete
    suspend fun delete(product: ProductEntity)
    
    @Query("SELECT * FROM Producto")
    fun getAllProducts(): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM Producto WHERE id_categoria_fk = :categoryId")
    fun getProductsByCategory(categoryId: Int): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM Producto WHERE sku = :sku")
    suspend fun getProductBySku(sku: String): ProductEntity?

    @Query("DELETE FROM Producto WHERE sku = :sku")
    suspend fun deleteBySku(sku: String)
}
