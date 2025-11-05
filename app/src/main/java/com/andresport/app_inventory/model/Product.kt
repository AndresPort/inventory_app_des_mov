package com.andresport.app_inventory.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey val productRef: String,   // PK, varchar(4)
    val productName: String,              // varchar(100)
    val unitPrice: Double,                // Decimal(10,2)
    val stock: Long                       // bigint
)
