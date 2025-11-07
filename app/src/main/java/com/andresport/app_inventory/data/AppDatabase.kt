package com.andresport.app_inventory.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import com.andresport.app_inventory.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Product::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "inventory_app_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Se ejecuta SOLO una vez, cuando se crea la base de datos
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).productDao()

                                dao.insertProduct(
                                    Product(
                                        productRef = "1",
                                        productName = "Pan artesanal",
                                        unitPrice = 2500.0,
                                        stock = 30
                                    )
                                )

                                dao.insertProduct(
                                    Product(
                                        productRef = "2",
                                        productName = "Croissant",
                                        unitPrice = 3000.0,
                                        stock = 15
                                    )
                                )

                                dao.insertProduct(
                                    Product(
                                        productRef = "3",
                                        productName = "Galletas de avena",
                                        unitPrice = 1500.0,
                                        stock = 50
                                    )
                                )
                                val products = dao.getAllProducts()
                                products.forEach {
                                    android.util.Log.d("ROOM_DB", "Producto insertado: ${it.productName} (${it.unitPrice})")
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration() // 👈 añade esto
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

