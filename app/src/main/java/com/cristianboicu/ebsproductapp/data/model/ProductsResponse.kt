package com.cristianboicu.ebsproductapp.data.model

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

data class Product(
    val id: Int,
    val category: Category,
    val name: String,
    val details: String,
    val size: String,
    val colour: String,
    val price: Int,
    @SerializedName("main_image")
    val mainImage: String,
)

data class Category(
    val name: String,
    val icon: String,
    val id: Int,
)