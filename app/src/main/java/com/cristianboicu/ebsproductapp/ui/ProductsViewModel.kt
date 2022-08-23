package com.cristianboicu.ebsproductapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianboicu.ebsproductapp.data.model.ProductsResponse
import com.cristianboicu.ebsproductapp.data.repository.IDefaultRepository
import com.cristianboicu.ebsproductapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val repository: IDefaultRepository) :
    ViewModel() {

    val allProducts: MutableLiveData<Resource<ProductsResponse>> = MutableLiveData()
    var allProductsPage = 1


    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            val response = repository.getAllProducts(1, 10)
            if (response.isSuccessful) {
                response.body()?.let { productsResponse ->
                    allProductsPage++
                    allProducts.postValue(Resource.Success(productsResponse))
                }
            } else allProducts.postValue(Resource.Error(response.message()))
        }
    }

}