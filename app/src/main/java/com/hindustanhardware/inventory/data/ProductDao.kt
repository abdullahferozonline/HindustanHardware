package com.hindustanhardware.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY name COLLATE NOCASE ASC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Long): Product?

    @Query(
        """
        SELECT * FROM products
        WHERE name LIKE '%' || :query || '%'
           OR sku LIKE '%' || :query || '%'
           OR barcode LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
        ORDER BY name COLLATE NOCASE ASC
        """
    )
    fun searchProducts(query: String): Flow<List<Product>>

    @Query("SELECT COUNT(*) FROM products")
    fun getProductCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM products")
    fun getTotalUnits(): Flow<Int>

    @Query("SELECT COUNT(*) FROM products WHERE quantity > minimumStock")
    fun getInStockCount(): Flow<Int>

    @Query(
        """
        SELECT COUNT(*) FROM products
        WHERE quantity > 0 AND quantity <= minimumStock
        """
    )
    fun getLowStockCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM products WHERE quantity <= 0")
    fun getOutOfStockCount(): Flow<Int>

    @Query(
        """
        SELECT * FROM products
        WHERE barcode = :barcode
        LIMIT 1
        """
    )
    suspend fun getProductByBarcode(barcode: String): Product?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)
}
