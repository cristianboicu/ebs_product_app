package com.cristianboicu.ebsproductapp.data.repository

import androidx.lifecycle.LiveData
import com.cristianboicu.ebsproductapp.data.model.Product
import com.cristianboicu.ebsproductapp.data.model.ProductDetails
import com.cristianboicu.ebsproductapp.data.model.ProductsResponse
import retrofit2.Response

interface IDefaultRepository {
    suspend fun getAllProducts(page: Int, pageSize: Int): Response<ProductsResponse>
    suspend fun getProductDetails(productId: Long): Response<ProductDetails>
    fun observeFavoriteProducts(): LiveData<List<Product>>
    suspend fun addProductToFavorites(product: Product)
    suspend fun removeProductFromFavorites(productId: Long)
}