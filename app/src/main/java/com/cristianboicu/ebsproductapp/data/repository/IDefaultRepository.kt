package com.cristianboicu.ebsproductapp.data.repository

import com.cristianboicu.ebsproductapp.data.model.ProductsResponse
import retrofit2.Response

interface IDefaultRepository {
    suspend fun getAllProducts(page: Int, pageSize: Int): Response<ProductsResponse>
}