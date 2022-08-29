package com.cristianboicu.ebsproductapp.data.model

import com.google.gson.annotations.SerializedName

data class ProductsResponseApiModel(
    val count: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("current_page")
    val currentPage: Int,
    val results: MutableList<ProductApiModel>,
) {
    fun asDomainModel(): ProductsResponseDomainModel {
        val list = mutableListOf<ProductDomainModel>()
        for (item in this.results) {
            list.add(item.asDomainModel())
        }
        return ProductsResponseDomainModel(totalPages, list)
    }
}

data class ProductApiModel(
    val id: Long,
    val name: String,
    val category: Category,
    val details: String,
    val size: String,
    val colour: String,
    val price: Int,
    @SerializedName("main_image")
    val mainImage: String,
) {
    fun asDomainModel(): ProductDomainModel {
        return ProductDomainModel(id,
            name,
            categoryId = category.id,
            details,
            size,
            colour,
            price,
            mainImage)
    }
}

data class ProductDetails(
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
    var favorite: Boolean = false,
) {
    fun asDomainModel(): ProductDomainModel {
        return ProductDomainModel(id,
            name,
            categoryId = category.id,
            details,
            size,
            colour,
            price,
            mainImage)
    }
}

data class Category(
    val name: String,
    val icon: String,
    val id: Int,
)

data class ProductImage(
    val image: String,
)