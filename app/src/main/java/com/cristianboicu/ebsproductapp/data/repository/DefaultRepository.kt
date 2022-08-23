package com.cristianboicu.ebsproductapp.data.repository

import com.cristianboicu.ebsproductapp.data.remote.IRemoteDataSource
import javax.inject.Inject

class DefaultRepository @Inject constructor(private val remoteDataSource: IRemoteDataSource) :
    IDefaultRepository {
}