package com.cristianboicu.ebsproductapp.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
import com.cristianboicu.ebsproductapp.data.repository.IDefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteProductsViewModel @Inject constructor(private val repository: IDefaultRepository) :
    ViewModel() {

    private val _favoriteProducts = repository.observeFavoriteProducts()
    val favoriteProducts = _favoriteProducts

    fun changeProductFavoriteStatus(product: ProductDomainModel) {
        viewModelScope.launch {
            product.favorite = !product.favorite
            if (!product.favorite) {
                repository.removeProductFromFavorites(productId = product.id)
            } else {
                repository.addProductToFavorites(product)
            }
        }

    }
}