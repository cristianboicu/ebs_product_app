package com.cristianboicu.ebsproductapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cristianboicu.ebsproductapp.data.model.Product

@Database(entities = [Product::class], version = 1)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun getProductsDao(): ProductsDao
}