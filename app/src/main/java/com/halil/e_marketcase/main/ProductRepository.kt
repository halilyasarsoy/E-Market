package com.halil.e_marketcase.main

import com.halil.e_marketcase.data.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getProducts(apiKey: String): List<Product> {
        return apiService.getProducts(apiKey)
    }
}
