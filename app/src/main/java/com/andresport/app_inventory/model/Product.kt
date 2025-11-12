package com.andresport.app_inventory.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey
    @ColumnInfo(name = "product_ref")
    val productRef: String,   // PK, varchar(4)
    @ColumnInfo(name = "product_name")
    val productName: String,              // varchar(100)
    @ColumnInfo(name = "unit_price")
    val unitPrice: Double,                // Decimal(10,2)
    @ColumnInfo(name = "stock")
    val stock: Long                       // bigint
){
    val total: Double
        get() = unitPrice * stock
}
