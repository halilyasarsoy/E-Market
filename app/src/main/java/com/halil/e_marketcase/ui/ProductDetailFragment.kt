package com.halil.e_marketcase.ui

import android.os.Bundle
import android.view.View
import com.halil.e_marketcase.databinding.FragmentProductDetailBinding
import com.halil.e_marketcase.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment :
    BaseFragment<FragmentProductDetailBinding>(FragmentProductDetailBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}