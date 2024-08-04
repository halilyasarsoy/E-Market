package com.halil.e_marketcase.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.halil.e_marketcase.R
import com.halil.e_marketcase.ui.adapter.CartAdapter
import com.halil.e_marketcase.data.CartItem
import com.halil.e_marketcase.databinding.FragmentCartPageBinding
import com.halil.e_marketcase.ui.base.BaseFragment
import com.halil.e_marketcase.ui.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartPageFragment : BaseFragment<FragmentCartPageBinding>(FragmentCartPageBinding::inflate) {
    private val viewModel: CartViewModel by viewModels({ requireActivity() })
    private lateinit var adapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        adapter = CartAdapter(emptyList(), ::onIncreaseQuantity, ::onDecreaseQuantity)
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartItems?.let {
                Log.d(
                    "CartPageFragment",
                    "Cart items observed: ${it.joinToString { item -> item.name }}"
                )
                adapter.updateProducts(it)
                updateTotalPrice(it.isNotEmpty())
                updateCartItemCount(it.size)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalPrice(hasItems: Boolean) {
        if (hasItems) {
            val totalPrice = viewModel.getTotalPrice()
            binding.totalPriceTextView.text = "Total: $totalPrice $"
            binding.totalPriceTextView.visibility = View.VISIBLE
            binding.textViewPrice.visibility = View.VISIBLE
            binding.complete.visibility = View.VISIBLE
            binding.emptyCartMessage.visibility = View.INVISIBLE
        } else {
            binding.totalPriceTextView.visibility = View.INVISIBLE
            binding.textViewPrice.visibility = View.INVISIBLE
            binding.complete.visibility = View.INVISIBLE
            binding.emptyCartMessage.visibility = View.VISIBLE
        }
    }


    private fun updateCartItemCount(count: Int) {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val badge = bottomNavigationView.getOrCreateBadge(R.id.cartPageFragment)
        badge.number = count
    }

    private fun onIncreaseQuantity(cartItem: CartItem) {
        viewModel.increaseQuantity(cartItem)
    }

    private fun onDecreaseQuantity(cartItem: CartItem) {
        viewModel.decreaseQuantity(cartItem)
    }
}