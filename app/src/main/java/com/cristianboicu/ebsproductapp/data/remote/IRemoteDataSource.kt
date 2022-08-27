package com.cristianboicu.ebsproductapp.data.remote

import com.cristianboicu.ebsproductapp.data.model.ProductDetails
import com.cristianboicu.ebsproductapp.data.model.ProductsResponseApiModel
import retrofit2.Response

interface IRemoteDataSource {
    suspend fun getAllProducts(page: Int, pageSize: Int): Response<ProductsResponseApiModel>
    suspend fun getProductDetails(productId: Long): Response<ProductDetails>
}