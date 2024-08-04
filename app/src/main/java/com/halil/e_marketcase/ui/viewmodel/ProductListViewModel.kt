package com.halil.e_marketcase.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halil.e_marketcase.data.Product
import com.halil.e_marketcase.main.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _allProducts = MutableLiveData<List<Product>>()
    val allProducts: LiveData<List<Product>> = _allProducts

    private val _filteredProducts = MutableLiveData<List<Product>>()
    val filteredProducts: LiveData<List<Product>> = _filteredProducts

    private val _favoriteProducts = MutableLiveData<List<Product>>()
    val favoriteProducts: LiveData<List<Product>> = _favoriteProducts

    private var _lastFilterQuery: String? = null

    init {
        loadProductsFromDb() // Database'den ürünleri yükler
    }

    private fun loadProductsFromDb() {
        viewModelScope.launch {
            repository.allProducts.observeForever { products ->
                if (products.isNullOrEmpty()) {
                    loadProducts() // Database boşsa API'den ürünleri yükler ve kaydeder
                    Log.d("ProductListViewModel", "Database is empty, loading products from API")
                } else {
                    _allProducts.postValue(products)
                    _filteredProducts.postValue(products)
                    updateFavoriteProducts(products)
                    Log.d("ProductListViewModel", "Loaded products from database: ${products.size} items")
                }
            }
        }
    }

    fun loadProducts(apiKey: String = "5fc9346b2af77700165ae514") {
        viewModelScope.launch {
            val products = repository.getProducts(apiKey)
            repository.insertAllProducts(products) // API'den çekilen ürünleri veritabanına kaydet
            _allProducts.postValue(products)
            _filteredProducts.postValue(products)
            updateFavoriteProducts(products)
        }
    }

    fun filterProducts(query: String) {
        _lastFilterQuery = query
        _allProducts.value?.let { productList ->
            val filteredList = productList.filter {
                it.name.contains(query, ignoreCase = true)
            }
            _filteredProducts.postValue(filteredList)
        }
    }

    fun setPriceFilter(filter: String) {
        _allProducts.value?.let { productList ->
            val filteredList = when (filter) {
                "Price < 250" -> productList.filter { (it.price.toDoubleOrNull() ?: 0.0) < 250 }
                "Price 250-500" -> productList.filter {
                    val price = it.price.toDoubleOrNull() ?: 0.0
                    price >= 250 && price <= 500
                }
                "Price > 501" -> productList.filter { (it.price.toDoubleOrNull() ?: 0.0) > 501 }
                else -> productList
            }
            _filteredProducts.postValue(filteredList)
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            val updatedProduct = product.copy(isFavorite = !product.isFavorite)
            repository.updateFavoriteStatus(updatedProduct.id, updatedProduct.isFavorite)
            Log.d("ProductListViewModel", "Toggled favorite status for product ID: ${updatedProduct.id} to ${updatedProduct.isFavorite}")
            updateProductInList(updatedProduct)
        }
    }

    private fun updateProductInList(product: Product) {
        val updatedList = _allProducts.value?.map {
            if (it.id == product.id) product else it
        }
        _allProducts.postValue(updatedList)
        _filteredProducts.postValue(updatedList?.filter {
            it.name.contains(_lastFilterQuery ?: "", ignoreCase = true)
        })
        updateFavoriteProducts(updatedList ?: emptyList())
    }

    fun updateFavoriteProducts(products: List<Product>) {
        val favoriteList = products.filter { it.isFavorite }
        _favoriteProducts.postValue(favoriteList)
    }
}
