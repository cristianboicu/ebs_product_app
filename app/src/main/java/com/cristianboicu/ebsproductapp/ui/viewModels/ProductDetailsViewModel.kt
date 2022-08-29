package com.cristianboicu.ebsproductapp.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cristianboicu.ebsproductapp.data.model.ProductDetails
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
import com.cristianboicu.ebsproductapp.data.repository.IDefaultRepository
import com.cristianboicu.ebsproductapp.di.BaseApplication
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

    private val _productDetails = MutableLiveData<Resource<ProductDetails>>()
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

    private fun handleProductDetailsResponse(response: Response<ProductDetails>): Resource<ProductDetails> {
        if (response.isSuccessful) {
            response.body()?.let { productsResponse ->
                return Resource.Success(productsResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun changeProductFavoriteStatus(favorites: Boolean) {
        val product = convertProductsDetailsToDomainModel()

        product?.let {
            it.favorite = favorites
            viewModelScope.launch {
                if (!it.favorite) {
                    repository.removeProductFromFavorites(it.id)
                } else {
                    repository.addProductToFavorites(it)
                }
            }
        }
    }

    private fun convertProductsDetailsToDomainModel(): ProductDomainModel? {
        _productDetails.value?.data?.let {
            return it.asDomainModel()
        }
        return null
    }
}
