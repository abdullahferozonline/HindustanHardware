package com.hindustanhardware.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val sku: String = "",

    val barcode: String = "",

    val category: String = "Other",

    val purchasePrice: Double = 0.0,

    val sellingPrice: Double = 0.0,

    val quantity: Int = 0,

    val minimumStock: Int = 5,

    val unit: String = "Piece",

    val rack: String = "",

    val notes: String = "",

    val createdAt: Long = System.currentTimeMillis()
)
