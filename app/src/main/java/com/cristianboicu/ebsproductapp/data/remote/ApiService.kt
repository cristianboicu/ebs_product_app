package com.cristianboicu.ebsproductapp.data.remote

import com.cristianboicu.ebsproductapp.data.model.Product
import com.cristianboicu.ebsproductapp.data.model.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/products")
    suspend fun getAllProducts(
        @Query("page")
        pageNumber: Int = 1,
        @Query("page_size")
        pageSize: Int = 10
    ): Response<ProductsResponse>

    @GET("/products/{id}")
    suspend fun getProductDetails(
        @Path("id")
        productId: Long
    ): Response<Product>
}