package com.halil.e_marketcase.ui.viewmodel

import android.util.Log
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

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun loadProducts(apiKey: String) {
        viewModelScope.launch {
            try {
                val products = repository.getProducts(apiKey)
                _products.value = products

                Log.d("ProductListViewModel", "Loaded products: ${products.size}")
            } catch (e: Exception) {

                Log.e("ProductListViewModel", "Error loading products", e)
            }
        }
    }
}
