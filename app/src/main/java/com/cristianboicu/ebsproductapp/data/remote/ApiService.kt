package com.cristianboicu.ebsproductapp.data.remote

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import com.cristianboicu.ebsproductapp.data.model.ProductDetails
import com.cristianboicu.ebsproductapp.data.model.ProductsResponseApiModel
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
        pageSize: Int = PAGE_SIZE,
    ): Response<ProductsResponseApiModel>

    @GET("/products/{id}")
    suspend fun getProductDetails(
        @Path("id")
        productId: Long,
    ): Response<ProductDetails>
}