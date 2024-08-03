package com.halil.e_marketcase.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.halil.e_marketcase.adapter.ProductListAdapter
import com.halil.e_marketcase.data.Product
import com.halil.e_marketcase.databinding.FragmentProductListBinding
import com.halil.e_marketcase.ui.base.BaseFragment
import com.halil.e_marketcase.ui.viewmodel.ProductListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment :
    BaseFragment<FragmentProductListBinding>(FragmentProductListBinding::inflate) {
    private lateinit var adapter: ProductListAdapter
    private val viewModel: ProductListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
        viewModel.loadProducts("5fc9346b2af77700165ae514") // Replace with your actual API key
    }

    private fun setupUI() {
        binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductListAdapter(emptyList(), ::onAddToCartClick, ::onFavClick)
        binding.productsRecyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter = ProductListAdapter(products, ::onAddToCartClick, ::onFavClick)
            binding.productsRecyclerView.adapter = adapter
        }
    }

    private fun onAddToCartClick(product: Product) {
        // Implement add to cart logic here
    }

    private fun onFavClick(product: Product) {
        // Implement favorite logic here
    }
}