package com.halil.e_marketcase.ui.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.halil.e_marketcase.R
import com.halil.e_marketcase.data.CartItem
import com.halil.e_marketcase.data.Product
import com.halil.e_marketcase.databinding.ItemProductBinding
import kotlin.reflect.KFunction1

class ProductListAdapter(
    private var products: List<Product>,
    private val onAddToCartClick: (Product) -> Unit,
    private val onFavClick: (Product) -> Unit,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productPrice.text = "${product.price} $"
            Glide.with(binding.productImage.context)
                .load(product.image)
                .placeholder(R.drawable.place_holder)
                .into(binding.productImage)

            val favIcon = if (product.isFavorite) {
                R.drawable.baseline_favorite_24 // Favori ise dolu yıldız ikonu
            } else {
                R.drawable.baseline_favorite_border_24 // Favori değilse boş yıldız ikonu
            }
            binding.favButton.setImageResource(favIcon)

            binding.addToCartButton.setOnClickListener {
                onAddToCartClick(product)
            }
            binding.favButton.setOnClickListener {
                onFavClick(product)
                startFavAnimation(binding.favButton)
            }
            binding.root.setOnClickListener {
                onItemClick(product)
            }
        }

        private fun startFavAnimation(view: View) {
            val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f)
            val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f)
            val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f)
            val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f)

            val alphaIn = ObjectAnimator.ofFloat(view, "alpha", 0.5f)
            val alphaOut = ObjectAnimator.ofFloat(view, "alpha", 1.0f)

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleUpX, scaleUpY, alphaIn)
            animatorSet.duration = 200
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    val resetAnimatorSet = AnimatorSet()
                    resetAnimatorSet.playTogether(scaleDownX, scaleDownY, alphaOut)
                    resetAnimatorSet.duration = 200
                    resetAnimatorSet.start()
                }
            })
            animatorSet.start()
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
