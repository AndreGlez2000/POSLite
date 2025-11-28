package com.example.testlite.database.dao

import androidx.room.*
import com.example.testlite.database.entities.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoria: CategoriaEntity): Long
    
    @Update
    suspend fun update(categoria: CategoriaEntity)
    
    @Delete
    suspend fun delete(categoria: CategoriaEntity)
    
    @Query("SELECT * FROM Categoria WHERE is_active = 1")
    fun getAllCategorias(): Flow<List<CategoriaEntity>>
    
    @Query("SELECT * FROM Categoria WHERE id = :id AND is_active = 1")
    suspend fun getCategoriaById(id: Int): CategoriaEntity?

    @Query("UPDATE Categoria SET is_active = 0 WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM Producto WHERE id_categoria_fk = :categoryId AND is_active = 1")
    suspend fun getProductCountByCategory(categoryId: Int): Int
}
