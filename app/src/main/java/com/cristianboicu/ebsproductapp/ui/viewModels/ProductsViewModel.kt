package com.cristianboicu.ebsproductapp.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.cristianboicu.ebsproductapp.R
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
import com.cristianboicu.ebsproductapp.data.model.ProductsResponseApiModel
import com.cristianboicu.ebsproductapp.data.model.ProductsResponseDomainModel
import com.cristianboicu.ebsproductapp.data.repository.IDefaultRepository
import com.cristianboicu.ebsproductapp.di.BaseApplication
import com.cristianboicu.ebsproductapp.util.Constants.QUERY_PAGE_SIZE
import com.cristianboicu.ebsproductapp.util.Resource
import com.cristianboicu.ebsproductapp.util.Utils.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    val app: Application,
    private val repository: IDefaultRepository,
) :
    AndroidViewModel(app) {

    val allProducts: MutableLiveData<Resource<ProductsResponseDomainModel>> = MutableLiveData()
    var allProductsPage = 1
    private var allProductsResponse: ProductsResponseDomainModel? = null

    val favoriteProducts =
        Transformations.map(repository.observeFavoriteProductsIds()) { favoriteList ->
            viewModelScope.launch(Dispatchers.Default) {
                allProducts.value?.data?.let {
                    allProducts.postValue(Resource.Success(syncAllProductsWithFavorites(favoriteList,
                        it)))
                }
            }
            favoriteList
        }

    init {
        getAllProducts()
    }

    fun getAllProducts() = viewModelScope.launch {
        safeGetAllProductsCall()
    }

    private fun syncAllProductsWithFavorites(
        favoriteList: List<Long>?,
        allProducts: ProductsResponseDomainModel,
    ): ProductsResponseDomainModel {
        favoriteList?.let {
            allProducts.results.onEach {
                it.favorite = favoriteList.contains(it.id)
            }
        }
        return allProducts
    }

    private fun handleAllProductsResponse(responseApiModel: Response<ProductsResponseApiModel>): Resource<ProductsResponseDomainModel> {
        if (responseApiModel.isSuccessful) {
            responseApiModel.body()?.let { productsResponse ->
                allProductsPage++

                val response = syncAllProductsWithFavorites(favoriteProducts.value,
                    productsResponse.asDomainModel())

                if (allProductsResponse == null) {
                    allProductsResponse = response
                } else {
                    val oldProducts = allProductsResponse?.results
                    val newProducts = response.results
                    oldProducts?.addAll(newProducts)
                }
                return Resource.Success(allProductsResponse ?: response)
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
                allProducts.postValue(Resource.Error(app.getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> allProducts.postValue(Resource.Error(app.getString(R.string.network_failure)))
                else -> allProducts.postValue(Resource.Error(app.getString(R.string.conversion_error)))
            }
        }
    }

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
