package com.cristianboicu.ebsproductapp.data.remote

interface IRemoteDataSource {
    fun fetchAllProducts(page: Int, pageSize: Int)
}