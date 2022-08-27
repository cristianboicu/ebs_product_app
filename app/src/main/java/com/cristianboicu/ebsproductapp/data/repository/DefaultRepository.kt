package com.cristianboicu.ebsproductapp.data.repository

import com.cristianboicu.ebsproductapp.data.local.ILocalDataSource
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
import com.cristianboicu.ebsproductapp.data.remote.IRemoteDataSource
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource,
) :
    IDefaultRepository {
    override suspend fun getAllProducts(page: Int, pageSize: Int) =
        remoteDataSource.getAllProducts(page, pageSize)

    override suspend fun getProductDetails(productId: Long) =
        remoteDataSource.getProductDetails(productId)

    override fun observeFavoriteProducts() = localDataSource.observeFavoriteProducts()

    override suspend fun addProductToFavorites(product: ProductDomainModel) =
        localDataSource.addProductToFavorites(product)

    override suspend fun removeProductFromFavorites(productId: Long) =
        localDataSource.removeProductFromFavorites(productId)
}