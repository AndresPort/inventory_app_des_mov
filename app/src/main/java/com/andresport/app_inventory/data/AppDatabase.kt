package com.andresport.app_inventory.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.andresport.app_inventory.model.Product   // ✅ AGREGA ESTA LÍNEA

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "inventory_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
