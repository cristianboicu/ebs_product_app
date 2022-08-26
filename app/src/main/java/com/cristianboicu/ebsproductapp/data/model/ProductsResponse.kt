package com.cristianboicu.ebsproductapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    val count: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("current_page")
    val currentPage: Int,
    val results: MutableList<Product>,
)

@Entity
data class Product(
    @PrimaryKey
    val id: Long,
    val name: String,
    val details: String,
    val size: String,
    val colour: String,
    val price: Int,
    @SerializedName("main_image")
    val mainImage: String,
    var favorite: Boolean = false
)

@Entity
data class ProductDetails(
    @PrimaryKey
    val id: Long,
    val category: Category,
    val name: String,
    val details: String,
    val size: String,
    val colour: String,
    val price: Int,
    @SerializedName("main_image")
    val mainImage: String,
    val images: List<ProductImage>,
    var liked: Boolean = false
)

data class Category(
    val name: String,
    val icon: String,
    val id: Int,
)

data class ProductImage(
    val image: String,
)