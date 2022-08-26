package com.cristianboicu.ebsproductapp.data.local

import androidx.lifecycle.LiveData
import com.cristianboicu.ebsproductapp.data.model.Product
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val productsDao: ProductsDao) : ILocalDataSource {
    override fun observeFavoriteProducts(): LiveData<List<Product>> =
        productsDao.observeFavoriteProducts()

    override suspend fun addProductToFavorites(product: Product) =
        productsDao.addProductToFavorites(product)

    override suspend fun removeProductFromFavorites(productId: Long) =
        productsDao.removeProductFromFavorites(productId)
}