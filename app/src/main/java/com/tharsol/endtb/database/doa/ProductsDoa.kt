package com.tharsol.endtb.database.doa

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tharsol.endtb.database.entity.ProductEntity
import com.tharsol.endtb.deserialize.Product

@Dao
interface ProductsDoa {


    @Insert(entity = ProductEntity::class)
    abstract fun insert(items: List<Product>): List<Long>

    @Query("select * from ".plus(ProductEntity.TABLE_NAME))
    fun getProducts(): List<Product>


}