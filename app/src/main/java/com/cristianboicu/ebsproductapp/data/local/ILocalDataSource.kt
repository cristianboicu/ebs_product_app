package com.cristianboicu.ebsproductapp.data.local

import androidx.lifecycle.LiveData
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel

interface ILocalDataSource {
    fun observeFavoriteProducts(): LiveData<List<ProductDomainModel>>
    fun observeFavoriteProductsIds(): LiveData<List<Long>>
    suspend fun addProductToFavorites(product: ProductDomainModel)
    suspend fun removeProductFromFavorites(productId: Long)
}
