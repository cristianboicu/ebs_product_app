package com.cristianboicu.ebsproductapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel

@Dao
interface ProductsDao {

    @Query("SELECT * from products")
    fun observeFavoriteProducts(): LiveData<List<ProductDomainModel>>

    @Insert(onConflict = REPLACE)
    suspend fun addProductToFavorites(product: ProductDomainModel)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun removeProductFromFavorites(productId: Long)

}
