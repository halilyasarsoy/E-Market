package com.halil.e_marketcase.main

import com.halil.e_marketcase.data.Product
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("products")
    suspend fun getProducts(@Header("Authorization") apiKey: String): List<Product>
}