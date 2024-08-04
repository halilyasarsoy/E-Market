package com.halil.e_marketcase.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.halil.e_marketcase.data.CartItem
import com.halil.e_marketcase.databinding.ItemCartBinding

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onIncreaseQuantity: (CartItem) -> Unit,
    private val onDecreaseQuantity: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.productNameTextView.text = cartItem.name
            binding.productPriceTextView.text = cartItem.price.toString()
            binding.quantityTextView.text = cartItem.quantity.toString()

            binding.increaseButton.setOnClickListener {
                onIncreaseQuantity(cartItem)
            }

            binding.decreaseButton.setOnClickListener {
                onDecreaseQuantity(cartItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount() = cartItems.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateProducts(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
}
