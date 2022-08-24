package com.cristianboicu.ebsproductapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cristianboicu.ebsproductapp.BaseApplication
import com.cristianboicu.ebsproductapp.Constants.QUERY_PAGE_SIZE
import com.cristianboicu.ebsproductapp.data.model.ProductsResponse
import com.cristianboicu.ebsproductapp.data.repository.IDefaultRepository
import com.cristianboicu.ebsproductapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    app: Application,
    private val repository: IDefaultRepository,
) :
    AndroidViewModel(app) {

    val allProducts: MutableLiveData<Resource<ProductsResponse>> = MutableLiveData()
    var allProductsPage = 1
    private var allProductsResponse: ProductsResponse? = null

    init {
        getAllProducts()
    }

    fun getAllProducts() = viewModelScope.launch {
        safeGetAllProductsCall()
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

    private suspend fun safeGetAllProductsCall() {
        allProducts.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getAllProducts(allProductsPage, QUERY_PAGE_SIZE)
                allProducts.postValue(handleAllProductsResponse(response))
            } else {
                allProducts.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> allProducts.postValue(Resource.Error("Network failure!"))
                else -> allProducts.postValue(Resource.Error("Conversion error!"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<BaseApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}