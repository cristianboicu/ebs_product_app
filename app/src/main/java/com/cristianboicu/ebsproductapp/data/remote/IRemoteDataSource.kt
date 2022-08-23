package com.cristianboicu.ebsproductapp.data.remote

import com.cristianboicu.ebsproductapp.data.model.ProductsResponse
import retrofit2.Response

interface IRemoteDataSource {
    suspend fun getAllProducts(page: Int, pageSize: Int): Response<ProductsResponse>
}