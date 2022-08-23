package com.cristianboicu.ebsproductapp.di

import com.cristianboicu.ebsproductapp.Constants.BASE_URL
import com.cristianboicu.ebsproductapp.data.remote.ApiService
import com.cristianboicu.ebsproductapp.data.remote.RemoteDataSource
import com.cristianboicu.ebsproductapp.data.repository.DefaultRepository
import com.cristianboicu.ebsproductapp.data.repository.IDefaultRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindDefaultRepository(
        defaultRepository: DefaultRepository,
    ): IDefaultRepository

    companion object {

        @Provides
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        fun provideApiService(retrofit: Retrofit): ApiService =
            retrofit.create(ApiService::class.java)

        @Provides
        fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource =
            RemoteDataSource(apiService)

        @Provides
        fun provideDefaultRepository(remoteDataSource: RemoteDataSource): DefaultRepository =
            DefaultRepository(remoteDataSource)
    }
}