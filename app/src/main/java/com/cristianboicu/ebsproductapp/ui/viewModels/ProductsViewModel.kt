package com.cristianboicu.ebsproductapp.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cristianboicu.ebsproductapp.Constants.QUERY_PAGE_SIZE
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
import com.cristianboicu.ebsproductapp.data.model.ProductsResponseApiModel
import com.cristianboicu.ebsproductapp.data.model.ProductsResponseDomainModel
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
class ProductsViewModel @Inject constructor(
    app: Application,
    private val repository: IDefaultRepository,
) :
    AndroidViewModel(app) {

    val allProducts: MutableLiveData<Resource<ProductsResponseDomainModel>> = MutableLiveData()
    var allProductsPage = 1
    private var allProductsResponseApiModel: ProductsResponseDomainModel? = null

    init {
        getAllProducts()
    }

    fun getAllProducts() = viewModelScope.launch {
        safeGetAllProductsCall()
    }

    private fun handleAllProductsResponse(responseApiModel: Response<ProductsResponseApiModel>): Resource<ProductsResponseDomainModel> {
        if (responseApiModel.isSuccessful) {
            responseApiModel.body()?.let { productsResponse ->
                allProductsPage++
                val response = productsResponse.asDomainModel()
                if (allProductsResponseApiModel == null) {
                    allProductsResponseApiModel = response
                } else {
                    val oldProducts = allProductsResponseApiModel?.results
                    val newProducts = response.results
                    oldProducts?.addAll(newProducts)
                }
                return Resource.Success(allProductsResponseApiModel ?: response)
            }
        }
        return Resource.Error(responseApiModel.message())
    }


    private suspend fun safeGetAllProductsCall() {
        allProducts.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(getApplication<BaseApplication>())) {
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

    fun changeProductFavoriteStatus(product: ProductDomainModel) {
        viewModelScope.launch {
            if (product.favorite) {
                product.favorite = !product.favorite
                repository.removeProductFromFavorites(productId = product.id)
            } else {
                product.favorite = !product.favorite
                repository.addProductToFavorites(product)
            }
        }
    }

}