package com.andresport.app_inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.andresport.app_inventory.model.Product   // ✅ IMPORTANTE

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)
}
