package com.cristianboicu.ebsproductapp.di

import android.content.Context
import androidx.room.Room
import com.cristianboicu.ebsproductapp.util.Constants.BASE_URL
import com.cristianboicu.ebsproductapp.data.local.LocalDataSource
import com.cristianboicu.ebsproductapp.data.local.ProductsDao
import com.cristianboicu.ebsproductapp.data.local.ProductsDatabase
import com.cristianboicu.ebsproductapp.data.remote.ApiService
import com.cristianboicu.ebsproductapp.data.remote.RemoteDataSource
import com.cristianboicu.ebsproductapp.data.repository.DefaultRepository
import com.cristianboicu.ebsproductapp.data.repository.IDefaultRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindDefaultRepository(
        defaultRepository: DefaultRepository,
    ): IDefaultRepository

    companion object {

        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context,
        ) = Room.databaseBuilder(
            context.applicationContext,
            ProductsDatabase::class.java,
            "products"
        ).build()

        @Singleton
        @Provides
        fun provideDao(db: ProductsDatabase) = db.getProductsDao()

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
        fun provideLocalDataSource(productsDao: ProductsDao): LocalDataSource =
            LocalDataSource(productsDao)

        @Provides
        fun provideDefaultRepository(
            localDataSource: LocalDataSource,
            remoteDataSource: RemoteDataSource,
        ): DefaultRepository =
            DefaultRepository(localDataSource, remoteDataSource)
    }
}