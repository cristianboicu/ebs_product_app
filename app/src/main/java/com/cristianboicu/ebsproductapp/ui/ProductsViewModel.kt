package com.cristianboicu.ebsproductapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianboicu.ebsproductapp.data.model.ProductsResponse
import com.cristianboicu.ebsproductapp.data.repository.IDefaultRepository
import com.cristianboicu.ebsproductapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val repository: IDefaultRepository) :
    ViewModel() {

    val allProducts: MutableLiveData<Resource<ProductsResponse>> = MutableLiveData()
    var allProductsPage = 1
    var allProductsResponse: ProductsResponse? = null

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            allProducts.postValue(Resource.Loading())
            val response = repository.getAllProducts(allProductsPage, 10)
            allProducts.postValue(handleAllProductsResponse(response))
        }
    }

    private fun handleAllProductsResponse(response: Response<ProductsResponse>): Resource<ProductsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { productsResponse ->
                allProductsPage++
                if (allProductsResponse == null) {
                    allProductsResponse = productsResponse
                } else {
                    val oldProducts = allProductsResponse?.results
                    val newProducts = productsResponse.results
                    oldProducts?.addAll(newProducts)
                }
                return Resource.Success(allProductsResponse ?: productsResponse)
            }
        }
        return Resource.Error(response.message())
    }

}