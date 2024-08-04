package com.halil.e_marketcase.ui.viewmodel

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
class FavViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _favoriteProducts = MutableLiveData<List<Product>>()
    val favoriteProducts: LiveData<List<Product>> = _favoriteProducts

    init {
        loadFavoriteProducts()
    }

    private fun loadFavoriteProducts() {
        viewModelScope.launch {
            val favorites = repository.allProducts.value?.filter { it.isFavorite } ?: emptyList()
            _favoriteProducts.postValue(favorites)
        }
    }
}
