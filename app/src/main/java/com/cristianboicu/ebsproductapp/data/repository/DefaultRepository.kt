package com.cristianboicu.ebsproductapp.data.repository

import com.cristianboicu.ebsproductapp.data.remote.IRemoteDataSource
import javax.inject.Inject

class DefaultRepository @Inject constructor(private val remoteDataSource: IRemoteDataSource) :
    IDefaultRepository {
    override suspend fun getAllProducts(page: Int, pageSize: Int) =
        remoteDataSource.getAllProducts(page, pageSize)

    override suspend fun getProductDetails(productId: Long) =
        remoteDataSource.getProductDetails(productId)
}