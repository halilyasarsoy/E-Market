package com.halil.e_marketcase.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.halil.e_marketcase.R
import com.halil.e_marketcase.data.Product
import com.halil.e_marketcase.databinding.ItemProductBinding



class ProductListAdapter(
    private var products: List<Product>,
    private val onAddToCartClick: (Product) -> Unit,
    private val onFavClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productPrice.text = "${product.price} $"
            Glide.with(binding.productImage.context)
                .load(product.image)
                .placeholder(R.drawable.placeee)
                .into(binding.productImage)

            binding.addToCartButton.setOnClickListener {
                onAddToCartClick(product)
            }
            binding.favButton.setOnClickListener {
                onFavClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
