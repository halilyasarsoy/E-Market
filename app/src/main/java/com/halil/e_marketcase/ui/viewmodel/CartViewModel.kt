package com.halil.e_marketcase.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halil.e_marketcase.data.CartItem
import com.halil.e_marketcase.main.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    val cartItems: LiveData<List<CartItem>> = repository.allCartItems

    fun addToCart(cartItem: CartItem) {
        viewModelScope.launch {
            val existingItem = cartItems.value?.find { it.name == cartItem.name }
            if (existingItem != null) {
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + cartItem.quantity)
                repository.updateCartItem(updatedItem)
            } else {
                repository.insertCartItem(cartItem)
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