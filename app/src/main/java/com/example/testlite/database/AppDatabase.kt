package com.example.testlite.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.testlite.database.dao.CategoriaDao
import com.example.testlite.database.dao.ProductDao
import com.example.testlite.database.dao.TicketDao
import com.example.testlite.database.entities.CategoriaEntity
import com.example.testlite.database.entities.ProductEntity
import com.example.testlite.database.entities.TicketEntity
import com.example.testlite.database.entities.TicketItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        CategoriaEntity::class,
        TicketEntity::class,
        TicketItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun ticketDao(): TicketDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "testlite_database"
                )
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
    
    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Seed database with initial data
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedDatabase(database.categoriaDao(), database.productDao())
                }
            }
        }
        
        private suspend fun seedDatabase(categoriaDao: CategoriaDao, productDao: ProductDao) {
            // Seed categories
            val categories = listOf(
                CategoriaEntity(nombre = "Papas"),
                CategoriaEntity(nombre = "Sodas"),
                CategoriaEntity(nombre = "Galletas"),
                CategoriaEntity(nombre = "Lácteos"),
                CategoriaEntity(nombre = "Carnes"),
                CategoriaEntity(nombre = "Frutas"),
                CategoriaEntity(nombre = "Verduras"),
                CategoriaEntity(nombre = "Limpieza")
            )
            
            categories.forEach { categoriaDao.insert(it) }
            
            // Seed products
            val products = listOf(
                ProductEntity("123", "Sabritas Sal", 15.0, 1),
                ProductEntity("124", "Ruffles Queso", 15.0, 1),
                ProductEntity("125", "Coca Cola 600ml", 18.0, 2),
                ProductEntity("126", "Pepsi 600ml", 16.0, 2),
                ProductEntity("127", "Emperador Chocolate", 20.0, 3),
                ProductEntity("128", "Leche Lala 1L", 28.0, 4),
                ProductEntity("129", "Huevo 1kg", 45.0, 5),
                ProductEntity("130", "Manzana Roja kg", 35.0, 6)
            )
            
            products.forEach { productDao.insert(it) }
        }
    }
}
