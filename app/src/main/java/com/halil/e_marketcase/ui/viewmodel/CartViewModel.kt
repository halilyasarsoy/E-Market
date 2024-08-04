package com.halil.e_marketcase.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halil.e_marketcase.data.CartItem
import com.halil.e_marketcase.main.ProductRepository
import com.halil.e_marketcase.other.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    val cartItems: LiveData<List<CartItem>> = repository.allCartItems
    private val _addToCartStatus = MutableLiveData<Resource<Unit>>()
    val addToCartStatus: LiveData<Resource<Unit>> = _addToCartStatus

    fun addToCart(cartItem: CartItem) {
        viewModelScope.launch {
            _addToCartStatus.value = Resource.Loading()
            try {
                val existingItem = cartItems.value?.find { it.name == cartItem.name }
                if (existingItem != null) {
                    val updatedItem =
                        existingItem.copy(quantity = existingItem.quantity + cartItem.quantity)
                    repository.updateCartItem(updatedItem)
                } else {
                    repository.insertCartItem(cartItem)
                }
                _addToCartStatus.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _addToCartStatus.value = Resource.Error(e.message ?: "An error occurred")
            } finally {
                _addToCartStatus.value = Resource.Completed()
            }
        }
    }

    fun increaseQuantity(cartItem: CartItem) {
        viewModelScope.launch {
            val updatedItem = cartItem.copy(quantity = cartItem.quantity + 1)
            repository.updateCartItem(updatedItem)
        }
    }

    fun decreaseQuantity(cartItem: CartItem) {
        viewModelScope.launch {
            if (cartItem.quantity > 1) {
                val updatedItem = cartItem.copy(quantity = cartItem.quantity - 1)
                repository.updateCartItem(updatedItem)
            } else {
                repository.deleteCartItem(cartItem)
            }
        }
    }

    fun getTotalPrice(): Double {
        return cartItems.value?.sumOf { it.price * it.quantity } ?: 0.0
    }
}
