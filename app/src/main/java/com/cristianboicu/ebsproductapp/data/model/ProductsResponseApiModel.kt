package com.cristianboicu.ebsproductapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
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
        return ProductDomainModel(id, name, categoryId = category.id, details, size, colour, price, mainImage)
    }
}


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
    var favorite: Boolean = false
)

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
)

data class Category(
    val name: String,
    val icon: String,
    val id: Int,
)

data class ProductImage(
    val image: String,
)