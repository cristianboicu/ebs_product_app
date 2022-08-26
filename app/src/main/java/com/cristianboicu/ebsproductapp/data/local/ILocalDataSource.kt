package com.cristianboicu.ebsproductapp.data.local

import androidx.lifecycle.LiveData
import com.cristianboicu.ebsproductapp.data.model.Product

interface ILocalDataSource {
    fun observeFavoriteProducts(): LiveData<List<Product>>
    suspend fun addProductToFavorites(product: Product)
    suspend fun removeProductFromFavorites(productId: Long)
}
