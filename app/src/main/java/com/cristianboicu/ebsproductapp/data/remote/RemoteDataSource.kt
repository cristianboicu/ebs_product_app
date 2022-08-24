package com.cristianboicu.ebsproductapp.data.remote

import javax.inject.Inject


class RemoteDataSource @Inject constructor(private val apiService: ApiService) : IRemoteDataSource {
    override suspend fun getAllProducts(page: Int, pageSize: Int) =
        apiService.getAllProducts(page, pageSize)

    override suspend fun getProductDetails(productId: Long) =
        apiService.getProductDetails(productId)

}