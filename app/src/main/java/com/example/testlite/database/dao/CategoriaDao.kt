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
    
    @Query("SELECT * FROM Categoria")
    fun getAllCategorias(): Flow<List<CategoriaEntity>>
    
    @Query("SELECT * FROM Categoria WHERE id = :id")
    suspend fun getCategoriaById(id: Int): CategoriaEntity?
}
