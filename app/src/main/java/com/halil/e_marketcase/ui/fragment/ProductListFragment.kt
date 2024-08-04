package com.halil.e_marketcase.ui.fragment

import android.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.halil.e_marketcase.ui.adapter.ProductListAdapter
import com.halil.e_marketcase.data.Product
import com.halil.e_marketcase.databinding.FragmentProductListBinding
import com.halil.e_marketcase.ui.base.BaseFragment
import com.halil.e_marketcase.ui.viewmodel.ProductListViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController
import com.halil.e_marketcase.data.CartItem
import com.halil.e_marketcase.ui.viewmodel.CartViewModel

@AndroidEntryPoint
class ProductListFragment : BaseFragment<FragmentProductListBinding>(FragmentProductListBinding::inflate) {

    private lateinit var adapter: ProductListAdapter
    private val viewModel: ProductListViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductListAdapter(emptyList(), ::onAddToCartClick, ::onFavClick, ::onItemClick)
        binding.productsRecyclerView.adapter = adapter

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        val filters = listOf("All", "Price < 250", "Price 250-500", "Price > 501")
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, filters)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filtersSpinner.adapter = spinnerAdapter

        binding.filtersSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFilter = filters[position]
                viewModel.setPriceFilter(selectedFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun observeViewModel() {
        viewModel.filteredProducts.observe(viewLifecycleOwner) { products ->
            products?.let {
                adapter.updateProducts(it)
            }
        }
    }

    private fun onAddToCartClick(product: Product) {
        val price = product.price.toDoubleOrNull() ?: 0.0
        val cartItem = CartItem(name = product.name, price = price)
        Log.d("ProductListFragment", "Adding to cart: ${cartItem.name}, ${cartItem.price}")
        cartViewModel.addToCart(cartItem)
    }

    private fun onFavClick(product: Product) {
        viewModel.toggleFavorite(product)
    }

    private fun onItemClick(product: Product) {
        val action = ProductListFragmentDirections.actionProductListFragmentToProductDetailFragment(product)
        findNavController().navigate(action)
    }
}

