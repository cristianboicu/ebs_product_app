package com.cristianboicu.ebsproductapp.data.remote

import javax.inject.Inject


class RemoteDataSource @Inject constructor(private val apiService: ApiService) : IRemoteDataSource {
    override fun fetchAllProducts(page: Int, pageSize: Int) {
    }
}