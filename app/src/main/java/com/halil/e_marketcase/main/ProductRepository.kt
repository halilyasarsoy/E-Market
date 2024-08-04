package com.halil.e_marketcase.main

import android.util.Log
import androidx.lifecycle.LiveData
import com.halil.e_marketcase.data.CartItem
import com.halil.e_marketcase.data.Product
import com.halil.e_marketcase.data.local.CartDao
import com.halil.e_marketcase.data.local.ProductDao
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ApiService,
    private val productDao: ProductDao,
    private val cartDao: CartDao
) {

    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

    suspend fun getProducts(apiKey: String): List<Product> {
        return apiService.getProducts(apiKey)
    }

    suspend fun insertAllProducts(products: List<Product>) {
        productDao.insertAll(products)

    }

    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean) {
        productDao.updateFavoriteStatus(id, isFavorite)
        Log.d("ProductRepository", "Updated favorite status for product ID: $id to $isFavorite")

    }

    val allCartItems: LiveData<List<CartItem>> = cartDao.getAllCartItems()

    suspend fun insertCartItem(cartItem: CartItem) {
        cartDao.insertCartItem(cartItem)
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.updateCartItem(cartItem)
    }

    suspend fun deleteCartItem(cartItem: CartItem) {
        cartDao.deleteCartItem(cartItem)
    }
}