package com.cristianboicu.ebsproductapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cristianboicu.ebsproductapp.BaseApplication
import com.cristianboicu.ebsproductapp.data.model.Product
import com.cristianboicu.ebsproductapp.data.repository.IDefaultRepository
import com.cristianboicu.ebsproductapp.util.Resource
import com.cristianboicu.ebsproductapp.util.Utils.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    app: Application,
    private val repository: IDefaultRepository,
) :
    AndroidViewModel(app) {

    private val _productDetails = MutableLiveData<Resource<Product>>()
    val productDetails = _productDetails

    fun getProductDetails(productId: Long) = viewModelScope.launch {
        safeGetProductDetailsCall(productId)
    }

    private suspend fun safeGetProductDetailsCall(productId: Long) {
        _productDetails.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(getApplication<BaseApplication>())) {
                val response = repository.getProductDetails(productId)
                _productDetails.postValue(handleProductDetailsResponse(response))
            } else {
                _productDetails.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _productDetails.postValue(Resource.Error("Network failure!"))
                else -> _productDetails.postValue(Resource.Error("Conversion error!"))
            }
        }
    }

    private fun handleProductDetailsResponse(response: Response<Product>): Resource<Product> {
        if (response.isSuccessful) {
            response.body()?.let { productsResponse ->
                return Resource.Success(productsResponse)
            }
        }
        return Resource.Error(response.message())
    }

}