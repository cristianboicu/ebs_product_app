package com.cristianboicu.ebsproductapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class ProductsResponseDomainModel(
    val totalPages: Int,
    val results: MutableList<ProductDomainModel>,
)

@Entity(tableName = "products")
data class ProductDomainModel(
    @PrimaryKey
    val id: Long,
    val name: String,
    val categoryId: Int,
    val details: String,
    val size: String,
    val colour: String,
    val price: Int,
    val mainImage: String,
    var favorite: Boolean = false,
)