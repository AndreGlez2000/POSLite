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
    
    @Query("SELECT * FROM Producto WHERE is_active = 1")
    fun getAllProducts(): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM Producto WHERE id_categoria_fk = :categoryId AND is_active = 1")
    fun getProductsByCategory(categoryId: Int): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM Producto WHERE sku = :sku AND is_active = 1")
    suspend fun getProductBySku(sku: String): ProductEntity?

    @Query("UPDATE Producto SET is_active = 0 WHERE sku = :sku")
    suspend fun deleteBySku(sku: String)
}
