package com.halil.e_marketcase.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.halil.e_marketcase.data.CartItem
import com.halil.e_marketcase.data.Product
import com.halil.e_marketcase.databinding.FragmentFavoritePageBinding
import com.halil.e_marketcase.ui.adapter.ProductFavoriteAdapter
import com.halil.e_marketcase.ui.base.BaseFragment
import com.halil.e_marketcase.ui.viewmodel.CartViewModel
import com.halil.e_marketcase.ui.viewmodel.ProductListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritePageFragment :
    BaseFragment<FragmentFavoritePageBinding>(FragmentFavoritePageBinding::inflate) {

    private lateinit var adapter: ProductFavoriteAdapter
    private val viewModel: ProductListViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter =
            ProductFavoriteAdapter(emptyList(), ::onAddToCartClick, ::onFavClick, ::onItemClick)
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteRecyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.favoriteProducts.observe(viewLifecycleOwner) { products ->
            products?.let {
                Log.d("FavoritePageFragment", "Updating adapter with ${it.size} favorite products")
                adapter.updateProducts(it)
                updateEmptyView(it.isEmpty())
            }
        }
    }

    private fun updateEmptyView(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyTextView.visibility = View.VISIBLE
            binding.favoriteRecyclerView.visibility = View.GONE
        } else {
            binding.emptyTextView.visibility = View.GONE
            binding.favoriteRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun onAddToCartClick(product: Product) {
        val price = product.price.toDoubleOrNull() ?: 0.0
        val cartItem = CartItem(name = product.name, price = price)
        Log.d("FavoritePageFragment", "Adding to cart: ${cartItem.name}, ${cartItem.price}")
        cartViewModel.addToCart(cartItem)
    }

    private fun onFavClick(product: Product) {
        viewModel.toggleFavorite(product)
    }

    private fun onItemClick(product: Product) {
        val action =
            FavoritePageFragmentDirections.actionFavoritePageFragmentToProductDetailFragment(product)
        findNavController().navigate(action)
    }
}
