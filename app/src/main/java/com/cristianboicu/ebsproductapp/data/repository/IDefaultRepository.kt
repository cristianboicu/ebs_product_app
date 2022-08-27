package com.cristianboicu.ebsproductapp.data.repository

import androidx.lifecycle.LiveData
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
import com.cristianboicu.ebsproductapp.data.model.ProductDetails
import com.cristianboicu.ebsproductapp.data.model.ProductsResponseApiModel
import retrofit2.Response

interface IDefaultRepository {
    suspend fun getAllProducts(page: Int, pageSize: Int): Response<ProductsResponseApiModel>
    suspend fun getProductDetails(productId: Long): Response<ProductDetails>
    fun observeFavoriteProducts(): LiveData<List<ProductDomainModel>>
    suspend fun addProductToFavorites(product: ProductDomainModel)
    suspend fun removeProductFromFavorites(productId: Long)
}