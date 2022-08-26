package com.cristianboicu.ebsproductapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.cristianboicu.ebsproductapp.data.model.Product

@Dao
interface ProductsDao {

    @Query("SELECT * from product")
    fun observeFavoriteProducts(): LiveData<List<Product>>

    @Insert(onConflict = REPLACE)
    suspend fun addProductToFavorites(product: Product)

    @Query("DELETE FROM product WHERE id = :productId")
    suspend fun removeProductFromFavorites(productId: Long)

}
